package zipp.vassal.stalintanks;

import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.command.Command;


public class MoraleDisplay  extends EmptyControl {

    public static final String COMMAND_PREFIX = "RECALCULATE_LOSSES";

    @Override
    public void addTo(Buildable parent) {
        GameModule mod = GameModule.getGameModule();
        mod.addCommandEncoder(this);
    }


    @Override
    public Command decode(String command) {
        if (command.equals(COMMAND_PREFIX)) {
            return new RefreshLossesCommand();
        } else {
            return null;
        }
    }

    @Override
    public String encode(Command c) {
        if (c instanceof RefreshLossesCommand) {
            return COMMAND_PREFIX;
        } else {
            return null;
        }
    }
}
