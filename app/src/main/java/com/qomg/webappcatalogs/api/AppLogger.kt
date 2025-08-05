package com.qomg.webappcatalogs.api

import android.content.Context
import android.util.Log

object AppLogger {
    private const val DEFAULT_TAG = "AppLogger"
    private var isInitialized = false

    fun initialize(context: Context, minPriority: Int = Log.DEBUG) {
        DatabaseLogger.initialize(context, minPriority)
        isInitialized = true
    }

    fun v(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        Log.v(tag, message, throwable)
        if (isInitialized) DatabaseLogger.getInstance().log(Log.VERBOSE, tag, message, throwable)
    }

    fun d(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        Log.d(tag, message, throwable)
        if (isInitialized) DatabaseLogger.getInstance().log(Log.DEBUG, tag, message, throwable)
    }

    fun i(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        Log.i(tag, message, throwable)
        if (isInitialized) DatabaseLogger.getInstance().log(Log.INFO, tag, message, throwable)
    }

    fun w(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        Log.w(tag, message, throwable)
        if (isInitialized) DatabaseLogger.getInstance().log(Log.WARN, tag, message, throwable)
    }

    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
        if (isInitialized) DatabaseLogger.getInstance().log(Log.ERROR, tag, message, throwable)
    }


}