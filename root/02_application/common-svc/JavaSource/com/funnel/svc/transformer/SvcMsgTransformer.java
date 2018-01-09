package com.funnel.svc.transformer;

import com.funnel.svc.SvcException;

import net.minidev.json.JSONObject;

/**
 * Created by wanghua4 on 2014/8/18. 服务消息转换器
 */
public interface SvcMsgTransformer {
	/**
	 * 转换器码,用于识别转换器
	 * 
	 * @return
	 */
	public String getCode();

	/**
	 * 将接收到的请求报文转换成json格式， 接收的消息可能非json格式
	 */
	public JSONObject transformReqMsg(String reqMsg);

	/**
	 * 将json格式的响应对象转换成目标格式报文
	 */
	public String transformRespMsg(JSONObject respMsg);

	/**
	 * 根据异常信息组装失败报文
	 */
	public String transformRespErrMsg(SvcException svcException);
}
