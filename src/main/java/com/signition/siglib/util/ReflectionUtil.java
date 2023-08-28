package com.signition.siglib.util;

import com.google.common.reflect.ClassPath;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Debug;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ReflectionUtil {
    public static Set<Class> getAnnotatedClass(Class<? extends Annotation> annotation, JavaPlugin plugin) {
        return getAnnotatedClass(annotation);
    }


    public static Set<Class> getAnnotatedClass(Class<? extends Annotation> annotation) {



        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.TypesAnnotated));

        return reflections.getTypesAnnotatedWith(annotation).stream().map(c -> (Class) c).collect(Collectors.toSet());
    }

    public static Set<Class<?>> loadAllClassesInClasspath(ClassLoader classLoader) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();

        Enumeration<URL> resources = classLoader.getResources("");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                loadClassesFromDirectory(resource.getPath(), classLoader, classes);
            } else if (resource.getProtocol().equals("jar")) {
                loadClassesFromJar(resource.getPath(), classLoader, classes);
            }
        }

        return classes;
    }
    private static void loadClassesFromDirectory(String directoryPath, ClassLoader classLoader, Set<Class<?>> classes) throws ClassNotFoundException {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            return;
        }

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = getClassNameFromFile(file, "");
                Class<?> clazz = loadClass(className, classLoader);
                classes.add(clazz);
            }
        }
    }

    private static void loadClassesFromJar(String jarFilePath, ClassLoader classLoader, Set<Class<?>> classes) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = getClassNameFromFile(new File(entry.getName()), "");
                Class<?> clazz = loadClass(className, classLoader);
                classes.add(clazz);
            }
        }

        jarFile.close();
    }

    private static String getClassNameFromFile(File file, String packageName) {
        String filePath = file.getPath();
        int packageIndex = filePath.indexOf(packageName.replace('.', File.separatorChar));
        int extensionIndex = filePath.lastIndexOf(".class");
        return filePath.substring(packageIndex, extensionIndex).replace(File.separatorChar, '.');
    }

    private static Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        return classLoader.loadClass(className);
    }

}
