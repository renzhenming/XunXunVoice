package com.example.myapplication;

/**
 * Created by renzhenming on 2017/7/20.
 */

public class TestJni {
    static {
        System.loadLibrary("hello");
    }
    public static native String hello();
}
