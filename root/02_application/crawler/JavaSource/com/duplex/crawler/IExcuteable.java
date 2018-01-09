package com.duplex.crawler;

import java.io.Serializable;

public interface IExcuteable extends Serializable {

	public ExecuteResult excute(JobContext jobContext,
			IExecuteObject executeObject);

	public String getKey();
}
