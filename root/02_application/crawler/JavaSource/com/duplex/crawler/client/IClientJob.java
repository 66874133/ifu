package com.duplex.crawler.client;

import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.core.starter.IJobStarter;

public interface IClientJob {
	
	public void start(IJobStarter starter);
	
	public void start();

	public IExecuteObject next();

	/**
	 * 当前任务是否允许从服务器端获取执行对象
	 * 
	 * @return
	 */
	public boolean canFetch();

	public void success(IExecuteObject executeObject);

	public void failed(IExecuteObject executeObject);

	public void add(IExecuteObject executeObject);

}
