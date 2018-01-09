package com.duplex.crawler.server.protocol;

import com.duplex.crawler.message.MessageObject;
import com.duplex.frame.handler.response.IMessageResponseHandler;

public interface IProtocolAdapter extends
		IMessageResponseHandler<MessageObject> {



	/**
	 * 查询任务状态
	 */
	public static String FETCH_JOB_STATUS_ALL = "fetch_job_status_all";
	public static String FETCH_JOB_STATUS_START = "fetch_job_status_start";
	public static String FETCH_JOB_STATUS_WAIT = "fetch_job_status_wait";
	public static String FETCH_JOB_STATUS_FAILED = "fetch_job_status_failed";
	public static String FETCH_JOB_STATUS_SUCCESS = "fetch_job_status_success";

	/**
	 * 获取可执行的任务
	 */

	public static String FETCH_JOB_ALL = "fetch_job_all";

	public static String FETCH_JOB_ALL_ACK = "fetch_job_all_ack";

	/**
	 * 获取执行对象
	 */
	public static String FETCH_OBJECT = "fetch_object";

	public static String FETCH_OBJECT_ACK = "fetch_object_ack";

	/**
	 * 设置服务器任务状态为running
	 */
	public static String UPDATE_JOB_RUNNING = "update_job_running";
	public static String UPDATE_JOB_RUNNING_ACK = "update_job_running_ack";

	/**
	 * 获取状态信息
	 */

	public static String FETCH_STATUS = "fetch_status";

	
	public String getMessageCode();
}
