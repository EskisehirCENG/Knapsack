package edu.estu;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * The 0–1 knapsack problem is a classical NP-hard optimization problem.
 * In this problem, one is given a knapsack with an integer capacity c and a set of n items,
 * which each have an integer profit p_i and an integer weight w_i.
 * The goal is to select a subset of items to put into the knapsack such
 * that the total value is maximized and the total weight does not
 * exceed the knapsack capacity.
 */
public class KP01 implements IKS {

    private final long capacity;
    private final HashSet<Item> items = new HashSet<>();

    @Override
    public Set<Item> items() {
        return items;
    }

    @Override
    public long capacity() {
        return capacity;
    }

    private String tag;

    @Override
    public String tag() {
        return tag;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    public KP01(long capacity, Collection<Item> items) {
        this.capacity = capacity;
        this.items.addAll(items);
    }

    public KP01(long capacity) {
        this.capacity = capacity;
    }

    static KP01 fromFile(Path path) {

        try (Scanner scanner = new Scanner(path)) {

            int numberOfItems = scanner.nextInt();
            HashSet<Item> items = new HashSet<>(numberOfItems);

            for (int i = 0; i < numberOfItems; i++) {
                items.add(new Item(scanner.nextInt(), scanner.nextLong(), scanner.nextLong()));
            }

            long capacity = scanner.nextLong();
            KP01 kp01 = new KP01(capacity, items);
            kp01.tag = path.getParent().getFileName().toString();
            items.clear();
            return kp01;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return "KP01{" +
                "capacity=" + capacity +
                ", numItems=" + items.size() +
                '}';
    }

    boolean trivial() {
        return items.stream().mapToLong(Item::weight).sum() <= capacity;
    }

    IKS random() {
    //TODO
       return null;
    }

    IKS greedy() {
       //TODO
       return null;
    }
}
