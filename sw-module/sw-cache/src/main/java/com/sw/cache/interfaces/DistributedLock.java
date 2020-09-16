package com.sw.cache.interfaces;

/**
 * @Description:  分布式锁的接口，封装共有属性，方法
 * @Author:       allenyll
 * @Date:         2020-04-02 23:48
 * @Version:      1.0
 */
public interface DistributedLock {

    long TIMEOUT_MILLIS = 30000;

    int RETRY_TIMES = Integer.MAX_VALUE;

    long SLEEP_MILLIS = 500;

    boolean lock(String key);

    boolean lock(String key, int retryTimes);

    boolean lock(String key, int retryTimes, long sleepTimes);

    boolean lock(String key, long expire);

    boolean lock(String key, long expire, int retryTimes);

    boolean lock(String key, long expire, int retryTimes, long sleepTimes);

    boolean unLock(String key);

}
