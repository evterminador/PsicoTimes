package com.example.formandocodigo.psicotimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.formandocodigo.psicotimes.service.StateUseService;

/**
 * Created by FormandoCodigo on 22/12/2017.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, StateUseService.class);
            context.startService(pushIntent);
        }
    }
}
