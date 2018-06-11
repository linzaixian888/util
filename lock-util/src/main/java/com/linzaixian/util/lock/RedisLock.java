/**
 * Copyright(c) Foresee Science & Technology Ltd. 
 */
package com.linzaixian.util.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <pre>
 * 没有根据redis自动过期的redis分布式锁
 * </pre>
 *
 * @author linzaixian@foresee.com.cn
 * @date 2018年5月21日
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录 
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

public class RedisLock implements Lock {
	private static final Logger logger=LoggerFactory.getLogger(RedisLock.class);
	private String key;
	private RedisTemplate<String,String> redisTemplate;
	private boolean locked = false;
	private long lockExpireMillis;
	private long timeout;
	private String lockExpireTimeStr=null;

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
				//说明未获取到锁，进一步检查锁是否已经超时
				logger.debug("开始检查锁是否已到过期时间");
				String lockVal=(String) redisTemplate.opsForValue().get(key);
				if(lockVal!=null&&Long.parseLong(lockVal)<System.currentTimeMillis()){
					//表明已经超时了，原来的线程可能可能出现意外未能及时释放锁
					logger.debug("锁已到过期时间，可以重新进行申请锁");
			        String oldLockVal=(String) redisTemplate.opsForValue().getAndSet(key, lockExpireTimeStr);
			        //oldLockVal是有可能为null的,如oldLockVal与lockVal一致则代表拿到锁
			        if(lockVal.equals(oldLockVal)) {
			        	logger.debug("成功获取到锁,锁过期时间毫秒数为:{},过期时间戳为:{}",lockExpireMillis,lockExpireTimeStr);
			        	// 成功获取到锁, 设置相关标识
			        	locked=true;
			        	return true;
			        }
				}
				 Thread.sleep(10L);
				
			}
		} catch (Exception e) {
			logger.error("锁定异常",e);
		}
		logger.debug("超时时间已到，最终无法获取锁");
		return false;
	}

	@Override
	public void unLock() {
		logger.debug("当前锁标识:{}",locked);
		if(locked) {
			//redis里的时间
            String lockVal = redisTemplate.opsForValue().get(key);
            //校验是否超过有效期，如果不在有效期内，那说明当前锁已经失效，有可能其他线程已取得锁，不进行删除锁操作
            if (lockVal != null && Long.parseLong(lockVal) > System.currentTimeMillis()) {
                redisTemplate.delete(key);
                logger.debug("删除redis的缓存,key:{},value:{}",key,lockVal);
            }else {
            	logger.warn("缓存已清空或者锁已过有效期，不进行相关处理操作");
            }
            locked = false;
            logger.debug("修改当前锁标识:{}",locked);
		}else {
			logger.debug("解锁前需先获取锁");
		}
	}
	@Override
	public boolean tryLock() {
		long lockExpireTime = System.currentTimeMillis() + lockExpireMillis + 1;// 锁超时时间
		lockExpireTimeStr = String.valueOf(lockExpireTime);
		if(redisTemplate.opsForValue().setIfAbsent(key, lockExpireTimeStr)) {
			logger.debug("成功获取到锁,锁过期时间毫秒数为:{},过期时间戳为:{}",lockExpireMillis,lockExpireTimeStr);
			// 成功获取到锁, 设置相关标识
			locked=true;
			return true;
		}
		logger.debug("获取不到锁");
		return false;
	}


	
}
