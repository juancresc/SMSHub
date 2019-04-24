package com.ar.smshub

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Activity


class SMSSendIntent : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                //sent SMS message successfully;
            }
            else -> {
                //sent SMS message failed
            }
        }
    }
}