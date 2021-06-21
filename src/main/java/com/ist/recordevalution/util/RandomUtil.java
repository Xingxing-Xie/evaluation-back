package com.ist.recordevalution.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomUtil {
    public static List<Long> randomList(int size, long min, long max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.longs(size, min, max)
            .boxed()
            .collect(Collectors.toList());
    }

    public static Integer randomInt(int min, int max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(min, max);
    }

    public static Double randomDouble(double min, double max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextDouble(min, max);
    }
}
