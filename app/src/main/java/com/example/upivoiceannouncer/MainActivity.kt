package com.example.upivoiceannouncer

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var enableButton: Button
    private lateinit var testTtsButton: Button
    private lateinit var eventLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.status_text)
        enableButton = findViewById(R.id.enable_button)
        testTtsButton = findViewById(R.id.test_tts_button)
        eventLog = findViewById(R.id.event_log)

        // Initialize TTS Manager
        AppRegistry.initializeTTS(this)

        // Set up button listeners
        enableButton.setOnClickListener {
            openNotificationSettings()
        }

        testTtsButton.setOnClickListener {
            testTTS()
        }

        // Update status
        updateStatus()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        updateStatus()
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaymentEvent(event: PaymentEventBus) {
        updateEventLog(event)
    }

    private fun updateStatus() {
        val isEnabled = isNotificationListenerEnabled()
        statusTextView.text = if (isEnabled) {
            "✓ Notification Listener Active"
        } else {
            "✗ Notification Listener Inactive"
        }

        statusTextView.setTextColor(
            ContextCompat.getColor(
                this,
                if (isEnabled) R.color.success_green else R.color.error_red
            )
        )

        enableButton.isEnabled = !isEnabled
    }

    private fun isNotificationListenerEnabled(): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.isNotificationListenerAccessGranted(ComponentName(this, NotificationListener::class.java))
        } else {
            val enabledServices = Settings.Secure.getString(
                contentResolver,
                "enabled_notification_listeners"
            ) ?: ""
            enabledServices.contains(ComponentName(this, NotificationListener::class.java).flattenToString())
        }
    }

    private fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }

    private fun testTTS() {
        val ttsManager = AppRegistry.getTTSManager()
        ttsManager.speak(
            "Test announcement. Received five hundred rupees from John Doe via Google Pay."
        )
    }

    private fun updateEventLog(event: PaymentEventBus) {
        val logText = "${event.upiApp}: ₹${event.amount} - ${event.amountInWords}\n"
        eventLog.text = (logText + (eventLog.text ?: "")).take(500)
    }
}