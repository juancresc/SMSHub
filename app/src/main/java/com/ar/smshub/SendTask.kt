package com.ar.smshub

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.beust.klaxon.Klaxon
import java.net.URL
import java.util.*


class SMS(val message: String, val number: String, val id: String)

class SendTask constructor(_settings: SettingsManager, _context: Context) : TimerTask() {
    var settings = _settings
    var mainActivity: MainActivity = _context as MainActivity

    override fun run() {

        val SENT_SMS_FLAG = "SENT_SMS"
        val DELIVER_SMS_FLAG = "DELIVER_SMS"

        val sentIn = Intent(SENT_SMS_FLAG)
        sentIn.putExtra("statusURL", settings.statusURL)
        sentIn.putExtra("deviceId", settings.deviceId)
        val sentPIn = PendingIntent.getBroadcast(mainActivity, 0, sentIn, 0)

        val deliverIn = Intent(DELIVER_SMS_FLAG)
        deliverIn.putExtra("statusURL", settings.statusURL)
        deliverIn.putExtra("deviceId", settings.deviceId)
        val deliverPIn = PendingIntent.getBroadcast(mainActivity, 0, deliverIn, 0)

        var apiResponse = khttp.post(
            url = settings.sendURL,
            data = mapOf(
                "deviceId" to settings.deviceId,
                "action" to "SEND"
            )
        )

        try {
            val sms = Klaxon().parse<SMS>(apiResponse.text)
            sentIn.putExtra("messageId", sms!!.id)
            deliverIn.putExtra("messageId", sms!!.id)
            val smsManager = SmsManager.getDefault() as SmsManager
            //smsManager.sendTextMessage(sms!!.number, null, sms!!.message, sentPIn, deliverPIn)
            mainActivity.runOnUiThread(Runnable {
                mainActivity.logMain("Sent to: " + sms!!.number + " - id: " + sms!!.id + " - message: " + sms!!.message)
            })


            Log.d("error", "SEND")
        } catch (e: com.beust.klaxon.KlaxonException) {
            if(apiResponse.text == ""){
                mainActivity.runOnUiThread(Runnable {
                    mainActivity.logMain(".", false)
                })
                Log.d("-->", "Nothing")
            }else{
                mainActivity.runOnUiThread(Runnable {
                    mainActivity.logMain("Error parsing response from server")
                })
                Log.d("error", "Error while parsing SMS")
            }


        } finally {
            // optional finally block
        }


    }

}
