package com.funnel.svc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.funnel.svc.handlers.Handler;
import com.funnel.svc.handlers.chain.DefaultHandlerChain;

public abstract class ServiceBase implements Service {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private final AtomicInteger doingNum = new AtomicInteger(0);
	private final AtomicInteger doneNum = new AtomicInteger(0);
	private final AtomicInteger errorNum = new AtomicInteger(0);
	private final AtomicLong maxExcuteTime = new AtomicLong(0);
	private final AtomicLong allExcuteTime = new AtomicLong(0);

	private boolean paused;
	private String pauseCause;
	private int maxRequestNum;

	private List<Handler> beforeHandlers = new ArrayList<Handler>();;
	private List<Handler> afterHandlers = new ArrayList<Handler>();;
	private List<Handler> errorHandlers = new ArrayList<Handler>();;

	private String svcCode;
	private String svcDesc;

	// 异步时，线程池使用，自定义线程池容量
	private int corePoolSize = 0;
	private int maxPoolSize = 0;
	private int queueCapacity = 0;

	public void setSvcCode(String svcCode) {
		this.svcCode = svcCode;
	}

	public void setSvcDesc(String svcDesc) {
		this.svcDesc = svcDesc;
	}

	public String getSvcCode() {
		return svcCode;
	}

	public String getSvcDesc() {
		return svcDesc;
	}

	@Override
	public void setBeforHandlers(List<Handler> handlers) {
		beforeHandlers = handlers;
	}

	@Override
	public void setAfterHandlers(List<Handler> handlers) {
		afterHandlers = handlers;
	}

	@Override
	public void setErrorHandlers(List<Handler> handlers) {
		errorHandlers = handlers;
	}

	public List<Handler> getBeforeHandlers() {
		return beforeHandlers;
	}

	public void setBeforeHandlers(List<Handler> beforeHandlers) {
		this.beforeHandlers = beforeHandlers;
	}

	public List<Handler> getAfterHandlers() {
		return afterHandlers;
	}

	public List<Handler> getErrorHandlers() {
		return errorHandlers;
	}

	@Override
	public void addBeforHandler(Handler handler) {
		beforeHandlers.add(handler);
	}

	@Override
	public void addAfterHandler(Handler handler) {
		afterHandlers.add(handler);
	}

	@Override
	public void addErrorHandler(Handler handler) {
		errorHandlers.add(handler);
	}

	public void enter() {
		doingNum.incrementAndGet();

	}

	public void exit() {
		doingNum.decrementAndGet();
		if (doneNum.incrementAndGet() == Integer.MAX_VALUE) {
			doneNum.set(0);
		}
	}

	public void term() {
		doingNum.decrementAndGet();
		if (errorNum.incrementAndGet() == Integer.MAX_VALUE) {
			errorNum.set(0);
		}
	}

	public int getDoneNum() {
		return doneNum.get();
	}

	public int getErrorNum() {
		return errorNum.get();
	}

	public int getDoingNum() {
		return doingNum.get();
	}

	public void pause(String cause) {
		paused = true;
		pauseCause = cause;
	}

	public void resume() {
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public String getPauseCause() {
		return pauseCause;
	}

	public void flowControl() throws SvcException {
		if (paused) {
			String text = "服务已暂停";
			if (pauseCause != null) {
				text += (": " + pauseCause);
			}
			throw new SvcException(Constant.ERROR_CODE_PAUSE_ERROR, text);
		}

		int max = maxRequestNum;
		if (max > 0 && doingNum.get() > max) {
			throw new SvcException(Constant.ERROR_CODE_FLOW_CONTRAL_ERROR,
					getSvcCode() + "的流量超过规定值" + max + ".");
		}
	}

	public int getMaxRequestNum() {
		return maxRequestNum;
	}

	public void setMaxRequestNum(int maxRequestNum) {
		this.maxRequestNum = maxRequestNum;
	}

	public void doBefore(SvcContext context) {
		if (this.getBeforeHandlers().size() > 0) {
			DefaultHandlerChain handlerChain = new DefaultHandlerChain(this
					.getBeforeHandlers().toArray(new Handler[0]));
			handlerChain.doHandler(context);
		}
	}

	public void doAfter(SvcContext context) {
		if (this.getAfterHandlers().size() > 0) {
			DefaultHandlerChain handlerChain = new DefaultHandlerChain(this
					.getAfterHandlers().toArray(new Handler[0]));
			handlerChain.doHandler(context);
		}
	}

	public void doError(SvcContext context) {
		if (this.getErrorHandlers().size() > 0) {
			DefaultHandlerChain handlerChain = new DefaultHandlerChain(this
					.getErrorHandlers().toArray(new Handler[0]));
			handlerChain.doHandler(context);
		}
	}

	@Override
	public void onFail(SvcContext context) {
	}

	public void setMaxExcuteTime(long maxExcuteTime) {
		if (this.maxExcuteTime.get() < maxExcuteTime) {
			this.maxExcuteTime.set(maxExcuteTime);
		}
	}

	public long getMaxExcuteTime() {
		return maxExcuteTime.get();
	}

	public long getAllExcuteTime() {
		return allExcuteTime.get();
	}

	public void addExcuteTime(long time) {
		allExcuteTime.addAndGet(time);
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
}
