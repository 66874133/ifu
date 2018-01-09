package com.discover.crawler.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtil {

	/**
	 * 获取json里某�?��key的�?
	 * 
	 * @param key
	 * @param jsonText
	 * @return
	 */
	public static List<String> getValues(String key, String jsonText) {
		JSONParser parser = new JSONParser();
		KeyFinder finder = new KeyFinder();
		finder.setMatchKey(key);
		List<String> list = new ArrayList<String>();
		try {
			while (!finder.isEnd()) {
				parser.parse(jsonText, finder, true);
				if (finder.isFound()) {
					finder.setFound(false);

					Object object = finder.getValue();

					list.add(String.valueOf(object));

				}
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) throws MalformedURLException,
			IOException {

		String jsonText = "{\"first\": 123, \"second\": [{\"k1\":{\"id\":\"id1\"}}, 4, 5, 6, {\"id\": 123}], \"third\": 789, \"id\": null}";

		String[] strings = getValues("id", jsonText).toArray(new String[0]);

		for (int i = 0; i < strings.length; i++) {
			System.out.println(strings[i]);
		}
	}

}

class KeyFinder implements ContentHandler {
	private Object value;
	private boolean found = false;
	private boolean end = false;
	private String key;
	private String matchKey;

	public void setMatchKey(String matchKey) {
		this.matchKey = matchKey;
	}

	public Object getValue() {
		return value;
	}

	public boolean isEnd() {
		return end;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public boolean isFound() {
		return found;
	}

	public void startJSON() throws ParseException, IOException {
		found = false;
		end = false;
	}

	public void endJSON() throws ParseException, IOException {
		end = true;
	}

	public boolean primitive(Object value) throws ParseException, IOException {
		if (key != null) {
			if (key.equals(matchKey)) {
				found = true;
				this.value = value;
				key = null;
				return false;
			}
		}
		return true;
	}

	public boolean startArray() throws ParseException, IOException {
		return true;
	}

	public boolean startObject() throws ParseException, IOException {
		return true;
	}

	public boolean startObjectEntry(String key) throws ParseException,
			IOException {
		this.key = key;
		return true;
	}

	public boolean endArray() throws ParseException, IOException {
		return false;
	}

	public boolean endObject() throws ParseException, IOException {
		return true;
	}

	public boolean endObjectEntry() throws ParseException, IOException {
		return true;
	}
}
