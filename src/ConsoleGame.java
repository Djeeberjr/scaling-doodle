/**
 * Contains the main game loop and connects the single components input, output and logic
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.0 08.01.18
 */
public class ConsoleGame {
    private Game game;

    ConsoleGame(){
        game = new Game(8,8);
    }

    public void run() {

        while(true) {

            try{
                Output.draw(game);
            }catch (InterruptedException e){
                break;
            }

            if(game.getWinner() >= 0){
                break;
            }

            char input = Input.getInput();

            if(input == 'c'){
                System.out.println("Good Bye");
                break;
            }

            game.processInput(Input.getMove(input));
        }
    }

}
