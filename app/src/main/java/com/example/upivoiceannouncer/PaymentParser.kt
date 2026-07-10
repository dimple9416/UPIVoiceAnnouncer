package com.example.upivoiceannouncer

class PaymentParser {

    companion object {
        private val UPI_APPS = mapOf(
            "com.google.android.apps.nbu.paisa.user" to "Google Pay",
            "com.phonepe.app" to "PhonePe",
            "net.one97.paytm" to "Paytm",
            "org.npci.upiapp" to "BHIM",
            "in.amazon.mShop.android.shopping" to "Amazon Pay"
        )

        private val FAILED_KEYWORDS = listOf("failed", "declined", "unsuccessful")
        private val PENDING_KEYWORDS = listOf("pending", "processing")
        private val CANCELLED_KEYWORDS = listOf("cancelled", "canceled")
    }

    fun parseNotification(
        packageName: String,
        title: String?,
        text: String?
    ): PaymentInfo? {
        // Check if it's a UPI app
        val upiApp = UPI_APPS[packageName] ?: return null

        // Check for failed/pending/cancelled payments
        val fullText = "$title $text".lowercase()
        if (isFailedPayment(fullText)) return null

        // Extract amount
        val amount = extractAmount(fullText) ?: return null

        // Extract sender name
        val senderName = extractSenderName(fullText) ?: "Unknown"

        // Convert amount to words
        val amountInWords = NumberToWords.convert(amount)

        return PaymentInfo(
            amount = amount,
            amountInWords = amountInWords,
            upiApp = upiApp,
            senderName = senderName
        )
    }

    private fun isFailedPayment(text: String): Boolean {
        val lowerText = text.lowercase()
        return FAILED_KEYWORDS.any { lowerText.contains(it) } ||
               PENDING_KEYWORDS.any { lowerText.contains(it) } ||
               CANCELLED_KEYWORDS.any { lowerText.contains(it) }
    }

    private fun extractAmount(text: String): Long? {
        // Pattern: ₹amount or Rs.amount
        val pattern = Regex("[₹Rs.]\\s*(\\d+(?:,\\d{3})*(?:\\.\\d{2})?)").find(text)
        return pattern?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull()?.toLong()
    }

    private fun extractSenderName(text: String): String? {
        // Common patterns: "from XYZ", "XYZ sent"
        val patterns = listOf(
            Regex("from\\s+([A-Za-z\\s]+?)(?:\\.|via|$)"),
            Regex("([A-Za-z\\s]+)\\s+sent"),
            Regex("([A-Za-z\\s]+)\\s+paid")
        )

        for (pattern in patterns) {
            val match = pattern.find(text.lowercase())
            if (match != null) {
                return match.groupValues[1].trim()
            }
        }
        return null
    }
}