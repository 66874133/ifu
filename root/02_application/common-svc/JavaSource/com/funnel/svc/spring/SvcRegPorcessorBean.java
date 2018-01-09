package com.funnel.svc.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.funnel.svc.Service;
import com.funnel.svc.cache.Cache;
import com.funnel.svc.transformer.SvcCodeAndTransformerCodeMapping;
import com.funnel.svc.transformer.SvcMsgTransformer;
import com.funnel.svc.transformer.TransformerUtil;
import com.funnel.svc.util.CacheUtil;
import com.funnel.svc.util.ServiceUtil;

/**
 * ����ת������ע����ô���bean
 * 
 * @author wanghua4
 * 
 */
public class SvcRegPorcessorBean implements BeanPostProcessor {
	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof Service) {
			logger.info("加载服务 beanName="+beanName);
			Service service = (Service) bean;
			ServiceUtil.addSvc(service);
			SvcCodeAndTransformerCodeMapping.addMapping(service.getSvcCode(),
					service.getTransfomerCode());
			logger.info("加载服务完成 beanName="+beanName);
		} else if (bean instanceof SvcMsgTransformer) {
			logger.info("加载SvcMsgTransformer... beanName="+beanName);
			TransformerUtil.addTransfomer((SvcMsgTransformer) bean);
			logger.info("加载SvcMsgTransformer完成 beanName="+beanName);
		} else if (bean instanceof Cache) {
			logger.info("加载缓存... beanName="+beanName);
			CacheUtil.addCache((Cache) bean);
			logger.info("加载缓存完成 beanName="+beanName);
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}
}
