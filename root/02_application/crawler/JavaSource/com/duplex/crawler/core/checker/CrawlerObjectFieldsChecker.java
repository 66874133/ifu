package com.duplex.crawler.core.checker;

import java.util.HashSet;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.config.CrawlerWriter;
import com.funnel.svc.util.StringUtils;


public class CrawlerObjectFieldsChecker implements ICrawlerObjectDataChecker {

	private String errorFields;
	private String warnFields;

	private HashSet<String> errorSet;
	private HashSet<String> warnSet;

	public CrawlerObjectFieldsChecker() {
		super();
	}

	public CrawlerObjectFieldsChecker(String errorFields, String warnFields) {
		super();
		this.errorFields = errorFields;
		this.warnFields = warnFields;
	}

	public boolean isRightData(CrawlerObject task, CrawlerWriter writer) {
		String[] names = task.getParameter().getParameters();

		HashSet<String> nameSet = new HashSet<String>();
		for (String name : names) {
			nameSet.add(name);
		}

		for (String error : getErrorSet()) {
			if (nameSet.contains(error)) {
				String[] values = task.getParameter().getValues(error);
				if (values.length == 0
						|| StringUtils.isNullOrEmpty(StringUtils
								.getStringFromStrings(values, ""))) {
					System.out.println(task + "data-error" + "error: field "
							+ error + " is empty.");

					return false;
				}
			} else {
				System.out.println(task + "error: field " + error
						+ " is not defined.");

				return false;
			}
		}

		for (String warn : getWarnrSet()) {
			if (nameSet.contains(warn)) {
				String[] values = task.getParameter().getValues(warn);
				if (values.length == 0
						|| StringUtils.isNullOrEmpty(StringUtils
								.getStringFromStrings(values, ""))) {
					System.out.println("warning: field " + warn + " is empty.");
				}
			} else {
				System.out.println("warning: field " + warn
						+ " is not defined.");
			}
		}

		return true;
	}

	private HashSet<String> getErrorSet() {
		if (errorSet == null) {
			errorSet = new HashSet<String>();
			if (!StringUtils.isNullOrEmpty(errorFields)) {
				String[] es = StringUtils.split(errorFields, ",");
				for (String e : es) {
					errorSet.add(e.trim());
				}
			}
		}

		return errorSet;
	}

	private HashSet<String> getWarnrSet() {
		if (warnSet == null) {
			warnSet = new HashSet<String>();
			if (!StringUtils.isNullOrEmpty(warnFields)) {
				String[] es = StringUtils.split(warnFields, ",");
				for (String e : es) {
					warnSet.add(e.trim());
				}
			}
		}

		return warnSet;
	}

	public String getErrorFields() {
		return errorFields;
	}

	public void setErrorFields(String errorFields) {
		this.errorFields = errorFields;
	}

	public String getWarnFields() {
		return warnFields;
	}

	public void setWarnFields(String warnFields) {
		this.warnFields = warnFields;
	}

	public String toString() {
		return "error:" + errorFields;
	}

}
