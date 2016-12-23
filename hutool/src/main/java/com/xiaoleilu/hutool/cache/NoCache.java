package com.xiaoleilu.hutool.cache;

import java.util.Iterator;

/**
 * 无缓存实现，用于快速关闭缓存
 * @author Looly,jodd
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class NoCache<K, V> implements Cache<K, V> {

	@Override
	public int capacity() {
		return 0;
	}

	@Override
	public long timeout() {
		return 0;
	}

	@Override
	public void put(K key, V object) {
		// 跳过
	}

	@Override
	public void put(K key, V object, long timeout) {
		// 跳过
	}

	@Override
	public V get(K key) {
		return null;
	}

	@Override
	public Iterator<V> iterator() {
		return null;
	}

	@Override
	public int prune() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void remove(K key) {
		// 跳过
	}

	@Override
	public void clear() {
		// 跳过
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
