package org.rapharino.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;

/**
 * Created By Rapharino on 2017/7/27 上午1:26
 */
public class ExecutorConfigurationLoader {

    private Set<ExecutorConfiguration> cfgset = new HashSet<ExecutorConfiguration>(4);

    public void add(ExecutorConfiguration configuration) {
        cfgset.add(configuration);
    }

    public Set<ExecutorConfiguration> getCfgset() {
        return cfgset;
    }

    /**
     * 加载
     */
    public void load() throws Exception {

        Digester digester = new Digester();
        digester.push(this);
        digester.addObjectCreate("executors/executor", ExecutorConfiguration.class);
        digester.addSetProperties("executors/executor");
        digester.addBeanPropertySetter("executors/executor/maximumPoolSize");
        digester.addBeanPropertySetter("executors/executor/corePoolSize");
        digester.addBeanPropertySetter("executors/executor/keepAlive/time", "keepAliveTime");
        digester.addBeanPropertySetter("executors/executor/keepAlive/timeUnit", "unit");
        digester.addSetProperties("executors/executor/blockingQueue", "class", "blockingQueue");
        digester.addBeanPropertySetter("executors/executor/blockingQueue/capacity", "blockingQueueCapacity");
        digester.addSetProperties("executors/executor/rejectedHandler", "class", "handler");
        digester.addSetNext("executors/executor", "add");

        File file = new File(System.getProperty("user.dir") + "/config/executor.xml");
        digester.parse(new FileInputStream(file));

        init(cfgset);
    }

    private void init(Set<ExecutorConfiguration> cfgset) {
        for (ExecutorConfiguration configuration : cfgset) {
            if (StringUtils.isBlank(configuration.getName())) {
                throw new NullPointerException("executor name is empty");
            }
            if (configuration.getMaximumPoolSize() < 1) {
                configuration.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);
            }
            if (configuration.getCorePoolSize() < 1) {
                configuration.setCorePoolSize(configuration.getMaximumPoolSize());
            }
            if (configuration.getKeepAliveTime() < 1) {
                configuration.setKeepAliveTime(10);
                configuration.setUnit("SECONDS");
            }
            if (StringUtils.isBlank(configuration.getBlockingQueue())) {
                configuration.setBlockingQueue(LinkedBlockingDeque.class.getName());
                configuration.setBlockingQueueCapacity(100000); // 10w
            }
            if (StringUtils.isBlank(configuration.getHandler()))
                configuration.setHandler(java.util.concurrent.ThreadPoolExecutor.DiscardPolicy.class.getName());
        }
    }

    public static void main(String[] args) throws Exception {

        ExecutorConfigurationLoader loader = new ExecutorConfigurationLoader();
        loader.load();
        for (ExecutorConfiguration executorConfiguration : loader.getCfgset()) {
            System.out.println(executorConfiguration);
        }
    }
}
