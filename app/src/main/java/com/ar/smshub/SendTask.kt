package com.ar.smshub

import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.telephony.SmsManager
import android.util.Log
import com.beust.klaxon.Klaxon
import java.net.URL
import java.util.*

class SMS(val message: String, val number: String)

class SendTask constructor(_URL: String) : TimerTask() {
    var URL: String = _URL

    override fun run() {
        val apiResponse = URL(URL).readText()
        val sms = Klaxon().parse<SMS>(apiResponse)

        val smsManager = SmsManager.getDefault() as SmsManager
        smsManager.sendTextMessage(sms!!.number, null, sms!!.message, null, null)
        
    }

}
