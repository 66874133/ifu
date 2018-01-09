package com.duplex.crawler.core.html;

import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.tags.CompositeTag;

public class NodeFactoryMaker {

	private static PrototypicalNodeFactory prototypicalNodeFactory;

	public static PrototypicalNodeFactory getPrototypicalNodeFactory() {
		if (null == prototypicalNodeFactory) {

			prototypicalNodeFactory = new PrototypicalNodeFactory();

			prototypicalNodeFactory.registerTag(new CustomSimpleTag("em"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("strong"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("label"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("font"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("dfn"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("ins"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("strike"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("BIG"));
			prototypicalNodeFactory.registerTag(new CustomSimpleTag("b"));
		}

		return prototypicalNodeFactory;
	}

}

class CustomSimpleTag extends CompositeTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tagName;

	public CustomSimpleTag(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String[] getIds() {
		return new String[] { tagName };
	}
}
