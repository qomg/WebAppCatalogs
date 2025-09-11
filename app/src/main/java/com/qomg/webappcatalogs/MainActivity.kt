package com.qomg.webappcatalogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.CommonStatusCodes.SUCCESS
import com.qomg.webappcatalogs.api.AppLogger
import com.qomg.webappcatalogs.network.NetworkUsage
import com.qomg.webappcatalogs.ui.theme.WebAppCatalogsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebAppCatalogsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        simulateSocketTimeout()
        simulateTimeoutWithCoroutine()
        NetworkUsage.updateNetworkStatus(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onTokenComplete(this)
        AppLogger.d("MyActivity", "Activity created")

        try {
            // Some operation
        } catch (e: Exception) {
            AppLogger.e("MyActivity", "Operation failed", e)
        }
    }

    override fun onStart() {
        super.onStart()
        val gpa = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (gpa == SUCCESS) return
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebAppCatalogsTheme {
        Greeting("Android")
    }
}