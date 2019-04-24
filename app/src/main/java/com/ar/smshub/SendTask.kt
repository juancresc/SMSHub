package com.ar.smshub

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.telephony.SmsManager
import android.util.Log
import com.beust.klaxon.Klaxon
import java.net.URL
import java.util.*

class SMS(val message: String, val number: String)

class SendTask constructor(_settings: SettingsManager) : TimerTask() {
    var settings = _settings


    override fun run() {

        var pendingIntent = PendingIntent();

        val apiResponse = URL(settings.sendURL).readText()
        try {
            val sms = Klaxon().parse<SMS>(apiResponse)
            val smsManager = SmsManager.getDefault() as SmsManager
            smsManager.sendTextMessage(sms!!.number, null, sms!!.message, new Pendi, null)

        }
        catch (e: com.beust.klaxon.KlaxonException) {
            Log.d("error","Error whle parsing URL")
        }
        finally {
            // optional finally block
        }



    }

}
