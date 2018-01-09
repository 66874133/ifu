package com.funnel.svc.comon;

import com.funnel.svc.SvcContext;


public interface AsyncSvcExecutor {
	public SvcContext getContext();

	public void afterThreadPoolRefuse();
}
