import java.io.IOException;

/**
 * Handles console output and makes the game look great
 */
public class Output {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    public static void draw(Game game) throws InterruptedException {
        final int height = game.getSizeY();
        final int width = game.getSizeX();

        clearScreen();
        drawGridTop(width);
        drawMatrixTop(width);
        for(int y = 0; y < height; y++) {
            drawLineTop(game, y);
            drawLineMiddle(game, y);
            drawLineBottom(game, y);
            if(y < height-1)
                drawMatrixLineSep(width);
        }
        drawMatrixBottom(width);
        System.out.println();

        drawLastEvent(game);
        drawCurrentPlayer(game);

        // make sure that everything has been written out before returning
        System.out.flush();
    }

    private static void drawGridTop(int numFracsPerLine) {
        // draw matrix top left corner
        System.out.print("         ");
        drawFracSep();

        // padding
        for (int i = 0; i < numFracsPerLine; i++) {
            System.out.print(String.format("%2d", i+1));
            drawFracSep();
        }

        System.out.println();
    }

    private static void drawMatrixTop(int numFracsPerLine) {
        // draw matrix top left corner
        System.out.print("        ┌");
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
        drawMatrixLeftWall(0);
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
        drawMatrixLeftWall(lineY+1);
        drawFracSep();

        // draw all fractions and players
        for (int x = 0; x < game.getSizeX(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                System.out.print("──");
            } else {
                // player on this field, draw him
                System.out.print(p.getFigure());
            }
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
        System.out.println();
    }

    private static void drawLineBottom(Game game, int lineY) {
        // draw left matrix wall
        drawMatrixLeftWall(0);
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
        drawMatrixLeftWall(0);
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
        System.out.print("        └");
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

    private static void drawMatrixLeftWall(int rowId) {
        if(rowId > 0) {
            System.out.print(String.format("%7d ", rowId));
        } else {
            System.out.print("        ");
        }
        System.out.print("│");
    }

    private static void drawFracSep() {
        System.out.print("   ");
    }

    private static void drawLastEvent(Game game) {
        if(game.getLastEvent() != null) {
            System.out.println(game.getLastEvent());
            System.out.println();
        }
    }

    private static void drawCurrentPlayer(Game game) {
        System.out.println("Player at turn: " + game.getCurrentPlayer().getFigure());
    }


    private static void clearScreen() throws InterruptedException {
        try {
            if (IS_WINDOWS)
            {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else
            {
                new ProcessBuilder("sh", "-c", "clear").inheritIO().start().waitFor();
            }
        } catch (IOException e) {
            System.out.println(e);
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        } catch (InterruptedException e) {
            throw e;
        }
    }
}