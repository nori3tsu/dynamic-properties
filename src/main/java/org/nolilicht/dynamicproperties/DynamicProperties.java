package org.nolilicht.dynamicproperties;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class DynamicProperties extends Properties {
	private static final long serialVersionUID = -6404550854352698622L;

	private DynamicPropertiesLoader dynamicPropertiesLoader;

	private ClassLoader classLoader;

	private String locations;

	private long reloadInterval;

	public DynamicProperties(String locations) throws IOException {
		this(Thread.currentThread().getContextClassLoader(), locations, 0);
	}

	public DynamicProperties(String locations, long reloadInterval)
			throws IOException {
		this(Thread.currentThread().getContextClassLoader(), locations,
				reloadInterval);
	}

	public DynamicProperties(ClassLoader classLoader, String locations,
			long reloadInterval) throws IOException {
		this.locations = locations;
		this.classLoader = classLoader;
		this.reloadInterval = reloadInterval;

		this.dynamicPropertiesLoader = new DynamicPropertiesLoader(classLoader,
				locations, reloadInterval);

		dynamicPropertiesLoader.load(this);
	}

	@Override
	public synchronized Object get(Object key) {
		try {
			dynamicPropertiesLoader.reload(this);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

		return super.get(key);
	}

	@Override
	public String getProperty(String key) {
		/*
		 * java.util.Propertiesクラスではjava.util.Hashtableのgetを呼び出しているため
		 * 自身のget(key)を呼び出すように修正。
		 */
		// Object oval = super.get(key);
		Object oval = this.get(key);
		String sval = (oval instanceof String) ? (String) oval : null;
		return ((sval == null) && (defaults != null)) ? defaults
				.getProperty(key) : sval;
	}

	@Override
	public synchronized boolean isEmpty() {
		try {
			dynamicPropertiesLoader.reload(this);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

		return super.isEmpty();
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		try {
			dynamicPropertiesLoader.reload(this);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

		return super.keys();
	}

	@Override
	public synchronized Enumeration<Object> elements() {
		try {
			dynamicPropertiesLoader.reload(this);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

		return super.elements();
	}

	@Override
	public synchronized boolean contains(Object value) {
		try {
			dynamicPropertiesLoader.reload(this);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

		return super.contains(value);
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		try {
			dynamicPropertiesLoader.reload(this);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

		return super.containsKey(key);
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public String getLocations() {
		return locations;
	}

	public long getReloadInterval() {
		return reloadInterval;
	}
}
