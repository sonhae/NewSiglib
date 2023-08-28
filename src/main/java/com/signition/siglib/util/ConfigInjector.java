package com.signition.siglib.util;

import com.google.inject.Inject;
import com.signition.siglib.config.annotations.Path;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ConfigInjector {

    public static <T> T injectConfig(Class<T> configClass, InputStream inputStream) {
        T configObject = null;
        try {
            configObject = configClass.getDeclaredConstructor().newInstance();
            for (Field field : configClass.getDeclaredFields()) {

                String path = field.getName();
                Class<?> type = field.getType();


                if (field.isAnnotationPresent(Path.class)) {
                    path = field.getAnnotation(Path.class).path();
                    type = field.getAnnotation(Path.class).type();
                }

                Object value = get(type, path, inputStream);
                setFieldValue(configObject, field, value);
            }

            return configObject;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T get(Class<T> type, String path, InputStream inputStream) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));

        return (T) config.get(path);
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
