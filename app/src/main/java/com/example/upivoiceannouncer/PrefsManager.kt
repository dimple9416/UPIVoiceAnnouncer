package com.example.upivoiceannouncer

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "upi_voice_announcer_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TTS_ENABLED_KEY = "tts_enabled"
        private const val VOLUME_KEY = "volume"
        private const val SPEECH_RATE_KEY = "speech_rate"
        private const val ANNOUNCE_FORMAT_KEY = "announce_format"
    }

    fun isTTSEnabled(): Boolean = prefs.getBoolean(TTS_ENABLED_KEY, true)

    fun setTTSEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(TTS_ENABLED_KEY, enabled).apply()
    }

    fun getVolume(): Int = prefs.getInt(VOLUME_KEY, 80)

    fun setVolume(volume: Int) {
        prefs.edit().putInt(VOLUME_KEY, volume.coerceIn(0, 100)).apply()
    }

    fun getSpeechRate(): Float = prefs.getFloat(SPEECH_RATE_KEY, 1.0f)

    fun setSpeechRate(rate: Float) {
        prefs.edit().putFloat(SPEECH_RATE_KEY, rate.coerceIn(0.5f, 2.0f)).apply()
    }

    fun getAnnounceFormat(): String = prefs.getString(
        ANNOUNCE_FORMAT_KEY,
        "Received {amount} from {name} via {app}"
    ) ?: "Received {amount} from {name} via {app}"

    fun setAnnounceFormat(format: String) {
        prefs.edit().putString(ANNOUNCE_FORMAT_KEY, format).apply()
    }
}