package utils;

import java.util.Random;

public class RandomUtils {

    private final static RandomUtils instance = new RandomUtils();

    private final Random random;

    private RandomUtils() {
        random = new Random();
    }

    public static RandomUtils getInstance() {
        return instance;
    }

    public int nextInt() {
        return random.nextInt();
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public double nextDouble() {
        return random.nextDouble();
    }
}
