package com.duplex.crawler.client;

import com.duplex.crawler.ExcuteDepend;
import com.duplex.crawler.ExcuteDependConfig;
import com.duplex.crawler.JobContext;
import com.duplex.crawler.core.config.CrawlerConf;

public class TestDependClientJob {

	public static void main(String[] args) {

		JobContext jobContext = new JobContext("test");

		ExcuteDepend depend = ExcuteDependConfig.fromFile("depend.xml")
				.getExcuteDepend();

		jobContext.setDepend(depend);
		CrawlerConf crawlerConf = CrawlerConf
				.fromName("D:\\SVN\\Code\\server\\trunk\\goodsBaseCrawler\\crawler-conf\\guangdiu_automobile.xml");
		jobContext.putParam("crawler-conf", crawlerConf);
		IClientJob clientJob = new DependClientJob(jobContext);

		clientJob.start(crawlerConf.getStarter());
		// for (int i = 0; i < 5; i++) {
		//
		// DependExecuteObject eTask = new DependExecuteObject();
		// eTask.setName("DependExecuteObject" + i);
		// threadPool.add(eTask);
		// System.out.println("add new ETask!" + " ETask-" + i);
		//
		// }

	}

}
