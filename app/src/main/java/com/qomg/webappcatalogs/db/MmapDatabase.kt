package com.qomg.webappcatalogs.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MmapEntry::class], version = 1)
abstract class MmapDatabase : RoomDatabase() {
    abstract fun mmapDao(): MmapDao

    companion object {
        @Volatile
        private var INSTANCE: MmapDatabase? = null

        fun initialize(context: Context): MmapDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MmapDatabase::class.java,
                    "mmap_database.db"
                )
                    .fallbackToDestructiveMigration(false)
                    //.setMaxSize(2 * 1024 * 1024) // 2MB
                    .build()
                    .also { INSTANCE = it }
            }
        }

        fun getInstance(): MmapDatabase {
            return INSTANCE ?: throw IllegalStateException("MmapDatabaseLogger not initialized")
        }
    }
}