package com.ar.smshub

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.ar.smshub.R

class SettingsManager {

    var isSendEnabled: Boolean = false
    var interval: Int = 1
    var lastSyncTimestamp = 1
    var syncMaxCount = 100
    var syncURL: String = ""
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
        val defaultSyncURL = context.resources.getString(R.string.preference_default_sync_url)
        val defaultDeviceId = context.resources.getString(R.string.preference_default_device_id)
        val defaultLastSync = context.resources.getInteger(R.integer.preference_default_last_sync_timestamp)
        val defaultSyncMaxCount = context.resources.getInteger(R.integer.preference_default_sync_max_count)

        this.isSendEnabled = sharedPref!!.getBoolean(context.getString(R.string.preference_send_enabled), defaultSendEnabled)
        this.interval = sharedPref!!.getInt(context.getString(R.string.preference_interval), defaultInterval)
        this.sendURL = sharedPref!!.getString(context.getString(R.string.preference_send_url), defaultSendURL)
        this.receiveURL = sharedPref!!.getString(context.getString(R.string.preference_receive_url), defaultReceiveURL)
        this.statusURL = sharedPref!!.getString(context.getString(R.string.preference_status_url), defaultStatusURL)
        this.syncURL = sharedPref!!.getString(context.getString(R.string.preference_sync_url), defaultSyncURL)
        this.deviceId = sharedPref!!.getString(context.getString(R.string.preference_device_id), defaultDeviceId)
        this.lastSyncTimestamp = sharedPref!!.getInt(context.getString(R.string.preference_last_sync_timestamp), defaultLastSync)
        this.syncMaxCount = sharedPref!!.getInt(context.getString(R.string.preference_sync_max_count), defaultSyncMaxCount)
    }

    fun updateSyncedAt(newValue: Int) {
        with(sharedPref!!.edit()) {
            putInt(context.getString(R.string.preference_last_sync_timestamp), newValue)
            commit()
        }
        this.updateSettings()
    }

    fun setSettings(_isSendEnabled: Boolean, _interval: Int, _sendURL: String,_receiveURL: String,_statusURL: String, _syncURL: String, _deviceId: String, _lastSyncTimestamp: Int, _syncMaxCount: Int){
        this.interval = _interval
        this.sendURL = _sendURL
        this.receiveURL = _receiveURL
        this.statusURL = _statusURL
        this.syncURL = _syncURL
        this.deviceId = _deviceId
        this.isSendEnabled = _isSendEnabled
        this.lastSyncTimestamp = _lastSyncTimestamp
        this.syncMaxCount = _syncMaxCount
        with(sharedPref!!.edit()) {
            putBoolean(context.getString(R.string.preference_send_enabled), _isSendEnabled)
            putString(context.getString(R.string.preference_send_url), _sendURL)
            putString(context.getString(R.string.preference_receive_url), _receiveURL)
            putString(context.getString(R.string.preference_status_url), _statusURL)
            putString(context.getString(R.string.preference_sync_url), _syncURL)
            putString(context.getString(R.string.preference_device_id), _deviceId)
            putInt(context.getString(R.string.preference_interval), _interval)
            putInt(context.getString(R.string.preference_last_sync_timestamp), _lastSyncTimestamp)
            putInt(context.getString(R.string.preference_sync_max_count), _syncMaxCount)
            commit()
            Log.d("--->_isSendEnabled", _isSendEnabled.toString())
            Toast.makeText(context, "Settings updated", Toast.LENGTH_SHORT).show()
        }
        this.updateSettings()
    }

}