package com.example.upivoiceannouncer

data class PaymentEventBus(
    val amount: Long,
    val amountInWords: String,
    val upiApp: String,
    val timestamp: Long = System.currentTimeMillis()
)