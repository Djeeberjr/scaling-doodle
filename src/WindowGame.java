import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Das Spiel als Fenster anzeigen
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
        this.scoresDisplay.setText("<html><div style='text-align: center;'><u>Scores</u> (Needed: " + Game.WIN_SCORE + ")</div><br>1. Mario: "
                + String.format("%.2f", game.getPlayer(0).score.doubleValue())
                +" <br>2. Luigi: "
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

        // create a
        scoresDisplay = new JLabel();
        scoresDisplay.setVerticalAlignment(JLabel.TOP);
        updateScore();
        scoresArea.add(scoresDisplay);

        // fill the game area with buttons
        gameArea.setLayout(new GridLayout(game.getHeight(), game.getWidth()));
        for(int i = 0; i < game.getWidth()*game.getHeight(); i++) {
            JButton button = new JButton();

            button.setOpaque(false);
            //button.setContentAreaFilled(false);
            //button.setBorderPainted(false);
            //button.setFocusPainted(false);


            button.setActionCommand(""+i);
            button.addActionListener(this);
            button.setEnabled(i % 3 == 0); // remove! just for testing // Its now a feature
            buttons[i] = button;
            gameArea.add(button);
        }

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
                    button.setIcon(null);


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

    @Override
    public void keyTyped(KeyEvent e) {
        //NOP
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //NOP

    }

    @Override
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
            }
        };

        // new
        menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
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

}
