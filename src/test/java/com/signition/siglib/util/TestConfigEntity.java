package com.signition.siglib.util;

import com.signition.siglib.config.annotations.Config;

@Config(fileName = "test.yml")

public class TestConfigEntity {
    private String a;
    private String b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
