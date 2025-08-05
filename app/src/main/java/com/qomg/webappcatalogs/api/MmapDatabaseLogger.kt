package com.qomg.webappcatalogs.api

import android.util.Log
import com.qomg.webappcatalogs.db.MmapDatabase
import com.qomg.webappcatalogs.db.MmapEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileReader
import kotlin.getValue

object MmapDatabaseLogger {
    private val database: MmapDatabase by lazy {
        MmapDatabase.getInstance()
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun logCurrentMmap() {
        scope.launch {
            try {
                val entries = parseMapsFile()
                database.mmapDao().insertAll(entries)

                // Keep only last 10 snapshots
                database.mmapDao().cleanOldEntries(
                    keepLast = 10,
                    pid = android.os.Process.myPid()
                )
            } catch (e: Exception) {
                Log.e("MmapLogger", "Failed to log mmap", e)
            }
        }
    }

    suspend fun getRecentMmap(pid: Int, limit: Int = 100): List<MmapEntry> {
        return database.mmapDao().getRecentEntries(pid, limit)
    }


    fun parseMapsFile(): List<MmapEntry> {
        val mmapEntries = mutableListOf<MmapEntry>()
        try {
            BufferedReader(FileReader("/proc/self/maps")).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.let { entry ->
                        val parts = entry.split("\\s+".toRegex())
                        if (parts.size >= 5) {
                            val (address, perms, offset, dev, inodeStr) = parts.take(5)
                            val pathname = parts.getOrNull(5)

                            val (start, end) = address.split("-").let {
                                it[0] to it.getOrElse(1) { "0" }
                            }

                            mmapEntries.add(
                                MmapEntry(
                                    processId = android.os.Process.myPid(),
                                    startAddress = start,
                                    endAddress = end,
                                    size = end.hexToLong() - start.hexToLong(),
                                    protection = perms,
                                    flags = parts.getOrNull(2) ?: "",
                                    offset = offset.hexToLong(),
                                    device = dev,
                                    inode = inodeStr.toLongOrNull() ?: 0,
                                    pathname = pathname,
                                    threadName = Thread.currentThread().name,
                                    isAnonymous = pathname.isNullOrEmpty()
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MmapLogger", "Error reading /proc/self/maps", e)
        }
        return mmapEntries
    }

    fun String.hexToLong() = java.lang.Long.parseLong(this, 16)
}