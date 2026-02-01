package zipp.vassal.stalintanks;

import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.command.Command;
import VASSAL.tools.imageop.ImageOp;
import VASSAL.tools.imageop.Op;
import VASSAL.tools.imageop.OwningOpMultiResolutionImage;
import java.awt.FlowLayout;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ScenarioPhasesSwitch extends EmptyControl {

    public static String CURRENT_PHASE = "currentPhase";
    public static String NUMBER_OF_TURNS = "turnsNumber";
    public static String SIDE_TO_MOVE_FIRST = "sideToMoveFirst";
    public static String GERMAN_SIDE = "German";
    public static String SOVIET_SIDE = "Soviet";

    // Short Phase names - will go to toolbar
    private static final String[] phases = new String[]{
            "1st Movement",
            "Stationary Fire",
            "Defensive Fire",
            "Mobile Fire",
            "2nd Movement",
            "1st Movement",
            "Stationary Fire",
            "Defensive Fire",
            "Mobile Fire",
            "2nd Movement",
    };

    // Full phase names - will go to tooltips
    private static final String[] toolTipPhases = new String[]{
            "A. First Movement",
            "B. Stationary Fire",
            "C. Defensive Fire",
            "D. Mobile Fire",
            "E. Second Movement",
            "A. First Movement",
            "B. Stationary Fire",
            "C. Defensive Fire",
            "D. Mobile Fire",
            "E. Second Movement",
    };

    private static final Set<Integer> firstPlayerPhases = Set.of(0, 1, 3, 4, 7);
    private ImageIcon backBtnIcon;
    private ImageIcon fwdBtnIcon;
    private ImageIcon backgroundIcon;
    private Map<String, String> htmlIcons = new HashMap<>();


    private JButton nextBtn;
    private JLabel infoLbl;
    private JButton prevBtn;

    @Override
    public void addTo(Buildable parent) {
        try {
            URL germanIconURL = GameModule.getGameModule().getDataArchive().getURL("images/german_icon.png");
            htmlIcons.put(GERMAN_SIDE, germanIconURL.toString());
        } catch (Exception e) {
            htmlIcons.put(GERMAN_SIDE, "");
        }
        try {
            URL sovietIcon = GameModule.getGameModule().getDataArchive().getURL("images/soviet_icon.png");
            htmlIcons.put(SOVIET_SIDE, sovietIcon.toString());
        } catch (Exception e) {
            htmlIcons.put(SOVIET_SIDE, "");
        }

        final ImageOp sop1 = Op.load("btn_step_back.svg");
        if (sop1.getImage() != null) {
            backBtnIcon = new ImageIcon(new OwningOpMultiResolutionImage(sop1));
        }
        final ImageOp sop2 = Op.load("btn_step_forward.svg");
        if (sop2.getImage() != null) {
            fwdBtnIcon = new ImageIcon(new OwningOpMultiResolutionImage(sop2));
        }
        final ImageOp sop3 = Op.load("turn_caption.png");
        if (sop3.getImage() != null) {
            backgroundIcon = new ImageIcon(new OwningOpMultiResolutionImage(sop3));
        }

        GameModule mod = GameModule.getGameModule();

        mod.addCommandEncoder(this);
        mod.getGameState().addGameComponent(this);

        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        layout.setAlignment(FlowLayout.LEFT);
        panel.setLayout(layout);

        prevBtn = new JButton(backBtnIcon);
        prevBtn.setDisabledIcon(fwdBtnIcon);
        prevBtn.setToolTipText("Previous phase...");
        prevBtn.addActionListener(e -> retractPhase());
        panel.add(prevBtn);

        JLabel background=new JLabel(backgroundIcon);
        background.setLayout(null);
        panel.add(background);

        infoLbl =  new JLabel();
        infoLbl.setOpaque(false);
        infoLbl.setBounds(6, 2, 188, 25);
        infoLbl.setHorizontalAlignment(SwingConstants.CENTER);

        background.add(infoLbl);

        nextBtn = new JButton(fwdBtnIcon);
        nextBtn.setDisabledIcon(backBtnIcon);
        nextBtn.setToolTipText("Next phase...");
        nextBtn.addActionListener(e -> advancePhase());
        panel.add(nextBtn);

        mod.getToolBar().add(panel);
    }

    private String getInfo(int currentTurn, int phase) {
        String phaseSide = getPhaseSide(phase);
        String icon = htmlIcons.get(phaseSide);
        return String.format("<html><table border=\"0\"  valign=\"middle\"><tr valign=\"middle\"><td valign=\"middle\">Turn %d.</td> <td valign=\"middle\"><img src=\"%s\"></td><td valign=\"middle\">%s</td></tr></table><html>", currentTurn, icon, phases[phase]);
    }

    public String getTooltip(int currentTurn, int phase) {
        String phaseSide = getPhaseSide(phase);
        return String.format("Turn %s. %s. [%s]", currentTurn, toolTipPhases[phase], phaseSide);
    }

    private void updatePhase() {
        int finalTurn = getNumberOfTurns();
        int phaseCount = getPhaseCounter();
        boolean nextEnable = phaseCount < finalTurn*10;
        nextBtn.setEnabled(nextEnable);
        boolean prevEnable = phaseCount > 0;
        prevBtn.setEnabled(prevEnable);
        int t = phaseCount / 10 + 1;
        int ph = phaseCount  % 10;
        infoLbl.setText(getInfo(t, ph));
        infoLbl.setToolTipText(getTooltip(t, ph));
    }

    private String getPhaseSide(int phase) {
        String firstSide = getSideToMoveFirst();
        String secondSide = GERMAN_SIDE.equals(firstSide) ? SOVIET_SIDE : GERMAN_SIDE;
        return firstPlayerPhases.contains(phase) ? firstSide : secondSide;
    }

    @Override
    public void setup(boolean gameStarting) {
        if (!gameStarting) {
            return;
        }
        updatePhase();
        GameModule.getGameModule().getMutableProperty(CURRENT_PHASE).addMutablePropertyChangeListener(evt -> updatePhase());
        GameModule.getGameModule().getMutableProperty(SIDE_TO_MOVE_FIRST).addMutablePropertyChangeListener(evt -> updatePhase());
    }

    public int getNumberOfTurns() {
        GameModule mod = GameModule.getGameModule();
        return Integer.parseInt(mod.getProperty(NUMBER_OF_TURNS).toString());
    }

    public String getSideToMoveFirst() {
        GameModule mod = GameModule.getGameModule();
        return mod.getProperty(SIDE_TO_MOVE_FIRST).toString();
    }

    private int getPhaseCounter() {
        GameModule mod = GameModule.getGameModule();
        return Integer.parseInt(mod.getProperty(CURRENT_PHASE).toString());
    }

    public int getCurrentPhase() {
        return getPhaseCounter() % 10;
    }

    public int getCurrentTurn() {
        return (getPhaseCounter() / 10) + 1;
    }

    private String logCurrentPhase(String prefix) {
        return "<span style=\"color: #9323F6;\">"
                + prefix + getTooltip(getCurrentTurn(), getCurrentPhase())
                + "</span>";
    }

    public void advancePhase() {
        int counter = getPhaseCounter();
        int finalTurn = getNumberOfTurns();

        if (counter >= finalTurn * 10) {
            return;
        }
        counter++;
        GameModule mod = GameModule.getGameModule();
        Command cmd = mod.getMutableProperty(CURRENT_PHASE).setPropertyValue(Integer.toString(counter));

        String newPhaseMsg = logCurrentPhase("Game advanced to: ");
        Command newPhaseMsgCmd = new Chatter.DisplayText(mod.getChatter(), "* " + newPhaseMsg);
        newPhaseMsgCmd.execute();
        cmd = cmd.append(newPhaseMsgCmd);

        mod.sendAndLog(cmd);
    }

    public void retractPhase() {
        int counter =  getPhaseCounter();
        if (counter == 0) {
            return;
        }
        counter--;
        GameModule mod = GameModule.getGameModule();
        Command cmd = mod.getMutableProperty(CURRENT_PHASE).setPropertyValue(Integer.toString(counter));

        String backPhaseMsg = logCurrentPhase("Game retracted to: ");
        Command backPhaseMsgCmd = new Chatter.DisplayText(mod.getChatter(), "* " + backPhaseMsg);
        backPhaseMsgCmd.execute();
        cmd = cmd.append(backPhaseMsgCmd);

        mod.sendAndLog(cmd);
    }
}
