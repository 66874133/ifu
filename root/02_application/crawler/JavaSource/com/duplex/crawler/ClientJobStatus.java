package com.duplex.crawler;

public class ClientJobStatus {

	private long waiting;
	private long processing;
	private long success;
	private long failed;
	private int threadsize;

	public long getWaiting() {
		return waiting;
	}

	public void setWaiting(long waiting) {
		this.waiting = waiting;
	}

	public long getProcessing() {
		return processing;
	}

	public void setProcessing(long processing) {
		this.processing = processing;
	}

	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public int getThreadsize() {
		return threadsize;
	}

	public void setThreadsize(int threadsize) {
		this.threadsize = threadsize;
	}

	public boolean isFinished() {
		if (0 == waiting) {
			return true;
		}
		return false;
	}

	public String detail() {
		return "可执行的任务" + waiting + "个" + " 正在执行成功的任务" + processing + "个"
				+ " 执行成功的任务" + success + "个" + " 执行失败的任务" + failed + "个"
				+ " 可使用的线程" + threadsize + "个";
	}
}
