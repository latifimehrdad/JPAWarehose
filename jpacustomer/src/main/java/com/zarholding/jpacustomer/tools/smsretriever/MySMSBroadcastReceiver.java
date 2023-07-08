package com.zarholding.jpacustomer.tools.smsretriever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * Created by m-latifi on 7/5/2023.
 */

public class MySMSBroadcastReceiver extends BroadcastReceiver {

    public static GetSms getSms;

    public interface GetSms {
        void onReceive(String message);
    }


    //---------------------------------------------------------------------------------------------- onReceive
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    if (getSms != null)
                        getSms.onReceive(message);
                    break;
                case CommonStatusCodes.TIMEOUT:
                    break;
            }
        }
    }
    //---------------------------------------------------------------------------------------------- onReceive


}

