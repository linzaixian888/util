package com.linzaixian.util.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;

public class RedisLock extends  AbstractRedisLock{
    private Jedis jedis;

    public RedisLock(String key, Jedis jedis) {
        super(key);
        this.jedis = jedis;
    }

    @Override
    public void del(String key) {
        jedis.del(key);
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public boolean setNx(String key, String value, long expire, TimeUnit timeUnit) {
//        SetParams setParams=SetParams.setParams().nx();
        if(TimeUnit.MILLISECONDS.equals(timeUnit)){
//            setParams.px(expire);
            return "OK".equals(jedis.set(key,value, "nx","px",expire));
        }else if(TimeUnit.SECONDS.equals(timeUnit)){
//            setParams.ex((int) expire);
            return "OK".equals(jedis.set(key,value, "nx","ex",expire));
        }
        return false;
//        return "OK".equals(jedis.set(key,value, setParams));
    }

    public static void main(String[] args) {
       JedisPool jedisPool=new JedisPool(new JedisPoolConfig(),"10.10.0.76",6379);
       new RedisLock("test1",jedisPool.getResource()).lock();
       jedisPool.close();
    }
}
