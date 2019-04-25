package com.ar.smshub

import android.os.AsyncTask

class PostReceivedMessage : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String {
        var receiveURL = params[0]
        var deviceId = params[1]
        var smsBody = params[2]
        var smsSender = params[3]
        khttp.post(
            url = receiveURL,
            data = mapOf("deviceId" to deviceId, "message" to smsBody, "number" to smsSender, "action" to "RECEIVED")
        )
        return "great!"
    }

    override fun onPostExecute(result: String) {

    }

    override fun onPreExecute() {}

    override fun onProgressUpdate(vararg values: Void) {}
}