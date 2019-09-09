package com.linzaixian.util.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLock implements Lock {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRedisLock.class);
    /**
     * 锁的key值
     */
    private String key;
    /**
     * 锁标识
     */
    private boolean locked = false;
    /**
     * 获取锁的最长堵塞超时时间(毫秒)
     */
    private long timeout = 60 * 1000;
    ;

    public AbstractLock(String key) {
        this.key = key;
    }

    @Override
    public synchronized boolean lock() {
        try {
            long start = System.currentTimeMillis();
            logger.debug("当前时间戳:{}", start);
            while (System.currentTimeMillis() - start < timeout) {
                logger.debug("开始进行锁定");
                if (tryLock()) {
                    return true;
                }
                Thread.sleep(10L);
            }
        } catch (Exception e) {
            logger.error("锁定异常", e);
        }
        logger.debug("超时时间已到，最终无法获取锁");
        return false;
    }

    @Override
    public boolean tryLock() {
        boolean result = tryLock(key);
        if (result) {
            locked = true;
        }
        return result;
    }

    @Override
    public void unLock() {
        logger.debug("当前锁标识:{}", locked);
        if (locked) {
            unLock(key);
            locked = false;
            logger.debug("修改当前锁标识:{}", false);
        } else {
            logger.warn("解锁前需先获取锁");
        }
        logger.debug("当前锁标识:{}", locked);
    }

    /**
     * 不堵塞的获取锁
     * @param key
     * @return
     */
    public abstract boolean tryLock(String key);

    /**
     * 释放锁
     * @param key
     */
    public abstract void unLock(String key);

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
