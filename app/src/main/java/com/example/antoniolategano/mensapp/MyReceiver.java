package com.example.antoniolategano.mensapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class MyReceiver extends BroadcastReceiver {

    static Intent service;

    @Override
    public void onReceive(Context context, Intent intent) {
        service = new Intent(context, MyIntentService.class);
        context.startService(service);
    }
}
