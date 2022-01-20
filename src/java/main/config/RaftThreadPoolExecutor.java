package main.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * raft thread pool
 */
@Slf4j
public class RaftThreadPoolExecutor extends ThreadPoolExecutor {

    private static final ThreadLocal<Long> threadLocal =  ThreadLocal.withInitial(System::currentTimeMillis);


    public RaftThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        threadLocal.get();
        log.debug("raft thread pool before Execute");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        log.debug("raft thread pool after Execute, cost time : {}", System.currentTimeMillis() - threadLocal.get());
        threadLocal.remove();
    }

    @Override
    protected void terminated() {
        log.info("active count : {}, queueSize : {}, poolSize : {}", getActiveCount(), getQueue().size(), getPoolSize());
    }

}
