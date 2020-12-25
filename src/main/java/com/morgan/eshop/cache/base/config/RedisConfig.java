package com.morgan.eshop.cache.base.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: redis集群配置类
 * @Date:2020/8/9
 * @User:morgan.b.chen
 */
@Configuration
public class RedisConfig {

    @Autowired
    private Environment environment;


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public GenericObjectPoolConfig redisPool(){
        return new GenericObjectPoolConfig();
    }

    @Bean
    @Primary
    public RedisClusterConfiguration redisClusterConfig(){
        Map<String,Object> source = new HashMap<>(8);
        source.put("spring.redis.cluster.nodes",environment.getProperty("spring.redis.cluster.nodes"));
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration",source));
        redisClusterConfiguration.setPassword(environment.getProperty("spring.redis.password"));
        return redisClusterConfiguration;
    }

    @Bean
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig redisPool,
                                                             @Qualifier("redisClusterConfig") RedisClusterConfiguration redisClusterConfig){
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(redisPool).build();
        return new LettuceConnectionFactory(redisClusterConfig,clientConfiguration);
    }

    @Bean
    @Primary
    public RedisTemplate redisTemplate(@Qualifier("lettuceConnectionFactory") RedisConnectionFactory redisConnectionFactory){
        return getRedisTemplate(redisConnectionFactory);
    }

    private RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

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
