package com.ar.smshub

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.ar.smshub.R

class SettingsManager {

    var isSendEnabled: Boolean = false
    var interval: Int
    var sendURL: String = ""
    var statusURL: String = ""
    var receiveURL: String = ""
    var deviceId: String = ""
    var sharedPref: SharedPreferences
    var context: Context

    constructor(_context: Context) {
        context = _context
        sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        val defaultSendEnabled = context.resources.getBoolean(R.bool.preference_default_send_enabled)
        val defaultInterval = context.resources.getInteger(R.integer.preference_default_interval)
        val defaultSendURL = context.resources.getString(R.string.preference_default_send_url)
        val defaultReceiveURL = context.resources.getString(R.string.preference_default_receive_url)
        val defaultStatusURL = context.resources.getString(R.string.preference_default_status_url)
        val defaultDeviceId = context.resources.getString(R.string.preference_default_device_id)

        isSendEnabled = sharedPref!!.getBoolean(context.getString(R.string.preference_send_enabled), defaultSendEnabled)
        interval = sharedPref!!.getInt(context.getString(R.string.preference_interval), defaultInterval)
        sendURL = sharedPref!!.getString(context.getString(R.string.preference_send_url), defaultSendURL)
        receiveURL = sharedPref!!.getString(context.getString(R.string.preference_receive_url), defaultReceiveURL)
        statusURL = sharedPref!!.getString(context.getString(R.string.preference_status_url), defaultStatusURL)
        deviceId = sharedPref!!.getString(context.getString(R.string.preference_device_id), defaultDeviceId)

    }

    fun setSettings(_isSendEnabled: Boolean, _interval: Int, _sendURL: String,_receiveURL: String,_statusURL: String, _deviceId: String){
        interval = _interval
        sendURL = _sendURL
        receiveURL = _receiveURL
        statusURL = _statusURL
        deviceId = _deviceId
        isSendEnabled = _isSendEnabled
        with(sharedPref!!.edit()) {
            putBoolean(context.getString(R.string.preference_send_enabled), _isSendEnabled)
            putString(context.getString(R.string.preference_send_url), sendURL)
            putString(context.getString(R.string.preference_receive_url), receiveURL)
            putString(context.getString(R.string.preference_status_url), statusURL)
            putString(context.getString(R.string.preference_device_id), _deviceId)
            putInt(context.getString(R.string.preference_interval), _interval)
            commit()
            Toast.makeText(context, "Settings updated", Toast.LENGTH_SHORT).show()
        }
    }

}