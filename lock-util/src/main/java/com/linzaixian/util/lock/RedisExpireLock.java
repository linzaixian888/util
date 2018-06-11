/**
 * Copyright(c) Foresee Science & Technology Ltd. 
 */
package com.linzaixian.util.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

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

public class RedisExpireLock implements Lock{
	private static final Logger logger=LoggerFactory.getLogger(RedisExpireLock.class);
	private String key;
	private RedisTemplate<String,String> redisTemplate;
	/**
	 * 锁标识
	 */
	private boolean locked = false;
	/**
	 * 锁定之后的过期时间
	 */
	private long lockExpireMillis=60*1000;
	/**
	 * 获取锁的最长堵塞超时时间
	 */
	private long timeout=10*1000;
	private String lockId=UUID.randomUUID().toString();;

	
	
	@Override
	public boolean lock() {
		try {
			long start = System.currentTimeMillis();
			logger.debug("当前时间戳:{}",start);
			while (System.currentTimeMillis() - start < timeout) {
				logger.debug("开始进行锁定");
				if(tryLock()) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("锁定异常",e);
		}
		logger.debug("超时时间已到，最终无法获取锁");
		return false;
	}
	
	@Override
	public boolean tryLock() {
		if(setNx(key, lockId, Expiration.from(lockExpireMillis, TimeUnit.MILLISECONDS))) {
			// 成功获取到锁, 设置相关标识
			logger.debug("成功获取到锁,该锁的唯一标识lockId是:{}",lockId);
			logger.debug("锁过期时间毫秒数为:{}",lockExpireMillis);
			locked=true;
			return true;
		}
		logger.debug("获取不到锁");
		return false;
	}


	@Override
	public void unLock() {
		logger.debug("当前锁标识:{}",locked);
		if(locked) {
			//redis里的时间
            String lockVal = redisTemplate.opsForValue().get(key);
            //检验是否可解锁
            if (lockVal != null && lockVal.equals(lockId)) {
                redisTemplate.delete(key);
                logger.debug("手动解锁成功,key:{},value:{}",key,lockVal);
            }else {
            	logger.warn("不满足手动解锁条件,当前缓存value:{},lockId:{}",lockVal,lockId);
            }
            locked = false;
            logger.debug("修改当前锁标识:{}",locked);
		}else {
			logger.debug("解锁前需先获取锁,lockId:{}",lockId);
		}
		
	}
	
	private boolean setNx( String key,  String value, final Expiration expiration) {
		RedisSerializer keySerializer=redisTemplate.getKeySerializer();
		RedisSerializer valueSerializer=redisTemplate.getValueSerializer();
		logger.debug("要放到redis里的key[{}],value[{}],过期时间[单位:{},数值:{}]",key,value,expiration.getTimeUnit(),expiration.getExpirationTime());
		final byte[] keyByte = keySerializer.serialize(key);
		final byte[] valueByte = valueSerializer.serialize(value);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.set(keyByte, valueByte, expiration, SetOption.SET_IF_ABSENT);
			}
		}, true);

	}

	public void setKey(String key) {
		logger.debug("设置锁的key：{}",key);
		this.key = key;
	}

	public void setLockExpireMillis(long lockExpireMillis) {
		this.lockExpireMillis = lockExpireMillis;
		logger.debug("设置锁的过期时间毫秒数：{}",lockExpireMillis);
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
		logger.debug("设置获取锁的超时毫秒数：{}",timeout);
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	
}
