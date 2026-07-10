package com.example.upivoiceannouncer

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isReady = false
    private val prefsManager = PrefsManager(context)

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
            tts?.language = Locale("en", "IN")
            tts?.setSpeechRate(prefsManager.getSpeechRate())
            Log.d("TTSManager", "TTS Initialized successfully")
        } else {
            Log.e("TTSManager", "TTS Initialization failed with status: $status")
        }
    }

    fun speak(text: String) {
        if (!isReady || tts == null) {
            Log.w("TTSManager", "TTS not ready, cannot speak")
            return
        }

        try {
            tts?.let {
                it.stop()
                it.setSpeechRate(prefsManager.getSpeechRate())
                it.setPitch(1.0f)
                it.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {}
                    override fun onError(utteranceId: String?) {
                        Log.e("TTSManager", "TTS Error for utterance: $utteranceId")
                    }
                })
                it.speak(text, TextToSpeech.QUEUE_FLUSH, null, "announcement")
            }
        } catch (e: Exception) {
            Log.e("TTSManager", "Error speaking text: ${e.message}")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }
}