package com.qomg.webappcatalogs.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qomg.webappcatalogs.BuildConfig
import java.util.concurrent.Executors

@Database(entities = [LogEntry::class], version = 1)
abstract class LogDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {
        @Volatile
        private var INSTANCE: LogDatabase? = null

        fun getInstance(context: Context): LogDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LogDatabase::class.java,
                    "log_database.db"
                )
                    .fallbackToDestructiveMigration(false)
                    // Add to LogDatabase companion object
                    .setQueryCallback({ sqlQuery, bindArgs ->
                        if (BuildConfig.DEBUG) {
                            Log.d("RoomQuery", "SQL: $sqlQuery\nArgs: ${bindArgs.joinToString()}")
                        }
                    }, Executors.newSingleThreadExecutor())
                    //.setMaxSize(2 * 1024 * 1024) // 2MB
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}