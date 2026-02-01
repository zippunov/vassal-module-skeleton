package zipp.hex;

public class PointyEvenLeftLayout extends PointyLayout {

    public PointyEvenLeftLayout(double horizontalSpacing, double verticalSpacing, Point2D origin) {
        super(horizontalSpacing, verticalSpacing, origin);
    }

    @Override
    public OffsetCoord toOffset(Hex h) {
        int col = h.getQ() + (h.getR() + (h.getR()&1)) / 2;
        int row = h.getR();
        return new OffsetCoord(col, row);
    }

    @Override
    public Hex toHex(OffsetCoord oc) {
        var q = oc.getCol() - (oc.getRow() + (oc.getRow()&1)) / 2;
        var r = oc.getRow();
        return new Hex(q, r, -q-r);
    }
}
