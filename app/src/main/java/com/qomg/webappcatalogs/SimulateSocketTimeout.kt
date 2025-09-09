package com.qomg.webappcatalogs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

fun simulateSocketTimeout() {
    val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS) // 连接超时 1 秒
        .readTimeout(1, TimeUnit.SECONDS)    // 读取超时 1 秒
        .writeTimeout(1, TimeUnit.SECONDS)   // 写入超时 1 秒
        .build()

    // 使用 httpbin.org 的延迟接口，它会延迟 5 秒返回
    val slowUrl = "https://httpbin.org/delay/5"

    val request = Request.Builder()
        .url(slowUrl)
        .build()

    // 注意：在 Android 中不要在主线程执行网络请求！
    Thread {
        try {
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                println("请求失败: ${response.code} - ${response.message}")
            } else {
                println("响应内容: ${response.body?.string()}")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            if (e is java.net.SocketTimeoutException) {
                println("⚠️ 捕获到 SocketTimeoutException: ${e.message}")
            } else {
                println("⚠️ 捕获到其他 IO 异常: ${e.javaClass.simpleName} - ${e.message}")
            }
        }
    }.start()
}

fun simulateTimeoutWithCoroutine() {
    val client = OkHttpClient.Builder()
        .readTimeout(1, TimeUnit.SECONDS)
        .build()

    val request = Request.Builder()
        .url("https://httpbin.org/delay/5")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                println("请求失败: ${response.code}")
            } else {
                println("响应: ${response.body?.string()}")
            }
        } catch (e: IOException) {
            if (e is java.net.SocketTimeoutException) {
                println("⚠️ 协程中捕获 SocketTimeoutException: ${e.message}")
            } else {
                println("⚠️ 捕获其他 IO 异常: ${e.javaClass.simpleName}")
            }
        }
    }
}
