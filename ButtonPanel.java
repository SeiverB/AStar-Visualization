import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.Timer;

public class ButtonPanel extends JPanel{

    protected JButton b1;

    public JButton start, frameAdvance, stop;
    public JToggleButton playPause;
    public JSpinner frameAdvanceTime;
    public JPanel spinnerFrame;
    public JLabel editorInstr, simulatorInstr;
    
    private Timer warningTimer;

    public ButtonPanel(ActionListener actionListener, ChangeListener changeListener){

        // Layout for buttons on panel
        this.setLayout(new GridBagLayout());
        this.setSize(getPreferredSize());

        GridBagConstraints c = new GridBagConstraints();

        this.editorInstr = new JLabel("""
            <HTML><tag><font size="3">
            LEFT CLICK a cell to cycle through tile types. Open = Grey, Grass = Light Green, Swamp = Dark Grey, Obstacle = Black<br/>
            RIGHT CLICK a cell to place the Goal or Start Node, as indicated by your mouse cursor. (Tiles are labelled with G and S respectively)
            <br/>
            </font></tag></HTML>""");
        editorInstr.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10; // make taller
        c.insets = new Insets(0, 5, 0, 0);
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        editorInstr.setVisible(true);
        add(editorInstr, c);

        this.simulatorInstr = new JLabel("""
            <HTML><tag><font size="3">
            The simulation above shows the pathfinder's current progress. The tile that is currently being analyzed is coloured magenta,
            and any tiles that are in the "open" list are coloured red. Once a candidate path is found, it will be denoted by a yellow line.<br/>
            To start the simulation at the current autoplay rate, press the start button. 
            The "Frame Advance" button may be used to move the pathfinder forward one step.
            <br/>
            </font></tag></HTML>""");
        simulatorInstr.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10; // make taller
        c.insets = new Insets(0, 5, 0, 0);
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        simulatorInstr.setVisible(false);
        add(simulatorInstr, c);

        this.start = new JButton("Start Pathfinding Simulation");
        start.setVerticalTextPosition(AbstractButton.CENTER);
        start.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        start.setActionCommand("start");
        start.addActionListener(actionListener);
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 20; // make taller
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        start.setVisible(true);
        add(start, c);


        this.playPause = new JToggleButton("Play");
        playPause.setMaximumSize(playPause.getPreferredSize());
        playPause.setMinimumSize(playPause.getPreferredSize());
        playPause.setVerticalTextPosition(AbstractButton.CENTER);
        playPause.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        playPause.setActionCommand("playPause");
        playPause.addActionListener(actionListener);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 20;
        c.ipady = 20; // make taller
        c.weightx = 0.3;
        c.gridx = 0;
        c.gridy = 1;
        playPause.setVisible(false);
        add(playPause, c);

        // Frame for frame advance rate spinner
        this.spinnerFrame = new JPanel();
        spinnerFrame.setLayout(new BoxLayout(this.spinnerFrame, BoxLayout.Y_AXIS));

        // Create Label for spinner
        JLabel spinnerLabel = new JLabel("Autoplay Rate (seconds)");
        spinnerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create spinner, and add change listener
        this.frameAdvanceTime = new JSpinner(new SpinnerNumberModel(0.2, 0, 5, 0.1));
        frameAdvanceTime.addChangeListener(changeListener);
        
        JComponent comp = frameAdvanceTime.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);

        spinnerFrame.add(spinnerLabel);
        spinnerFrame.add(this.frameAdvanceTime);
        frameAdvanceTime.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.ipady = 0; // make taller
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.125;
        c.gridx = 1;
        c.gridy = 1;
        spinnerFrame.setVisible(false);
        add(spinnerFrame, c);


        this.frameAdvance = new JButton("Frame Advance");
        frameAdvance.setVerticalTextPosition(AbstractButton.CENTER);
        frameAdvance.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        frameAdvance.setActionCommand("frameAdvance");
        frameAdvance.addActionListener(actionListener);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;
        c.ipady = 20; // make taller
        c.weightx = 0.125;
        c.gridx = 2;
        c.gridy = 1;
        frameAdvance.setVisible(false);
        add(frameAdvance, c);

        this.stop = new JButton("Exit Simulation (Edit Mode)");
        stop.setVerticalTextPosition(AbstractButton.CENTER);
        stop.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        stop.setActionCommand("stop");
        stop.addActionListener(actionListener);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 20; // make taller
        c.weightx = 0.3;
        c.gridx = 3;
        c.gridy = 1;
        stop.setVisible(false);
        add(stop, c);


        this.warningTimer = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.setText("Start Pathfinding Simulation");
                start.setBackground(new JButton().getBackground());
            }
        });

    }

    public void startWarning(){
        start.setText("[WARNING] Missing start and/or goal!");
        start.setBackground(Color.RED);
        this.warningTimer.restart();
    }

    public void setState(int state){
        switch(state){
            case 0:
                this.start.setVisible(true);
                this.playPause.setVisible(false);
                this.frameAdvance.setVisible(false);
                this.stop.setVisible(false);
                this.spinnerFrame.setVisible(false);
                this.editorInstr.setVisible(true);
                this.simulatorInstr.setVisible(false);
                break;
            case 1:
                this.start.setVisible(false);
                this.playPause.setVisible(true);
                this.frameAdvance.setVisible(true);
                this.stop.setVisible(true);
                this.spinnerFrame.setVisible(true);
                this.editorInstr.setVisible(false);
                this.simulatorInstr.setVisible(true);
        }
    }

    public void setPauseState(Boolean state){
        this.playPause.setSelected(!state);
        updateToggleText();
    }

    // Returns false is game is paused, true if game is running
    public boolean updateToggleText(){
        if(this.playPause.isSelected()){
            this.playPause.setText("Pause");
            return true;
        }
        else{
            this.playPause.setText("Play");
            return false;
        }

    }

}
