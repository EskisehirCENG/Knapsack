package edu.estu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Generate one million random integers and find the maximum of them.
 * Implement both sequential and parallel version.
 * Create two variants of the parallel version: (i) using synchronized block and (ii) AtomicVariables
 * Of course be aware of any race condition in the parallel versions.
 */
public class OneMillionRandom {

    static void printInts(int... args) {
        for (int a : args) {
            System.out.println(a);
        }
    }

    public static void main(String[] args) throws IOException {

        printInts(sequential(),
                sequentialAccum(),
                synch(),
                synchAccum(),
                atomic(),
                atomicAccum(),
                atomic2(),
                atomicAccum2());

        Path root = Paths.get("problemInstances/");
        try (Stream<Path> stream = Files.find(root, 5, (t, u) -> u.isRegularFile() && "test.in".equals(t.getFileName().toString()))) {
            stream.parallel().forEach(System.out::println);
        }
    }


    public static int sequential() {
        int max = -1;
        for (int i = 0; i < 1_000_000; i++) {
            int aRandomInt = new Random().nextInt();
            if (aRandomInt > max)
                max = aRandomInt;
        }
        return max;
    }

    public static int sequentialAccum() {
        int max = -1;
        for (int i = 0; i < 1_000_000; i++) {
            max = Math.max(max, new Random().nextInt());
        }
        return max;
    }


    public static int atomic2() {
        AtomicReference<Integer> max = new AtomicReference<>(-1);

        IntStream.range(0, 1_000_000).boxed().parallel().forEach(i -> {
            max.getAndUpdate(z -> Math.max(z, new Random().nextInt()));
        });

        return max.getPlain();
    }

    public static int atomicAccum2() {

        AtomicReference<Integer> max = new AtomicReference<>(-1);

        IntStream.range(0, 1_000_000).boxed().parallel().forEach(i -> {
            max.accumulateAndGet(new Random().nextInt(), Math::max);
        });

        return max.getPlain();
    }


    public static int synch() {
        Random rand = new Random();
        final Integer[] max = {-1};

        IntStream.range(0, 1_000_000).boxed().parallel().forEach(i -> {

            Integer aRandomInt = rand.nextInt();
            synchronized (rand) {
                if (aRandomInt > max[0])
                    max[0] = aRandomInt;
            }
        });

        return max[0];
    }

    public static int synchAccum() {
        final Integer[] max = {-1};
        IntStream.range(0, 1_000_000).boxed().parallel().forEach(i -> {
            synchronized (max[0]) {
                max[0] = Math.max(max[0], new Random().nextInt());
            }
        });

        return max[0];
    }

    public static int atomic() {
        AtomicInteger max = new AtomicInteger(-1);

        IntStream.range(0, 1_000_000).boxed().parallel().forEach(i -> {
            max.updateAndGet(z -> Math.max(z, new Random().nextInt()));
        });

        return max.getPlain();
    }

    public static int atomicAccum() {
        AtomicInteger max = new AtomicInteger(-1);

        IntStream.range(0, 1_000_000).boxed().parallel().forEach(i -> {
            max.accumulateAndGet(new Random().nextInt(), Math::max);
        });

        return max.getPlain();
    }

}
