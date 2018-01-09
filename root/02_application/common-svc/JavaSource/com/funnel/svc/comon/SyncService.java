package com.funnel.svc.comon;

import com.funnel.svc.Constant;
import com.funnel.svc.Service;
import com.funnel.svc.ServiceBase;
import com.funnel.svc.SvcContext;
import com.funnel.svc.SvcException;
import com.funnel.svc.handlers.FlowCallbackHandler;
import com.funnel.svc.handlers.FlowErrorCallbackHandler;
import com.funnel.svc.util.ServiceUtil;

public abstract class SyncService extends ServiceBase {
	public SyncService() {
		this.addAfterHandler(new FlowCallbackHandler());
		this.addErrorHandler(new FlowErrorCallbackHandler());
	}

	@Override
	public int getMode() {
		return Service.MODE_SYNCHRONOUS;
	}

	private String transfomerCode = "json";

	@Override
	public String getTransfomerCode() {
		return transfomerCode;
	}

	public void setTransfomerCode(String transfomerCode) {
		this.transfomerCode = transfomerCode;
	}

	public void request(SvcContext context) {
		flowControl();
		long begin = System.currentTimeMillis();
		try {
			enter();
			doBefore(context);
			// 如此做为了spring中使用时事物加在process方法上
			((SyncService) ServiceUtil.getSvcByCode(this.getSvcCode()))
					.process(context);
			doAfter(context);
			exit();
			long endtm = System.currentTimeMillis();
			setExcuteTime(endtm - begin);
			if ((endtm - begin) > 3000) {
				logger.info("服务svcCode:" + context.getSvcCode() + " seq:"
						+ context.getSeqno() + ",执行业务方法时间：" + (endtm - begin)
						+ "ms");
			}
		} catch (Exception e) {
			SvcException failException = null;
			if (e instanceof SvcException) {
				failException = (SvcException) e;
			} else {
				failException = new SvcException(Constant.ERROR_CODE_BIZ_ERROR,
						e);
			}
			context.setFailException(failException);
			this.onFail(context);
			doError(context);
			term();
			long endtm = System.currentTimeMillis();
			setExcuteTime(endtm - begin);
			logger.info("服务svcCode:" + context.getSvcCode() + " seq:"
					+ context.getSeqno() + ",执行业务失败时间：" + (endtm - begin)
					+ "ms");
			throw failException;
		}
	}

	private void setExcuteTime(long time) {
		addExcuteTime(time);
		setMaxExcuteTime(time);
	}

	public abstract void process(SvcContext context);
}
