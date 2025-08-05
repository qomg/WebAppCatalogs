package com.qomg.webappcatalogs.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mmap_entries")
data class MmapEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val processId: Int,
    val startAddress: String,
    val endAddress: String,
    val size: Long,
    val protection: String,
    val flags: String,
    val offset: Long,
    val device: String,
    val inode: Long,
    val pathname: String?,
    val threadName: String?,
    val isAnonymous: Boolean
)