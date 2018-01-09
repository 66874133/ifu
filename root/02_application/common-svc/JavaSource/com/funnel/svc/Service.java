package com.funnel.svc;

import java.util.List;

import com.funnel.svc.handlers.Handler;

public interface Service {
	int getMode();

	public static final int MODE_SYNCHRONOUS = 1; // 同步
	public static final int MODE_ASYNCHRONOUS = 2; // 异步

	String getSvcCode();

	String getSvcDesc();

	String getTransfomerCode();

	/**
	 * 业务方法之前的处理器s
	 * 
	 * @param handlers
	 */
	void setBeforHandlers(List<Handler> handlers);

	/**
	 * 添加单个处理器
	 * 
	 * @param handlers
	 */
	void addBeforHandler(Handler handler);

	/**
	 * 接收请求
	 * 
	 * @param context
	 * @return
	 */
	void request(SvcContext context);

	/**
	 * 业务方法之后的处理器s
	 * 
	 * @param handlers
	 */
	void setAfterHandlers(List<Handler> handlers);

	/**
	 * 添加单个处理器
	 * 
	 * @param handlers
	 */
	void addAfterHandler(Handler handler);

	/**
	 * 业务方法失败后的处理器s
	 * 
	 * @param handlers
	 */
	void setErrorHandlers(List<Handler> handlers);

	/**
	 * 添加单个处理器
	 * 
	 * @param handlers
	 */
	void addErrorHandler(Handler handler);

	/**
	 * 失败处理方法
	 * 
	 * @param context
	 */
	void onFail(SvcContext context);
	/**
	 * 进入处理
	 */
	void enter();

	/**
	 * 正常退出处理
	 */
	void exit();

	/**
	 * 出错退出处理
	 */
	void term();

	/**
	 * 暂停，与终止不同，端点实例不删除
	 * 
	 * @param cause
	 *            暂停原因
	 */
	void pause(String cause);

	/**
	 * 重新启动
	 */
	void resume();

	/**
	 * 是否已被暂停
	 */
	boolean isPaused();

	/**
	 * 得到暂停原因
	 * 
	 * @return
	 */
	String getPauseCause();

	/**
	 * 得到已处理总数
	 * 
	 * @return
	 */
	int getDoneNum();

	/**
	 * 正在处理的请求
	 * 
	 * @return
	 */
	int getDoingNum();

	/**
	 * 得到出错处理数
	 * 
	 */
	int getErrorNum();

	long getMaxExcuteTime();

	long getAllExcuteTime();

	/**
	 * 设置最大请求数(流控指标)
	 */
	void setMaxRequestNum(int maxRequestNum);

	/**
	 * 得到最大请求数(流控指标)
	 * 
	 * @return
	 */
	int getMaxRequestNum();

	/**
	 * 流控(同步处理中调用)
	 * 
	 * @throws Exception
	 */
	void flowControl();

}
