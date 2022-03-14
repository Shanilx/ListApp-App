package com.listapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class MySMSBroadcastReceiver extends BroadcastReceiver {
    private OTPReceiveListener otpReceiver = null;

    public void initOTPListener(OTPReceiveListener receiver) {
        this.otpReceiver = receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            Log.d("ssv", String.valueOf(status));

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    if (otpReceiver != null ) {
                        //message = message.replace("<#> Your ExampleApp code is: ", "");
                        //   String str="sdfvsdf68fsdfsf8999fsdf09";
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);

                        otpReceiver.onOTPReceived(consentIntent);
                    }
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    if (otpReceiver != null) {
                        otpReceiver.onOTPTimeOut();
                    }
                    break;
            }
        }
    }

    public interface OTPReceiveListener {

        void onOTPReceived(Intent intent);

        void onOTPTimeOut();
    }
}