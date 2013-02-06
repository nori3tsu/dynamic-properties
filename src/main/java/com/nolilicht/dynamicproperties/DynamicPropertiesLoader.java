package com.nolilicht.dynamicproperties;

import java.io.IOException;
import java.util.Properties;

public class DynamicPropertiesLoader {
	public static final String LOCATION_SEPARATOR = ",";
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	PropertiesLoader[] loaders;

	long reloadInterval;

	boolean alwaysLoading;

	long lastLoaded;

	DynamicPropertiesLoader(ClassLoader classLoader, String locations,
			long checkInterval) {
		this.reloadInterval = checkInterval;
		if (locations == null || locations.equals("")) {
			throw new IllegalArgumentException();
		}

		String[] locationStrings;
		if (locations.contains(LOCATION_SEPARATOR)) {
			locationStrings = locations.split(LOCATION_SEPARATOR);
		} else {
			String location = locations;
			locationStrings = new String[] { location };
		}

		int locationsLength = locationStrings.length;
		this.loaders = new PropertiesLoader[locationsLength];

		for (int i = 0; i < locationsLength; i++) {
			String location = locationStrings[i];

			if (location.startsWith(CLASSPATH_URL_PREFIX)) {
				location = location.substring(CLASSPATH_URL_PREFIX.length()).trim();
				this.loaders[i] = new ClassPathPropertiseLoader(classLoader, location);
			} else {
				this.loaders[i] = new FilePropertiseLoader(location.trim());
			}
		}

		if (this.reloadInterval <= 0) {
			this.alwaysLoading = true;
		}

		this.lastLoaded = 0;
	}

	synchronized void load(Properties props, long now) throws IOException {
		props.clear();

		for (PropertiesLoader loader : this.loaders) {
			Properties loadedProps = loader.load();

			props.putAll(loadedProps);
		}

		this.lastLoaded = now;
	}

	void reload(Properties props) throws IOException {
		long now = System.currentTimeMillis();
		if (!alwaysLoading && (now - lastLoaded) < reloadInterval) {
			return;
		}

		load(props, now);
	}
}
