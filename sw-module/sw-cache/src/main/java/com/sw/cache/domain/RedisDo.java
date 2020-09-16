package com.sw.cache.domain;

import com.sw.cache.util.SpringContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @Description:  Redis 开关
 * @Author:       allenyll
 * @Date:         2019/2/28 9:54 PM
 * @Version:      1.0
 */
@Configuration
public class RedisDo {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    public Jedis jedis;

    public void close(){
        jedis.disconnect();
        jedis = null;
    }

    public Jedis open(){

        JedisPoolConfig config = SpringContextHolder.getBean("redisConfigs");

        JedisPool pool;
        pool = new JedisPool(config, host, port);

        boolean borrowOrOprSuccess = true;
        try {
            jedis = pool.getResource();
            // do redis opt by instance
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                // pool.returnBrokenResource(jedis);
            }
        } finally {
            if (borrowOrOprSuccess) {
                // pool.returnResource(jedis);
            }
        }
        jedis = pool.getResource();
        return jedis;
    }

}
