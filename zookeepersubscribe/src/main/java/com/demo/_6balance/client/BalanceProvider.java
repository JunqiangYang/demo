package com.demo._6balance.client;

/**
 * 负载的算法接口
 * 
 * @author jerome_s@qq.com
 * @param <T>
 */
public interface BalanceProvider<T> {

	/**
	 * 获取经过算法算出的负载均衡器
	 * 
	 * @author jerome_s@qq.com
	 * @return
	 */
	public T getBalanceItem();

}
