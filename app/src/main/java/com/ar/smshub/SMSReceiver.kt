package com.ar.smshub

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log.*
import android.support.v4.app.NotificationCompat.getExtras
import android.os.Bundle


class SMSReceiver : BroadcastReceiver() {


    private var TAG = "SmsBroadcastReceiver"

    lateinit var serviceProviderNumber: String
    lateinit var serviceProviderSmsCondition: String

    private var listener: Listener? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsSender = ""
            var smsBody = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.displayOriginatingAddress
                    smsBody += smsMessage.messageBody
                }
            } else {
                val smsBundle = intent.extras
                if (smsBundle != null) {
                    val pdus = smsBundle.get("pdus") as Array<Any>
                    if (pdus == null) {
                        // Display some error to the user
                        e(TAG, "SmsBundle had no pdus key")
                        return
                    }
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in messages.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        smsBody += messages[i]!!.getMessageBody()
                    }
                    smsSender = messages[0]!!.getOriginatingAddress()
                }
            }
            e(TAG, smsBody)
            var settingsManager = SettingsManager(context)

            PostReceivedMessage().execute(settingsManager.receiveURL, settingsManager.deviceId, smsBody, smsSender)


            val i = Intent("SMS_RECEIVED")
            // Data you need to pass to activity
            i.putExtra("number", smsSender)
            i.putExtra("message", smsBody)
            context.sendBroadcast(i)

            if (::serviceProviderNumber.isInitialized && smsSender == serviceProviderNumber && smsBody.startsWith(
                    serviceProviderSmsCondition
                )
            ) {
                if (listener != null) {
                    listener!!.onTextReceived(smsBody)
                }
            }
        }
    }


    internal interface Listener {
        fun onTextReceived(text: String)
    }
}