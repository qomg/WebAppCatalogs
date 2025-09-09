package com.qomg.webappcatalogs

import org.junit.Test
import io.mockk.mockk
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue

class MockkTest {
    interface AuthService {
        fun login(username: String, password: String): Boolean
    }

    @Test
    fun `login should return true when credentials valid`() {
        val authMock = mockk<AuthService>()

        // Stubbing 配置
        every {
            authMock.login(
                username = eq("admin"), // 精确匹配
                password = any()        // 任意密码
            )
        } returns true

        assertTrue(authMock.login("admin", "123456"))
        verify(exactly = 1) { authMock.login(any(), any()) }
    }

    class PaymentProcessor {
        fun process(amount: Double) {
            if (amount <= 0) throw IllegalArgumentException()
            // 真实支付逻辑
        }
    }

    @Test
    fun `process should throw when amount invalid`() {
        val processor = mockk<PaymentProcessor>()

        every { processor.process(any()) } throws IllegalArgumentException("Invalid amount")

        assertThrows(IllegalArgumentException::class.java) {
            processor.process(-100.0)
        }
    }

    object NetworkConfig {
        fun getBaseUrl() = "https://production.api"
    }

    @Test
    fun `mock singleton object`() {
        mockkObject(NetworkConfig)

        every { NetworkConfig.getBaseUrl() } returns "https://test.api"

        assertEquals("https://test.api", NetworkConfig.getBaseUrl())

        unmockkObject(NetworkConfig) // 清理
    }
}