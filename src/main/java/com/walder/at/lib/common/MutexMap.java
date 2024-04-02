package com.walder.at.lib.common;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@NoArgsConstructor
public class MutexMap<K, V> {

	private final Map<K, V> map = new HashMap<>();
	private final Lock mutex = new ReentrantLock();

	public boolean containsKey(K key) {
		mutex.lock();
		return map.containsKey(key);
	}

	public V get(final K key) {
		try {
			return map.get(key);
		} finally {
			mutex.unlock();
		}
	}

	public void put(final K key, final V value) {
		try {
			map.put(key, value);
		} finally {
			mutex.unlock();
		}
	}
}