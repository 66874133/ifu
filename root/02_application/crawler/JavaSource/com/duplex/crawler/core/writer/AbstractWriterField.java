package com.duplex.crawler.core.writer;

import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.adjuster.IValueAdjuster;
import com.duplex.crawler.core.html.HTMLPath;

public abstract class AbstractWriterField implements IWriterField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9217561732363401680L;
	protected String name;
	protected String append;
	protected boolean multiple;
	protected boolean job;
	protected boolean handleUnicode;
	protected boolean handleDiscard;
	protected IValueAdjuster adjuster;

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public boolean isJobField() {
		return job;
	}

	public void setJob(boolean job) {
		this.job = job;
	}

	public abstract String[] getAllValues(CrawlerObject task, HTMLPath parser);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppend() {
		return append;
	}

	public void setAppend(String append) {
		this.append = append;
	}

	public boolean needHandleUnicode() {
		return handleUnicode;
	}

	public void setHandleUnicode(boolean handleUnicode) {
		this.handleUnicode = handleUnicode;
	}

	public boolean needHandleDiscard() {
		return handleDiscard;
	}

	public void setHandleDiscard(boolean handleDiscard) {
		this.handleDiscard = handleDiscard;
	}

	public IValueAdjuster getAdjuster() {
		return adjuster;
	}

	public void setAdjuster(IValueAdjuster adjuster) {
		this.adjuster = adjuster;
	}

	public String[] getValues(CrawlerObject task, HTMLPath path) {
		String[] ss = new String[] {};
		try {
			ss = getAllValues(task, path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < ss.length; i++) {
			if (ss[i] != null) {
				ss[i] = ss[i].trim();
			}
		}

		if (adjuster != null) {
			for (int i = 0; i < ss.length; i++) {
				ss[i] = adjuster.adjust(ss[i], task);
			}
		}
		if (multiple) {

			String str = "";
			if (append != null && !"".endsWith(append.trim())) {

				if (ss.length > 0) {
					str = ss[0];
					for (int i = 1; i < ss.length; i++) {
						if (i == ss.length - 1) {
							str = str + append + ss[i];
						} else {
							str = str + append + ss[i] + append;
						}

					}
				}

			}

			if ("".equals(str)) {
				return ss;
			} else {
				return new String[] { str };
			}

		} else {
			if (ss.length > 0) {
				return new String[] { ss[0] };
			} else {
				return new String[] {};
			}
		}
	}

}
