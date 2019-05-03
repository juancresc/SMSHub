package com.ar.smshub

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.beust.klaxon.Klaxon
import java.util.*
import khttp.responses.Response

class SMS(val message: String, val number: String, val messageId: String)

class SendTask constructor(_settings: SettingsManager, _context: Context) : TimerTask() {
    var settings = _settings
    var mainActivity: MainActivity = _context as MainActivity

    override fun run() {
        lateinit var apiResponse : Response
        try {
            apiResponse = khttp.post(
                url = settings.sendURL,
                data = mapOf(
                    "deviceId" to settings.deviceId,
                    "action" to "SEND"
                )
            )
        } catch (e: Exception) {
            Log.d("-->", "Cannot connect to URL")
            return
        }
        var sms: SMS? = SMS("", "", "")
        var canSend: Boolean = false
        try {
            sms = Klaxon().parse<SMS>(apiResponse.text)
            canSend = true
        } catch (e: com.beust.klaxon.KlaxonException) {
            if (apiResponse.text == "") {
                mainActivity.runOnUiThread(Runnable {
                    mainActivity.logMain(".", false)
                })
                Log.d("-->", "Nothing")
            } else {
                mainActivity.runOnUiThread(Runnable {
                    mainActivity.logMain("Error parsing response from server: " + apiResponse.text)
                })
                Log.d("error", "Error while parsing SMS" + apiResponse.text)
            }
        } finally {
            // optional finally block
        }
        if (canSend) {
            val sentIn = Intent(mainActivity.SENT_SMS_FLAG)
            settings.updateSettings()
            sentIn.putExtra("messageId", sms!!.messageId)
            sentIn.putExtra("statusURL", settings.statusURL)
            sentIn.putExtra("deviceId", settings.deviceId)
            sentIn.putExtra("delivered", 0)


            val sentPIn = PendingIntent.getBroadcast(mainActivity, mainActivity.nextRequestCode(), sentIn,0)

            val deliverIn = Intent(mainActivity.DELIVER_SMS_FLAG)
            deliverIn.putExtra("messageId", sms!!.messageId)
            deliverIn.putExtra("statusURL", settings.statusURL)
            deliverIn.putExtra("deviceId", settings.deviceId)
            deliverIn.putExtra("delivered", 1)


            val deliverPIn = PendingIntent.getBroadcast(mainActivity, mainActivity.nextRequestCode(), deliverIn, 0)

            val smsManager = SmsManager.getDefault() as SmsManager
            smsManager.sendTextMessage(sms!!.number, null, sms!!.message, sentPIn, deliverPIn)
            mainActivity.runOnUiThread(Runnable {
                mainActivity.logMain("Sent to: " + sms!!.number + " - id: " + sms!!.messageId + " - message: " + sms!!.message)
            })
            Log.d("-->", "Sent!")

        }


    }

}
