package ru.headrich.topjava.util;

import ru.headrich.topjava.util.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Montana on 22.06.2016.
 */
public class ConfigLoader {
    public static String filename  = "jdbc.properties";
    public static  Properties loadProps() throws IOException{
        Properties props=null;
        try(InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(filename)){
            if (is != null) {
                props = new Properties();
                props.load(is);
            }else{
                System.out.println("Can`t load config from file "+ filename);
                throw new NotFoundException("Can`t load config from file "+ filename+ " File not found");
            }

        }

    return props;
    }

}

