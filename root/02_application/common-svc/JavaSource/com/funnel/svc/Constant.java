package com.funnel.svc;

public interface Constant {
	// 系统级别错误码
	public static final String ERROR_CODE_SYS_ERROR = "SYSERROR";
	// 线程池满，拒绝处理错误码
	public static final String ERROR_CODE_THREAD_POOL_REFUSE_ERROR = "threadPoolRefuse";
	// 流控被拒绝错误码
	public static final String ERROR_CODE_FLOW_CONTRAL_ERROR = "flowContral";
	// 服务被暂停错误码
	public static final String ERROR_CODE_PAUSE_ERROR = "pauseError";
	// 业务级别错误码
	public static final String ERROR_CODE_BIZ_ERROR = "BIZERROR";

	// 消息头
	public static String MESSAGE_HEAD_FIELD = "head";
	// 消息头请求模式mode
	public static String MESSAGE_HEAD_MODE_FIELD = "mode";
	// 以主题模式请求，服务码对应的所有地址都会被调用，返回一个整体的报文
	public static String MESSAGE_HEAD_MODE_TOPIC = "topic";
	// 渠道字段
	public static String MESSAGE_CHANNEL_FIELD = "bizChannel";
	// 回调字段
	public static String MESSAGE_CALLBACK_FIELD = "callback";

	// 接收的消息序列号字段
	public static String MESSAGE_SEQUENCENO_FIELD = "seqNo";
	// 接收的服务码字段
	public static String MESSAGE_SVC_CODE = "svcCode";
	public static String MESSAGE_REQ_DATA = "reqData";
	public static String MESSAGE_RETRY_OBJ = "retryObj";
	public static String CALL_SVC_FAIL_NUM = "failNum";
	public static String LOCAL_SCV = "localSvc";
	// 消息请求响应状态码字段
	public static String REQ_RESPONSE_STATE_FIELD = "status";
	// 消息请求响应成功状态码
	public static String REQ_RESPONSE_STATE_SUCCESS = "success";
	// 消息请求响应失败状态码
	public static String REQ_RESPONSE_STATE_FAIL = "fail";

	public static String REQ_RESPONSE_FAIL_CODE = "failCode";
	// 消息请求响应失败,失败信息
	public static String REQ_RESPONSE_STATE_FAIL_INFO = "failInfo";

	// 异步失败回调时，返回上一步接收的信息
	public static String MESSAGE_LAST_MSG_FIELD = "lastMsg";

	// 目标服务字段，用于总线
	public static String TRGERT_SVC_CODE = "targetSvcCode";
	// 来源渠道，用于总线
	public static String SOURCE_CHANNEL = "sourceChannel";
	// 原始请求数据，用于总线
	public static String SOURCE_REQ_MSG = "sourceReqMsg";
	// 原始请求数据，用于总线
	public static String BUS_REQ_DATA = "busReqData";
	// 响应来源的响应数据，用于总线
	public static String SOURCE_RESP_MSG = "sourceRespMsg";
	// 响应来源的响应数据，用于总线
	public static String BUS_RESP_DATA = "busRespData";

	// 目标请求数据，用于总线
	public static String TRGERT_REQ_MSG = "targetReqMsg";
	// 目标的响应数据，用于总线
	public static String TRGERT_RESP_MSG = "targetRespMsg";

	// 失败异常，用于总线
	public static String FAIL_EXCEPTION = "failException";

}
