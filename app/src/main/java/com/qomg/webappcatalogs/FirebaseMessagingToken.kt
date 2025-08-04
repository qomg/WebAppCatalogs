package com.qomg.webappcatalogs

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

private const val TAG = "Firebase"

fun onTokenComplete(context: Context) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

        // Log and toast
        val msg = context.getString(R.string.msg_token_fmt, token)
        Log.d(TAG, msg)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    })
}