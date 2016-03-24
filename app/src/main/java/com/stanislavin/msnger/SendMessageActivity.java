package com.stanislavin.msnger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SendMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        JNIMsnger msnger = new JNIMsnger();
        msnger.sendMessage("79213276120".toCharArray(), "Hello from Android/Java!".toCharArray(), 0, 0);
    }
}
