package zipp.hex;

public abstract class PointyLayout extends Layout {

    public PointyLayout(double horizontalSpacing, double verticalSpacing, Point2D origin) {
        super(horizontalSpacing, verticalSpacing, origin);
    }

    @Override
    public Point2D toPoint2D(Hex h) {
        // to Implement
        return null;
//        double x = hexSize * (SQRT_3 * h.getQ() + SQRT_3_BY_2 * h.getR()) + origin.getX();
//        double y = hexSize * SQRT_3_BY_2 * h.getR() + origin.getY();
//        return new  Point2D(x, y);
    }

    @Override
    public Hex toHex(Point2D p) {
        return null;
        // to implement
//        double x = p.getX() - origin.getX();
//        double  y = p.getY() - origin.getY();
//        double q = ((SQRT_3 / 3) * x - (1.0 / 3) * y) / hexSize;
//        double r = ((2.0 / 3) * y) / hexSize;
//        return roundAxial(q, r);
    }

    public double getHexHalfHeight() {
        return horizontalSpacing/2;
    }

    @Override
    public IntersectionType intersect(Point2D p1, Point2D p2, Hex h) {
        return IntersectionType.NOT_CROSSES;
    }
}
