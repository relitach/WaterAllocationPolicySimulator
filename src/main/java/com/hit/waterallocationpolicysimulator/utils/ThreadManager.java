package com.hit.waterallocationpolicysimulator.utils;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadManager {
//    private static Logger logger = LogManager.getLogger(ThreadManger.class);
    private static ThreadManager instance = null;
    private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(15);
    private final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    private ThreadManager() {

    }

    public static ThreadManager getInstance() {
        if (instance == null) {
//            logger.debug("Create ThreadManger ");
            instance = new ThreadManager();
        }
        return instance;
    }


    public ScheduledExecutorService getScheduledThreadPool() {
        return scheduledThreadPool;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void shutDown() {
//        logger.info("shutDown ThreadPoolExecutor ");
        threadPoolExecutor.shutdown();
        scheduledThreadPool.shutdown();
    }
}
