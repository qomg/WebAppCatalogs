package com.qomg.webappcatalogs.ui.dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qomg.webappcatalogs.db.LogDatabase
import com.qomg.webappcatalogs.ui.theme.WebAppCatalogsTheme

class LogViewerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebAppCatalogsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        LogList()
                    }
                }
            }
        }
    }
}

@Composable
fun LogList() {
    val logs by LogDatabase.getInstance(LocalContext.current)
        .logDao()
        .getLogs(500)
        .collectAsStateWithLifecycle(emptyList())
    LazyColumn {
        items(logs, key = { it.id }) {
            Column {
                Text(it.tag)
                Text(it.message)
                Text(it.throwable ?: "")
            }
        }
    }
}