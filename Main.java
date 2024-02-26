import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

	//declare and initialize the frame
    static JFrame f = new JFrame("A* Pathfinder");
    public static int DELAY = 17; // Frame time in milliseconds

    // Game Object
    private static SearchGame game;

    // Menu bar
    private static JMenuBar menuBar;

    // Button Action Listener
    private static ActionListener buttonListener;

    // Slider change listener
    private static ChangeListener changeListener;

    // Button Panel
    private static ButtonPanel buttonPanel;

    // Frame Timer
    private static Timer frameTimer;


    public static void main(String[] args) {

		//make it so program exits on close button click
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        

        // Create menubar
        Main.menuBar = new JMenuBar();


        // Add game object to frame
        f.setContentPane(createContentPane());

        
        // Start frame timer once it is initialized
        frameTimer.start();
        
        //the size of the game will be 810x810, the size of the JFrame needs to be slightly larger
        f.setSize(816,850);

        f.setLocationRelativeTo(null);

		//show the window
        f.setVisible(true);

	}

    public static Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        
        //Create game area
        game = new SearchGame();

        game.setMaximumSize(new Dimension(800, 800));

        //add a frame timer object
        frameTimer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //repaint the screen
                game.repaint();
                
            }
        });
        
        // Create action listener for button panel
        Main.buttonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switch(e.getActionCommand()){
                    case "start":
                        if(game.isBoardValid()){
                            Main.buttonPanel.setState(1);
                            Main.menuBar.setVisible(false);
                            game.startGame();
                            game.setFrameAdvanceTime((float)(double)Main.buttonPanel.frameAdvanceTime.getValue());
                        }
                        else{
                            Main.buttonPanel.startWarning();
                        }
                        break;
                    case "frameAdvance":
                        game.advanceFrame();
                        game.setPauseState(true);
                        Main.buttonPanel.setPauseState(true);
                        break;
                    case "playPause":
                        game.setPauseState(!Main.buttonPanel.updateToggleText());
                        break;
                    case "stop":
                        Main.buttonPanel.updateToggleText();
                        Main.buttonPanel.setState(0);
                        Main.menuBar.setVisible(true);
                        game.setPauseState(true);
                        Main.buttonPanel.setPauseState(true);
                        game.exitGame();
                        break;
                    default:
                        break;
                }
            }
        };

        Main.changeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                game.setFrameAdvanceTime((float)(double)Main.buttonPanel.frameAdvanceTime.getValue());
            }
        };

        // Create button panel
        Main.buttonPanel = new ButtonPanel(Main.buttonListener, Main.changeListener);

        //Add the game area to the content pane.
        contentPane.add(game, BorderLayout.CENTER);

        //Add button panel to content pane
        contentPane.add(Main.buttonPanel, BorderLayout.SOUTH);

        // First parent menu option (edit).
        JMenu menu = new JMenu("Edit");
        menuBar.add(menu);

        //a group of JMenuItems
        JMenuItem clearItem = new JMenuItem("Clear All Cells");
        clearItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                game.clearBoard();
            }
        });

        menu.add(clearItem);

        // Add menubar to frame
        f.setJMenuBar(menuBar);
 
        return contentPane;
    }
}