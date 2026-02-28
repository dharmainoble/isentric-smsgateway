package com.isentric.bulkgateway.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SmppConfig {

    private static Properties props = new Properties();

    static {
        try (InputStream input =
                     SmppConfig.class.getClassLoader()
                             .getResourceAsStream("smpp-credentials.properties")) {

            if (input == null) {
                throw new RuntimeException("smpp-credentials.properties not found in resources folder");
            }

            props.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getUsername(String operator, String shortcode) {
        System.out.println("getUsername" + " for " + operator + " and shortcode " + shortcode);
        return props.getProperty(operator + "." + shortcode + ".username");
    }

    public static String getPassword(String operator, String shortcode) {
        return props.getProperty(operator + "." + shortcode + ".password");
    }

    public static String getServiceId(String operator, String shortcode) {
        return props.getProperty(operator + "." + shortcode + ".serviceid");
    }


}
