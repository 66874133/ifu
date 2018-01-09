package com.duplex.crawler;

import java.util.List;

import com.duplex.crawler.client.IClientJob;

public class DependExcuteThread extends Thread {

	private IClientJob clientJob;

	private JobContext jobContext;

	private boolean running = false;

	public DependExcuteThread(JobContext jobContext, IClientJob clientJob) {
		this.clientJob = clientJob;
		this.jobContext = jobContext;
	}

	public void exit() {
		running = false;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			String key = null;
			DependExecuteObject dependExecuteObject;
			synchronized (clientJob) {

				dependExecuteObject = (DependExecuteObject) clientJob.next();
				if (null != dependExecuteObject) {
					key = dependExecuteObject.step.getRun();

					if (null == key) {

						if (jobContext.getDepend().isFinished(
								dependExecuteObject.getTaskProgress())) {
							clientJob.success(dependExecuteObject);
							System.out.println(dependExecuteObject
									+ "->处理完成 处理轨迹"
									+ dependExecuteObject.getTaskProgress());

						}

					}
				}

			}
			// 数据池里面没有任务
			if (null != dependExecuteObject) {

				if (null != key) {
					IExcuteable excuteable = jobContext.getDepend().getExcutor(
							key);
					System.out.println(excuteable.getKey()
							+ " begin to execute" + dependExecuteObject);
					ExecuteResult result = excuteable.excute(jobContext,
							dependExecuteObject);
					synchronized (clientJob) {
						dependExecuteObject
								.addTaskProgress(excuteable.getKey());
					}

					/**
					 * 处理完成看是否分裂出新的待处理对象
					 */
					if (result.getCode() == ExecuteResult.CODE_OK) {

						if (null != result.getNewList()
								&& result.getNewList().size() > 0) {
							List<IExecuteObject> list = result.getNewList();
							for (IExecuteObject o : list) {
								clientJob.add(o);
							}
							System.out.println(dependExecuteObject
									+ "->处理成功 处理轨迹"
									+ dependExecuteObject.getTaskProgress());
						}

					} else {
						clientJob.failed(dependExecuteObject);
						System.out.println(dependExecuteObject + "[错误详情:"
								+ result.getMessage() + "]->处理失败 处理轨迹"
								+ dependExecuteObject.getTaskProgress());
					}

				}

			}// 数据池里面没有任务 暂停
			else {
				try {
					System.out.println(this.getName() + "->no task !");
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		System.out.println(this.getName() + " running=" + running + "->exit !");
	}
}
