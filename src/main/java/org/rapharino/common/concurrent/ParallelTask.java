
package org.rapharino.common.concurrent;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created By Rapharino on 2017/11/1 下午2:41
 *
 * 并行任务
 */
public class ParallelTask<S extends List, T extends List> extends RecursiveTask<T> {

    private ParallelThreadPool executor;

    private int start;

    private int end;

    private final ParallelThreadPool.Task<S, T> task;

    public ParallelTask(ParallelThreadPool executor, ParallelThreadPool.Task<S, T> task, int start, int end) {
        this.executor = executor;
        this.task = task;
        this.start = start;
        this.end = end;
    }

    // compute (core process)
    @Override
    protected T compute() {
        T total = null;
        boolean canCompute = (end - start) < this.executor.particle;
        if (canCompute) {
            T r =this.task.compute((S) executor.taskData.subList(start, end+1));
            if (total == null) {
                total = r;
            } else {
                total.addAll(r);
            }
        } else {
            int middle = (end + start) / 2;

            ParallelTask<S, T> leftTask = new ParallelTask<>(executor, task, start, middle);
            ParallelTask<S, T> rightTask = new ParallelTask<>(executor, task, middle + 1, end);

            // 执行子任务
            leftTask.fork();
            rightTask.fork();

            // 等待子任务执行完成，并得到结果
            T left = leftTask.join();
            T right = rightTask.join();

            // union
            if (total == null) {
                total = left;
            } else {
                total.addAll(left);
            }
            total.addAll(right);
        }
        return total;
    }
}
