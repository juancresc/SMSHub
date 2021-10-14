package com.ar.smshub

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import khttp.responses.Response
import org.json.JSONArray

class IncomingSMS(val message: String, val number: String, val receivedAt: String)

class SyncIncomingSMS constructor(_settings: SettingsManager, _context: Context) {
    private var settings = _settings
    private var mainActivity: MainActivity = _context as MainActivity
    private val maxCount = settings.syncMaxCount
    private val lastSyncAt = settings.lastSyncTimestamp

    fun run() {
        lateinit var apiResponse : Response

        // 5 seconds for safety. Makes sure we don't skip any messages
        val timestamp = (System.currentTimeMillis() / 1000).toInt() - 5
        val items = fetch()
        if (items.isEmpty()) { return }

        val messagesJson = items.map {
            mapOf(
                "message" to it.message,
                "number" to it.number,
                "unixTimestamp" to it.receivedAt
            )
        }

        apiResponse = khttp.post(
            url = settings.syncURL,
            data = mapOf(
                "deviceId" to settings.deviceId,
                "action" to "SYNC",
                "messages" to JSONArray(messagesJson)
            )
        )
        if (apiResponse.statusCode in 200..299) {
            settings.updateSyncedAt(timestamp)
        }
    }

    private fun fetch(): List<IncomingSMS> {
        val contentResolver: ContentResolver = mainActivity.contentResolver
        val uri = Uri.parse("content://sms/inbox")
        val projection = arrayOf("_id", "address", "body", "date", "type")
        val cursor = contentResolver.query(
            uri,
            projection,
            "type = 1", // incoming messages only, not sure if necessary since we're querying sms/inbox
            null,
            "date desc"
        )
        val messagesToSync = mutableListOf<IncomingSMS>()
        if(cursor == null) { throw InstantiationException("cursor could not be instantiated") }
        if (cursor.moveToFirst()) {
            do {
                if (messagesToSync.count() > maxCount) { break }
                val address = cursor.getString(cursor.getColumnIndex("address"))
                val body = cursor.getString(cursor.getColumnIndex("body"))
                val date = cursor.getInt(cursor.getColumnIndex("date"))
                if (date < lastSyncAt) { break }
                messagesToSync.add(IncomingSMS(body, address, date.toString()))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return messagesToSync
    }

}