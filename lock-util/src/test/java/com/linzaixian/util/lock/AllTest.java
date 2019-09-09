package com.linzaixian.util.lock;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({RedisLockTest.class,SpringRedisLockTest.class})
@RunWith(Suite.class)
public class AllTest {
}
