package zipp.hex;

import java.util.Objects;

public class OffsetCoord {
    private final int col;
    private final int row;

    public OffsetCoord(int col, int row){
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OffsetCoord that = (OffsetCoord) o;
        return col == that.col && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
