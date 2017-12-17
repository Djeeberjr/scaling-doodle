import java.io.IOException;

//Henri
public class Output {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    public static void draw(Game game){
        final int height = game.getSizeY();
        final int width = game.getSizeX();

        drawMatrixTop(width);
        for(int y = 0; y < height; y++) {
            drawLineTop(game, y);
            drawLineMiddle(game, y);
            drawLineBottom(game, y);
        }
        drawMatrixBottom(width);
    }

    private static void drawMatrixTop(int numFracsPerLine) {
        // draw matrix top left corner
        System.out.printf("%c ", 218);

        // padding
        for (int i = 0; i < numFracsPerLine; i++) {
            System.out.print("   ");
        }

        // draw matrix top right corner
        System.out.printf("%c", 191);
        System.out.println();
    }

    private static void drawLineTop(Game game, int lineY) {
        // draw left matrix wall
        System.out.printf("%c ", 179);

        // draw all fractions
        for (int x = 0; x < game.getSizeX(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                System.out.printf("%2d ", game.getFieldPos(x, lineY).getNumerator());
            } else {
                // player here, draw no fraction
                System.out.print("   ");
            }
        }

        // draw right matrix wall
        System.out.printf("%c", 179);
        System.out.println();
    }

    private static void drawLineMiddle(Game game, int lineY) {
        // draw left matrix wall
        System.out.printf("%c ", 179);

        // draw all fractions and players
        for (int x = 0; x < game.getSizeX(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                System.out.printf("%c%c ", 196, 196);
            } else {
                // player on this field, draw him
                System.out.printf("%c%s ", 177, p.name);
            }
        }

        // draw right matrix wall
        System.out.printf("%c", 179);
        System.out.println();
    }

    private static void drawLineBottom(Game game, int lineY) {
        // draw left matrix wall
        System.out.printf("%c ", 179);

        // draw all fractions
        for (int x = 0; x < game.getSizeX(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                System.out.printf("%2d ", game.getFieldPos(x, lineY).getDenominator());
            } else {
                // player here, draw no fraction
                System.out.print("   ");
            }
        }

        // draw right matrix wall
        System.out.printf("%c", 179);
        System.out.println();
    }

    private static void drawMatrixBottom(int numFracsPerLine) {
        // draw matrix top bottom left corner
        System.out.printf("%c ", 192);

        // padding
        for (int i = 0; i < numFracsPerLine; i++) {
            System.out.print("   ");
        }

        // draw matrix bottom right corner
        System.out.printf("%c", 217);
        System.out.println();
    }


    private void clearScreen() throws IOException {
        if(IS_WINDOWS)
            Runtime.getRuntime().exec("cls");
        else
            Runtime.getRuntime().exec("clear;clear");
    }
}