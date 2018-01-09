package com.funnel.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderConfigurerCustomer extends
		PropertyPlaceholderConfigurer {
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		List<Entry> list = new ArrayList<Entry>();
		Set<Map.Entry<Object, Object>> entrys = props.entrySet();
		for (Entry<Object, Object> entry : entrys) {
			list.add(entry);
		}
		synchronized (this) {
			for (Entry e : list) {
				String key = String.valueOf(e.getKey());

				props.put(key, String.valueOf(e.getValue()));
				logger.info("key=" + key + " value=" + e.getValue());
			}
		}
		super.processProperties(beanFactoryToProcess, props);
	}

}
