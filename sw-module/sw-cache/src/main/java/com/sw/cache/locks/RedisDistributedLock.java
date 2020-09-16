package com.sw.cache.locks;

import com.sw.cache.util.StringUtil;
import com.sw.cache.abstracts.AbstractDistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:  基于redis分布式缓存
 * @Author:       allenyll
 * @Date:         2020-04-03 00:02
 * @Version:      1.0
 */
@Component
public class RedisDistributedLock extends AbstractDistributedLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDistributedLock.class);

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    private final ThreadLocal<String> lockId = new ThreadLocal<>();

    public static final String UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = "if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
                "then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end ";
    }

//    public RedisDistributedLock(RedisTemplate<Object, Object> redisTemplate) {
//        super();
//        this.redisTemplate = redisTemplate;
//    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        // 第一次解锁
        boolean result = tryLock(key, expire);

        // 如果result为空并且重试次数大于0
        while (!result && (retryTimes-- > 0)){
            try {
                LOGGER.info("lock failed, retrying..." + retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = tryLock(key, expire);
        }
        return result;
    }

    private boolean tryLock(String key, long expire) {
        SetParams setParams = new SetParams();
        setParams.nx();
        setParams.px(expire);
        try {
            String result = redisTemplate.execute((RedisCallback<String>) redisConnection -> {
                JedisCommands jedisCommands = (JedisCommands) redisConnection.getNativeConnection();
                String uuid = StringUtil.getUUID32();
                LOGGER.info("uuid:{}", uuid);
                lockId.set(uuid);
                return jedisCommands.set(key, uuid, setParams);
            });
            return "OK".equals(result);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("set redis error");
        }
        return false;
    }

    @Override
    public boolean unLock(String key) {
        try {
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> ids = new ArrayList<>();
            ids.add(lockId.get());
            LOGGER.info("ids:{}", ids);
            Long result = redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
                Object nativeConnection = redisConnection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群操作
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_SCRIPT, keys, ids);
                }

                // 单机操作
                if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_SCRIPT, keys, ids);
                }
                return 0L;
            });
            return result != null && result > 0;
        } catch (Exception e) {
           LOGGER.error("unlock redis error");
        }
        return false;
    }
}
