package zipp.vassal.stalintanks;

import VASSAL.command.Command;

public class RefreshLossesCommand  extends Command {
    @Override
    protected void executeCommand() {
        Scenario.updateLossesInfo();
    }

    @Override
    protected Command myUndoCommand() {
        return this;
    }
}
