package com.nolilicht.dynamicproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilePropertiseLoader extends PropertiesLoader {
	public FilePropertiseLoader(String location) {
		this.location = location;
	}

	@Override
	public Properties load() throws IOException {
		InputStream inStream = new FileInputStream(location);
		return load(inStream);
	}
}
