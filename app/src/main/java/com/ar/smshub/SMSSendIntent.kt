package com.ar.smshub

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Activity
import java.net.URL


class SMSSendIntent : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var status : String
        when (resultCode) {
            Activity.RESULT_OK -> {
                status = "SENT"
            }
            else -> {
                status = "FAILED"
            }
        }
        var statusUrl = intent!!.getStringExtra("statusURL")
        var deviceId =  intent!!.getStringExtra("deviceId")
        var messageId =  intent!!.getStringExtra("messageId")

        khttp.post(
            url = statusUrl,
            data = mapOf("deviceId" to deviceId, "messageId" to messageId, "status" to status)
        )
    }
}