package com.sw.system.controller;

import com.sw.cache.locks.RedisDistributedLock;
import com.sw.cache.util.CacheUtil;
import com.sw.common.util.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.*;

/**
 * @Description:  缓存管理
 * @Author:       allenyll
 * @Date:         2020/8/31 2:46 下午
 * @Version:      1.0
 */
@ApiIgnore
@RestController
@RequestMapping("cache")
public class CacheController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    RedisDistributedLock redisDistributedLock;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final int RETRY_TIMES = 10;

    private static final Long EXPIRE = 1000L;

    private static final Long SLEEP_TIME = 5000L;


    int count = 0;

    @RequestMapping("lock")
    @ResponseBody
    public String distributedLock() throws InterruptedException {
        int _count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(_count);
        ExecutorService executorService = newFixedThreadPool(_count);
        LOGGER.info("开始计算");

        for (int i=0; i<_count; i++) {
            executorService.execute(() -> {
                boolean result = redisDistributedLock.lock("redis_lock", EXPIRE, RETRY_TIMES, SLEEP_TIME);
                LOGGER.info(Thread.currentThread().getName()+" result:{}", result);
                try {
                    if (result) {
                        count++;
                    }
                } finally {
                    boolean unlockResult = redisDistributedLock.unLock("redis_lock");
                    LOGGER.info("unlock:{}", unlockResult);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        LOGGER.info("count:{}", count);
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list =  cacheUtil.getAllCache();
        result.put("list", list);
        return DataResponse.success(result);
    }

    @ResponseBody
    @RequestMapping(value = "remove/{key}", method = RequestMethod.DELETE)
    public DataResponse removeRedisByKey(@PathVariable String key){
        cacheUtil.remove(key);
        return DataResponse.success();
    }

    @ResponseBody
    @RequestMapping(value = "removeAll", method = RequestMethod.DELETE)
    public DataResponse removeAllCache(){
        cacheUtil.removeAll();
        return DataResponse.success();
    }
}
