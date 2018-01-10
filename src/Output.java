import java.io.IOException;

/**
 * Handles console output (draws the game)
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.8 09.01.18
 */
public class Output {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    public static void draw(Game game) throws InterruptedException {
        final int height = game.getHeight();
        final int width = game.getWidth();

        clearScreen();
        System.out.println();

        // draw end screen if a player won
        int winner = game.getWinner();
        if(winner >= 0) {
            System.out.println();
            System.out.println();
            System.out.println("       + + + + + + + + + + + + + + + + + + + + + + + + + + + +");
            System.out.println("       +                                                     +");
            if(winner > 0)
                System.out.println("       +                 WE'VE GOT A WINNER!                 +");
            else
                System.out.println("       +                   GAME HAS ENDED!                   +");
            System.out.println("       +                                                     +");
            System.out.println("       + + + + + + + + + + + + + + + + + + + + + + + + + + + +");
            System.out.println();

            if(winner > 0) {
                winner--;
                System.out.println(String.format("   Congratulations, %s Player %d! You won with %.2f of %d score!",
                        game.getPlayer(winner).getFigure(), winner+1, game.getPlayer(winner).score.doubleValue(), Game.WIN_SCORE));
            } else {
                System.out.println("   Drawn! No more winning is possible.");
                System.out.println("   There's not enough score left on the field for anyone to reach " + Game.WIN_SCORE + ".");
            }

            // print all the scores
            System.out.println();
            System.out.println("SCORES");
            System.out.println("============");
            for (int i = 0; i < Game.NUM_PLAYERS; i++) {
                Player p = game.getPlayer(i);
                System.out.println(String.format("%s Player %d: %s (%.2f)", p.getFigure(), i+1, p.score.toString(), p.score.doubleValue()));
            }

            System.out.println();
            System.out.println();
            System.out.println();

            return;
        }

        // draw game
        drawGridTop(width);
        drawMatrixTop(width);
        for(int y = 0; y < height; y++) {
            drawLineTop(game, y); drawRightColumn(game, y*4+0);
            drawLineMiddle(game, y); drawRightColumn(game, y*4+1);
            drawLineBottom(game, y); drawRightColumn(game, y*4+2);
            if(y < height-1) {
                drawMatrixLineSep(width); drawRightColumn(game, y*4+3);
            }
        }
        drawMatrixBottom(width);
        System.out.println();

        drawLastEvent(game);
        drawCurrentPlayer(game);

        // make sure that everything has been written out before returning
        System.out.flush();
    }

    private static void drawRightColumn(Game game, int lineY) {
        System.out.print("  ");
        if(lineY == 0) {
            System.out.print("Score Limit: " + Game.WIN_SCORE);
        } else if(lineY == 1) {
            System.out.printf("Value left: %.2f", game.getFieldSum().doubleValue());
        } else if(lineY >= 3 && lineY-3 < game.getNumPlayers()) {
            int i = lineY-3;
            Player p = game.getPlayer(i);
            //System.out.printf("%s Player %d: %s (%.2f)", p.getFigure(), i, p.score.toString(), p.score.doubleValue());
            System.out.printf("%s Player %d: %.2f", p.getFigure(), i+1, p.score.doubleValue());
            if(game.getCurrentPlayer() == p) {
                System.out.print(" ««");
            }
        } else if((lineY-=game.getNumPlayers()) == 4) {
            // XXX: add more info
        } else if(lineY == 5) {
            // XXX: add even more info
        }

        System.out.println();
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
        for (int x = 0; x < game.getWidth(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                Fraction frac = game.getFieldPos(x, lineY);
                if(frac == null) {
                    // no fraction here; draw eternal void
                    System.out.print("  ");
                } else {
                    System.out.printf("%2d", frac.getNumerator());
                }
            } else {
                // player here, draw no fraction
                System.out.print("  ");
            }
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
    }

    private static void drawLineMiddle(Game game, int lineY) {
        // draw left matrix wall
        drawMatrixLeftWall(lineY+1);
        drawFracSep();

        // draw all fractions and players
        for (int x = 0; x < game.getWidth(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction seperator
                if(game.getFieldPos(x, lineY) == null) {
                    // no fraction here; draw eternal void
                    System.out.print("  ");
                } else {
                    System.out.print("──");
                }
            } else {
                // player on this field, draw him
                System.out.print(p.getFigure());
            }
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
    }

    private static void drawLineBottom(Game game, int lineY) {
        // draw left matrix wall
        drawMatrixLeftWall(0);
        drawFracSep();

        // draw all fractions
        for (int x = 0; x < game.getWidth(); x++) {
            Player p = game.getPlayerAt(x, lineY);
            if(p == null) {
                // no player here; draw fraction
                Fraction frac = game.getFieldPos(x, lineY);
                if(frac == null) {
                    // no fraction here; draw eternal void
                    System.out.print("  ");
                } else {
                    System.out.printf("%2d", frac.getDenominator());
                }
            } else {
                // player here, draw no fraction
                System.out.print("  ");
            }
            drawFracSep();
        }

        // draw right matrix wall
        System.out.print("│");
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