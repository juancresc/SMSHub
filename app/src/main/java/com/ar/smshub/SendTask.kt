package com.ar.smshub

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.telephony.SmsManager
import android.util.Log
import com.beust.klaxon.Klaxon
import java.net.URL
import java.util.*

class SMS(val message: String, val number: String, val id: String)

class SendTask constructor(_settings: SettingsManager, _context: Context) : TimerTask() {
    var settings = _settings
    var context = _context

    override fun run() {

        val SENT_SMS_FLAG = "SENT_SMS"
        val DELIVER_SMS_FLAG = "DELIVER_SMS"

        val sentIn = Intent(SENT_SMS_FLAG)
        sentIn.putExtra("statusURL",settings.statusURL)
        sentIn.putExtra("deviceId",settings.deviceId)
        val sentPIn = PendingIntent.getBroadcast(context, 0, sentIn, 0)

        val deliverIn = Intent(DELIVER_SMS_FLAG)
        deliverIn.putExtra("statusURL",settings.statusURL)
        deliverIn.putExtra("deviceId",settings.deviceId)
        val deliverPIn = PendingIntent.getBroadcast(context, 0, deliverIn, 0)


        val apiResponse = URL(settings.sendURL).readText()
        try {
            val sms = Klaxon().parse<SMS>(apiResponse)
            sentIn.putExtra("messageId",sms!!.id)
            deliverIn.putExtra("messageId",sms!!.id)
            val smsManager = SmsManager.getDefault() as SmsManager
            smsManager.sendTextMessage(sms!!.number, null, sms!!.message, sentPIn, deliverPIn)

        }
        catch (e: com.beust.klaxon.KlaxonException) {
            Log.d("error","Error while parsing URL")
        }
        finally {
            // optional finally block
        }



    }

}
