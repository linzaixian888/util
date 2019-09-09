package com.linzaixian.util.lock;

import org.junit.*;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisLockTest {
    private static JedisPool jedisPool;
    private RedisLock lock;
    @BeforeClass
    public static void BeforeClass (){
         jedisPool=new JedisPool(new JedisPoolConfig(),"10.10.0.76",6379);
    }
    @Before
    public  void before(){
        lock=new RedisLock("test",jedisPool.getResource());
    }
    @After
    public void After(){
        lock.unLock();
    }
    @AfterClass
    public static void AfterClass(){
        jedisPool.close();
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
