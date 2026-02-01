package zipp.vassal.painters;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import zipp.vassal.tools.ScreenDPI;

public class ArrowPainter {
    private final int halfWidth = 2  * ScreenDPI.getPixelDensityInt();
    private final int halfHeadWidth = 7 * ScreenDPI.getPixelDensityInt();
    private final Line2D.Double arrowLine;
    private final Point2D.Double headStart;
    private final double cosAlpha;
    private final double sinAlpha;

    public ArrowPainter(zipp.hex.Point2D p1, zipp.hex.Point2D p2, double hexHeight) {
        final Line2D.Double fullLine = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        final double fullLength = fullLine.getP1().distance(fullLine.getP2());
        final double margin =  3 * hexHeight / 4;
        final double margin1Ratio = margin / fullLength;
        final Point2D.Double marginP1 = new Point2D.Double(
                margin1Ratio * (fullLine.getP2().getX() - fullLine.getP1().getX()) + fullLine.getP1().getX(),
                margin1Ratio * (fullLine.getP2().getY() - fullLine.getP1().getY()) + fullLine.getP1().getY()
        );
        final double margin2Ratio = 1 - margin / fullLength;
        final Point2D.Double marginP2 = new Point2D.Double(
                margin2Ratio * (fullLine.getP2().getX() - fullLine.getP1().getX()) + fullLine.getP1().getX(),
                margin2Ratio * (fullLine.getP2().getY() - fullLine.getP1().getY()) + fullLine.getP1().getY()
        );

        arrowLine = new Line2D.Double(marginP1.getX(), marginP1.getY(), marginP2.getX(), marginP2.getY());
        double length = arrowLine.getP1().distance(arrowLine.getP2());
        double headLength = 20 * ScreenDPI.getPixelDensityInt();
        final double headRatio = 1 - headLength / length;
        headStart = new Point2D.Double(
                headRatio * (arrowLine.getP2().getX() - arrowLine.getP1().getX()) + arrowLine.getP1().getX(),
                headRatio * (arrowLine.getP2().getY() - arrowLine.getP1().getY()) + arrowLine.getP1().getY()
        );
        if (Math.abs(arrowLine.y2 - arrowLine.y1) < 1.0) {
            cosAlpha = 0;
            sinAlpha = 1;
        } else if (Math.abs(arrowLine.x2 - arrowLine.x1) < 1.0) {
            cosAlpha = 1;
            sinAlpha = 0;
        } else {
            double arrowTan = (arrowLine.y2 - arrowLine.y1) / (arrowLine.x2 - arrowLine.x1);
            double alpha = Math.atan(-1/ arrowTan);
            cosAlpha = Math.cos(alpha);
            sinAlpha = Math.sin(alpha);
        }
    }

    private Point2D.Double orthogonalPoint(Point2D startPoint, double len) {
        Point2D.Double p = (Point2D.Double) startPoint;
        double x = p.x + len * cosAlpha;
        double y = p.y + len * sinAlpha;
        return new Point2D.Double(x, y);
    }

    private Polygon buildArrowPolygon() {
        Polygon result = new Polygon();
        List<Point2D.Double> points = new ArrayList<>();
        points.add(orthogonalPoint(arrowLine.getP1(), halfWidth));
        points.add(orthogonalPoint(headStart, halfWidth));
        points.add(orthogonalPoint(headStart, halfHeadWidth));
        points.add((Point2D.Double) arrowLine.getP2());
        points.add(orthogonalPoint(headStart, -halfHeadWidth));
        points.add(orthogonalPoint(headStart, -halfWidth));
        points.add(orthogonalPoint(arrowLine.getP1(), -halfWidth));
        for (Point2D.Double p : points) {
            result.addPoint((int) Math.round(p.x), (int) Math.round(p.y));
        }
        return result;
    }

    private Polygon buildHalfArrowPolygon(boolean top) {
        int sign = top ? 1 : -1;
        Polygon result = new Polygon();
        List<Point2D.Double> points = new ArrayList<>();
        points.add((Point2D.Double) arrowLine.getP1());
        points.add(orthogonalPoint(arrowLine.getP1(), sign * (halfWidth + 2 * ScreenDPI.getPixelDensityInt())));
        points.add(orthogonalPoint(headStart, sign * (halfWidth + 2 * ScreenDPI.getPixelDensityInt())));
        points.add(orthogonalPoint(headStart, sign * halfHeadWidth));
        points.add((Point2D.Double) arrowLine.getP2());

        for (Point2D.Double p : points) {
            result.addPoint((int) Math.round(p.x), (int) Math.round(p.y));
        }
        return result;
    }

    public void drawArrow(Graphics g, Color fill, Color stroke) {
        Polygon p = buildArrowPolygon();
        drawPolygon(g, p, fill, stroke);
    }

    public void drawHalfArrow(Graphics g, boolean top, Color fill, Color stroke) {
        Polygon p = buildHalfArrowPolygon(top);
        drawPolygon(g, p, fill, stroke);
    }

    private static void drawPolygon(Graphics g, Polygon p, Color fill, Color borderColor) {
        Graphics2D g2 = (Graphics2D) g;
        final Composite oldComposite = g2.getComposite();
        final Stroke oldStroke = g2.getStroke();
        final Color oldColor = g2.getColor();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(fill);
        g2.fill(p);
        g2.setColor(borderColor);
        g2.draw(p);
        g2.setComposite(oldComposite);
        g2.setStroke(oldStroke);
        g2.setColor(oldColor);
    }
}
