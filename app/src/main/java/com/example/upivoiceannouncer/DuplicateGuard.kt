package com.example.upivoiceannouncer

import android.content.Context
import android.content.SharedPreferences

class DuplicateGuard(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "duplicate_guard",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val DUPLICATE_WINDOW = 5000L // 5 seconds
        private const val CLEANUP_INTERVAL = 60000L // 1 minute
        private const val LAST_CLEANUP_KEY = "last_cleanup"
    }

    fun isDuplicate(paymentInfo: PaymentInfo): Boolean {
        cleanupOldEntries()

        val key = generateKey(paymentInfo)
        val lastTime = prefs.getLong(key, 0L)
        val currentTime = System.currentTimeMillis()

        return (currentTime - lastTime) < DUPLICATE_WINDOW
    }

    fun recordPayment(paymentInfo: PaymentInfo) {
        val key = generateKey(paymentInfo)
        prefs.edit().putLong(key, System.currentTimeMillis()).apply()
    }

    private fun generateKey(paymentInfo: PaymentInfo): String {
        return "payment_${paymentInfo.upiApp}_${paymentInfo.amount}_${paymentInfo.senderName}"
    }

    private fun cleanupOldEntries() {
        val lastCleanup = prefs.getLong(LAST_CLEANUP_KEY, 0L)
        val currentTime = System.currentTimeMillis()

        if ((currentTime - lastCleanup) > CLEANUP_INTERVAL) {
            prefs.edit().apply {
                val allEntries = prefs.all
                for ((key, _) in allEntries) {
                    if (key != LAST_CLEANUP_KEY && key.startsWith("payment_")) {
                        val timestamp = prefs.getLong(key, 0L)
                        if ((currentTime - timestamp) > CLEANUP_INTERVAL) {
                            remove(key)
                        }
                    }
                }
                putLong(LAST_CLEANUP_KEY, currentTime)
            }.apply()
        }
    }
}