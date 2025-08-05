package com.qomg.webappcatalogs.api

import android.content.Context
import android.util.Log
import com.qomg.webappcatalogs.db.LogDatabase
import com.qomg.webappcatalogs.db.LogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DatabaseLogger private constructor(
    context: Context,
    private val minPriority: Int = Log.DEBUG
) {
    private val logDao by lazy { LogDatabase.getInstance(context).logDao() }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        @Volatile
        private var INSTANCE: DatabaseLogger? = null

        fun initialize(context: Context, minPriority: Int = Log.DEBUG) {
            INSTANCE = DatabaseLogger(context, minPriority)
        }

        fun getInstance(): DatabaseLogger {
            return INSTANCE ?: throw IllegalStateException("DatabaseLogger not initialized")
        }
    }

    fun log(priority: Int, tag: String, message: String, throwable: Throwable? = null) {
        if (priority < minPriority) return

        scope.launch {
            try {
                logDao.insert(
                    LogEntry(
                        priority = priority,
                        tag = tag,
                        message = message,
                        throwable = throwable?.stackTraceToString()
                    )
                )
            } catch (e: Exception) {
                // Fallback to system log if database fails
                Log.e("DatabaseLogger", "Failed to save log to database", e)
            }
        }
    }

    fun cleanup() {
        scope.launch {
            logDao.cleanOldLogs()
        }
    }
}