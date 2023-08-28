package com.signition.siglib.config;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.signition.siglib.TestPlugin;
import com.signition.siglib.util.ConfigInjector;
import com.signition.siglib.util.TestConfigEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class TestConfigInjector {
    ServerMock server;
    TestPlugin plugin;



    @BeforeEach
    public void before() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(TestPlugin.class);

    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }


    @Test
    public void TestConfigDI() {
        Injector injector = Guice.createInjector(new ConfigModule(plugin));
        TestConfigEntity entity = injector.getInstance(TestConfigEntity.class);
        Assertions.assertThat(entity.getA()).isEqualTo("test!");
    }

    @Test
    public void TestConfigInject() {
        String resourcePath = "test.yml";

        
        ClassLoader classLoader = TestConfigInjector.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                ConfigInjector ci = new ConfigInjector();
                TestConfigEntity e = ci.injectConfig(TestConfigEntity.class, inputStream);

                Assertions.assertThat(e.getA()).isEqualTo("test!");
            } else {
                System.out.println("Resource not found: " + resourcePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
