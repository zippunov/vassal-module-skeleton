package zipp.hex;

import java.util.Objects;

public abstract class Layout {
    protected static final double SQRT_3 = Math.sqrt(3.0);
    protected static final double SQRT_3_BY_2 = SQRT_3 / 2;
    protected static final double THREE_BY_TWO = 1.5;

    protected final Point2D origin;


    protected final double horizontalSpacing;
    protected final double verticalSpacing;

    public Layout(double horizontalSpacing, double verticalSpacing, Point2D origin) {
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.origin = Objects.requireNonNullElseGet(origin, () -> new Point2D(0, 0));
    }

    protected Hex roundAxial(double qf, double rf) {
        double sf = -qf - rf;
        int q = (int) Math.round(qf);
        int r = (int) Math.round(rf);
        int s = (int) Math.round(sf);

        double qDiff = Math.abs(qf - q);
        double rDiff = Math.abs(rf - r);
        double sDiff = Math.abs(sf - s);

        if (qDiff > rDiff && qDiff > sDiff) {
            q = -r - s;
        } else if (rDiff > sDiff) {
            r = -q - s;
        } else {
            s = -q - r;
        }
        return new Hex(q, r, s);
    }

    public abstract double getHexHalfHeight();

    public abstract OffsetCoord toOffset(Hex h);

    public abstract Hex toHex(OffsetCoord oc);

    public abstract Point2D toPoint2D(Hex h);

    public abstract Hex toHex(Point2D p);

    // Intersection of the Hex with the line segment defined by two end-points
    public abstract IntersectionType intersect(Point2D p1, Point2D p2, Hex h);
}
