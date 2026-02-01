package zipp.vassal.stalintanks;

import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.ChangePiece;
import VASSAL.command.ChangeTracker;
import VASSAL.command.Command;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;
import VASSAL.counters.KeyCommand;
import VASSAL.counters.Stack;
import VASSAL.tools.NamedKeyStroke;

import javax.swing.*;
import java.awt.*;

public class SendToGraveyard extends Decorator {

    public static final String ID = "sendToGraveyard;";
    public static final String BACK_MAP = "backMap"; // NON-NLS
    public static final String BACK_POINT = "backPoint"; // NON-NLS

    private static final int germanRootX = 100;
    private static final int germanRootY = 100;
    private static final int sovietRootX = 817;
    private static final int sovietRootY = 100;
    private static final int stepPixels = 135;

    private static final KeyStroke sendToGraveyardStroke = KeyStroke.getKeyStroke('G', 0);
    private static final NamedKeyStroke unitMovedStroke = new NamedKeyStroke("unitMoved");

    public SendToGraveyard() {
        this(ID, null);
    }

    public SendToGraveyard(String type, GamePiece inner) {
        mySetType(type);
        setInner(inner);
    }

    @Override
    public void mySetState(String newState) {
    }

    @Override
    public String myGetState() {
        return "";
    }

    @Override
    public String myGetType() {
        return ID;
    }

    @Override
    public void mySetType(String type) {
    }

    @Override
    protected KeyCommand[] myGetKeyCommands() {
        GamePiece gp = Decorator.getOutermost(this);
        Map m = gp.getMap();
        if (!Scenario.getMainMap().equals(m)) {
            return new KeyCommand[0];
        }
        return new KeyCommand[]{
                new KeyCommand("Send to Graveyard", sendToGraveyardStroke, gp)
        };
    }

    @Override
    public Command myKeyEvent(KeyStroke stroke) {
        if (sendToGraveyardStroke.equals(stroke)) {
            Command c = moveToGraveyard();
            Scenario.updateLossesInfo();
            return c;
        }
        if (unitMovedStroke.getKeyStroke().equals(stroke)){
            Scenario.updateLossesInfo();
            return new RefreshLossesCommand();
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Send to graveyard";
    }

    /**
     * @return the help file for this trait
     */
    @Override
    public HelpFile getHelpFile() {
        return null;
    }

    @Override
    public void draw(Graphics g, int x, int y, Component obs, double zoom) {
        piece.draw(g, x, y, obs, zoom);
    }

    /**
     * @return The area which this GamePiece occupies when drawn at the point (0,0)
     */
    @Override
    public Rectangle boundingBox() {
        return piece.boundingBox();
    }


    @Override
    public Shape getShape() {
        return piece.getShape();
    }

    @Override
    public String getName() {
        return piece.getName();
    }

    private Command moveToGraveyard() {
        final GamePiece outer = Decorator.getOutermost(this);
        Point targetPoint = getTargetPoint(outer);
        final Map oldMap = outer.getMap();
        final Stack parent = outer.getParent();
        Map map = Scenario.getGraveyard();
        final ChangeTracker tracker = new ChangeTracker(this);
        setProperty(BACK_MAP, getMap());
        setProperty(BACK_POINT, getPosition());
        Command c = tracker.getChangeCommand();
        c = c.append(putOldProperties(this));
        c = c.append(map.placeOrMerge(outer, targetPoint));
        String oldState = outer.getState();
        outer.setProperty("rotation", 1);
        c.append(new ChangePiece(outer.getId(), oldState, outer.getState()));
        if (parent != null) {
            c = c.append(parent.pieceRemoved(outer));
        }
        c = c.append(new RefreshLossesCommand());
        oldMap.repaint();
        map.repaint();
        return c;
    }

    private Point getTargetPoint(GamePiece gp) {
        String side = gp.getProperty("side").toString();
        int count = Scenario.getGraveyardCount(side);
        int y = (count / 5) * stepPixels;
        if (Scenario.GERMAN.equals(side)) {
            y += germanRootY;
        } else {
            y += sovietRootY;
        }
        int x = (count % 5) * stepPixels;
        if (Scenario.GERMAN.equals(side)) {
            x += germanRootX;
        } else {
            x += sovietRootX;
        }
        return new Point(x, y);
    }
}
