package zipp.hex;

public class FlatEvenTopLayout extends FlatLayout {

    public FlatEvenTopLayout(double horizontalSpacing, double verticalSpacing, Point2D origin) {
        super(horizontalSpacing, verticalSpacing, origin);
    }

    @Override
    public OffsetCoord toOffset(Hex h) {
        int col = h.getQ();
        int row = h.getR() + (h.getQ() + (h.getQ() & 1)) / 2;
        return new OffsetCoord(col, row);
    }

    @Override
    public Hex toHex(OffsetCoord oc) {
        var q = oc.getCol();
        var r = oc.getRow() - (oc.getCol() + (oc.getCol() & 1)) / 2;
        return new Hex(q, r, -q-r);
    }
}
