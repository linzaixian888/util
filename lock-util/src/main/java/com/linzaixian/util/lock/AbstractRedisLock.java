package com.linzaixian.util.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisLock extends AbstractLock {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRedisLock.class);
    /**
     * 锁定之后的过期时间
     */
    private long lockExpireMillis = 60 * 1000;

    private String lockId = UUID.randomUUID().toString();

    public  AbstractRedisLock(String key) {
        super(key);
    }

    @Override
    public boolean tryLock(String key) {
        if (setNx(key, lockId, lockExpireMillis,TimeUnit.MILLISECONDS)) {
            // 成功获取到锁, 设置相关标识
            logger.debug("成功获取到锁,该锁的唯一标识lockId是:{}", lockId);
            logger.debug("锁过期时间毫秒数为:{}",lockExpireMillis);
            return true;
        }
        logger.debug("获取不到锁");
        return false;
    }

    @Override
    public void unLock(String key) {
        String lockVal = get(key);
        if (lockId.equals(lockVal)) {
            del(key);
            logger.debug("手动解锁成功,key:{},value:{}", key, lockVal);
        } else {
            logger.warn("已不具有当前锁标识,当前缓存value:{},lockId:{}", lockVal, lockId);
        }
    }

    /**
     * redis的del命令
     * @param key
     */
    public abstract void del(String key);

    /**
     * reids的get命令
     * @param key
     * @return
     */
    public abstract String get(String key);

    /**
     * redis的setnx命令
     * @param key
     * @param value
     * @param expire
     * @param timeUnit
     * @return
     */
    public abstract boolean setNx(String key, String value, long expire,TimeUnit timeUnit) ;

    public void setLockExpireMillis(long lockExpireMillis) {
        this.lockExpireMillis = lockExpireMillis;
    }
}
