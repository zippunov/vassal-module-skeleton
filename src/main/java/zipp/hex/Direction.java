package zipp.hex;

public interface Direction {
    HexVector getVector();
    Direction rotate(int steps);

    int diff(Direction d);

    String getName();

    int getOrdinal();
}
