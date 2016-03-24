package com.stanislavin.msnger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SendMessageActivity extends AppCompatActivity {

    JNIMsnger mMsnger = new JNIMsnger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Log.i("SendMessageActivity", "calling sendMessage in JNI");

        JNIMsnger.Message msg = new JNIMsnger.Message();
        msg.number = "79213276120";
        msg.text = "Hello from Android/Java!";
        msg.lat = 59.879285;
        msg.lon = 30.442554;
        mMsnger.send(msg, new JNIMsnger.IMessageResultHandler() {
            @Override
            public void onMessageSent(final int code) {
                Log.i("SendMessageActivity", "message was sent, code=" + code);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(SendMessageActivity.this, "message was sent, code=" + code, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }
}
