import java.util.Random;

public class Util {
    private static final Random rng = new Random(System.nanoTime());

    /**
     * Returns a random number in range [min..max]
     * @param min the smallest possible number (inclusive)
     * @param max the largest possible number (inclusive)
     * @return the random number
     */
    public static int randInt(int min, int max) {
        if(min > max) {
            throw new IllegalArgumentException(String.format("min (%d) is larger than max (%d)", min, max));
        } else if(min == max) {
            return min;
        }
        return rng.nextInt(max-min)+min;
    }
}
