package zipp.vassal.stalintanks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import zipp.hex.Hex;
import zipp.hex.IntersectionType;
import zipp.hex.Layout;
import zipp.hex.Point2D;

public class Terrain {
    private static final Map<Integer, Set<Integer>> terrain = new HashMap<>(){{
        put(1, Set.of(3, 4, 22));
        put(2, Set.of(7, 20, 21));
        put(3, Set.of(6, 18));
        put(4, Set.of(0, 1, 12, 16, 17, 18));
        put(5, Set.of(11, 14, 15, 17, 18));
        put(6, Set.of(9, 10, 14, 15, 19));
        put(7, Set.of(3, 14, 15, 18));
        put(8, Set.of(13));
        put(9, Set.of(0, 1, 6, 15));
        put(10, Set.of(6, 12, 15));
        put(11, Set.of(6));
        put(12, Set.of(5,  14));
        put(13, Set.of(-2, 4, 7, 13, 15));
        put(14, Set.of(-6, -5, -3, 7, 12, 14, 15));
        put(15, Set.of(-6, -4, 7, 14));
        put(16, Set.of(-7, -4));
        put(17, Set.of(10, 11));
        put(18, Set.of(-4, 8, 11));
        put(19, Set.of(-4, 0, 3, 4, 6, 7, 10));
        put(20, Set.of(-1, 0));
        put(21, Set.of(0, 11, 12));
        put(22, Set.of(-10, -9, 7, 11));
        put(23, Set.of(6, 7, 10, 11));
    }};

    private static final Map<String, TerrainType> terrainType = new HashMap<>(){{
        //key = <hex S>_<hex R>
        put("-8_-6", TerrainType.WOODS);
        put("-9_-7", TerrainType.WOODS);
        put("-9_-6", TerrainType.WOODS);
        put("-9_-5", TerrainType.WOODS);

        put("-4_0", TerrainType.WOODS);
        put("-5_1", TerrainType.WOODS);

        put("-12_-10", TerrainType.HILL);
        put("-13_-9", TerrainType.HILL);

        put("-4_3", TerrainType.HILL);
        put("-5_4", TerrainType.HILL);

        put("-11_-2", TerrainType.HILL);
        put("-11_-3", TerrainType.HILL);
        put("-11_-4", TerrainType.HILL);
        put("-12_-4", TerrainType.HILL);

        put("-9_0", TerrainType.WOODS);
        put("-10_1", TerrainType.WOODS);

        put("-14_-4", TerrainType.WOODS);
        put("-15_-4", TerrainType.WOODS);

        put("-10_3", TerrainType.HILL);

        put("-9_6", TerrainType.WOODS);
        put("-9_7", TerrainType.WOODS);

        put("-19_-1", TerrainType.WOODS);
        put("-19_0", TerrainType.WOODS);
        put("-20_0", TerrainType.WOODS);
        put("-21_0", TerrainType.WOODS);

        put("-15_6", TerrainType.HILL);
        put("-16_6", TerrainType.HILL);

        put("-17_4", TerrainType.WOODS);
        put("-17_5", TerrainType.WOODS);

        put("-17_6", TerrainType.BUILDING);

        put("-15_9", TerrainType.HILL);
        put("-16_10", TerrainType.HILL);
        put("-16_11", TerrainType.HILL);
        put("-16_12", TerrainType.HILL);

        put("-22_3", TerrainType.HILL);
        put("-23_4", TerrainType.HILL);

        put("-20_7", TerrainType.WOODS);
        put("-21_7", TerrainType.WOODS);
        put("-22_7", TerrainType.WOODS);

        put("-25_6", TerrainType.HILL);
        put("-26_7", TerrainType.HILL);
        put("-26_8", TerrainType.HILL);

        put("-22_12", TerrainType.BUILDING);

        put("-19_14", TerrainType.WOODS);
        put("-20_14", TerrainType.WOODS);
        put("-21_14", TerrainType.WOODS);
        put("-21_13", TerrainType.WOODS);
        put("-20_15", TerrainType.WOODS);
        put("-21_15", TerrainType.WOODS);
        put("-22_15", TerrainType.WOODS);
        put("-20_16", TerrainType.WOODS);
        put("-21_17", TerrainType.WOODS);
        put("-22_17", TerrainType.WOODS);
        put("-21_18", TerrainType.WOODS);
        put("-22_18", TerrainType.WOODS);
        put("-23_18", TerrainType.WOODS);

        put("-29_6", TerrainType.WOODS);
        put("-29_7", TerrainType.WOODS);
        put("-30_7", TerrainType.WOODS);

        put("-24_15", TerrainType.HILL);
        put("-25_15", TerrainType.HILL);

        put("-26_14", TerrainType.WOODS);
        put("-26_13", TerrainType.WOODS);
        put("-26_12", TerrainType.WOODS);

        put("-27_10", TerrainType.WOODS);
        put("-28_11", TerrainType.WOODS);
        put("-29_11", TerrainType.WOODS);
        put("-29_10", TerrainType.WOODS);

        put("-22_20", TerrainType.HILL);
        put("-23_21", TerrainType.HILL);
        put("-23_22", TerrainType.HILL);

        put("-25_18", TerrainType.WOODS);
        put("-25_19", TerrainType.WOODS);

        put("-28_15", TerrainType.WOODS);
        put("-28_14", TerrainType.WOODS);
        put("-29_14", TerrainType.WOODS);
        put("-29_15", TerrainType.WOODS);

        put("-32_11", TerrainType.WOODS);
        put("-33_11", TerrainType.WOODS);
        put("-33_10", TerrainType.WOODS);
        put("-33_12", TerrainType.WOODS);
        put("-34_11", TerrainType.WOODS);

        put("-18_17", TerrainType.STREAM);
        put("-18_15", TerrainType.STREAM);
        put("-18_14", TerrainType.STREAM);
        put("-17_13", TerrainType.STREAM);
        put("-17_12", TerrainType.STREAM);
        put("-17_11", TerrainType.STREAM);
        put("-17_10", TerrainType.STREAM);
        put("-17_9", TerrainType.STREAM);
        put("-17_8", TerrainType.STREAM);
        put("-19_8", TerrainType.STREAM);
        put("-20_8", TerrainType.STREAM);
        put("-21_8", TerrainType.STREAM);
        put("-22_8", TerrainType.MARSH);
        put("-23_8", TerrainType.MARSH);
        put("-24_8", TerrainType.MARSH);
        put("-24_9", TerrainType.MARSH);
        put("-25_9", TerrainType.MARSH);

    }};

    public static TerrainType getTerrain(Hex h) {
        String k = h.getS() + "_" + h.getR();
        if (!terrainType.containsKey(k)){
            return TerrainType.PLAIN;
        }
        return terrainType.get(k);
    }

    public static boolean isBlocking(int q, int r) {
        if (!terrain.containsKey(q)) {
            return false;
        }
        return terrain.get(q).contains(r);
    }

    public static boolean isLOSBlocked(Hex h1, Hex h2, Layout layout) {
        if (h1.getDistance(h2) == 1) {
            return false;
        }
        int minQ = Math.min(h1.getQ(), h2.getQ());
        int maxQ = Math.max(h1.getQ(), h2.getQ());
        int minR = Math.min(h1.getR(), h2.getR());
        int maxR = Math.max(h1.getR(), h2.getR());
        Point2D p1 = layout.toPoint2D(h1);
        Point2D p2 = layout.toPoint2D(h2);
        boolean left = false;
        boolean right = false;
        for (int i = minQ; i <= maxQ; i++) {
            for (int j = minR; j <= maxR; j++) {
                if (h1.getQ() == i && h1.getR() == j) {
                    continue;
                }
                if (h2.getQ() == i && h2.getR() == j) {
                    continue;
                }
                if (isBlocking(i, j)) {
                    IntersectionType type = layout.intersect(p1, p2, new Hex(i ,j));
                    if (type == IntersectionType.CROSSES) {
                        return true;
                    }
                    if (type == IntersectionType.TOUCHES_LEFT) {
                        left = true;
                    }
                    if (type == IntersectionType.TOUCHES_RIGHT) {
                        right = true;
                    }
                    if (left && right) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

