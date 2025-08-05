package com.qomg.webappcatalogs.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_entries")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val priority: Int, // Log.VERBOSE, Log.DEBUG, etc.
    val tag: String,
    val message: String,
    val throwable: String? = null
)