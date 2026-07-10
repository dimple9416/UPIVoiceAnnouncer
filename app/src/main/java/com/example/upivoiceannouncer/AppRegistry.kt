package com.example.upivoiceannouncer

import android.content.Context

object AppRegistry {

    private var ttsManager: TTSManager? = null
    private var prefsManager: PrefsManager? = null

    fun initializeTTS(context: Context) {
        if (ttsManager == null) {
            ttsManager = TTSManager(context)
            prefsManager = PrefsManager(context)
        }
    }

    fun getTTSManager(): TTSManager {
        return ttsManager ?: throw IllegalStateException("TTSManager not initialized. Call initializeTTS() first.")
    }

    fun getPrefsManager(): PrefsManager {
        return prefsManager ?: throw IllegalStateException("PrefsManager not initialized. Call initializeTTS() first.")
    }
}
