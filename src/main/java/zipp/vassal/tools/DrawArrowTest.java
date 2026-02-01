package zipp.vassal.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

class ArrowCanvas extends JComponent {
    double headLength = 20.0;
    int halfWidth = 3;
    int halfHeadWidth = 7;
    Line2D.Double arrowLine;
    Point2D.Double headStart;
    double length;
    double arrowTan;
    double orthoTan;
    double alpha;
    double cosOrthoAlpha;

    ArrowCanvas(int x1, int y1, int x2, int y2) {
        arrowLine = new Line2D.Double(x1, y1, x2, y2);
        length = arrowLine.getP1().distance(arrowLine.getP2());
        double headRatio = 1 - headLength / length;
        headStart = new Point2D.Double(
                headRatio * (arrowLine.getP2().getX() - arrowLine.getP1().getX()) + arrowLine.getP1().getX(),
                headRatio * (arrowLine.getP2().getY() - arrowLine.getP1().getY()) + arrowLine.getP1().getY()
        );
        arrowTan = (arrowLine.y2 - arrowLine.y1) / (arrowLine.x2 - arrowLine.x1);
        orthoTan = -1/arrowTan;
        alpha = Math.atan(orthoTan);
        cosOrthoAlpha = Math.cos(alpha);
    }

    Point2D.Double orthogonalPoint(Point2D startPoint, double len) {
        Point2D.Double p = (Point2D.Double) startPoint;
        double dx = len * cosOrthoAlpha;
        double x = p.x + dx;
        double y = p.y + dx * orthoTan;
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
        points.add(orthogonalPoint(arrowLine.getP1(), sign * halfWidth));
        points.add(orthogonalPoint(headStart, sign * halfWidth));
        points.add(orthogonalPoint(headStart, sign * halfHeadWidth));
        points.add((Point2D.Double) arrowLine.getP2());

        for (Point2D.Double p : points) {
            result.addPoint((int) Math.round(p.x), (int) Math.round(p.y));
        }
        return result;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.gray);
        Polygon p = buildHalfArrowPolygon(false);
        g2.fill(p);
    }
}
public class DrawArrowTest {

    public static void main(String[] a) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, 450, 450);
        window.getContentPane().add(new ArrowCanvas(50, 70, 250, 180));
        window.setVisible(true);
    }
}
