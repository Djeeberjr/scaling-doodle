import java.io.IOException;
import java.util.Scanner;

public class Input {

    static Scanner sc = new Scanner(System.in);

    public static char getInput() throws IOException{
        String s;
        while(true){
            s = sc.nextLine();

            if(s.length() >= 1){
                return s.charAt(0);
            }
        }


    }

    public static Move getMove(char input){
        switch (input){
            case 'w':
                return Move.UP;
            case 'a':
                return Move.LEFT;
            case 's':
                return Move.DOWN;
            case 'd':
                return Move.RIGHT;
            default:
                return Move.INVALID;
        }
    }
}
