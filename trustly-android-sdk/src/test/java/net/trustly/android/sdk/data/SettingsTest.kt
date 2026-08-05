package net.trustly.android.sdk.data

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsTest {

    private val gson = Gson()

    @Test
    fun shouldParseLegacyResponseWithoutCrossOriginFields() {
        val json = "{\"settings\":{\"integrationStrategy\":\"webview\"}}"

        val settings = gson.fromJson(json, Settings::class.java)

        assertEquals("webview", settings.settings.integrationStrategy)
        assertNull(settings.crossOriginStorage)
        assertNull(settings.crossOriginEncryptionKey)
        assertNull(settings.crossOriginStorageUrl)
        assertNull(settings.crossOriginData)
        assertFalse(settings.isCrossOriginStorageEnabled())
    }

    @Test
    fun shouldParseResponseWithCrossOriginFields() {
        val json = "{\"settings\":{\"integrationStrategy\":\"webview\"}," +
            "\"crossOriginStorage\":true," +
            "\"crossOriginEncryptionKey\":\"a2V5\"," +
            "\"crossOriginStorageUrl\":\"https://trustly.com/cross-origin\"," +
            "\"crossOriginData\":\"aXY6Y2lwaGVy\"}"

        val settings = gson.fromJson(json, Settings::class.java)

        assertEquals(true, settings.crossOriginStorage)
        assertEquals("a2V5", settings.crossOriginEncryptionKey)
        assertEquals("https://trustly.com/cross-origin", settings.crossOriginStorageUrl)
        assertEquals("aXY6Y2lwaGVy", settings.crossOriginData)
        assertTrue(settings.isCrossOriginStorageEnabled())
    }

    @Test
    fun shouldNotBeEnabledWhenFlagOnButUrlMissing() {
        val settings = Settings(
            settings = StrategySetting("webview"),
            crossOriginStorage = true,
            crossOriginStorageUrl = null
        )

        assertFalse(settings.isCrossOriginStorageEnabled())
    }

    @Test
    fun shouldNotBeEnabledWhenFlagOnButUrlBlank() {
        val settings = Settings(
            settings = StrategySetting("webview"),
            crossOriginStorage = true,
            crossOriginStorageUrl = "  "
        )

        assertFalse(settings.isCrossOriginStorageEnabled())
    }

    @Test
    fun shouldNotBeEnabledWhenFlagOffButUrlPresent() {
        val settings = Settings(
            settings = StrategySetting("webview"),
            crossOriginStorage = false,
            crossOriginStorageUrl = "https://trustly.com/cross-origin"
        )

        assertFalse(settings.isCrossOriginStorageEnabled())
    }

    @Test
    fun shouldPreserveCrossOriginFieldsAcrossGsonRoundTrip() {
        val original = Settings(
            settings = StrategySetting("webview"),
            crossOriginStorage = true,
            crossOriginEncryptionKey = "a2V5",
            crossOriginStorageUrl = "https://trustly.com/cross-origin",
            crossOriginData = "aXY6Y2lwaGVy"
        )

        val restored = gson.fromJson(gson.toJson(original), Settings::class.java)

        assertEquals(original, restored)
        assertTrue(restored.isCrossOriginStorageEnabled())
    }

}
