package com.qomg.webappcatalogs

import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertTrue
import org.junit.Test

import java.util.concurrent.TimeUnit

import org.junit.Assert.fail
import java.io.IOException
import java.net.SocketTimeoutException

class SocketTimeoutTest {

    @Test
    fun testReadTimeout() {
        val server = MockWebServer()
        server.start()

        // 延迟 6 秒返回响应体
        val delayedResponse = MockResponse()
            .setBody("This response is delayed on purpose.")
            .setBodyDelay(6, TimeUnit.SECONDS)

        server.enqueue(delayedResponse)

        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS) // 只等 3 秒
            .build()

        val request = Request.Builder()
            .url(server.url("/delayed"))
            .build()

        try {
            val response = client.newCall(request).execute()
            fail("Expected SocketTimeoutException due to read timeout, but got response: $response")
        } catch (e: Exception) {
            println("Caught expected exception: ${e.javaClass.simpleName}, message=${e.message}")
            assertTrue(e is SocketTimeoutException || e.cause is SocketTimeoutException)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun testWithOkHttp() {
        val client = OkHttpClient.Builder().build()

        // 模拟发起异步请求
        client.newCall(Request.Builder().url("https://example.com").build())
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    println("Response received")
                }

                override fun onFailure(call: Call, e: IOException) {
                    println("Request failed: ${e.message}")
                }
            })

        // 等待一会儿，模拟异步回调完成
        Thread.sleep(2000)

        // 优雅关闭：关闭 Dispatcher 的线程池
        val dispatcher = client.dispatcher
        dispatcher.executorService.shutdown() // 停止接收新任务
        try {
            // 最多等待 5 秒，让现有任务完成
            if (!dispatcher.executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                println("⚠️ 有些任务没有在 5 秒内完成，但强制关闭了")
                dispatcher.executorService.shutdownNow() // 可选：尝试中断所有任务
            }
        } catch (e: InterruptedException) {
            dispatcher.executorService.shutdownNow()
            Thread.currentThread().interrupt() // 恢复 interrupt 标志
        }
    }
}
