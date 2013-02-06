package com.nolilicht.dynamicproperties.spring;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.nolilicht.dynamicproperties.DynamicProperties;
import com.nolilicht.dynamicproperties.DynamicPropertiesLoader;

public class DynamicPropertiesFactory implements InitializingBean, FactoryBean {
	private DynamicProperties properties;

	private ClassLoader classLoader;

	private List<String> locations;

	private long reloadInterval;

	public void afterPropertiesSet() throws Exception {
		this.classLoader = this.classLoader != null ? this.classLoader : Thread.currentThread().getContextClassLoader();

		StringBuilder locationStrings = new StringBuilder();
		Iterator<String> locationsIterator = locations.iterator();
		while (locationsIterator.hasNext()) {
			locationStrings.append(locationsIterator.next());
			if (locationsIterator.hasNext()) {
				locationStrings.append(DynamicPropertiesLoader.LOCATION_SEPARATOR);
			}
		}

		this.properties = new DynamicProperties(this.classLoader, locationStrings.toString(), this.reloadInterval);
	}

	public Object getObject() throws BeansException {
		return this.properties;
	}

	public Class<?> getObjectType() {
		return DynamicProperties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public long getReloadInterval() {
		return reloadInterval;
	}

	public void setReloadInterval(long reloadInterval) {
		this.reloadInterval = reloadInterval;
	}
}
