package com.stanislavin.msnger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SendMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        JNIMsnger msnger = new JNIMsnger();
        Log.i("JNIMsnger", "calling sendMessage in JNI");
        msnger.sendMessage("79213276120".toCharArray(), "Hello from Android/Java!".toCharArray(), 0, 0);
    }
}
