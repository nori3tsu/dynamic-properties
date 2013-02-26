package org.nolilicht.dynamicproperties;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.junit.Test;

public class DynamicPropertiesTest {
	private static final String CLASSPATH_URL_PREFIX = "classpath:";
	private static final File CLASSPATH_DIR;
	private static final String CURRENT_PACKAGE_CLASSPATH = DynamicPropertiesTest.class.getPackage().getName().replace(".", "/");

	private static final String PROPERTEIS1_CLASSPATH = CURRENT_PACKAGE_CLASSPATH + "/" + "test-dynamic.properties1";
	private static final String PROPERTEIS2_CLASSPATH = CURRENT_PACKAGE_CLASSPATH + "/" + "test-dynamic.properties2";

	private static final String PROPERTIES1_FILEPATH;
	private static final String PROPERTIES2_FILEPATH;
	static {
		try {
			CLASSPATH_DIR = new File(DynamicPropertiesTest.class.getResource("").toURI());
			PROPERTIES1_FILEPATH = Thread.currentThread().getContextClassLoader().getResource(PROPERTEIS1_CLASSPATH).toURI().getPath();
			PROPERTIES2_FILEPATH = Thread.currentThread().getContextClassLoader().getResource(PROPERTEIS2_CLASSPATH).toURI().getPath();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static final String TMP_PROPERTIES_FILENAME = "tmp.properties";

	@Test
	public void getClassPathLocation() throws Exception {
		// クラスパスからの読み込み
		Properties props = new DynamicProperties(CLASSPATH_URL_PREFIX + PROPERTEIS1_CLASSPATH);
		assertEquals(props.get("a"), "1");
	}

	@Test
	public void getFileLocation() throws Exception {
		// パスからの読み込み
		Properties props = new DynamicProperties(PROPERTIES1_FILEPATH);
		assertEquals(props.get("a"), "1");
	}

	@Test
	public void getClassPathLocations() throws Exception {
		// クラスパスからの複数ファイル読み込み
		Properties props = new DynamicProperties(CLASSPATH_URL_PREFIX + PROPERTEIS1_CLASSPATH + "," +
				CLASSPATH_URL_PREFIX + PROPERTEIS2_CLASSPATH);
		assertEquals(props.size(), 3);
		assertEquals(props.get("c"), "4");
	}

	@Test
	public void getFileLocations() throws Exception {
		// パスからの複数ファイル読み込み
		Properties props = new DynamicProperties(PROPERTIES1_FILEPATH + "," +
				PROPERTIES2_FILEPATH);
		assertEquals(props.size(), 3);
		assertEquals(props.get("c"), "4");
	}

	@Test
	public void intervel() throws Exception {
		File tmpFile = new File(CLASSPATH_DIR, TMP_PROPERTIES_FILENAME);
		OutputStream propsWriterOutputStream = null;
		try {
			// 書き込み用のプロパティを作成
			propsWriterOutputStream = new FileOutputStream(tmpFile);
			Properties propsWriter = new Properties();
			propsWriter.store(propsWriterOutputStream, null);

			// テスト対象のDynamicProperties
			Properties props = new DynamicProperties(tmpFile.getAbsolutePath(), 1000L);
			assertNull(props.get("a"));

			// 値を設定してファイルに書き込み
			propsWriter.put("a", "1");
			propsWriter.store(propsWriterOutputStream, null);

			// リロード間隔に達していない場合は読み込まれていない
			assertNull(props.get("a"));

			Thread.sleep(1500L);

			// リロード間隔に達した場合は再読み込み
			assertNotNull(props.get("a"));
		} finally {
			if (propsWriterOutputStream != null) {
				propsWriterOutputStream.close();
			}

			if (tmpFile.exists()) {
				tmpFile.delete();
			}
		}
	}

	@Test(expected = ClassPathFileNotFoundException.class)
	public void classloader() throws Exception {
		ClassLoader dummyClassLoader = new ClassLoader(null) {
		};

		// 異なるクラスローダーであるためファイルが読み込めない
		new DynamicProperties(dummyClassLoader, CLASSPATH_URL_PREFIX + PROPERTEIS1_CLASSPATH, 0);
	}

	@Test
	public void clear() throws Exception {
		Properties props = new DynamicProperties(CLASSPATH_URL_PREFIX + PROPERTEIS1_CLASSPATH);

		assertTrue(props.size() > 0);

		props.clear();

		assertTrue(props.size() == 0);
	}
}
