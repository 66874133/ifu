package com.duplex.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExecuteStep {

	public int index;
	private List<String> keys = new ArrayList<String>();

	private Map<String, Boolean> keyToStatus = new ConcurrentHashMap<String, Boolean>();

	private List<String> fetched = new ArrayList<String>();

	public synchronized boolean isFinished(List<String> keys) {

		return keys.containsAll(fetched);

	}

	public synchronized boolean isCanExecute() {

		for (String key : keys) {
			if (!keyToStatus.containsKey(key)) {
				return true;
			}
		}
		return false;

	}

	public synchronized void setRun(String key) {
		keyToStatus.put(key, true);
	}

	public synchronized void setRun() {
		for (String key : keys) {
			if (!keyToStatus.containsKey(key)) {
				keyToStatus.put(key, true);
				return;
			}
		}

	}

	public synchronized String getRun() {

		for (String key : keys) {
			if (keyToStatus.containsKey(key) && !fetched.contains(key)) {
				fetched.add(key);
				return key;
			}
		}
		return null;

	}

	public ExecuteStep(List<String> keys) {
		this.keys = keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public boolean contains(String key) {
		return keys.contains(key);
	}
}
