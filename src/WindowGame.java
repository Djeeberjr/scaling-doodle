import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Das Spiel als Fenster anzeigen
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.0 05.04.2018
 */

public class WindowGame extends JFrame implements ActionListener, KeyListener, IGame, IOutput {

    private Game game;
    private IOutput output;
    private JButton buttons[];
    private JLabel scoresDisplay;
    private ImageIcon playerIcons[];

    WindowGame() {
        this.game = new Game(8,8);
        this.output = this;
        this.buttons = new JButton[8*8];

    }

    private void updateScore(){
        this.scoresDisplay.setText("<html><div style='text-align: center;'><u>Scores</u> (Needed: " + Game.WIN_SCORE + ")</div><br>Mario: "
                + String.format("%.2f", game.getPlayer(0).score.doubleValue())
                +" <br>Luigi: "
                + String.format("%.2f", game.getPlayer(1).score.doubleValue())
                +" </html>");
    }

    private void setupIcons(){
        playerIcons = new ImageIcon[game.getNumPlayers()];

        for (int i = 0; i < game.getNumPlayers(); i++) {
            playerIcons[i] = Resources.getSprite(i);
            Image image = playerIcons[i].getImage(); // transform it
            Image newimg = image.getScaledInstance(buttons[0].getWidth(), buttons[0].getHeight(),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            playerIcons[i] = new ImageIcon(newimg);  // transform it back
        }
    }

    private void setupWindow() {
        // set up window
        setTitle("Scaling Doodle");
        setSize(900, 600);
        setMinimumSize(new Dimension(900,600));
        setLocationRelativeTo(null); // centered
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createMenuBar();

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(this);

        // build the gui

        /*
            gameArea   scoresArea
        |---------------------
        |             | scores
        |             |
        |    field    |
        |             |
        |             |
        |---------------------

         */
        Container cp = getContentPane();

        JPanel gameArea = new JPanel(); // at the right
        JPanel scoresArea = new JPanel(); // at the left

        // fill the scores area
        scoresArea.setLayout(new GridLayout());
        scoresArea.setPreferredSize(new Dimension(300, 600));

        // create the scores display
        scoresDisplay = new JLabel();
        scoresDisplay.setVerticalAlignment(JLabel.TOP);
        updateScore();
        scoresArea.add(scoresDisplay);
        generateButtons(gameArea);


        // put everything together
        cp.setLayout(new BorderLayout());
        cp.add(gameArea, BorderLayout.CENTER);
        cp.add(scoresArea, BorderLayout.LINE_END);


        // update everything once
        setVisible(true);
        //setVisible(false);
        setupIcons();

        this.draw(game);
        //setVisible(true);

    }

    private void generateButtons(JPanel gameArea) {
        // fill the game area with buttons
        gameArea.setLayout(new GridLayout(game.getHeight(), game.getWidth()));
        for(int i = 0; i < game.getWidth()*game.getHeight(); i++) {
            JButton button = new JButton();

            button.setOpaque(false);

            button.setActionCommand(""+i);
            button.addActionListener(this);
            button.setEnabled(i % 3 == 0); // remove! just for testing // Its now a feature
            buttons[i] = button;
            gameArea.add(button);
        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem;

        //Build the first menu.
        JMenu menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menu.getAccessibleContext().setAccessibleDescription("Game");
        menuBar.add(menu);

        WindowGame self = this;
        ActionListener listener = e -> {
            switch (e.getActionCommand()) {
                case "New":
                    new WindowGame().runGame();
                    break;
                case "Exit":
                    self.dispose();
                    break;
                case "Save":
                    saveGame();
                    break;
                case "Load":
                    loadGame();
                    break;
            }
        };

        // new
        menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(listener);
        menu.add(menuItem);



        // Save
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        // Save
        menuItem = new JMenuItem("Load");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        menu.addSeparator();

        // exit
        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        this.setJMenuBar(menuBar);
    }

    private void saveGame(){
        JFileChooser c = new JFileChooser();

        int rVal = c.showSaveDialog(this);

        if(rVal == JFileChooser.APPROVE_OPTION){
            //Save the game
            FileOutputStream fs;
            ObjectOutputStream os;
            try{
                fs = new FileOutputStream(c.getSelectedFile());
                os = new ObjectOutputStream(fs);
                os.writeObject(this.game);
                os.close();
            }catch (Exception e){
                //RIP
                System.out.println("RIP");
            }

        }
    }


    private void loadGame(){
        JFileChooser c = new JFileChooser();
        c.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        int rVal = c.showSaveDialog(this);

        if(rVal == JFileChooser.APPROVE_OPTION) {
            //Load Game
            FileInputStream fs;
            ObjectInputStream is;
            try{
                fs = new FileInputStream(c.getSelectedFile());
                is = new ObjectInputStream(fs);
                this.game = (Game)is.readObject();

                is.close();
                draw(game);
            }catch (Exception e){
                //RIP
                //System.out.println(e.getMessage());
            }

        }
    }

    @Override //ActionListener
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();

        // handle the button press
        int bx = game.indexToX(Integer.parseInt(button.getActionCommand()));
        int by = game.indexToY(Integer.parseInt(button.getActionCommand()));

        Player p = game.getCurrentPlayer();
        int px = p.x;
        int py = p.y;

        int dx = bx-px;
        int dy = by-py;

        game.processInput(dx, dy);

        // redraw
        draw(game);
    }

    @Override //IGame
    public void runGame() {
        setupWindow();
    }

    @Override //IOutput
    public void draw(Game game) {

        // update the text and enabledness of all the buttons + the scores
        for(int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setBackground(null);

            // check for a player on this field
            Player playerOnField = game.getPlayerOn(i);
            Fraction field = game.getFieldIndex(i);
            if(playerOnField != null) {
                //button.setText(playerOnField.getFigure());

                //Set texture
                button.setDisabledIcon(playerOnField == game.getPlayer(0) ? playerIcons[0] : playerIcons[1]);
                button.setIcon(playerOnField == game.getPlayer(0) ? playerIcons[0] : playerIcons[1]);
                button.setText("");
                button.setEnabled(false);
                // highlight players
                button.setBackground(playerOnField == game.getPlayer(0) ? Color.BLUE : Color.ORANGE);
                button.setVisible(true);
//            } else if(field == null) {
                // no player and no fraction; we can go there but nothing happens
//                button.setEnabled(true);
//                button.setVisible(true);
//                button.setText("");
            } else {
                // there is still a fraction here, display it
                button.setIcon(null);
                if(field != null) {
                    button.setText(
                            "<html><div style='text-align: center; font: bold \"Noto Sans\" monospace;'>"
                                    + field.getNumerator() + "<br>"
                                    + "──" + "<br>"
                                    + field.getDenominator()
                                    + "</div></html>");
                } else {
                    // field is null
                    button.setText("");
                    //Unset the texture
                    button.setDisabledIcon(null);



                }

                // check whether the button can be clicked
                // this is the tricky part; we have to determine whether the current player can go on this field
                Player currentPlayer = game.getCurrentPlayer();
                int fx = game.indexToX(i);
                int fy = game.indexToY(i);
                int px = currentPlayer.x;
                int py = currentPlayer.y;
                boolean clickable =
                        (py == fy && (fx == px-1 || fx == px+1)) // horizontal
                                || (px == fx && (fy == py-1 || fy == py+1)); // vertical
                button.setEnabled(clickable);
                button.setVisible(true);
            }
        }

        //Update score
        updateScore();

        // check if we've got a winner and show the endscreen
        int winner = game.getWinner();
        if(winner != 0) {
            String[] buttonMsgs = { "Play again", "Exit" };

            int rc;
            if(winner > 0) {
                rc = JOptionPane.showOptionDialog(
                        this,
                        "Player " + winner + " has won with a score of " +
                                String.format("%.2f", game.getPlayer(winner - 1).score.doubleValue()), "Game over!",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, playerIcons[winner - 1], buttonMsgs, buttonMsgs[0]);
            } else {
                rc = JOptionPane.showOptionDialog(
                        this,
                        "Draw! No one can reach " + Game.WIN_SCORE + " points anymore.", "Game over!",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttonMsgs, buttonMsgs[0]);
            }

            if(rc == 0) {
                new WindowGame().runGame();
            }
            this.dispose();
        }

    }

    @Override //KeyListner
    public void keyTyped(KeyEvent e) {
        //NOP
    }

    @Override //KeyListner
    public void keyPressed(KeyEvent e) {
        //NOP
    }

    @Override //KeyListner
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();

        switch (keycode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                game.processInput(Move.UP);
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                game.processInput(Move.DOWN);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                game.processInput(Move.RIGHT);
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                game.processInput(Move.LEFT);
                break;
            default:
                return;
        }

        draw(game);

    }

}
