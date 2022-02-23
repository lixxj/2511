package unsw.stream.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import unsw.stream.Fruit;
import unsw.stream.Table;
import unsw.stream.User;

public class Tests {
    // After doing Task 1 update this test.
    // @Test
    // public void SimpleTest() {
    //     List<User> users = new ArrayList<User>();
    //     users.add(new User(true, "A", "Devs"));
    //     users.add(new User(true, "B", "Devs"));
    //     users.add(new User(false, "C", "Testers"));
    //     users.add(new User(true, "D", "Business Analysts"));
    //     users.add(new User(true, "E", "CEO"));

    //     Table<User> table = new Table<User>(users);
    //     assertIterableEquals(Arrays.asList(), table.toView().skip(5));
    //     assertIterableEquals(users, table.toView().skip(0));
    //     assertIterableEquals(Arrays.asList(), table.toView().take(0));
    //     assertIterableEquals(users, table.toView().take(5));

    //     // it's okay to use streams in tests only
    //     assertIterableEquals(users.stream().skip(2).collect(Collectors.toList()), table.toView().skip(2));
    //     // there is no take(int) in java so we'll just make do with a more 'awful' styled variant here
    //     assertIterableEquals(users.stream().takeWhile(x -> x.userId().equals("C") == false).collect(Collectors.toList()), table.toView().take(2));

    //     assertIterableEquals(users.stream().skip(2).takeWhile(x -> x.userId().equals("D") == false).collect(Collectors.toList()), table.toView().skip(2).take(1));
    // }

    // After doing Task 1 uncomment all tests below this line
    
    @Test
    public void Task2_Count() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        assertEquals(5, table.toView().count());
        assertEquals(0, table.toView().take(0).count());
        assertEquals(0, table.toView().skip(5).count());
        assertEquals(1, table.toView().skip(4).take(1).count());
    }

    @Test
    public void Task2_Select() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        assertIterableEquals(Arrays.asList("Devs", "Devs", "Testers", "Business Analysts", "CEO"), table.toView().select(x -> x.jobTitle()));
        assertIterableEquals(Arrays.asList("Devs"), table.toView().select(x -> x.jobTitle()).take(1));
    }

    @Test
    public void Task2_Reduce() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        
        assertEquals(false, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalAnd, true));
        assertEquals(true, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalOr, true));
        assertEquals("Devs, Devs, Testers, Business Analysts, CEO", table.toView().select(x -> x.jobTitle()).reduce((acc, cur) -> acc.isEmpty() ? cur : acc + ", " + cur, ""));
    }

    // My test
    @Test
    public void Task2_Reduce_Few() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        
        assertEquals(true, table.toView().select(x -> x.isActive()).take(2).reduce(Boolean::logicalAnd, true));
        assertEquals(false, table.toView().select(x -> x.isActive()).take(3).reduce(Boolean::logicalAnd, true));
        assertEquals(true, table.toView().select(x -> x.isActive()).take(2).reduce(Boolean::logicalOr, false));
        assertEquals(false, table.toView().select(x -> x.isActive()).skip(2).reduce(Boolean::logicalAnd, true));
    }

    @Test
    public void Task3_ParallelReduce() throws InterruptedException, ExecutionException {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 10; i++)
            users.add(new User(false, "A" + i, "G" + i));

        Table<User> table = new Table<User>(users);
        
        // assertEquals(false, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalAnd, true));

        assertEquals(true, table.toView().select(x -> x.isActive()).reduce((curr, init) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            return curr || init;
        }, true));
        
        // assertDoesNotThrow(() -> {
        //     assertEquals(false, table.toView().select(x -> x.isActive()).parallelReduce(Boolean::logicalAnd, Boolean::logicalAnd, true, 16));
        //     assertEquals(true, table.toView().select(x -> x.isActive()).parallelReduce(Boolean::logicalOr, Boolean::logicalOr, true, 16));
        // });

        // To write a proper test, note that the or operation is too cheap here. In other words, it is cheaper than the actual
        // reduction so parallelReduce will still take less time;

        assertEquals(true, table.toView().select(x -> x.isActive()).parallelReduce((curr, init) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            return curr || init;
        }, Boolean::logicalOr, true, 16));
    }

    @Test
    public void Task3_ParallelReduce_Large() {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                users.add(new User(true, "A", "Devs"));
                users.add(new User(false, "B", "Devs"));
                users.add(new User(true, "C", "Testers"));
                users.add(new User(false, "D", "Business Analysts"));
                users.add(new User(true, "E", "CEO"));
            }
        }

        Table<User> table = new Table<User>(users);
        long before = System.currentTimeMillis();
        assertEquals(false, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalAnd, true));
        assertEquals(true, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalOr, true));
        long after = System.currentTimeMillis();
        long reduceTimeDifference = after - before;
        System.out.println("Reduce Large Time Difference: " + reduceTimeDifference);

        before = System.currentTimeMillis();
        assertDoesNotThrow(() -> {
            assertEquals(false, table.toView().select(x -> x.isActive()).parallelReduce(Boolean::logicalAnd, Boolean::logicalAnd, true, 16));
            assertEquals(true, table.toView().select(x -> x.isActive()).parallelReduce(Boolean::logicalOr, Boolean::logicalOr, true, 16));
        });

        after = System.currentTimeMillis();
        long parallelReduceTimeDifference = after - before;
        System.out.println("Parallel Reduce Large Time Difference: " + parallelReduceTimeDifference);
        
        // Multi-threaded reduce should take longer than single-threaded reduce
        assertTrue(parallelReduceTimeDifference > reduceTimeDifference);
    }
    
    @Test
    public void Task3_ParallelReduce_Integers() {
        List<Fruit> fruits = new ArrayList<Fruit>();
        for (int i = 0; i < 100000; i++) {
            fruits.add(new Fruit("Apple", "Green", 2));
            fruits.add(new Fruit("Banana", "Green", 2));
            fruits.add(new Fruit("Mango", "Green", 2));
            fruits.add(new Fruit("Cherry", "Red", 2));
            fruits.add(new Fruit("Grape", "Green", 2));
        }

        Table<Fruit> table = new Table<Fruit>(fruits);
        long before = System.currentTimeMillis();
        assertEquals(1000000, table.toView().select(Fruit::getAge).reduce(Integer::sum, 0));
        long after = System.currentTimeMillis();
        long reduceTimeDifference = after - before;
        System.out.println("Reduce Large Time Difference: " + reduceTimeDifference);

        before = System.currentTimeMillis();
        assertDoesNotThrow(() -> {
            assertEquals(1000000, table.toView().select(Fruit::getAge).parallelReduce(Integer::sum, Integer::sum, 0, 16));
        });
        after = System.currentTimeMillis();
        long parallelReduceTimeDifference = after - before;
        System.out.println("Parallel Reduce Large Time Difference: " + parallelReduceTimeDifference);
        
        // Multi-threaded reduce should take longer than single-threaded reduce
        // assertTrue(parallelReduceTimeDifference < reduceTimeDifference);
    }   
}
