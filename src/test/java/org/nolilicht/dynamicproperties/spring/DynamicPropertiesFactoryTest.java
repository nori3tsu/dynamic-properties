package org.nolilicht.dynamicproperties.spring;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nolilicht.dynamicproperties.DummyClassLoader;
import org.nolilicht.dynamicproperties.DynamicProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


@ContextConfiguration(locations = {"/test-application-context.xml"})
public class DynamicPropertiesFactoryTest extends AbstractJUnit4SpringContextTests {
	@Test
	public void factory() throws Exception {
		DynamicProperties props = (DynamicProperties) this.applicationContext.getBean("test-dynamic-properties-factory.properties");

		assertEquals("classpath:test-dynamic-properties-factory.properties", props.getLocations());
		assertEquals(60000L, props.getReloadInterval());
		assertEquals(DummyClassLoader.class, props.getClassLoader().getClass());
	}
}
