package org.rapharino.common.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParallelThreadPoolTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void submit() throws Exception {

        ParallelThreadPool<List<Integer>, List<String>> executor = new ParallelThreadPool<>(6, 6);
        Future<List<String>> future = executor.submit(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), taskData -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<String> result = new ArrayList<>();
            for (Integer taskDatum : taskData) {
                result.add(taskDatum.toString());
            }
            return result;
        });

        for (String s : future.get()) {
            System.out.println(s);
        }
    }

}