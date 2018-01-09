package com.funnel.svc.batch;

import java.util.List;

import com.funnel.svc.ServiceCallUtil;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.AsyncService;
import com.funnel.svc.util.ServiceUtil;

/**
 * 简单调度实现
 * 
 * @author wanghua4
 * 
 */
public class SimpleSchedulSvc extends AsyncService {
	private boolean runFlag = true;
	// 启动后多久开始执行
	private Integer startDelay = 60000;
	// 调度间隔时间
	private Integer interval = 60000;
	private List<String> targetSvcCodes;

	@Override
	public String getSvcCode() {
		return "simpleSchedulSvc";
	}

	@Override
	public String getSvcDesc() {
		return "批处理定时调度";
	}

	@Override
	public void process(SvcContext context) {
		if (!runFlag) {
			start();
		}
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("调度:" + targetSvcCodes + " 开始");
				try {
					Thread.sleep(startDelay);
				} catch (Exception e) {
					logger.error("服务调度程序睡眠失败", e);
				}
				while (runFlag) {
					for (String targetSvcCode : targetSvcCodes) {
						try {
							if (ServiceUtil.getSvcByCode(targetSvcCode)
									.getDoingNum() <= 0) {
								logger.info("启动服务:" + targetSvcCode);
								ServiceCallUtil
										.callService(targetSvcCode, new SvcContext(),null);
							}
						} catch (Exception e) {
							logger.error("服务调度:" + targetSvcCode + "失败", e);
						}
					}

					try {
						Thread.sleep(interval);
					} catch (Exception e) {
						logger.error("服务调度程序睡眠失败", e);
					}

				}
			}
		}).start();
	}

	public void stop() {
		runFlag = false;
		logger.info("停止检查批处理执行结果");
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public List<String> getTargetSvcCodes() {
		return targetSvcCodes;
	}

	public void setTargetSvcCodes(List<String> targetSvcCodes) {
		this.targetSvcCodes = targetSvcCodes;
	}

	public Integer getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(Integer startDelay) {
		this.startDelay = startDelay;
	}

}
