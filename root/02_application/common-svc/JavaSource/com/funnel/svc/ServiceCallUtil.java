package com.funnel.svc;

import java.util.Map;
import java.util.UUID;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import com.funnel.svc.config.ServiceConf;
import com.funnel.svc.util.JsonUtil;
import com.funnel.svc.util.ServiceUtil;
import com.funnel.svc.util.StringUtils;

/**
 * 服务调用工具类
 * 
 * @author wanghua4
 * 
 */
public class ServiceCallUtil {
	protected static final Logger logger = Logger
			.getLogger(ServiceCallUtil.class);

	/**
	 * 调用服务,带默认失败重试策略
	 * 
	 * @param svcCode
	 *            服务码
	 * @param reqMsgObje
	 *            请求信息json对象
	 */
	public static JSONObject callService(String svcCode,
			Map<String, Object> reqMsgMap) {
		JSONObject reqMsgObj = JsonUtil.toJSONObject(reqMsgMap);
		try {
			return callTargetService(svcCode, reqMsgObj);
		} catch (SvcException e) {
			return retryCallSvc(e, svcCode, reqMsgObj,
					new RetryAfterFlowControl());
		}

	}

	/**
	 * 调用服务,带指定失败重试策略
	 * 
	 * @param svcCode
	 *            服务码
	 * @param reqMsgObje
	 *            请求信息json对象
	 */
	public static JSONObject callService(String svcCode,
			Map<String, Object> reqMsgMap,
			RetryAfterCallSvcFail retryAfterCallSvcFail) {
		JSONObject reqMsgObj = JsonUtil.toJSONObject(reqMsgMap);
		try {
			return callTargetService(svcCode, reqMsgObj);
		} catch (SvcException e) {
			return retryCallSvc(e, svcCode, reqMsgObj, retryAfterCallSvcFail);
		}

	}

	/**
	 * 调用服务,带默认失败重试策略
	 * 
	 * @param svcCode
	 *            服务码
	 * @param reqMsgObje
	 *            请求信息json对象
	 */
	public static JSONObject callService(String svcCode, JSONObject reqMsgObj) {
		try {
			return callTargetService(svcCode, reqMsgObj);
		} catch (SvcException e) {
			return retryCallSvc(e, svcCode, reqMsgObj,
					new RetryAfterFlowControl());
		}

	}

	/**
	 * 调用服务,带指定失败重试策略
	 * 
	 * @param svcCode
	 *            服务码
	 * @param reqMsgObje
	 *            请求信息json对象
	 */
	public static JSONObject callService(String svcCode, JSONObject reqMsgObj,
			RetryAfterCallSvcFail retryAfterCallSvcFail) {
		try {
			return callTargetService(svcCode, reqMsgObj);
		} catch (SvcException e) {
			return retryCallSvc(e, svcCode, reqMsgObj, retryAfterCallSvcFail);
		}

	}
	
	public static JSONObject callService(String svcCode, SvcContext context,
			RetryAfterCallSvcFail retryAfterCallSvcFail) {
		try {
			return callTargetService(svcCode, context);
		} catch (SvcException e) {
			return retryCallSvc(e, svcCode, context, retryAfterCallSvcFail);
		}

	}

	/**
	 * 调用目标服务
	 * 
	 * @param svcCode
	 *            服务码
	 * @param reqMsgObje
	 *            请求信息json对象
	 */
	private static JSONObject callTargetService(String svcCode,
			JSONObject reqMsgObj) {
		Service service = ServiceUtil.getSvcByCode(svcCode);
		// 如果本地服务存在，则调用本地服务
		if (null != service) {
			return callLocalService(service, reqMsgObj);
		}
		// 从服务注册中心获取服务地址
		if (StringUtils.hasText(ServiceConf.getSvcRegCenterUrl())) {
			JSONObject resp = callRemoteService(svcCode, reqMsgObj);
			if (Constant.REQ_RESPONSE_STATE_SUCCESS.equals(resp
					.get(Constant.REQ_RESPONSE_STATE_FIELD))) {
				return resp;
			}
			throw new SvcException(
					(String) resp.get(Constant.REQ_RESPONSE_FAIL_CODE),
					(String) resp.get(Constant.REQ_RESPONSE_STATE_FAIL_INFO));
		}
		// 如果都不满足则抛出异常
		throw new SvcException("根据服务码：" + svcCode + " 未查找到对应的服务");
	}
	
	private static JSONObject callTargetService(String svcCode,
			SvcContext context) {
		Service service = ServiceUtil.getSvcByCode(svcCode);
		// 如果本地服务存在，则调用本地服务
		if (null != service) {
			return callLocalService(service, context);
		}
		// 从服务注册中心获取服务地址
		if (StringUtils.hasText(ServiceConf.getSvcRegCenterUrl())) {
			JSONObject resp = callRemoteService(svcCode, context);
			if (Constant.REQ_RESPONSE_STATE_SUCCESS.equals(resp
					.get(Constant.REQ_RESPONSE_STATE_FIELD))) {
				return resp;
			}
			throw new SvcException(
					(String) resp.get(Constant.REQ_RESPONSE_FAIL_CODE),
					(String) resp.get(Constant.REQ_RESPONSE_STATE_FAIL_INFO));
		}
		// 如果都不满足则抛出异常
		throw new SvcException("根据服务码：" + svcCode + " 未查找到对应的服务");
	}

	/**
	 * 调用本地服务
	 * 
	 * @param service
	 * @param reqMsgObj
	 * @return
	 */
	private static JSONObject callLocalService(Service service,
			JSONObject reqMsgObj) {
		SvcContext context = buildSvcContext(service.getSvcCode(), reqMsgObj);
		service.request(context);
		return context.getResponseData();
	}

	/**
	 * 调用本地服务
	 * 
	 * @param service
	 * @param reqMsgObj
	 * @return
	 */
	private static JSONObject callLocalService(Service service,
			SvcContext context) {
		service.request(context);
		return context.getResponseData();
	}
	
	/**
	 * 调用重试策略，重试调用目标服务
	 * 
	 * @param e
	 * @param svcCode
	 * @param reqMsgObj
	 * @param retryAfterCallSvcFail
	 * @return
	 */
	private static JSONObject retryCallSvc(SvcException e, String svcCode,
			JSONObject reqMsgObj, RetryAfterCallSvcFail retryAfterCallSvcFail) {
		if (null == retryAfterCallSvcFail) {
			throw e;
		}
		JSONObject retryParam = new JSONObject();
		retryParam.put(Constant.REQ_RESPONSE_STATE_FIELD,
				Constant.REQ_RESPONSE_STATE_FAIL);
		retryParam.put(Constant.REQ_RESPONSE_STATE_FAIL_INFO, e.getMessage());
		retryParam.put(Constant.REQ_RESPONSE_FAIL_CODE, e.getCode());

		if (null == retryParam.get(Constant.CALL_SVC_FAIL_NUM)) {
			retryParam.put(Constant.CALL_SVC_FAIL_NUM, 0);
		} else {
			retryParam.put(Constant.CALL_SVC_FAIL_NUM,
					(Integer) retryParam.get(Constant.CALL_SVC_FAIL_NUM) + 1);
		}
		// 如果要retry
		if (retryAfterCallSvcFail.isRetry(retryParam)) {
			retryParam.put(Constant.MESSAGE_SVC_CODE, svcCode);
			retryParam.put(Constant.MESSAGE_REQ_DATA, reqMsgObj);
			retryParam.put(Constant.MESSAGE_RETRY_OBJ, retryAfterCallSvcFail);
			return retryAfterCallSvcFail.doRety(retryParam);
		}
		throw e;
	}
	
	private static JSONObject retryCallSvc(SvcException e, String svcCode,
			SvcContext context, RetryAfterCallSvcFail retryAfterCallSvcFail) {
		if (null == retryAfterCallSvcFail) {
			throw e;
		}
		JSONObject retryParam = new JSONObject();
		retryParam.put(Constant.REQ_RESPONSE_STATE_FIELD,
				Constant.REQ_RESPONSE_STATE_FAIL);
		retryParam.put(Constant.REQ_RESPONSE_STATE_FAIL_INFO, e.getMessage());
		retryParam.put(Constant.REQ_RESPONSE_FAIL_CODE, e.getCode());

		if (null == retryParam.get(Constant.CALL_SVC_FAIL_NUM)) {
			retryParam.put(Constant.CALL_SVC_FAIL_NUM, 0);
		} else {
			retryParam.put(Constant.CALL_SVC_FAIL_NUM,
					(Integer) retryParam.get(Constant.CALL_SVC_FAIL_NUM) + 1);
		}
		// 如果要retry
		if (retryAfterCallSvcFail.isRetry(retryParam)) {
			retryParam.put(Constant.MESSAGE_SVC_CODE, svcCode);
			retryParam.put(Constant.MESSAGE_REQ_DATA, context.getRequestData());
			retryParam.put(Constant.MESSAGE_RETRY_OBJ, retryAfterCallSvcFail);
			return retryAfterCallSvcFail.doRety(retryParam);
		}
		throw e;
	}

	/**
	 * 调用远程服务
	 * 
	 * @param svcCode
	 *            服务码
	 * @param reqMsgObje
	 *            请求信息json对象
	 */
	private static JSONObject callRemoteService(String svcCode,
			JSONObject sourceReqMsgObj) {
		JSONObject reqMsgObj = setDefaultReqData(sourceReqMsgObj);
		return CallServiceUtil.callRemoteSvc(svcCode, reqMsgObj);
	}
	
	private static JSONObject callRemoteService(String svcCode,
			SvcContext context) {
		JSONObject reqMsgObj = setDefaultReqData(context.getRequestData());
		return CallServiceUtil.callRemoteSvc(svcCode, context);
	}

	/**
	 * 设置框架相关默认请求参数
	 * 
	 * @param sourceReqMsgObj
	 * @return
	 */
	private static JSONObject setDefaultReqData(JSONObject sourceReqMsgObj) {
		JSONObject reqMsgObj = sourceReqMsgObj;
		if (null == reqMsgObj) {
			reqMsgObj = new JSONObject();
		}
		if (!StringUtils.hasText((String) reqMsgObj
				.get(Constant.MESSAGE_SEQUENCENO_FIELD))) {
			reqMsgObj.put(Constant.MESSAGE_SEQUENCENO_FIELD, getSequenceNo());
		}
		return reqMsgObj;
	}

	/**
	 * 创建服务上下文对象
	 * 
	 * @param svcCode
	 * @param requestJson
	 * @return
	 */
	public static SvcContext buildSvcContext(String svcCode,
			JSONObject requestJson) {
		SvcContext svcContext = new SvcContext();
		svcContext.setServerName(ServiceConf.getServerName());
		svcContext.setSvcCode(svcCode);
		if (null == requestJson) {
			requestJson = new JSONObject();
		}

		String seqNo = (String) requestJson
				.get(Constant.MESSAGE_SEQUENCENO_FIELD);
		if (StringUtils.hasText(seqNo)) {
			svcContext.setSeqno(seqNo);
		} else {
			svcContext.setSeqno(getSequenceNo());
			requestJson.put(Constant.MESSAGE_SEQUENCENO_FIELD,
					svcContext.getSeqno());
		}

		svcContext.setRequestData(requestJson);
		return svcContext;
	}

	/**
	 * 生成序列号
	 * 
	 * @return 生成的序列号
	 */
	public static String getSequenceNo() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
