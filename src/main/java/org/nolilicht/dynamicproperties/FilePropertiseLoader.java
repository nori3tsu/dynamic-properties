package org.nolilicht.dynamicproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilePropertiseLoader extends PropertiesLoader {
	FilePropertiseLoader(String location) {
		this.location = location;
	}

	@Override
	Properties load() throws IOException {
		InputStream inStream = null;

		try {
			inStream = new FileInputStream(location);
			return load(inStream);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
	}
}
