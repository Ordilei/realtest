package cc.mycode.realtest.core;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class ResourceLocator {

	private static Properties properties;
	
	public static String getProperty(String propertie){
		if(properties==null){
		ResourceBundle resource = ResourceBundle.getBundle("config");
		return convertResourceBundleToProperties(resource).getProperty(propertie);
		}else{
			return properties.getProperty(propertie);
		}
		
	}
	
	private static Properties convertResourceBundleToProperties(ResourceBundle resource) {
		Properties properties = new Properties();

		Enumeration<String> keys = resource.getKeys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			properties.put(key, resource.getString(key));
		}

		return properties;
	}
	
}
