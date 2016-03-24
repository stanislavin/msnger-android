package com.stanislavin.msnger;

/**
 * Created by cpjv68 on 24/03/16.
 */
public class JNIMsnger {

    static {
        System.loadLibrary("msnger-jni");
    }
    native void sendMessage(char[] number, char[] message, double lat, double lon);
}
