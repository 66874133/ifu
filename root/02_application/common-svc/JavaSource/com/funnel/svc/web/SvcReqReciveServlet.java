package com.funnel.svc.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.funnel.svc.ServiceCallUtil;
import com.funnel.svc.SvcContext;
import com.funnel.svc.SvcException;
import com.funnel.svc.transformer.SvcCodeAndTransformerCodeMapping;
import com.funnel.svc.transformer.SvcMsgTransformer;
import com.funnel.svc.transformer.TransformerUtil;
import com.funnel.svc.transformer.impl.JsonFormatTransformer;
import com.funnel.svc.util.JsonUtil;
import com.funnel.svc.util.StringUtils;

public class SvcReqReciveServlet extends HttpServlet {
	protected final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = -2208865984493412214L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long t = System.currentTimeMillis();
		req.setCharacterEncoding("UTF-8");
		JSONObject excuteReslut = excuteRequest(req);
		boolean isSuccess = (Boolean) excuteReslut.get("isSuccess");
		if (isSuccess) {
			writeSuccessResponseInfo(resp, (String) excuteReslut.get("respMsg"));
			if ((System.currentTimeMillis() - t) > 30000) {
				logger.info("处理请求:" + (String) excuteReslut.get("svcCode")
						+ " seqNo:" + (String) excuteReslut.get("seqNo")
						+ " 成功，耗时：" + (System.currentTimeMillis() - t)
						+ " 请求数据:" + (String) excuteReslut.get("requestMsg")
						+ " 响应数据:" + (String) excuteReslut.get("respMsg"));
			}
		} else {
			String failMsg = getFailMsg(
					(SvcMsgTransformer) excuteReslut.get("transformer"),
					(SvcException) excuteReslut.get("failException"));
			writeFailResponse(resp, failMsg);
			logger.error("处理请求:" + (String) excuteReslut.get("svcCode")
					+ " 失败，耗时：" + (System.currentTimeMillis() - t) + " 请求数据:"
					+ (String) excuteReslut.get("requestMsg") + " 响应数据:"
					+ failMsg, (SvcException) excuteReslut.get("failException"));
		}
	}

	private byte[] readUploadInput(HttpServletRequest req) {
		DiskFileItemFactory diskFactory = new DiskFileItemFactory();
		// threshold 极限、临界值，即硬盘缓存 1M
		diskFactory.setSizeThreshold(4 * 1024);
		// repository 贮藏室，即临时文件目录
		// diskFactory.setRepository(new File(tempPath));

		ServletFileUpload upload = new ServletFileUpload(diskFactory);
		// 设置允许上传的最大文件大小 4M
		upload.setSizeMax(4 * 1024 * 1024);
		// 解析HTTP请求消息头
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);// 判断是否是表单文件类型
			if (isMultipart) {
				List<FileItem> fileItems = upload.parseRequest(req);
				for (FileItem fi : fileItems) {
					logger.info(fi.getFieldName());
					if ("file".equalsIgnoreCase(fi.getFieldName())) {
						return fi.get();
					}
				}
			}

		} catch (FileUploadException e) {
			logger.error("", e);
		}
		return null;
	}

	private JSONObject excuteRequest(HttpServletRequest req) {
		JSONObject excuteReslut = new JSONObject();
		String seqNo = null;
		try {
			byte[] input = readUploadInput(req);
			String reciveSvcCode = readSvcCode(req);
			excuteReslut.put("svcCode", reciveSvcCode);
			// 从请求中获取数据
			String requestMsg = readRequestData(req);
			excuteReslut.put("requestMsg", requestMsg);
			// 根据服务码获取对应的转换器
			SvcMsgTransformer transformer = getSvcMsgTransformerBySvcCode(reciveSvcCode);
			excuteReslut.put("transformer", transformer);

			// 转换请求报文为json格式
			JSONObject requestJson = transformReqMsg(transformer, requestMsg);
			excuteReslut.put("requestJson", requestJson);

			SvcContext context = ServiceCallUtil.buildSvcContext(reciveSvcCode,
					requestJson);
			context.setInput(input);
			// 调用目标服务，并获得响应json对象
			JSONObject responseJson = ServiceCallUtil.callService(
					reciveSvcCode, context, null);
			excuteReslut.put("responseJson", responseJson);
			// 将响应json对象转换为目标响应报文格式
			String respMsg = getRespMsg(transformer, responseJson);
			excuteReslut.put("respMsg", respMsg);
			excuteReslut.put("isSuccess", true);
		} catch (SvcException e) {
			excuteReslut.put("isSuccess", false);
			excuteReslut.put("failException", e);
		} catch (Exception e) {
			excuteReslut.put("isSuccess", false);
			excuteReslut.put("failException",
					new SvcException("UNKNOW", e.getMessage()));
		}
		excuteReslut.put("seqNo", seqNo);
		return excuteReslut;
	}

	private SvcMsgTransformer getSvcMsgTransformerBySvcCode(String svcCode) {
		String transformerCode = SvcCodeAndTransformerCodeMapping
				.getTransfomerCodeBySvcCode(svcCode);
		if (!StringUtils.hasText(transformerCode)) {
			return new JsonFormatTransformer();
		}
		SvcMsgTransformer currentTransformer = TransformerUtil
				.getTransfomerByCode(transformerCode);
		if (null != currentTransformer) {
			return currentTransformer;
		} else {
			return new JsonFormatTransformer();
		}
	}

	private String getFailMsg(SvcMsgTransformer transformer,
			SvcException svcException) {
		return transformer.transformRespErrMsg(svcException);
	}

	private void writeFailResponse(HttpServletResponse resp,
			String failResponseMsg) throws IOException {

		resp.setContentType("text/xml;UTF-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter writer = resp.getWriter();
		writer.write(failResponseMsg);
		writer.flush();
	}

	private JSONObject transformReqMsg(SvcMsgTransformer transformer,
			String reqMsg) {
		JSONObject requestJson = transformer.transformReqMsg(reqMsg);
		if (null == requestJson) {
			throw new SvcException("转换接收报文失败：接收的报文为:" + reqMsg);
		}
		return requestJson;
	}

	private String getRespMsg(SvcMsgTransformer transformer,
			JSONObject responseJson) {
		return transformer.transformRespMsg(responseJson);
	}

	/**
	 * 约定请求url最后为交易码
	 * 
	 * @param req
	 *            请求对象
	 * @return 处理消息的目标服务类id
	 */
	private String readSvcCode(HttpServletRequest req) {
		String svcCode = req.getRequestURI().substring(
				req.getRequestURI().lastIndexOf("/") + 1);
		if (StringUtils.hasText(svcCode)) {
			return svcCode;
		} else {
			throw new SvcException("服务码svcCode不能为空");
		}
	}

	/**
	 * 从request中读取json格式内容
	 * 
	 * @param req
	 *            请求对象
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String readRequestData(HttpServletRequest req) throws IOException {
		if (req.getParameterMap().isEmpty()) {
			InputStream in = req.getInputStream();
			ByteArrayOutputStream outstream = new ByteArrayOutputStream(4096);
			byte[] b = new byte[4096];
			int len;
			while ((len = in.read(b)) > 0) {
				outstream.write(b, 0, len);
			}
			outstream.close();
			return new String(outstream.toByteArray());
		} else {
			Set<Map.Entry> entrieSet = req.getParameterMap().entrySet();
			Map reqMap = new HashMap();
			for (Map.Entry entry : entrieSet) {
				reqMap.put(entry.getKey(), ((String[]) entry.getValue())[0]);
			}
			return JsonUtil.toJSONString(reqMap);
		}
	}

	/**
	 * 回写请求处理成功消息
	 * 
	 * @param resp
	 *            响应对象
	 */
	private void writeSuccessResponseInfo(HttpServletResponse resp,
			String respMsg) throws IOException {
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.write(respMsg);
		writer.flush();
	}
}
