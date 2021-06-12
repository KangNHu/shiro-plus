package org.codingeasy.shiroplus.core;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
* 一个简化的读写锁
 *@see  ReentrantReadWriteLock
* @author : kangning <a>2035711178@qq.com</a>
*/
public class ReadWriteLock {

	/**
	 * 读写锁
	 */
	private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

	/**
	 * 读锁
	 */
	private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();


	/**
	 * 写锁
	 */
	private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

	/**
	 * 获取读锁
	 */
	public void readLock() {
		readLock.lock();
	}


	/**
	 * 释放读锁
	 */
	public void unReadLock(){
		readLock.unlock();
	}

	/**
	 * 获取写锁
	 */
	public void writeLock() {
		writeLock.lock();
	}
	/**
	 * 释放写锁
	 */
	public void unWriteLock(){
		writeLock.unlock();
	}

	/**
	 * 读执行
	 * @param supplier 执行体
	 * @param <T> 返回的数据类型
	 * @return 返回执行体的结果
	 */
	public <T>T readExecute(Supplier<T> supplier){
		this.readLock();
		try {
			return supplier.get();
		}finally {
			this.unReadLock();
		}
	}


	/**
	 * 写执行
	 * @param supplier 执行体
	 * @param <T> 返回的数据类型
	 * @return  返回执行体的结果
	 */
	public <T>T writeExecute(Supplier<T> supplier){
		this.writeLock();
		try {
			return supplier.get();
		}finally {
			this.unWriteLock();
		}
	}

}
