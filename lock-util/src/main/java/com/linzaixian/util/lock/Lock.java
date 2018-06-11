/**
 * Copyright(c) Foresee Science & Technology Ltd. 
 */
package com.linzaixian.util.lock;
/**
 * <pre>
 * 锁
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

public interface Lock {
	/**
	 * 不堵塞的获取锁
	 * @return boolean
	 */
	boolean tryLock();
	/**
	 * 堵塞的获取锁
	 * @return boolean
	 */
	boolean lock();
	/**
	 * 解锁
	 */
	void unLock();
}
