package com.qomg.webappcatalogs.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.speech.tts.TextToSpeech
import java.util.*

class SpeechWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private lateinit var tts: TextToSpeech

    override fun doWork(): Result {
        tts = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.CHINA
                tts.speak(inputData.getString("text") ?: "", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}