package com.listapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by syscraft on 8/2/2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            if (sender.contains("LSTAPP")) {
                String messageBody = smsMessage.getMessageBody();
                mListener.messageReceived(messageBody.substring(35,41));
            }
        }
    }

    /*
        Sender:  HP-LSTAPP
        msg: Your ListApp Verification Code is: 123456. Please do not reply to this message. Thanks for using ListApp.
    */

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}


