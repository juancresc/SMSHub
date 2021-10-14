package com.ar.smshub

import android.Manifest
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.support.v4.app.NotificationCompat.getExtras
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.telephony.SmsManager
import android.util.Log


class MainActivity : AppCompatActivity() {
    var request_code = 0
    var MY_PERMISSIONS_REQUEST_SMS_ALL = 1
    val SENT_SMS_FLAG = "SMS_SENT"
    val RECEIVED_SMS_FLAG = "SMS_RECEIVED"
    val DELIVER_SMS_FLAG = "DELIVER_SMS"

    protected lateinit var settingsManager: SettingsManager
    lateinit var timerSend: Timer
    var sendIntent = SMSSendIntent()
    var deliverIntent = SMSSendIntent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        settingsManager = SettingsManager(this)
        var mainFragment = MainFragment()
        mainFragment.arguments = intent.extras
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_view, mainFragment, "MAIN")
        transaction.commit()
        fragmentManager.executePendingTransactions()
        //initialize timer for the first time
        updateTimer()
        requestSMSPermission()

        // Inside OnCreate Method
        try {
        registerReceiver(broadcastReceiver, IntentFilter(RECEIVED_SMS_FLAG))
        registerReceiver(sendIntent, IntentFilter(SENT_SMS_FLAG))
        registerReceiver(deliverIntent, IntentFilter(DELIVER_SMS_FLAG))
        } catch (e: IllegalArgumentException) {
            Log.d("-->", "Already subscribed")
        }
    }

    override fun onStop() {
        /*
        try {
            unregisterReceiver(sendIntent)
            unregisterReceiver(deliverIntent)
            unregisterReceiver(broadcastReceiver)
        } catch (e: IllegalArgumentException) {
            Log.d("-->", "No receivers")
        }*/
        super.onStop()

    }


    fun nextRequestCode(): Int {
        return ++this.request_code
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.flags
            val b = intent.extras
            val number = b!!.getString("number")
            val message = b!!.getString("message")
            logMain("Message received and posted from: $number")
        }
    }


    fun logMain(message: String, newline: Boolean = true) {
        val mainFragment: MainFragment
        try {
            mainFragment = fragmentManager.findFragmentByTag("MAIN") as MainFragment
        } catch (e: kotlin.TypeCastException) {
            return
        }
        if (newline) {
            mainFragment.textMainLog.setText(mainFragment.textMainLog.text.toString() + "\n" + message)
        } else {
            mainFragment.textMainLog.setText(mainFragment.textMainLog.text.toString() + message)
        }
        var scrollAmount =
            mainFragment.textMainLog.getLayout().getLineTop(mainFragment.textMainLog.getLineCount()) - mainFragment.textMainLog.getHeight()
        // if there is no need to scroll, scrollAmount will be <=0
        if (scrollAmount > 0) {
            mainFragment.textMainLog.scrollTo(0, scrollAmount)
        } else {
            mainFragment.textMainLog.scrollTo(0, 0)
        }
    }

    fun updateTimer() {
        settingsManager.updateSettings()
        Log.d("---->", "Update timer")
        Log.d("--->setM.isSend", settingsManager.isSendEnabled.toString())
        if (settingsManager.isSendEnabled) {
            startTimer()
        } else {
            cancelTimer()
        }
    }

    fun cancelTimer() {
        Log.d("---->", "Cancel timer")
        if (::timerSend.isInitialized) {
            timerSend.cancel()
        }
        timerSend = Timer("SendSMS", true)
    }

    fun startTimer() {
        Log.d("---->", "Start timer")
        if (::timerSend.isInitialized) {
            timerSend.cancel()
        }
        timerSend = Timer("SendSMS", true)
        if (settingsManager.isSendEnabled) {
            val seconds = settingsManager.interval * 60
            val interval: Long
            if (BuildConfig.DEBUG) {
                interval = (seconds * 400).toLong()
            } else {
                interval = (seconds * 1000).toLong()
            }
            //this does not work
            //logMain("Timer started at " + minutes.toString())
            Log.d("---->", "Timer started at " + interval.toString())
            timerSend.schedule(SendTask(settingsManager, this), interval, interval)
        }
    }

    private fun requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Luigi", "Permission has been granted!")
        } else {
            Log.d("Luigi", "No permission")
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS),
            MY_PERMISSIONS_REQUEST_SMS_ALL
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if(grantResults.isNotEmpty()) {
            val str = if (grantResults[0] == PackageManager.PERMISSION_GRANTED) "GRANTED" else "DENIED"
            Log.d("SMSHub(PermResult)", "Code: $requestCode, permission $str")
        } else {
            Log.d("SMSHub(PermResult)", "Code: $requestCode grantResults is empty")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> {
                var settingsFragment = fragmentManager.findFragmentByTag("SETTINGS") as? SettingsFragment
                if (settingsFragment == null) {
                    settingsFragment = SettingsFragment()
                }
                val transaction = fragmentManager.beginTransaction()
                transaction.addToBackStack("MAIN")
                transaction.replace(R.id.main_view, settingsFragment, "SETTINGS")
                transaction.commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}
