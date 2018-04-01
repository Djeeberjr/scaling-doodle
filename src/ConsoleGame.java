/**
 * Contains the main game loop and connects the single components input, output and logic
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.0 08.01.18
 */
public class ConsoleGame implements IGame {
    private Game game;
    private IOutput output;

    ConsoleGame(){
        game = new Game(8,8);
        output = new ConsoleOutput();
    }

    public void runGame() {

        while(true) {

            try{
                output.draw(game);
            }catch (InterruptedException e){
                break;
            }

            if(game.getWinner() >= 0){
                break;
            }

            char input = ConsoleInput.getInput();

            if(input == 'c'){
                System.out.println("Good Bye");
                break;
            }

            game.processInput(ConsoleInput.getMove(input));
        }
    }

}
