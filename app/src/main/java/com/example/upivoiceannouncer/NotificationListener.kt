package com.example.upivoiceannouncer

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.greenrobot.eventbus.EventBus

class NotificationListener : NotificationListenerService() {

    private lateinit var duplicateGuard: DuplicateGuard
    private lateinit var paymentParser: PaymentParser
    private lateinit var ttsManager: TTSManager

    override fun onListenerConnected() {
        super.onListenerConnected()
        duplicateGuard = DuplicateGuard(this)
        paymentParser = PaymentParser()
        ttsManager = AppRegistry.getTTSManager()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if (sbn == null) return

        // Get notification details
        val notification = sbn.notification
        val bundle = notification.contentText
        val title = notification.contentTitle
        val packageName = sbn.packageName

        // Check if it's a payment notification
        val paymentInfo = paymentParser.parseNotification(packageName, title, bundle)

        if (paymentInfo != null) {
            // Check for duplicates
            val isDuplicate = duplicateGuard.isDuplicate(paymentInfo)

            if (!isDuplicate) {
                // Record this notification
                duplicateGuard.recordPayment(paymentInfo)

                // Announce payment
                announcePayment(paymentInfo)

                // Send event via EventBus
                val event = PaymentEventBus(
                    amount = paymentInfo.amount,
                    amountInWords = paymentInfo.amountInWords,
                    upiApp = paymentInfo.upiApp,
                    timestamp = System.currentTimeMillis()
                )
                EventBus.getDefault().post(event)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    private fun announcePayment(paymentInfo: PaymentInfo) {
        val announcement = "Received ${paymentInfo.amountInWords} rupees from ${paymentInfo.senderName} via ${paymentInfo.upiApp}"
        ttsManager.speak(announcement)
    }
}

data class PaymentInfo(
    val amount: Long,
    val amountInWords: String,
    val upiApp: String,
    val senderName: String,
    val timestamp: Long = System.currentTimeMillis()
)