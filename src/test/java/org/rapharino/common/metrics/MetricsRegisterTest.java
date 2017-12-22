package org.rapharino.common.metrics;

import static org.rapharino.common.metrics.MetricsRegister.Metrics;

import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.Counter;

public class MetricsRegisterTest {



    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void counter() throws Exception {
        Counter counter = Metrics.counter(new MetricsRegister.MetricName(MetricsRegisterTest.class,"test"));

        for (int i = 0; i < 2; i++) {
            Thread.sleep(1000);
            counter.inc();
        }
    }
}