package zipp.vassal.stalintanks;

import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.PlayerRoster;
import VASSAL.counters.GamePiece;
import VASSAL.counters.PieceFinder;
import VASSAL.counters.Stack;
import zipp.hex.Hex;
import zipp.hex.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scenario {

    public static final String MAIN_MAP_NAME = "";
    public static final String GRAVEYARD_MAP_NAME = "Graveyard";
    public static final String SOVIET = "Soviet";
    public static final String GERMAN = "German";

    public static VASSAL.build.module.Map getMapByName(String name) {
        for (VASSAL.build.module.Map m : VASSAL.build.module.Map.getMapList()) {
            if (name.equals(m.getMapName())){
                return m;
            }
        }
        return null;
    }

    public static VASSAL.build.module.Map getGraveyard() {
        return getMapByName("Graveyard");
    }

    private static JDialog getGraveyardWindow() {
        JPanel jp = (JPanel)  getGraveyard().getComponent();
        if (jp != null) {
            return (JDialog) jp.getTopLevelAncestor();
        }
        return null;
    }

    public static VASSAL.build.module.Map getMainMap() {
        return VASSAL.build.module.Map.getMapList()
                .stream()
                .filter(VASSAL.build.module.Map::isFirstMap)
                .findFirst()
                .orElse(null);
    }

    public static String getMySide() {
        final PlayerRoster r = GameModule.getGameModule().getPlayerRoster();
        if (r != null) {
            for (final PlayerRoster.PlayerInfo pi : r.getPlayers()) {
                if (pi.playerId.equals(GameModule.getActiveUserId())) {
                    return pi.getSide();
                }
            }
        }
        return null;
    }

    public static List<GamePiece> getMapPieces(VASSAL.build.module.Map map) {
        List<GamePiece> rslt = new ArrayList<>();
        for (GamePiece gp : map.getAllPieces()) {
            if (gp instanceof Stack) {
                Stack s = (Stack) gp;
                rslt.addAll(s.asList());
            } else {
                rslt.add(gp);
            }
        }
        return rslt;
    }

    public static int getGermanMoraleLimit() {
        GameModule mod = GameModule.getGameModule();
        return Integer.parseInt(mod.getProperty("germanMoraleThreshold").toString());
    }

    public static int getSovietMoraleLimit() {
        GameModule mod = GameModule.getGameModule();
        return Integer.parseInt(mod.getProperty("sovietMoraleThreshold").toString());
    }

    public static int getGraveyardCount(String side) {
        int count = 0;
        VASSAL.build.module.Map m = getGraveyard();
        if (m == null) {
            return count;
        }
        for (GamePiece gp : getMapPieces(m)) {
            if (side.equals(gp.getProperty("side"))) {
                count++;
            }
        }
        return count;
    }

    public static int getGraveyardGermanUnitsCount() {
        int count = 0;
        VASSAL.build.module.Map m = getGraveyard();
        if (m == null) {
            return count;
        }
        for (GamePiece gp : getMapPieces(m)) {
            if (GERMAN.equals(gp.getProperty("side"))) {
                count++;
            }
        }
        return count;
    }

    public static int getGraveyardSovietUnitsCount() {
        int count = 0;
        VASSAL.build.module.Map m = getGraveyard();
        if (m == null) {
            return count;
        }
        for (GamePiece gp : getMapPieces(m)) {
            if (SOVIET.equals(gp.getProperty("side"))) {
                count++;
            }
        }
        return count;
    }

    public static void updateLossesInfo() {
        int germanLost = getGraveyardGermanUnitsCount();
        int germanThreshold = getGermanMoraleLimit();
        int sovietLost = getGraveyardSovietUnitsCount();
        int sovietThreshold = getSovietMoraleLimit();
        JDialog window = getGraveyardWindow();
        if (window != null) {
            window.setTitle("Graveyard. German: " + germanLost + "/" + germanThreshold
            + ", Soviet: "+ sovietLost + "/" + sovietThreshold);
        }
    }

    public static GamePiece getUnitInThePoint(Map m, Point2D p) {
       return m.findAnyPiece(new Point((int) p.getX(), (int) p.getY()), PieceFinder.PIECE_IN_STACK);
    }

    public static String getToHitRoll(Map m, Point2D fromP, Point2D toP, int distance, boolean blocked) {
        if (blocked) {
            return "LOS blocked";
        }
        GamePiece unit = getUnitInThePoint(m, fromP);
        GamePiece target = getUnitInThePoint(m, toP);
        if (unit != null && unit.getProperty("unit_type") == "infantry") {
            if (target == null) {
                return "";
            }
            if (target.getProperty("unit_type") == "infantry" && distance > 10) {
                return "Out of range";
            }
            if (target.getProperty("unit_type") == "armor" && distance > 4) {
                return "Out of range";
            }
            return "In Range";
        }

        if (distance < 2) {
            if (unit != null && unit.getProperty("Moved").equals(Boolean.TRUE)) {
                return "2D6 Roll: 9-";
            }
            return "2D6 Roll: 12-";
        }
        if (distance < 6) {
            return "2D6 Roll: 9-";
        }
        if (distance < 13) {
            return "2D6 Roll: 8-";
        }
        if (distance < 20) {
            return "2D6 Roll: 7-";
        }
        return "2D6 Roll: 6-";
    }

    public static String getToHitDRM(Map m, Point2D fromP, Point2D toP, Hex toHex) {
        GamePiece unit = getUnitInThePoint(m, fromP);
        GamePiece target = getUnitInThePoint(m, toP);
        if (unit == null || target == null || unit.getProperty("unit_type") == "infantry") {
            return "No DRM";
        }
        int drm = 0;

        if (unit.getProperty("Moved").equals(Boolean.TRUE)) {
            drm += 3;
        }

        // if target infantry drm += 2 and return
        if (target.getProperty("unit_type") == "infantry") {
            drm += 2;
            return "DRM: +" + drm;
        }

        if (unit.getProperty("precise") == "true") {
            drm -= 2;
        }
        TerrainType tt = Terrain.getTerrain(toHex);
        if (tt == TerrainType.HILL) {
            drm += 3;
        } else if (tt == TerrainType.WOODS) {
            drm += 2;
        }
        if (drm < 0) {
            return "DRM: " + drm;
        }
        return "DRM: +" + drm;
    }
}
