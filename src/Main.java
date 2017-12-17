import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game(8, 8);

        while(true) {
            Output.draw(game);
            char input = Input.getInput();

            if(input == 'c'){
                System.out.println("Good Bye");
                break;
            }

            game.processInput(Input.getMove(input));

            if(game.getWinner() > 0){
                Output.draw(game);
                System.out.println("We have a winner!");
                break;
            }
        }

    }
}
