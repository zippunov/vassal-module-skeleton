package zipp.hex;

import java.util.Objects;

public class HexVector {
    private final int dQ;
    private final int dR;
    private final int dS;

    public HexVector(int dQ, int dR, int dS) {
        if (dQ + dR + dS != 0) {
            throw new RuntimeException(String.format("Invalid Hex constructor values %d, %d, %d", dQ, dR, dS));
        }
        this.dQ = dQ;
        this.dR = dR;
        this.dS = dS;
    }

    public HexVector(int dQ, int dR) {
        this(dQ, dR, -dQ - dR);
    }

    public HexVector(Hex start, Hex finish) {
        this(
                finish.getQ() - start.getQ(),
                finish.getR() - start.getR(),
                finish.getS() - start.getS()
        );
    }

    public int getdQ() {
        return dQ;
    }

    public int getdR() {
        return dR;
    }

    public int getdS() {
        return dS;
    }

    public int getLength() {
        return Math.max(
                Math.abs(dQ),
                Math.max(Math.abs(dR), Math.abs(dS))
        );
    }

    public HexVector add(HexVector v) {
        return new HexVector(dQ + v.dQ, dR + v.dR, dS + v.dS);
    }

    public HexVector multiply(int v) {
        return new HexVector(dQ * v, dR * v, dS * v);
    }

    public HexVector negate() {
        return new HexVector(-dQ, -dR, -dS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HexVector hexVector = (HexVector) o;
        return dQ == hexVector.dQ && dR == hexVector.dR && dS == hexVector.dS;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dQ, dR, dS);
    }

    @Override
    public String toString() {
        return "HexVector{" +
                "dQ=" + dQ +
                ", dR=" + dR +
                ", dS=" + dS +
                '}';
    }
}
