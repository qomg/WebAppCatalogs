package com.qomg.webappcatalogs

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qomg.webappcatalogs.api.AppLogger
import com.qomg.webappcatalogs.db.LogDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.qomg.webappcatalogs", appContext.packageName)
    }

    @Test
    fun testLogInsertion() = runTest {
        val testMessage = "Test log message"
        AppLogger.d("TestTag", testMessage)

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val logDao = LogDatabase.getInstance(appContext).logDao()
        val logs = logDao.getLogs(1).first()
        assertTrue(logs.any { it.message == testMessage })
    }
}