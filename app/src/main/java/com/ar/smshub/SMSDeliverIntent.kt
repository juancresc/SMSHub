package com.ar.smshub

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Activity
import java.net.URL


class SMSDeliverIntent : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var statusUrl = intent!!.getStringExtra("statusURL")
        var deviceId =  intent!!.getStringExtra("deviceId")
        var messageId =  intent!!.getStringExtra("messageId")
        var finalStatusUrl = "$statusUrl?deviceId=$deviceId&messageId=$messageId&status=DELIVERED"
        URL(finalStatusUrl).readText()
    }
}