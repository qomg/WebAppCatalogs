package com.qomg.webappcatalogs.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MmapDao {
    @Insert
    suspend fun insertAll(entries: List<MmapEntry>)

    @Query("SELECT * FROM mmap_entries WHERE processId = :pid ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentEntries(pid: Int, limit: Int): List<MmapEntry>

    @Query("""
        DELETE FROM mmap_entries 
        WHERE processId = :pid 
        AND id NOT IN (
            SELECT id FROM mmap_entries 
            WHERE processId = :pid 
            ORDER BY timestamp DESC 
            LIMIT :keepLast
        )
    """)
    suspend fun cleanOldEntries(keepLast: Int, pid: Int)
}