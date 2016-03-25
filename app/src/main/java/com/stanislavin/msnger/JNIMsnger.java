package com.stanislavin.msnger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stanislav Slavin on 24/03/16.
 */
public class JNIMsnger {

    interface IMessageResultHandler {
        void onMessageSent(int code);
    }

    static class Message {
        String number;
        String text;
        double lat;
        double lon;
    }

    private ArrayList<IMessageResultHandler> mHandlers = new ArrayList<IMessageResultHandler>();

    static {
        System.loadLibrary("msnger-jni");
    }

    public JNIMsnger() {
        init();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        deinit();
    }

    private byte[] string2Bytes(String str) {
        byte[] strbytes = str.getBytes(StandardCharsets.US_ASCII);
        // append zero-terminator
        byte[] bytes = Arrays.copyOf(strbytes, strbytes.length + 1);
        return bytes;
    }

    public void send(Message message, IMessageResultHandler handler) {
        mHandlers.add(handler);
        byte number[] = string2Bytes(message.number);
        byte text[] = string2Bytes(message.text);
        sendMessage(number, text, message.lat, message.lon);
    }

    private void onMessageSent(int code) {
        // messages are processed by core engine in FIFO manner
        // so expect that oldest handler is the one to notify
        if (!mHandlers.isEmpty()) {
            IMessageResultHandler h = mHandlers.remove(0);
            h.onMessageSent(code);
        }
    }

    private native void init();
    public native void deinit();
    private native void sendMessage(byte[] number, byte[] message, double lat, double lon);
}
