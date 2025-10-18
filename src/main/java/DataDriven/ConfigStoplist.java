/**
 * @author semyvolos_h
 * @date 8/29/2023 3:11 PM
 */
package DataDriven;

import java.io.InputStream;
import java.util.Properties;

public class ConfigStoplist {

    private static Properties properties;
    private static final String dataFile = "/DataDriven/api.stoplist.properties";

    private static void loadProperties(){
        properties = new Properties();
        InputStream in = ConfigOnboarding.class.getResourceAsStream(dataFile);

        try {
            properties.load(in);
            assert in != null;
            in.close();
        } catch (Exception e){
            System.out.println("Unable to read resource file");
            System.out.println(e.getMessage());
        }
    }

    public static String getProperty(String propertyName){
        if(properties == null){
            loadProperties();
        }
        return properties.getProperty(propertyName);
    }
}
