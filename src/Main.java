/**
 *     MaxFrac Game
 *       an IProg project
 *
 * This is the main class to be executed from command line
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 *
 * @version 2017-12-21
 */
public class Main {
    public static void main(String[] args) {

        String type = "w";
        if(args.length > 0)
            type = args[0].toLowerCase();



        IGame game;
        switch (type.charAt(0)) {
            case 'c':
                game = new ConsoleGame();
                break;
            case 'w':
                game = new WindowGame();
                break;
            default:
                System.out.println("Type '" + type + "' does not exist; try 'console' or 'window'");
                return;
        }

        game.runGame();
    }

}
