/**
 * Copyright(c) Foresee Science & Technology Ltd.
 */
package com.linzaixian.util.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 根据redis自动过期的redis分布式锁
 * </pre>
 *
 * @author linzaixian@foresee.com.cn
 * @date 2018年6月8日
 * @version 1.00.00
 *
 *          <pre>
 * 修改记录 
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

public class SpringRedisLock extends AbstractRedisLock {
    private static final Logger logger = LoggerFactory.getLogger(SpringRedisLock.class);
	private RedisTemplate<String, String> redisTemplate;

	public SpringRedisLock(String key, RedisTemplate<String, String> redisTemplate) {
		super(key);
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean setNx(String key, String value, final long expire, final TimeUnit timeUnit) {
		RedisSerializer keySerializer = redisTemplate.getKeySerializer();
		RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
		final byte[] keyByte = keySerializer.serialize(key);
		final byte[] valueByte = valueSerializer.serialize(value);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.set(keyByte, valueByte, Expiration.from(expire,timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT);
			}
		}, true);
	}
	@Override
	public void del(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public static void main(String[] args) {
		RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName("10.10.0.76");
		redisStandaloneConfiguration.setPort(6379);
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder  jpcb =
				(JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        jpcb.poolConfig(new JedisPoolConfig());
		JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(redisStandaloneConfiguration,jpcb.build());
		RedisTemplate<String,String> redisTemplate=new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();
		new SpringRedisLock("test",redisTemplate).lock();
	}

}
