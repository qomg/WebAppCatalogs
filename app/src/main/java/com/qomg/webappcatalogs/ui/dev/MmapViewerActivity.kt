package com.qomg.webappcatalogs.ui.dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qomg.webappcatalogs.api.MmapDatabaseLogger
import com.qomg.webappcatalogs.ui.theme.WebAppCatalogsTheme
import kotlinx.coroutines.flow.flow

class MmapViewerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebAppCatalogsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MmapFile()
                    }
                }
            }
        }
    }
}

@Composable
fun MmapFile() {
    val list by flow {
        emit(MmapDatabaseLogger.getRecentMmap(android.os.Process.myPid()))
    }.collectAsStateWithLifecycle(emptyList())
    LazyColumn {
        items(list, key = { it.id }) {
            Text(it.toString())
        }
    }
}