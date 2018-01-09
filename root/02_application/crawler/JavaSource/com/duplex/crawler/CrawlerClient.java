package com.duplex.crawler;

import com.duplex.crawler.client.ClientJobAgent;
import com.duplex.crawler.client.protocol.CrawlerClient_Handler;
import com.duplex.crawler.client.protocol.FETCH_JOB_ALL_ACK_Handler;
import com.duplex.crawler.client.protocol.FETCH_OBJECT_ACK_Handler;

public class CrawlerClient {
	
	public static void main(String[] args) {
		ClientJobAgent clientJobAgent = new ClientJobAgent("localhost:1234");

		CrawlerClient_Handler objectResponseHandler = new CrawlerClient_Handler();
		
	
		objectResponseHandler.addHandler(new FETCH_JOB_ALL_ACK_Handler(clientJobAgent));
		objectResponseHandler.addHandler(new FETCH_OBJECT_ACK_Handler(
				clientJobAgent));
		
		clientJobAgent.register(objectResponseHandler);
		clientJobAgent.start();
	}

}
