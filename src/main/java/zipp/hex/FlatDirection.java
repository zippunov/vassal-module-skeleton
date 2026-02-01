package zipp.hex;

public enum FlatDirection implements Direction {
    N(0, "N", new HexVector(0, -1, 1)),
    NE(1, "NE", new HexVector(1, -1, 0)),
    SE(2, "SE", new HexVector(1, 0, -1)),
    S(3, "S", new HexVector(0, 1, -1)),
    SW(4, "SW", new HexVector(-1, 1, 0)),
    NW(5, "NW", new HexVector(-1, 0, 1));

    private static final FlatDirection[] dirs = {N, NE, SE, S, SW, NW};

    private final int ordinal;
    private final String name;
    private final HexVector v;

    public static Direction getByOrdinal(int idx) {
        return dirs[idx];
    }

    FlatDirection(int ordinal, String name, HexVector v) {
        this.ordinal = ordinal;
        this.name = name;
        this.v = v;
    }

    @Override
    public HexVector getVector() {
        return v;
    }

    @Override
    public Direction rotate(int steps) {
        int newIdx = (ordinal + steps) % 6;
        if (newIdx < 0) {
            newIdx += 6;
        }
        return dirs[newIdx];
    }

    @Override
    public int diff(Direction d) {
        if (d == null || getClass() != d.getClass()) {
            throw new RuntimeException("Direction must be non null FlatDirection type");
        }
        int diff  = ((FlatDirection) d).ordinal - this.ordinal;
        if (Math.abs(diff) > 3) {
            int sign = diff / Math.abs(diff);
            diff = -sign * (6 - Math.abs(diff));
        }
        return diff;
    }

    public int getOrdinal() {
        return this.ordinal;
    }

    @Override
    public String getName() {
        return name;
    }
}
