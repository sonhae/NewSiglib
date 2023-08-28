package com.signition.siglib.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.signition.siglib.TestPlugin;
import com.signition.siglib.config.ConfigModule;
import com.signition.siglib.config.annotations.Config;
import org.apache.commons.lang3.ClassPathUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;

import static java.io.File.pathSeparator;

public class ReflectionTest {

    ServerMock server;
    TestPlugin plugin;

    Injector i;

    @BeforeEach
    public void before() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(TestPlugin.class);
        i = Guice.createInjector(new ConfigModule(plugin));
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void reflectionTest(){
         ReflectionUtil.getAnnotatedClass(Config.class).stream().map(Class::getName).forEach(System.out::println);

    }

    @Test
    void AnnotationFindTest() {
        Assertions.assertThat(this.i.getInstance(TestConfigEntity.class).getA()).isEqualTo("test!");
    }
}
