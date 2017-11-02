
package org.rapharino.common.concurrent;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Created By Rapharino on 2017/11/1 下午3:32
 * 基于 ForkJoinPool , 实现 细粒度任务切分,并行运算后, 结果同步合并.
 *
 * @param <S> 输入值类型
 * @param <T> 输出值类型
 */
public class ParallelThreadPool<S extends List, T extends List> {

    // 并行度
    private int parallelism;
    // 任务粒度
    protected int particle;

    private static final int DEFAULT_PARTICLE = 10;

    private Future<T> future;

    protected S taskData;

    private ForkJoinPool forkJoinPool;

    public ParallelThreadPool(int parallelism, int particle) {
        this.parallelism = parallelism;
        this.particle = particle <= 0 ? DEFAULT_PARTICLE : particle;
        forkJoinPool = new ForkJoinPool(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
    }

    public ParallelThreadPool() {
        this(Runtime.getRuntime().availableProcessors(), DEFAULT_PARTICLE);
    }

    public Future<T> submit(S taskData, Task<S, T> task) {
        this.taskData = taskData;
        return (this.future = forkJoinPool.submit(new ParallelTask(this, task, 0, taskData.size() - 1)));
    }

    // get Future
    public Future<T> getFuture() {
        return future;
    }

    /**
     * 最小粒度任务
     */
    public interface Task<S, T> {
        T compute(S particleData);
    }

    public ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }
}
