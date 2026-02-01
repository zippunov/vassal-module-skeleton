package zipp.vassal.stalintanks;

import VASSAL.build.AbstractFolder;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;

import java.awt.*;
import java.awt.event.MouseEvent;

import VASSAL.command.Command;
import VASSAL.counters.GamePiece;
import VASSAL.tools.imageop.ImageOp;
import VASSAL.tools.imageop.Op;
import VASSAL.tools.imageop.OwningOpMultiResolutionImage;
import VASSAL.tools.swing.SwingUtils;
import zipp.hex.FlatOddTopLayout;
import zipp.hex.Hex;
import zipp.hex.Layout;
import zipp.hex.Point2D;
import zipp.vassal.painters.ArrowPainter;

import javax.swing.*;

public class SmartLOSThread extends VASSAL.build.module.map.LOS_Thread {

    private static final Color RANGE_BG_COLOR = new Color(255, 255, 255, 200);

    Layout layout = new FlatOddTopLayout(162.84482671904343, 189.4, new Point2D(181.3, 224.5));

    JButton losBtn;
    Icon onIcon;
    Icon offIcon;

    public SmartLOSThread() {
    }

    @Override
    public void addTo(Buildable b) {
        if (b instanceof AbstractFolder) {
            b = ((AbstractFolder)b).getNonFolderAncestor();
        }

        final ImageOp sop1 = Op.load("btn_thread_on.svg");
        if (sop1.getImage() != null) {
            onIcon = new ImageIcon(new OwningOpMultiResolutionImage(sop1));
        }
        final ImageOp sop2 = Op.load("btn_thread_off.svg");
        if (sop2.getImage() != null) {
            offIcon = new ImageIcon(new OwningOpMultiResolutionImage(sop2));
        }
        losBtn = new JButton(offIcon);
        losBtn.setToolTipText("Show LOS thread");
        losBtn.addActionListener(e -> launch());

        idMgr.add(this);
        map = (Map) b;
        map.getView().addMouseMotionListener(this);
        map.addDrawComponent(this);
        map.getToolBar().add(losBtn);
        GameModule.getGameModule().addCommandEncoder(this);

        GameModule.getGameModule().getGameState().addGameComponent(this);
    }

    protected void launch() {
        super.launch();
        updateIcon();
    }

    public void reset() {
        super.reset();
        updateIcon();
    }

    private void updateIcon() {
        if (!visible) {
            losBtn.setIcon(offIcon);
            losBtn.setToolTipText("Show LOS thread");
        } else {
            losBtn.setIcon(onIcon);
            losBtn.setToolTipText("Hide LOS thread");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!SwingUtils.isMainMouseButtonDown(e)) {
            return;
        }

        if (!persisting && !mirroring) {
            if (retainAfterRelease && !(ctrlWhenClick && persistence.equals(CTRL_CLICK))) {
                retainAfterRelease = false;
                if (global.equals(ALWAYS)) {
                    final Command com = new LOSCommand(this, getAnchor(), getArrow(), false, true);
                    GameModule.getGameModule().sendAndLog(com);
                }
            } else if (e.getWhen() != lastRelease) {
                visible = false;
                if (global.equals(ALWAYS) || global.equals(WHEN_PERSISTENT)) {
                    if (persistence.equals(ALWAYS) || (ctrlWhenClick && persistence.equals(CTRL_CLICK))) {
                        anchor = lastAnchor;
                        final Command com = new LOSCommand(this, getAnchor(), getArrow(), true, false);
                        GameModule.getGameModule().sendAndLog(com);
                        setPersisting(true);
                    } else {
                        final Command com = new LOSCommand(this, getAnchor(), getArrow(), false, false);
                        GameModule.getGameModule().sendAndLog(com);
                    }
                }
                map.setPieceOpacity(1.0f);
                map.popMouseListener();
                map.repaint();
            }
            lastRelease = e.getWhen();

            if (getLosCheckCount() > 0) {
                reportFormat.setProperty(FROM_LOCATION, anchorLocation);
                reportFormat.setProperty(TO_LOCATION, lastLocation);
                reportFormat.setProperty(RANGE, lastRange);
                reportFormat.setProperty(CHECK_COUNT, String.valueOf(getLosCheckCount()));
                reportFormat.setProperty(CHECK_LIST, getLosCheckList());

                GameModule.getGameModule().getChatter().send(reportFormat.getLocalizedText(this, "Editor.report_format"));
            }
        }
        ctrlWhenClick = false;
        updateIcon();
    }

    @Override
    public void draw(Graphics g, Map m) {
        if (initializing || !visible) {
            return;
        }
        Hex h1 = layout.toHex(new Point2D(anchor.getX(), anchor.getY()));
        Hex h2 = layout.toHex(new Point2D(arrow.getX(), arrow.getY()));
        boolean blocked = Terrain.isLOSBlocked(h1, h2, layout);
        int distance = h1.getDistance(h2);

       String drmMsg = Scenario.getToHitDRM(map, layout.toPoint2D(h1), layout.toPoint2D(h2), h2);

        if (distance < 1) {
            return;
        }

        final Graphics2D g2d = (Graphics2D) g;
        Color c = blocked ? new Color(255, 0, 0, 100) : new Color(0, 0, 255, 100);
        g.setColor(c);
        final Stroke oldStroke = g2d.getStroke();
        final Color oldColor = g2d.getColor();
        final double os_scale = g2d.getDeviceConfiguration().getDefaultTransform().getScaleX();
        final Point mapAnchor = map.mapToDrawing(anchor, os_scale);
        final Point mapArrow = map.mapToDrawing(arrow, os_scale);
        if (losThickness > 1) {
            g2d.setStroke(new BasicStroke(losThickness));
        }
        ArrowPainter painter = new ArrowPainter(
               new Point2D(mapAnchor.x, mapAnchor.y),
                new Point2D(mapArrow.x, mapArrow.y),
                40
        );

        painter.drawArrow(g, c, Color.black);
        drawRange(g, distance, layout.toPoint2D(h1), layout.toPoint2D(h2), blocked, drmMsg);

        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);

        lastAnchor = anchor;
        lastArrow = arrow;
    }

    public void drawRange(Graphics g, int range, Point2D fromP, Point2D toP, boolean blocked, String drmMsg) {
        final Graphics2D g2d = (Graphics2D) g;
        final double os_scale = g2d.getDeviceConfiguration().getDefaultTransform().getScaleX();

        final Point mapArrow = map.mapToDrawing(arrow, os_scale);

        g.setFont(RANGE_FONT.deriveFont((float)(RANGE_FONT.getSize() * os_scale)));
        final FontMetrics fm = g.getFontMetrics();

        final String rangeMsg = "Range: " + range;
        final String rollMsg = Scenario.getToHitRoll(map, fromP, toP, range, blocked);


        int textWidth = Math.max(fm.stringWidth(rangeMsg), fm.stringWidth(rollMsg));
        textWidth = Math.max(textWidth, fm.stringWidth(drmMsg));
        final int textHeight = fm.getHeight();

        final int fullWidth = textWidth + 10;
        final int fullHeight = textHeight * 3+10;

        final int x0 = mapArrow.x;
        final int y0 = mapArrow.y + 30;

        g.setColor(RANGE_BG_COLOR);
        g.fillRect(x0 - fullWidth / 2, y0, fullWidth, fullHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x0 - fullWidth / 2, y0, fullWidth, fullHeight);
        g.setColor(Color.RED);

        g.drawString(rangeMsg, x0 - fullWidth / 2 + 5, y0 + textHeight);
        g.drawString(rollMsg, x0 - fullWidth / 2 + 5, y0 + textHeight + textHeight);
        g.drawString(drmMsg, x0 - fullWidth / 2 + 5, y0 + textHeight + textHeight + textHeight);

        lastRangeRect.x = x0 - fullWidth / 2 - 5;
        lastRangeRect.y = y0 - 5;
        lastRangeRect.width = fullWidth + 10;
        lastRangeRect.height = fullHeight + 10;
        lastRangeRect = map.drawingToMap(lastRangeRect, os_scale);

        lastRange = String.valueOf(range);
    }
}
