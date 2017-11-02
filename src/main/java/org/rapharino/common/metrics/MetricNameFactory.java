
package org.rapharino.common.metrics;

/**
 * Created By Rapharino on 2017/10/31 下午8:41
 *
 * MetricName Factory
 */
public interface MetricNameFactory {

    /**
     * Create a qualified name from given metric name.
     *
     * @param metricName part of qualified name.
     * @return new String with given metric name.
     */
    MetricsRegister.MetricName createMetricName(String metricName);
}
