package com.stanislavin.msnger;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
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

    static final int ERROR_CODE_SUCCESS                      = 0;
    static final int ERROR_CODE_UNSUCCESSFUL                 = -1;
    static final int ERROR_CODE_TIMEOUT                      = -5;
    static final int ERROR_CODE_GIS_FAILURE                  = -6;
    static final int ERROR_GIS_TIMEOUT                       = -7;
    static final int ERROR_INFOBIP_TIMEOUT                   = -8;
    static final int ERROR_INFOBIP_REJECTED_NO_DESTINATION   = -9;
    static final int ERROR_INFOBIP_PENDING                   = -10;
    static final int ERROR_HOST_NOT_FOUND                    = -11;
    static final int ERROR_GIS_RESULTS_EMPTY                 = -12;
    static final int ERROR_INFOBIP_REJECTED_NO_PREFIX        = -13;
    
    
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
                showAlert(getTitleFromErrorCode(code), getMessageFromErrorCode(code));
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
    
    private String getTitleFromErrorCode(int code) {
        switch (code) {
            case ERROR_CODE_SUCCESS:
            case ERROR_INFOBIP_PENDING:
                return "Message sent";
            case ERROR_CODE_UNSUCCESSFUL:
            case ERROR_CODE_TIMEOUT:
            case ERROR_CODE_GIS_FAILURE:
            case ERROR_GIS_TIMEOUT:
            case ERROR_INFOBIP_TIMEOUT:
            case ERROR_INFOBIP_REJECTED_NO_DESTINATION:
            case ERROR_HOST_NOT_FOUND:
            case ERROR_GIS_RESULTS_EMPTY:
            case ERROR_INFOBIP_REJECTED_NO_PREFIX:
                return "Message not sent";
        }
        return "Message was sent";
    }

    String getMessageFromErrorCode(int code) {
        switch (code) {
            case ERROR_CODE_SUCCESS:
                return "";
            case ERROR_CODE_UNSUCCESSFUL:
                return "Please try again later.";
            case ERROR_CODE_TIMEOUT:
                return "Timed out while waiting for result. Try again later.";
            case ERROR_CODE_GIS_FAILURE:
                return "Cannot retrieve street location. Try again later.";
            case ERROR_GIS_TIMEOUT:
                return "Timed out while getting street location. Try again later.";
            case ERROR_INFOBIP_TIMEOUT:
                return "Timed out while sending message. Try again later.";
            case ERROR_INFOBIP_REJECTED_NO_DESTINATION:
                return "Please check if recipient number is correct.";
            case ERROR_INFOBIP_PENDING:
                return "Your message was submitted to operator for delivery.";
            case ERROR_HOST_NOT_FOUND:
                return "Cannot connect to the server. Please check your connectivity settings.";
            case ERROR_GIS_RESULTS_EMPTY:
                return "Cannot obtain street address. Please check your GPS settings.";
            case ERROR_INFOBIP_REJECTED_NO_PREFIX:
                return "Invalid prefix in recipient number.";
        }
        return "Message was sent";
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
