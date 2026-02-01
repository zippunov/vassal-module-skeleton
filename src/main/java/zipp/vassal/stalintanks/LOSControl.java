package zipp.vassal.stalintanks;

import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.command.Command;
import VASSAL.tools.imageop.ImageOp;
import VASSAL.tools.imageop.Op;
import VASSAL.tools.imageop.OwningOpMultiResolutionImage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class LOSControl extends EmptyControl {

    public static final AtomicBoolean losOn = new AtomicBoolean(false);

    JButton losBtn;
    Icon onIcon;
    Icon offIcon;

    public void actionPerformed(ActionEvent e) {
        losOn.set(!losOn.get());

        if (losOn.get()) {
            losBtn.setIcon(offIcon);
            losBtn.setToolTipText("Switch LOS on");
        } else {
            losBtn.setIcon(onIcon);
            losBtn.setToolTipText("Switch LOS off");
        }
    }

    @Override
    public void addTo(Buildable parent) {
        final ImageOp sop1 = Op.load("btn_thread_on.svg");
        if (sop1.getImage() != null) {
            onIcon = new ImageIcon(new OwningOpMultiResolutionImage(sop1));
        }
        final ImageOp sop2 = Op.load("btn_thread_off.svg");
        if (sop2.getImage() != null) {
            offIcon = new ImageIcon(new OwningOpMultiResolutionImage(sop2));
        }

        GameModule mod = GameModule.getGameModule();

        mod.addCommandEncoder(this);
        mod.getGameState().addGameComponent(this);

        losBtn = new JButton(onIcon);
        losBtn.setToolTipText("Switch LOS on");
        losBtn.addActionListener(this::actionPerformed);
        mod.getToolBar().add(losBtn);
    }

    @Override
    public void setup(boolean gameStarting) {
        losBtn.setIcon(onIcon);
        losBtn.setToolTipText("Switch LOS on");
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
