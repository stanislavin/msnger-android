package com.stanislavin.msnger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessageActivity extends AppCompatActivity {
    
    JNIMsnger mMsnger = new JNIMsnger();
    TextView  mTvNumber;
    TextView  mTvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        mProgressDialog = new ProgressDialog(this);

        mTvMessage = (TextView)findViewById(R.id.editText2);
        mTvMessage.setMovementMethod(new ScrollingMovementMethod());

        mTvNumber = (TextView)findViewById(R.id.editText);

        Button bt = (Button)findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMessagePressed();
            }
        });

        Log.i("SendMessageActivity", "calling sendMessage in JNI");
    }

    void onSendMessagePressed() {

        hideKeyboard();

        if (mTvNumber.getText().toString().isEmpty()) {
            showToast("Number is empty!");
            return;
        }

        JNIMsnger.Message msg = new JNIMsnger.Message();
        msg.number = mTvNumber.getText().toString();
        msg.text = mTvMessage.getText().toString();
        Location loc = getLocation();
        if (loc == null) {
            showAlert("Your location unknown", "Please check GPS settings.");
            return;
        } else {
            msg.lat = loc.getLatitude();
            msg.lon = loc.getLongitude();
        }

        showProgress("Sending message", "Please wait...");
        mMsnger.send(msg, new JNIMsnger.IMessageResultHandler() {
            @Override
            public void onMessageSent(final int code) {
                Log.i("SendMessageActivity", "message was sent, code=" + code);
                hideProgress();
                showAlert(Errors.getTitleFromErrorCode(code),Errors.getMessageFromErrorCode(code));
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    Location getLocation() {
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return loc;
    }

    void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(SendMessageActivity.this, text, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    void showAlert(final String title, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(SendMessageActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing, just hide dialog
                            }
                        })
                        .show();
            }
        });
    }

    ProgressDialog mProgressDialog;
    void showProgress(String title, String message) {
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    void hideProgress() {
        mProgressDialog.dismiss();
    }
}
