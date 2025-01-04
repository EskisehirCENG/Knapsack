# Yet another optimization challenge: the 0–1 knapsack problem and the hard instances

## Problem description

The [0–1 knapsack](https://www.sciencedirect.com/science/article/pii/S0925231223007531) problem (KP01) is a classical NP-hard combinatorial optimization problem.
KP01 has many applications in real-world complex problems such as resource distribution, portfolio optimization, etc.
In KP01 problem, one is given a knapsack with an _integer_ capacity and a **set** of  items, which each have an integer profit and an integer weight.
The goal is to select a subset of items to put into the knapsack such that the total value is maximized and the total weight does not exceed the knapsack capacity.

The task is to Design and Implement the following(s)

Phase 1:  Create java class(es) to represent a 0–1 knapsack problem instance given in the format described in the [GitHub repository](https://github.com/JorikJooken/knapsackProblemInstances).
A dataset of 3240 problem instances is made publicly available by [Jooken, Leyman, and De Causmaecker (2022)](https://www.sciencedirect.com/science/article/pii/S037722172101016X). 
Each problem instance is distributed as a text file named *test.in*.
The first line of the file represents the number of items, *n*. Each of the *n* following lines describe an item and contains 3 integers: 
* the id of the item, 
* the profit of the item and 
* the weight of the item. 
The last line contains an integer describing the knapsack capacity, *c*.

For example, if the *test.in* file contains the following:

```
3
1 3 8
2 2 8
3 9 1
10
```

This describes a problem instance in which there are *n*=3 items and the knapsack has a capacity of *c*=10.
The item with id 1 has a profit of 3 and a weight of 8.
The item with id 2 has a profit of 2 and a weight of 8.
The item with id 3 has a profit of 9 and a weight of 1.

Hint: To represent an item, the following Record declaration could be used.
Record classes, which are a special kind of class, help to model plain data aggregates with less ceremony than normal classes.
A record declaration specifies in a header a description of its contents; the appropriate accessors, constructor, equals, hashCode, and toString methods are created automatically.
A record's fields are final because the class is intended to serve as a simple "data carrier"; thus Record classes are immutable.

``` java
public record Item(int id, long profit, long weight) {

    double unitValue() {
        return (double) profit / weight;
    }
}
```

Please notice that **long** is used here instead of **int**. 
This is because 3240 problem instances, which can be downloaded from the [GitHub repository](https://github.com/JorikJooken/knapsackProblemInstances),
contain large/big integer numbers that exceed the int boundary. Thus, the primitive type long is required.

Phase 2: Implement a method that checks for the trivial case of the KP01 problem. Also write a junit test case for it.
The method should return boolean=true when the bag that we are trying to fill is larger than the sum of the all items.
In other words; if all items fit in to our bag, then this is a trivial problem instance since the solution is to just pick all items!

Phase 3: Implement (**i**) a strategy that generates a random solution to the KP01 problem,
(**ii**) a [greedy algorithm](https://www.geeksforgeeks.org/greedy-algorithms/) to generate a greedy solution to the KP01 problem.

Random solution is a [randomized algorithm](https://www.slideshare.net/anniyappa/randomized-algorithms-ver-10), which generates different solution each time when it is run.
Greedy solution (hint: sort descending the items according to unit price [i.e. profit/weight] ) is deterministic on the other hand.

Phase 4: Generate one million random solutions and then print/return the best (i.e. having the maximum profit among 1M random solutions) of them for a given problem instance.
Of course do not keep all solutions in the memory! Here is a pseudocode:
``` java
long maxProfit = Long.MIN_VALUE;
IKS best = null;
for(int i=0;i<1_000_000_000;i++)
{
   IKS randomSolution = generateRandomSolution();
   if(randomSolution.totalProfit() > maxProfit)
   {
        maxProfit = randomSolution.totalProfit();
        best = randomSolution;
   }
}
best.print();
return best;
```
Phase 5: Repeat phase 4 using parallelism (e.g. multi-threads).
Hint: Read [atomic variables](https://www.baeldung.com/java-atomic-variables) and AtomicReference's methods. 
Of course be aware of any race condition in the parallel versions.
``` java
    public IKS oneMillionParallel() {
        long maxProfit = Long.MIN_VALUE;
        IKS best = null;

        IntStream.range(0, 1_000_000_000).boxed().parallel().forEach(i -> {

            IKS randomSolution = generateRandomSolution();
            if (randomSolution.totalProfit() > maxProfit) {
                maxProfit = randomSolution.totalProfit();
                best = randomSolution;
            }
        });

        best.print();
        return best;
    }
```

Phase 6: Create two variants of the parallel version:
(_i_) using synchronized block and (_ii_) AtomicVariables
When you benchmark the running times of each variant, `java -Xmx5g -server -jar target/KP01.jar problemInstances/*/test.in`
you should notice two things:
Parallel versions run much faster than sequential of course, and the CPU load in activity monitor shows all cores are utilized!
Make sure you do not burn your computer.
AtomicVariables are faster when used correctly, see `accumulateAndGet` and `updateAndGet` methods, than `synchronized` keyword. 

Please rest assured: you do not need to complete all phases to get a 100 mark for the project. 
The starter code gives you the driver code, an interface with default implementations for various methods, and so on.
All you need to implement two methods in KP01.java:

``` java
 public class KP01 implements IKS {
 
   IKS random() {
       //TODO
       return null;
    }

    IKS greedy() {
       //TODO
       return null;
    }
}
```
Just make sure that you do not modify `src/test/java` folder (green coloured), which contains JUnit Test Cases.
You can add additional classes (which implements IKS interface for example) or add additional methods to the existing classes. 
But do not System.out.println extra information that you use to debug during the development phase. Remove them at the end.
The program should print one new line of information per problem instance. The output will look like something:

``` bash
...
n_600_c_100000000_g_10_f_0.1_eps_0.1_s_200	Greedy[numItems=69, totalWeight=94303243, totalValue=94305262]	Random[numItems=69, totalWeight=99967984, totalValue=99968873]	5730/10000
n_800_c_1000000_g_6_f_0.3_eps_1e-05_s_300	Greedy[numItems=235, totalWeight=999994, totalValue=1008989]	Random[numItems=216, totalWeight=999985, totalValue=1001514]	0/10000
...
```
Final note : `mvn compile` passes for this public template repository. Once you implement the two methods, `mvn clean package` should pass!