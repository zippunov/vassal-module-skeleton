package zipp.hex;

import java.util.List;

public abstract class FlatLayout extends Layout {

    public FlatLayout(double horizontalSpacing, double verticalSpacing, Point2D origin) {
        super(horizontalSpacing, verticalSpacing, origin);
    }

    @Override
    public Point2D toPoint2D(Hex h) {
        double x = horizontalSpacing * h.getQ() + origin.getX();
        double y = (verticalSpacing / 2) * h.getQ() + verticalSpacing * h.getR() + origin.getY();
        return new  Point2D(x, y);
    }

    @Override
    public Hex toHex(Point2D p) {
        double x = p.getX() - origin.getX();
        double  y = p.getY() - origin.getY();
        double q = x / horizontalSpacing;
        double r = y/verticalSpacing - x /(2 * horizontalSpacing);
        return roundAxial(q, r);
    }

    public double getHexHalfHeight() {
        return verticalSpacing / 2;
    }

    @Override
    public IntersectionType intersect(Point2D p1, Point2D p2, Hex h) {
        boolean left = false;
        boolean center = false;
        boolean right = false;
        for (Point2D p : hexPoints(h)) {
            Turn t = getTurn(p1, p2, p);
            if (t == Turn.LEFT) {
                left = true;
            } else if (t == Turn.STRAIGHT) {
                center = true;
            } else {
                right = true;
            }
        }
        if (left && right) {
            return IntersectionType.CROSSES;
        }
        if (left && center) {
            return IntersectionType.TOUCHES_LEFT;
        }
        if (right && center) {
            return IntersectionType.TOUCHES_RIGHT;
        }
        return IntersectionType.NOT_CROSSES;
    }

    private List<Point2D> hexPoints(Hex hex) {
        double v2 = verticalSpacing / 2;
        double h3 = horizontalSpacing / 3;
        Point2D center = toPoint2D(hex);
        Point2D p1 = new Point2D(center.getX() - h3, center.getY() - v2);
        Point2D p2 = new Point2D(center.getX() + h3, center.getY() - v2);
        Point2D p3 = new Point2D(center.getX() + 2 * h3, center.getY());
        Point2D p4 = new Point2D(center.getX() + h3, center.getY() + v2);
        Point2D p5 = new Point2D(center.getX() - h3, center.getY() + v2);
        Point2D p6 = new Point2D(center.getX() - 2 * h3, center.getY());
        return List.of(p1, p2, p3, p4, p5, p6);
    }

    private Turn getTurn(Point2D p1, Point2D p2, Point2D p3) {
        double cross = (p3.getX() - p1.getX()) * (p2.getY() - p1.getY()) - (p2.getX() - p1.getX()) * (p3.getY() - p1.getY());
        if (Math.abs(cross) < 0.04) {
            cross = 0.0;
        }
        return (cross > 0) ? Turn.LEFT : ((cross == 0) ? Turn.STRAIGHT : Turn.RIGHT);
    }
}
