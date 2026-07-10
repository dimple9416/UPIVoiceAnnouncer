package com.example.upivoiceannouncer

import org.junit.Test
import org.junit.Assert.*

class PaymentParserTest {

    private val parser = PaymentParser()

    @Test
    fun testGooglePayParsing() {
        val result = parser.parseNotification(
            "com.google.android.apps.nbu.paisa.user",
            "You received ₹500",
            "From John Doe via Google Pay"
        )

        assertNotNull(result)
        assertEquals(500, result?.amount)
        assertEquals("Google Pay", result?.upiApp)
    }

    @Test
    fun testPhonePeParsing() {
        val result = parser.parseNotification(
            "com.phonepe.app",
            "Money received: ₹1000",
            "From Jane Smith"
        )

        assertNotNull(result)
        assertEquals(1000, result?.amount)
        assertEquals("PhonePe", result?.upiApp)
    }

    @Test
    fun testFailedPaymentFiltering() {
        val result = parser.parseNotification(
            "com.phonepe.app",
            "Payment failed: ₹500",
            "Your payment to XYZ failed"
        )

        assertNull(result)
    }

    @Test
    fun testNumberToWordsConversion() {
        assertEquals("Five hundred", NumberToWords.convert(500))
        assertEquals("One thousand", NumberToWords.convert(1000))
        assertEquals("One lakh", NumberToWords.convert(100000))
        assertEquals("One crore", NumberToWords.convert(10000000))
    }

    @Test
    fun testPaytmParsing() {
        val result = parser.parseNotification(
            "net.one97.paytm",
            "You received ₹250",
            "From Alice Johnson via Paytm"
        )

        assertNotNull(result)
        assertEquals(250, result?.amount)
        assertEquals("Paytm", result?.upiApp)
    }

    @Test
    fun testZeroAmount() {
        val result = NumberToWords.convert(0)
        assertEquals("Zero", result)
    }
}