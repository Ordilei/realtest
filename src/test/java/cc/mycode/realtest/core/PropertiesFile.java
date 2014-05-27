package cc.mycode.realtest.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {
	
	private static Properties props;

	private PropertiesFile() {}
	
	public static String getProperty(String key) {
		if(props == null) {
			props = new Properties();
			try {
				props.load(new FileInputStream("src/test/resources/test.properties"));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Failed to load test.properties", e);
			}
		}
		return props.getProperty(key);
	}

}
