package org.nolilicht.dynamicproperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClassPathPropertiseLoader extends PropertiesLoader {
	private ClassLoader classLoader;

	ClassPathPropertiseLoader(ClassLoader classLoader, String location) {
		this.classLoader = classLoader;
		this.location = location;
	}

	@Override
	Properties load() throws IOException {
		InputStream inStream = null;

		try {
			inStream = this.classLoader.getResourceAsStream(location);
			if (inStream == null) {
				throw new ClassPathFileNotFoundException(location);
			}

			return load(inStream);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
	}
}
