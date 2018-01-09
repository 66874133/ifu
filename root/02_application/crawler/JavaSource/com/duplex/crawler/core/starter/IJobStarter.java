package com.duplex.crawler.core.starter;

import java.io.Serializable;
import java.util.List;

import com.duplex.crawler.IExecuteObject;

public interface IJobStarter extends Serializable {

	public List<IExecuteObject> getExecuteObjects();
}
