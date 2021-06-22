package com.sw.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 配置
 * @Author: yu.leilei
 * @Date: 下午 5:25 2018/5/28 0028
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host:}")
    private String host;

    @Value("${spring.redis.port:}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.timeout:}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-idle:}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle:}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-active:}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait:}")
    private int maxWait;

    /**
     * 获取JedisPoolConfig配置
     * @return
     */
    @Bean("redisConfigs")
    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig redisConfig = new JedisPoolConfig();
        redisConfig.setMaxIdle(maxIdle);
        redisConfig.setMinIdle(minIdle);
        redisConfig.setMaxTotal(maxActive);
        redisConfig.setMaxWaitMillis(maxWait);
        return redisConfig;
    }

    /**
     * 获取JedisConnectionFactory工厂
     * @return
     */
//    @Bean
//    public JedisConnectionFactory getConnectionFactory(){
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        return factory;
//    }
    public RedisConnectionFactory getConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        //获得默认的连接池构造
        //这里需要注意的是，edisConnectionFactoryJ对于Standalone模式的没有（RedisStandaloneConfiguration，JedisPoolConfig）的构造函数，对此
        //我们用JedisClientConfiguration接口的builder方法实例化一个构造器，还得类型转换
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcf = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //修改我们的连接池配置
        jpcf.poolConfig(getRedisConfig());
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcf.build();

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    /**
     * 获取RedisTemplate模板
     * @return
     */
    /*@Bean
    public RedisTemplate<?, ?> getRedisTemplate(){
        RedisTemplate<?, ?> redisTemplate = new StringRedisTemplate(getConnectionFactory());
        return redisTemplate;
    }*/

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(getConnectionFactory());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
