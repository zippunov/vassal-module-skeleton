package zipp.vassal.stalintanks;

import VASSAL.build.GameModule;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;
import java.util.Map;

public class Debug {
    public static void printAllUnits() {
        GameModule mod = GameModule.getGameModule();
        for (GamePiece gp : mod.getGameState().getAllPieces()) {
            mod.getChatter().send("============================");
            mod.getChatter().send("Game piece: " + gp.getId());
            mod.getChatter().send("name: " + gp.getName());
            mod.getChatter().send("class: " + gp.getClass().getName());
            if (gp instanceof Decorator) {
                Decorator d = (Decorator) gp;
                for (Map.Entry<String, Object> entry : d.getProperties().entrySet()) {
                    mod.getChatter().send(entry.getKey() + " => " + entry.getValue().toString());
                }
            }
        }
        mod.getChatter().send("============================");
    }

    public static void printGlobalProperties(String[] props) {
        GameModule mod = GameModule.getGameModule();
        mod.getChatter().send("============================");
        mod.getChatter().send("Global properties:");
        for (String key : props) {
            mod.getChatter().send(key +" => " + mod.getMutableProperty(key).getPropertyValue());
        }
        mod.getChatter().send("============================");
    }

    public static void print(String s) {
        GameModule.getGameModule().getChatter().send(s);
    }
}
