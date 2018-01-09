package com.duplex.crawler;

import java.util.ArrayList;
import java.util.List;

import com.funnel.svc.util.FileUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 执行依赖关系
 * 
 * @author Jerry
 * 
 */
public class ExcuteDependConfig {

	private List<IExcuteable> executors = new ArrayList<IExcuteable>();

	private String depend;

	public List<IExcuteable> getExecutors() {
		return executors;
	}

	public void setExecutors(List<IExcuteable> executors) {
		this.executors = executors;
	}

	public void addExecutor(IExcuteable executorConfig) {
		executors.add(executorConfig);
	}

	public String getDepend() {
		return depend;
	}

	public void setDepend(String depend) {
		this.depend = depend;
	}

	public ExcuteDepend getExcuteDepend() {
		ExcuteDepend excuteDepend = new ExcuteDepend();

		String[] strs = depend.split("\\|");

		for (int i = 0; i < strs.length; i++) {

			String[] temp = strs[i].split("-");

			String[] font = temp[0].split(",");
			String[] back = temp[1].split(",");

			for (int j = 0; j < font.length; j++) {

				for (int k = 0; k < back.length; k++) {
					excuteDepend.addDepend(font[j], back[k]);
				}

			}
		}

		for (IExcuteable executor : executors) {
			excuteDepend.putExcutor(executor.getKey(), executor);
		}

		return excuteDepend;

	}

	public static ExcuteDependConfig fromFile(String name) {
		String content = FileUtil.getFileContent(name, "utf8");
		return fromXMLContent(content);
	}

	public static ExcuteDependConfig fromXMLContent(String xml) {
		return (ExcuteDependConfig) getXStream().fromXML(xml);
	}

	public static XStream getXStream() {
		XStream xs = new XStream(new DomDriver());

		xs.alias("execute-depend-config", ExcuteDependConfig.class);
		xs.aliasAttribute(ExcuteDependConfig.class, "executors", "executors");
		xs.aliasAttribute(ExcuteDependConfig.class, "depend", "depend");

		return xs;
	}

	public static void main(String[] args) {

		ExcuteDependConfig config = new ExcuteDependConfig();

		config = ExcuteDependConfig.fromFile("depend.xml");


		System.out.println(config.getXStream().toXML(config));
	}

}
