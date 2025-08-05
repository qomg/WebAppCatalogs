package com.qomg.webappcatalogs

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.qomg.webappcatalogs.api.AppLogger
import com.qomg.webappcatalogs.api.MmapDatabaseLogger
import com.qomg.webappcatalogs.db.MmapDatabase
import com.qomg.webappcatalogs.BuildConfig

class MyApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        AppLogger.initialize(this, if (BuildConfig.DEBUG) Log.VERBOSE else Log.INFO)

        MmapDatabase.initialize(this)

        // Log initial mmap state
        MmapDatabaseLogger.logCurrentMmap()
    }
}