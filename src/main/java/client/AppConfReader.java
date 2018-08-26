package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class AppConfReader {

    private static Properties prop = new Properties();
    private static Logger logger = LoggerFactory.getLogger(AppConfReader.class);

    public static String getConfDir(){
        final String appConfName = "app_conf.txt";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream input = classloader.getResourceAsStream(appConfName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            prop.load(reader);
        } catch (IOException e) {
           logger.error("Can't load property file ", appConfName);
           return null;
        }

        final String KEY = "conf.directory";
        return prop.getProperty(KEY);
    }
}
