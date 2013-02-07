package com.nolilicht.dynamicproperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class PropertiesLoader {
	private static final String XML_FILE_SUFFIX = ".xml";

	protected String location;

	abstract Properties load() throws IOException ;

	protected Properties load(InputStream inStream) throws IOException {
		try {
			Properties props = new Properties();
			if (isXml()) {
				props.loadFromXML(inStream);
			} else {
				props.load(inStream);
			}

			return props;
		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
	}

	protected boolean isXml() {
		return location.endsWith(XML_FILE_SUFFIX);
	}
}
