package com.zarholding.jpacustomer.tools.smsretriever;

import android.content.Context;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;

/**
 * Created by m-latifi on 7/6/2023.
 */

public class SmsClient {

    //---------------------------------------------------------------------------------------------- start
    public void start(Context context) {
        SmsRetrieverClient client = SmsRetriever.getClient(context);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(aVoid -> {});
        task.addOnFailureListener(e -> {});
    }
    //---------------------------------------------------------------------------------------------- start
}
