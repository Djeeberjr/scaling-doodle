import java.io.IOException;

public class ConsoleGame {
    Game game;

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
