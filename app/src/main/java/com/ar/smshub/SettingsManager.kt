package com.ar.smshub

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.ar.smshub.R

class SettingsManager {

    var isSendEnabled: Boolean = false
    var interval: Int = 3
    var URL: String = ""
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
        val defaultURL = context.resources.getString(R.string.preference_default_url)
        val defaultDeviceId = context.resources.getString(R.string.preference_default_device_id)

        isSendEnabled = sharedPref!!.getBoolean(context.getString(R.string.preference_send_enabled), defaultSendEnabled)
        interval = sharedPref!!.getInt(context.getString(R.string.preference_interval), defaultInterval)
        URL = sharedPref!!.getString(context.getString(R.string.preference_url), defaultURL)
        deviceId = sharedPref!!.getString(context.getString(R.string.preference_device_id), defaultDeviceId)

    }

    fun setIsSendEnabled(_isSendEnabled: Boolean){
        with(sharedPref!!.edit()) {
            putBoolean(context.getString(R.string.preference_send_enabled), _isSendEnabled)
            Toast.makeText(context, "Settings updated", Toast.LENGTH_SHORT).show()
            commit()
        }
    }

    fun setSettings(_interval: Int, _URL: String, _deviceId: String){
        interval = _interval
        URL = _URL
        deviceId = _deviceId
        with(sharedPref!!.edit()) {
            putString(context.getString(R.string.preference_url), _URL)
            putString(context.getString(R.string.preference_device_id), _deviceId)
            putInt(context.getString(R.string.preference_interval), _interval)
            Toast.makeText(context, "Settings updated", Toast.LENGTH_SHORT).show()
            commit()
        }
    }

}