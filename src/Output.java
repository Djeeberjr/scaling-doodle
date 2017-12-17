import java.io.IOException;

//Henri
public class Output {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    public static void draw(Game game){
        final int height = game.getSizeY();
        final int width = game.getSizeX();

        clearScreen();
        drawMatrixTop(width);
        for(int y = 0; y < height; y++) {
            drawLineTop(game, y);
            drawLineMiddle(game, y);
            drawLineBottom(game, y);
            if(y < height-1)
                drawMatrixLineSep(width);
        }
        drawMatrixBottom(width);

        // make sure that everything has been written out before returning
        System.out.flush();
    }

    private static void drawMatrixTop(int numFracsPerLine) {
        // draw matrix top left corner
        System.out.print("┌");
        drawFracSep();

        // padding
        for (int i = 0; i < numFracsPerLine; i++) {
            System.out.print("  ");
            drawFracSep();
        }

        // draw matrix top right corner
        System.out.print("┐");
        System.out.println();
    }

    private static void drawLineTop(Game game, int lineY) {
        // draw left matrix wall
        System.out.print("│");
        drawFracSep();

        // draw all fractions
        for (int x = 0; x < game.getSizeX(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                System.out.printf("%2d", game.getFieldPos(x, lineY).getNumerator());
            } else {
                // player here, draw no fraction
                System.out.print("  ");
            }
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
        System.out.println();
    }

    private static void drawLineMiddle(Game game, int lineY) {
        // draw left matrix wall
        System.out.print("│");
        drawFracSep();

        // draw all fractions and players
        for (int x = 0; x < game.getSizeX(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                System.out.print("──");
            } else {
                // player on this field, draw him
                System.out.printf("%c%s", p.name, p.name);
            }
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
        System.out.println();
    }

    private static void drawLineBottom(Game game, int lineY) {
        // draw left matrix wall
        System.out.print("│");
        drawFracSep();

        // draw all fractions
        for (int x = 0; x < game.getSizeX(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                System.out.printf("%2d", game.getFieldPos(x, lineY).getDenominator());
            } else {
                // player here, draw no fraction
                System.out.print("  ");
            }
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
        System.out.println();
    }

    private static void drawMatrixLineSep(int numFracsPerLine) {
        // draw left matrix wall
        System.out.print("│");
        drawFracSep();

        // padding
        for (int i = 0; i < numFracsPerLine; i++) {
            System.out.print("  ");
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
        System.out.println();
    }

    private static void drawMatrixBottom(int numFracsPerLine) {
        // draw matrix top bottom left corner
        System.out.print("└");
        drawFracSep();

        // padding
        for (int i = 0; i < numFracsPerLine; i++) {
            System.out.print("  ");
            drawFracSep();
        }

        // draw matrix bottom right corner
        System.out.print("┘");
        System.out.println();
    }

    private static void drawFracSep() {
        System.out.print("   ");
    }


    private static void clearScreen() {
        try {
            if (IS_WINDOWS)
                Runtime.getRuntime().exec("cls");
            else
                Runtime.getRuntime().exec("sh -c 'clear;clear'");
        } catch (IOException e) {
            System.out.println(e);
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}