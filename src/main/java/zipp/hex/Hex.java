package zipp.hex;

import java.util.Objects;

public class Hex {
    private final int q;
    private final int r;
    private final int s;

    public Hex(int q, int r, int s) {
        if (q + r + s  != 0) {
            throw new RuntimeException(String.format("Invalid Hex constructor values %d, %d, %d", q, r, s));
        }
        this.q = q;
        this.r = r;
        this.s = s;
    }

    public Hex(int q, int r) {
        this(q, r, -q - r);
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public int getS() {
        return s;
    }

    public Hex add(HexVector v) {
        return new Hex(
                q + v.getdQ(),
                r + v.getdR(),
                s + v.getdS()
        );
    }

    public HexVector substract(Hex h) {
        return new HexVector(
             q - h.q,
             r - h.r,
             s - h.s
        );
    }

    public Hex getNeigbor(Direction d) {
        return add(d.getVector());
    }

    public int getDistance(Hex h) {
        return new HexVector(this, h).getLength();
    }

    /**
     *
     * @param d direction from the origin
     * @param target hex
     * @return Return -1 if target to the left from direction, 0 of target right on direction, 1 if target id to the right from direction
     */
    public int getTurn(Direction d, Hex target) {
        HexVector dv = d.getVector();
        int result = 0;
        if (dv.getdQ() == 0) {

            result = target.q - q == 0 ? 0 : (target.q - q) / Math.abs(target.q - q);
        } else if (dv.getdR() == 0) {
            result = target.r - r == 0 ? 0 : (target.r - r) / Math.abs(target.r - r);
        } else {
            result = target.s - s == 0 ? 0 : - (target.s - s) / Math.abs(target.s - s);
        }
        if (d.getOrdinal() > 2) {
            result = - result;
        }
        return result;
    }

    public boolean inTheArc(Direction left, Direction right, Hex target) {
        return getTurn(left, target) >= 0 && getTurn(right, target) <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hex hex = (Hex) o;
        return Double.compare(hex.q, q) == 0 && Double.compare(hex.r, r) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, s);
    }
}
