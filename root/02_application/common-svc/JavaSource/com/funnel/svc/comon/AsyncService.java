package com.funnel.svc.comon;

import java.util.concurrent.ThreadPoolExecutor;

import com.funnel.svc.Service;
import com.funnel.svc.ServiceBase;
import com.funnel.svc.SvcContext;
import com.funnel.svc.handlers.AsyncCallbackHandler;
import com.funnel.svc.handlers.AsyncErrorCallbackHandler;
import com.funnel.svc.handlers.FlowCallbackHandler;
import com.funnel.svc.handlers.FlowErrorCallbackHandler;
import com.funnel.svc.util.ServiceUtil;

public abstract class AsyncService extends ServiceBase {

	@Override
	public int getMode() {
		return Service.MODE_ASYNCHRONOUS;
	}

	private String transfomerCode = "json";

	@Override
	public String getTransfomerCode() {
		return transfomerCode;
	}

	public void setTransfomerCode(String transfomerCode) {
		this.transfomerCode = transfomerCode;
	}

	public AsyncService() {
		this.addAfterHandler(new AsyncCallbackHandler());
		this.addAfterHandler(new FlowCallbackHandler());
		this.addErrorHandler(new FlowErrorCallbackHandler());
		this.addErrorHandler(new AsyncErrorCallbackHandler());
	}

	@Override
	public void request(SvcContext context) {
		flowControl();
		ThreadPoolExecutor executor = AsyncServiceUtil.getDefaultExecutor();
		if (getCorePoolSize() > 0 && getMaxPoolSize() > 0) {
			// 使用自身线程池
			executor = AsyncServiceUtil.getExecutor(this.getSvcCode(),
					getCorePoolSize(), getMaxPoolSize(), getQueueCapacity());
		}
//		logger.debug("接收到序列号:" + context.getSeqno());
		executor.execute(new AsyncSvcExecuteThread((AsyncService) ServiceUtil
				.getSvcByCode(this.getSvcCode()), context));
//		logger.debug("序列号:" + context.getSeqno() + "处理完成");

	}

	public abstract void process(SvcContext context);

}
