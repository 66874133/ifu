
package com.funnel.svc.comon;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.funnel.svc.Constant;
import com.funnel.svc.SvcException;

public class AsyncServiceUtil
{
    protected static final Logger logger = Logger.getLogger(AsyncServiceUtil.class);

    // 异步执行线程池
    private static ThreadPoolExecutor defaultExecutor;

    private static int queueCapacity;

    private static int keepAliveSeconds = 60;

    private static Map<String, ThreadPoolExecutor> executors = new HashMap<String, ThreadPoolExecutor>();

    public static ThreadPoolExecutor getDefaultExecutor()
    {
        return defaultExecutor;
    }

    public static void createDefaultExecutor(int corePoolSize, int maxPoolSize, int queueCapacity)
    {
        AsyncServiceUtil.defaultExecutor = createThreadPoolExecutor(corePoolSize, maxPoolSize, queueCapacity);
    }

    public static void createExecutor(String key, int corePoolSize, int maxPoolSize, int queueCapacity)
    {
        executors.put(key, createThreadPoolExecutor(corePoolSize, maxPoolSize, queueCapacity));
    }

    public static ThreadPoolExecutor getExecutor(String key, int corePoolSize, int maxPoolSize, int queueCapacity)
    {
        if (executors.containsKey(key))
        {
            return executors.get(key);
        }
        createExecutor(key, corePoolSize, maxPoolSize, queueCapacity);
        return executors.get(key);
    }

    public static void shutdown()
    {
        defaultExecutor.shutdownNow();
        for (ThreadPoolExecutor executor : executors.values())
        {
            executor.shutdown();
        }
    }

    public static int getQueueCapacity()
    {
        return queueCapacity;
    }

    public static void setQueueCapacity(int queueCapacity)
    {
        AsyncServiceUtil.queueCapacity = queueCapacity;
    }

    private static ThreadPoolExecutor createThreadPoolExecutor(int corePoolSize, int maxPoolSize, int queueCapacity)
    {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(queueCapacity), buildRejectedExecutionHandler());
    }

    /**
     * 创建线程池满后拒绝的任务处理器
     * 
     * @return
     */
    private static RejectedExecutionHandler buildRejectedExecutionHandler()
    {
        return new RejectedExecutionHandler()
        {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
            {
                logger.warn("当前执行中线程数:" + executor.getActiveCount() + " 排队等待数量达到：" + executor.getQueue().size()
                    + " 执行异步线程执行者的线程池拒绝处理方法");
                throw new SvcException(Constant.ERROR_CODE_THREAD_POOL_REFUSE_ERROR, "服务器处理队列已满，拒绝处理");
                // AsyncSvcExecutor asyncSvcExecutor = (AsyncSvcExecutor) r;
                // asyncSvcExecutor.afterThreadPoolRefuse();
            }
        };
    }
}
