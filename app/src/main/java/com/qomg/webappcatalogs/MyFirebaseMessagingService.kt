package com.qomg.webappcatalogs

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Locale

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")
        val data = remoteMessage.data
        // Check if message contains a data payload.
        if (data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: $data")

            // Check if data needs to be processed by long running job
            if (needsToBeScheduled()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob(data)
            } else {
                // Handle message within 10 seconds
                handleNow(data)
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    private fun needsToBeScheduled() = true

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    private fun scheduleJob(data: Map<String, String>) {
        // [START dispatch_job]
        val pairs = data.entries.map { (k,v) ->
            k to v
        }.toTypedArray()
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(workDataOf(*pairs))
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
        // [END dispatch_job]
    }

    private fun handleNow(data: Map<String, String>) {
        Log.d(TAG, "Short lived task is done.")
        val body = data["body"] ?: return
        sendNotification(applicationContext, body)
        performTextToSpeech(applicationContext, body)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        var tts: TextToSpeech? = null

        /**
         * 触发语音播报
         */
        private fun performTextToSpeech(context: Context, text: String) {
            // 实现语音播报逻辑，可以使用 TextToSpeech 或第三方 SDK
            // 示例：使用 Android 的 TextToSpeech
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.CHINA
                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            val body = inputData.getString("body")
            if (!body.isNullOrEmpty()) {
                sendNotification(applicationContext, body)
                performTextToSpeech(applicationContext, body)
            }
            return Result.success()
        }
    }
}