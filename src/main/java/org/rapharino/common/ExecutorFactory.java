//package org.rapharino.common;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.reflect.Constructor;
//import java.util.Map;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * Created By Rapharino on 2017/7/27 上午3:08
// */
//public class ExecutorFactory {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorFactory.class);
//
//
//    private final static Map<String, ThreadPoolExecutor> store = new ConcurrentHashMap<String, ThreadPoolExecutor>(8);
//
//    // Thread safety!
//    public static ThreadPoolExecutor get(String name) {
//        ThreadPoolExecutor threadPoolExecutor = store.get(name);
//        if (threadPoolExecutor == null)
//            synchronized (ExecutorFactory.class) {
//                try {
//                    if ((threadPoolExecutor = store.get(name)) == null) {
//                        store.put(name, (threadPoolExecutor = Parser.parse(name)));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        return threadPoolExecutor;
//    }
//
//    static class Parser {
//
//        private final static ExecutorConfigurationLoader loader = new ExecutorConfigurationLoader();
//
//        static {
//            try {
//                loader.load();
//            } catch (Exception e) {
//                LOGGER.error("executor.xml load error",e);
//            }
//        }
//
//        protected static ThreadPoolExecutor parse(String name) throws Exception {
//
//            ExecutorConfiguration select = null;
//            for (ExecutorConfiguration configuration : loader.getCfgset()) {
//                if (configuration.getName().trim().toLowerCase().equals(name.trim().toLowerCase())) {
//                    select = configuration;
//                    break;
//                }
//            }
//
//            if (select == null)
//                throw new NullPointerException("cant find configuration for " + name);
//
//            final String exname = select.getName();
//
//            Constructor queueClazz = Class.forName(select.getBlockingQueue())
//                    .getDeclaredConstructor(new Class[]{int.class});
//            queueClazz.setAccessible(true);
//
//            return new JMXableThreadPoolExecutor(
//                    "ThreadPoolExecutor:name=" + exname,
//                    select.getCorePoolSize(),
//                    select.getMaximumPoolSize(),
//                    select.getKeepAliveTime(),
//                    select.getTimeUnit(),
//                    (BlockingQueue) queueClazz.newInstance(new Object[]{select.getBlockingQueueCapacity()}),
//                    new ThreadFactory() {
//                        final AtomicLong threadNumber = new AtomicLong(1);
//
//                        @Override
//                        public Thread newThread(Runnable r) {
//                            Thread t = new Thread(r, exname + "-thread-"
//                                    + threadNumber.getAndIncrement());
//                            t.setDaemon(true);
//                            t.setPriority(Thread.NORM_PRIORITY);
//                            return t;
//                        }
//
//                    }, (RejectedExecutionHandler) Class.forName(select.getHandler()).newInstance());
//
//        }
//
//        public static void main(String[] args) throws InterruptedException {
//
//            for (int i = 0; i < 100000000; i++) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ExecutorFactory.get("Main").submit(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//                }).start();
//            }
//            Thread.sleep(1111111111);
//
//        }
//
//    }
//}
