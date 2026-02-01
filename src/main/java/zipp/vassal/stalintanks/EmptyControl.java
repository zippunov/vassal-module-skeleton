package zipp.vassal.stalintanks;

import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.module.GameComponent;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;

public class EmptyControl extends AbstractConfigurable implements CommandEncoder, GameComponent {
    @Override
    public String[] getAttributeDescriptions() {
        return new String[0];
    }

    @Override
    public Class<?>[] getAttributeTypes() {
        return new Class[0];
    }

    @Override
    public String[] getAttributeNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String key, Object value) {

    }

    @Override
    public String getAttributeValueString(String key) {
        return null;
    }

    @Override
    public void removeFrom(Buildable parent) {

    }

    @Override
    public HelpFile getHelpFile() {
        return null;
    }

    @Override
    public Class[] getAllowableConfigureComponents() {
        return new Class[0];
    }

    @Override
    public void addTo(Buildable parent) {
    }

    @Override
    public void setup(boolean gameStarting) {
    }

    @Override
    public Command getRestoreCommand() {
        return null;
    }

    @Override
    public Command decode(String command) {
        return null;
    }

    @Override
    public String encode(Command c) {
        return null;
    }
}
