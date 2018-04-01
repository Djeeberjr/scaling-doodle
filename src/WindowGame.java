import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Das Spiel als Fenster anzeigen
 */
public class WindowGame extends JFrame implements ActionListener, IGame, IOutput {

    private Game game;
    private IOutput output;
    private JButton buttons[];

    WindowGame() {
        this.game = new Game(8,8);
        this.output = this;
        this.buttons = new JButton[8*8];
    }

    private void setupWindow() {
        // set up window
        setTitle("Scaling Doodle");
        setSize(900, 600);
        setLocationRelativeTo(null); // centered
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
        JLabel scoresDisplay = new JLabel();
        scoresDisplay.setVerticalAlignment(JLabel.TOP);
        scoresDisplay.setText("<html><div style='text-align: center;'>-= Scores =-</div><br>1. profi<br>2. noob.</html>");
        scoresArea.add(scoresDisplay);

        // fill the game area with buttons
        gameArea.setLayout(new GridLayout(game.getHeight(), game.getWidth()));
        for(int i = 0; i < game.getWidth()*game.getHeight(); i++) {
            JButton button = new JButton();
            button.setActionCommand(""+i);
            button.addActionListener(this);
            button.setEnabled(i % 3 == 0); // TODO: remove! just for testing
            buttons[i] = button;
            gameArea.add(button);
        }

        // put everything together
        cp.setLayout(new BorderLayout());
        cp.add(gameArea, BorderLayout.CENTER);
        cp.add(scoresArea, BorderLayout.LINE_END);

        // update everything once
        this.draw(game);

        setVisible(true);
    }

    @Override //ActionListener
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();

        // handle the button press

        // redraw
        draw(game);
    }

    @Override //IGame
    public void runGame() {
        setupWindow();
    }

    @Override //IOutput
    public void draw(Game game) {

        // this function should update the text and enabledness of all the buttons + scores

        for(int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setBackground(null);

            // check for a player on this field
            Player playerOnField = game.getPlayerOn(i);
            Fraction field = game.getFieldIndex(i);
            if(playerOnField != null) {
                button.setText(playerOnField.getFigure());
                button.setEnabled(false);
                // highlight players
                if(playerOnField == game.getCurrentPlayer())
                    button.setBackground(Color.CYAN);
                else
                    button.setBackground(Color.WHITE);
                button.setVisible(true);
            } else if(field == null) {
                // no player and no fraction; hide the button.
                button.setEnabled(false);
                button.setVisible(false);
            } else {
                // there is still a fraction here, display it
                button.setText(
                        "<html><div style='text-align: center; font: bold \"Noto Sans\" monospace;'>"
                                +field.getNumerator() + "<br>"
                                +"──" + "<br>"
                                +field.getDenominator()
                                +"</div></html>");

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

    }

}