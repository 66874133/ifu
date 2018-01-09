package com.duplex.crawler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JobStatus implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4910570733128166914L;
	private Map<String, String> map = new HashMap<String, String>();

	public void put(String name, String value) {
		map.put(name, value);
	}



}
