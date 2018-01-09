package com.duplex.crawler;

import com.duplex.crawler.core.config.CrawlerConf;
import com.duplex.crawler.server.DefaultServerJob;
import com.duplex.crawler.server.IServerJob;
import com.duplex.crawler.server.ServerJobAgent;
import com.duplex.crawler.server.protocol.CrawlerServer_Handler;
import com.duplex.crawler.server.protocol.FETCH_JOB_ALL_Handler;
import com.duplex.crawler.server.protocol.FETCH_OBJECT_Handler;
import com.duplex.crawler.server.protocol.UPDATE_JOB_RUNNING_Handler;

public class CrawlerServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerJobAgent serverJobAgent = new ServerJobAgent(1234);

		CrawlerServer_Handler crawlerServer_Handler = new CrawlerServer_Handler();
		crawlerServer_Handler.addHandler(new FETCH_JOB_ALL_Handler(
				serverJobAgent));
		crawlerServer_Handler.addHandler(new UPDATE_JOB_RUNNING_Handler(
				serverJobAgent));
		crawlerServer_Handler.addHandler(new FETCH_OBJECT_Handler(
				serverJobAgent));

		serverJobAgent.register(crawlerServer_Handler);

		serverJobAgent.start();

		ExcuteDepend depend = ExcuteDependConfig.fromFile("depend.xml")
				.getExcuteDepend();

		CrawlerConf crawlerConf = new CrawlerConf();

		crawlerConf = CrawlerConf
				.fromName("D:\\ssvn\\root\\02_application\\crawler\\crawler-conf\\guangdiu_automobile.xml");

		JobContext jobContext = new JobContext(crawlerConf.getSource() + "_"
				+ crawlerConf.getName());

		jobContext.setDepend(depend);
		jobContext.putParam("crawler-conf", crawlerConf);

		// IServerJob serverJob = new DefaultServerJob(jobContext.getJobKey());
		IServerJob serverJob = new DefaultServerJob(jobContext.getJobKey(),
				crawlerConf.getStarter());
		serverJobAgent.startJob(jobContext, serverJob);

	}

}
