package zipp.vassal.stalintanks;

import VASSAL.build.module.BasicCommandEncoder;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;

public class StalinTanksCommandEncoder extends BasicCommandEncoder {
    public Decorator createDecorator(String type, GamePiece inner) {
        if (type.startsWith(SendToGraveyard.ID)) {
            return new SendToGraveyard(type, inner);
        }

        // Now allow BasicCommandEncoder to run so that "normal" Traits process
        return super.createDecorator(type, inner);
    }
}
