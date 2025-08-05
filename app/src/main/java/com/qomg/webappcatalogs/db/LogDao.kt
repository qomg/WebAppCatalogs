package com.qomg.webappcatalogs.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Insert
    suspend fun insert(logEntry: LogEntry)

    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC LIMIT :limit")
    fun getLogs(limit: Int = 200): Flow<List<LogEntry>>

    @Query("DELETE FROM log_entries WHERE timestamp < :threshold")
    suspend fun cleanOldLogs(threshold: Long = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)
}