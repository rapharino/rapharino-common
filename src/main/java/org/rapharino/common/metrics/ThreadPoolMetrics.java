
package org.rapharino.common.metrics;

import static org.rapharino.common.metrics.MetricsRegister.Metrics;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.*;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.JmxReporter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ThreadPoolMetrics {

    /** Number of active tasks. */
    public final Gauge<Integer> activeTasks;

    /** Number of tasks that had blocked before being accepted (or rejected). */
    public final Counter totalBlocked;

    /**
     * Number of tasks currently blocked, waiting to be accepted by
     * the executor (because all threads are busy and the backing queue is full).
     */
    public final Counter currentBlocked;

    /** Number of completed tasks. */
    public final Gauge<Long> completedTasks;

    /** Number of tasks waiting to be executed. */
    public final Gauge<Long> pendingTasks;

    /** Maximum number of threads before it will start queuing tasks */
    public final Gauge<Integer> maxPoolSize;

    private MetricNameFactory factory;

    /**
     * Create metrics for given ThreadPoolExecutor.
     *
     * @param executor Thread pool
     * @param path Type of thread pool
     * @param poolName Name of thread pool to identify metrics
     */
    public ThreadPoolMetrics(final ThreadPoolExecutor executor, String path, String poolName) {
        this.factory = new ThreadPoolMetricNameFactory("ThreadPools", path, poolName);

        activeTasks = Metrics.register(factory.createMetricName("ActiveTasks"), new Gauge<Integer>() {

            public Integer getValue() {
                return executor.getActiveCount();
            }
        });
        totalBlocked = Metrics.counter(factory.createMetricName("TotalBlockedTasks"));
        currentBlocked = Metrics.counter(factory.createMetricName("CurrentlyBlockedTasks"));
        completedTasks = Metrics.register(factory.createMetricName("CompletedTasks"), new Gauge<Long>() {

            public Long getValue() {
                return executor.getCompletedTaskCount();
            }
        });
        pendingTasks = Metrics.register(factory.createMetricName("PendingTasks"), new Gauge<Long>() {

            public Long getValue() {
                return executor.getTaskCount() - executor.getCompletedTaskCount();
            }
        });
        maxPoolSize = Metrics.register(factory.createMetricName("MaxPoolSize"), new Gauge<Integer>() {

            public Integer getValue() {
                return executor.getMaximumPoolSize();
            }
        });
    }

    public void release() {
        Metrics.remove(factory.createMetricName("ActiveTasks"));
        Metrics.remove(factory.createMetricName("PendingTasks"));
        Metrics.remove(factory.createMetricName("CompletedTasks"));
        Metrics.remove(factory.createMetricName("TotalBlockedTasks"));
        Metrics.remove(factory.createMetricName("CurrentlyBlockedTasks"));
        Metrics.remove(factory.createMetricName("MaxPoolSize"));
    }

    public static Object getJmxMetric(MBeanServerConnection mbeanServerConn, String jmxPath, String poolName,
                                      String metricName) {
        String name = String.format("org.apache.cassandra.metrics:type=ThreadPools,path=%s,scope=%s,name=%s", jmxPath,
                poolName, metricName);

        try {
            ObjectName oName = new ObjectName(name);
            if (!mbeanServerConn.isRegistered(oName)) {
                return "N/A";
            }

            switch (metricName) {
                case "ActiveTasks":
                case "PendingTasks":
                case "CompletedTasks":
                    return JMX.newMBeanProxy(mbeanServerConn, oName, JmxReporter.JmxGaugeMBean.class).getValue();
                case "TotalBlockedTasks":
                case "CurrentlyBlockedTasks":
                    return JMX.newMBeanProxy(mbeanServerConn, oName, JmxReporter.JmxCounterMBean.class).getCount();
                default:
                    throw new AssertionError("Unknown metric name " + metricName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading: " + name, e);
        }
    }

    public static Multimap<String, String> getJmxThreadPools(MBeanServerConnection mbeanServerConn) {
        try {
            Multimap<String, String> threadPools = HashMultimap.create();
            Set<ObjectName> threadPoolObjectNames = mbeanServerConn
                    .queryNames(new ObjectName("org.apache.cassandra.metrics:type=ThreadPools,*"), null);
            for (ObjectName oName : threadPoolObjectNames) {
                threadPools.put(oName.getKeyProperty("path"), oName.getKeyProperty("scope"));
            }

            return threadPools;
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException("Bad query to JMX server: ", e);
        } catch (IOException e) {
            throw new RuntimeException("Error getting threadpool names from JMX", e);
        }
    }

    static class ThreadPoolMetricNameFactory implements MetricNameFactory {

        private final String type;

        private final String path;

        private final String poolName;

        ThreadPoolMetricNameFactory(String type, String path, String poolName) {
            this.type = type;
            this.path = path;
            this.poolName = poolName;
        }

        public MetricsRegister.MetricName createMetricName(String metricName) {
            String groupName = ThreadPoolMetrics.class.getPackage().getName();
            StringBuilder mbeanName = new StringBuilder();
            mbeanName.append(groupName).append(":");
            mbeanName.append("type=").append(type);
            mbeanName.append(",path=").append(path);
            mbeanName.append(",scope=").append(poolName);
            mbeanName.append(",name=").append(metricName);

            return new MetricsRegister.MetricName(groupName, type, metricName, path + "." + poolName,
                    mbeanName.toString());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolMetrics threadPoolMetrics = new ThreadPoolMetrics(new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()),"internal","pooooooolname");



        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        String mbeanName = "org.apache.cassandra." + "ss" + ":type=";

        try
        {
            mbs.registerMBean(threadPoolMetrics, new ObjectName(mbeanName));
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        }

        Thread.sleep(10000000);
        //this( ,"internal","pooooooooolname");
    }

}
