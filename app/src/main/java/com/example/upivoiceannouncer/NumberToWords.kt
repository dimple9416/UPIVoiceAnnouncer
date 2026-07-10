package com.example.upivoiceannouncer

object NumberToWords {

    private val ones = arrayOf(
        "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    )

    private val teens = arrayOf(
        "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
        "sixteen", "seventeen", "eighteen", "nineteen"
    )

    private val tens = arrayOf(
        "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    )

    private val scales = arrayOf(
        "", "thousand", "lakh", "crore"
    )

    fun convert(number: Long): String {
        if (number == 0L) return "Zero"
        if (number < 0) return "Minus " + convert(-number)

        var num = number
        var groupIndex = 0
        val result = mutableListOf<String>()

        while (num > 0) {
            if (num % 100 != 0L) {
                result.add(0, convertGroup(num % 100) + if (groupIndex > 0) " " + scales[groupIndex] else "")
            }
            num /= 100
            groupIndex++
        }

        return result.joinToString(" ").trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    private fun convertGroup(number: Long): String {
        var num = number
        val result = mutableListOf<String>()

        // Hundreds
        if (num >= 100) {
            result.add(ones[(num / 100).toInt()] + " hundred")
            num %= 100
        }

        // Tens and ones
        when {
            num >= 20 -> {
                result.add(tens[(num / 10).toInt()])
                if (num % 10 != 0L) {
                    result.add(ones[(num % 10).toInt()])
                }
            }
            num >= 10 -> {
                result.add(teens[(num - 10).toInt()])
            }
            num > 0 -> {
                result.add(ones[num.toInt()])
            }
        }

        return result.joinToString(" ").trim()
    }
}