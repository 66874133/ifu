package com.funnel.util;

public class KVCountStat implements ICountStat {

	private String key;
	private long value;

	public KVCountStat(String key,long value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public long getCountValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "KVCountStat [key=" + key + ", value=" + value + "]";
	}

}
