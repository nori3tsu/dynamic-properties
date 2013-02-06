package com.nolilicht.dynamicproperties;

import java.io.IOException;

public class ClassPathFileNotFoundException extends IOException {
	private static final long serialVersionUID = -102180806859614672L;

	ClassPathFileNotFoundException(String location) {
		super(location);
	}
}
