package com.duplex.crawler.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.duplex.crawler.ClientJobStatus;
import com.duplex.crawler.DependExcuteThread;
import com.duplex.crawler.DependExecuteObject;
import com.duplex.crawler.ExcuteDepend;
import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobContext;
import com.duplex.crawler.core.starter.IJobStarter;

public class DependClientJob implements IClientJob {

	private Logger logger = Logger.getLogger(this.getClass());
	private List<IExecuteObject> waiting = new ArrayList<IExecuteObject>();

	private List<IExecuteObject> processing = new ArrayList<IExecuteObject>();

	private List<IExecuteObject> success = new ArrayList<IExecuteObject>();

	private List<IExecuteObject> failed = new ArrayList<IExecuteObject>();

	private JobContext jobContext;

	private ClientJobStatus clientJobStatus = new ClientJobStatus();

	private boolean running = false;
	/**
	 * 可使用的线程
	 */
	private List<DependExcuteThread> threads = new ArrayList<DependExcuteThread>();

	private int threadCount = 5;

	public DependClientJob(JobContext jobContext, int threadCount) {
		this.jobContext = jobContext;
		this.threadCount = threadCount;
		ExcuteThreadPoolDaemonThread daemonThread = new ExcuteThreadPoolDaemonThread();
		daemonThread.start();

	}

	public DependClientJob(JobContext jobContext) {
		this.jobContext = jobContext;
		ExcuteThreadPoolDaemonThread daemonThread = new ExcuteThreadPoolDaemonThread();
		daemonThread.start();
	}

	public void stop() {
		for (DependExcuteThread thread : threads) {
			thread.exit();
		}

		running = false;
	}

	public void start() {
		running = true;
		logger.info("CrawlerClientJob is start!");
		excute();
	}

	public void start(IJobStarter starter) {
		running = true;
		logger.info("CrawlerClientJob is start!");
		List<IExecuteObject> list = starter.getExecuteObjects();
		waiting.addAll(list);
		excute();
	}

	private void excute() {

		for (int i = 0; i < threadCount; i++) {
			DependExcuteThread excuteThread = new DependExcuteThread(
					jobContext, this);
			excuteThread.start();
			threads.add(excuteThread);

		}

	}

	public synchronized void success(IExecuteObject eTask) {

		waiting.remove(eTask);
		success.add(eTask);

	}

	public synchronized void failed(IExecuteObject eTask) {
		waiting.remove(eTask);

		if (!failed.contains(eTask)) {
			failed.add(eTask);
		}

	}

	public synchronized void add(IExecuteObject eTask) {

		waiting.add(eTask);

	}

	public IExecuteObject next() {

		for (IExecuteObject eTask : waiting) {
			DependExecuteObject executeObject = (DependExecuteObject) eTask;

			ExcuteDepend depend = jobContext.getDepend();

			if (null == executeObject.step) {
				executeObject.step = depend.getNextStep(-1);

			}

			if (executeObject.step.isCanExecute()) {
				executeObject.step.setRun();
				return executeObject;
			} else {
				if (executeObject.step.isFinished(executeObject
						.getTaskProgress())) {
					executeObject.step = depend
							.getNextStep(executeObject.step.index);
					executeObject.step.setRun();
					return executeObject;
				}

			}

		}

		return null;

	}

	public boolean canFetch() {
		return true;
	}

	public ClientJobStatus getJobStatus() {
		clientJobStatus.setFailed(failed.size());
		clientJobStatus.setWaiting(waiting.size());
		clientJobStatus.setProcessing(processing.size());
		clientJobStatus.setSuccess(success.size());
		clientJobStatus.setThreadsize(threads.size());
		return clientJobStatus;
	}

	public List<IExecuteObject> getSuccess() {
		return success;
	}

	private class ExcuteThreadPoolDaemonThread extends Thread {

		@Override
		public void run() {
			while (true) {
				Date date = new Date();
				if (running) {

					logger.info("ExcuteThreadPoolDaemonThread " + date
							+ "可执行的任务" + waiting.size() + "个" + " 正在执行成功的任务"
							+ processing.size() + "个" + " 执行成功的任务"
							+ success.size() + "个" + " 执行失败的任务" + failed.size()
							+ "个" + " 可使用的线程" + threads.size() + "个");

					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					logger.info("ExcuteThreadPoolDaemonThread exit! "
							+ date + "可执行的任务" + waiting.size() + "个"
							+ " 正在执行成功的任务" + processing.size() + "个"
							+ " 执行成功的任务" + success.size() + "个" + " 执行失败的任务"
							+ failed.size() + "个" + " 可使用的线程" + threads.size()
							+ "个");
					break;
				}
			}
		}

	}

}
