package com.linzaixian.util.lock;

import org.junit.*;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

public class SpringRedisLockTest {
    private static RedisTemplate<String,String> redisTemplate;
    private static SpringRedisLock lock;
    @BeforeClass
    public static void BeforeClass (){
        RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("10.10.0.76");
        redisStandaloneConfiguration.setPort(6379);
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder  jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        jpcb.poolConfig(new JedisPoolConfig());
        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(redisStandaloneConfiguration,jpcb.build());
        redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }
    @Before
    public  void before(){
        lock=new SpringRedisLock("test2",redisTemplate);
    }
    @After
    public void After(){
        lock.unLock();
    }


    @Test
    public void testUnLock(){
        Assert.assertTrue(lock.tryLock());
        lock.unLock();
        Assert.assertTrue(lock.tryLock());
    }

    @Test
    public void testTryLock(){
        Assert.assertTrue(lock.tryLock());
    }
    @Test
    public void testTryLock2(){
        Assert.assertTrue(lock.tryLock());
        Assert.assertTrue(!lock.tryLock());
    }
    @Test
    public void testTryLock3(){
        Assert.assertTrue(lock.tryLock());
        lock.unLock();
        Assert.assertTrue(lock.tryLock());
    }
    @Test
    public void testLock(){
        Assert.assertTrue(lock.lock());

    }
    @Test
    public void testLock2(){
        lock.setLockExpireMillis(2000);//2秒后锁失效
        lock.setTimeout(1000);//在1秒内尝试获取锁
        Assert.assertTrue(lock.lock());
        Assert.assertTrue(!lock.lock());
    }
    @Test
    public void testLock3(){
        lock.setLockExpireMillis(1000);//1秒后锁失效
        lock.setTimeout(2000);//在2秒内尝试获取锁
        Assert.assertTrue(lock.lock());
        Assert.assertTrue(lock.lock());
    }
}
