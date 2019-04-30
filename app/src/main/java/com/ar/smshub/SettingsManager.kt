package com.ar.smshub

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.ar.smshub.R

class SettingsManager {

    var isSendEnabled: Boolean = false
    var interval: Int = 1
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
        this.updateSettings()
    }

    fun updateSettings(){

        val defaultSendEnabled = context.resources.getBoolean(R.bool.preference_default_send_enabled)
        val defaultInterval = context.resources.getInteger(R.integer.preference_default_interval)
        val defaultSendURL = context.resources.getString(R.string.preference_default_send_url)
        val defaultReceiveURL = context.resources.getString(R.string.preference_default_receive_url)
        val defaultStatusURL = context.resources.getString(R.string.preference_default_status_url)
        val defaultDeviceId = context.resources.getString(R.string.preference_default_device_id)

        this.isSendEnabled = sharedPref!!.getBoolean(context.getString(R.string.preference_send_enabled), defaultSendEnabled)
        this.interval = sharedPref!!.getInt(context.getString(R.string.preference_interval), defaultInterval)
        this.sendURL = sharedPref!!.getString(context.getString(R.string.preference_send_url), defaultSendURL)
        this.receiveURL = sharedPref!!.getString(context.getString(R.string.preference_receive_url), defaultReceiveURL)
        this.statusURL = sharedPref!!.getString(context.getString(R.string.preference_status_url), defaultStatusURL)
        this.deviceId = sharedPref!!.getString(context.getString(R.string.preference_device_id), defaultDeviceId)
    }

    fun setSettings(_isSendEnabled: Boolean, _interval: Int, _sendURL: String,_receiveURL: String,_statusURL: String, _deviceId: String){
        this.interval = _interval
        this.sendURL = _sendURL
        this.receiveURL = _receiveURL
        this.statusURL = _statusURL
        this.deviceId = _deviceId
        this.isSendEnabled = _isSendEnabled
        with(sharedPref!!.edit()) {
            putBoolean(context.getString(R.string.preference_send_enabled), _isSendEnabled)
            putString(context.getString(R.string.preference_send_url), _sendURL)
            putString(context.getString(R.string.preference_receive_url), _receiveURL)
            putString(context.getString(R.string.preference_status_url), _statusURL)
            putString(context.getString(R.string.preference_device_id), _deviceId)
            putInt(context.getString(R.string.preference_interval), _interval)
            commit()
            Log.d("--->_isSendEnabled", _isSendEnabled.toString())
            Toast.makeText(context, "Settings updated", Toast.LENGTH_SHORT).show()
        }
        this.updateSettings()
    }

}