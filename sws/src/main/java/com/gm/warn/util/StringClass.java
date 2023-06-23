package com.gm.warn.util;

public class StringClass {

    String press;

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public StringClass(String press) {
        this.press = press;
    }
    public StringClass() {
        this.press = "";
    }

    @Override
    public String toString() {
        return "StringClass{" +
                "press='" + press + '\'' +
                '}';
    }
}
