package com.strelive.utils;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class ConfigUtils {
    public static String getProperty(String key) {
        Config config = ConfigProvider.getConfig();
        return config.getValue(key, String.class);
    }
}
