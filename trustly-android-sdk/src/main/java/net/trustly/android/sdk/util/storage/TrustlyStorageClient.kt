package net.trustly.android.sdk.util.storage

import android.database.sqlite.SQLiteFullException
import android.util.Base64
import android.util.Log
import android.webkit.CookieManager
import java.io.IOException
import java.nio.charset.StandardCharsets

class TrustlyStorageClient(
    private val storageUrl: String = DEFAULT_STORAGE_URL,
    private val cryptoEngine: TrustlyCryptoEngine = TrustlyCryptoEngine(),
    private val cookieManagerProvider: () -> CookieManager = { CookieManager.getInstance() }
) {

    fun setItem(key: String, value: String): Boolean {
        val encryptedValue = cryptoEngine.encrypt(value) ?: return false
        val cookieName = normalizeKey(key)
        val cookieValue = buildCookie(cookieName, encryptedValue, COOKIE_MAX_AGE_SECONDS)

        try {
            val cookieManager = cookieManagerProvider()
            cookieManager.setCookie(storageUrl, cookieValue)
            cookieManager.flush()
            return true
        } catch (_: SQLiteFullException) {
            Log.w(TAG, "Cookie storage full; personalization disabled")
        } catch (_: IOException) {
            Log.w(TAG, "Cookie storage I/O failure; personalization disabled")
        } catch (_: IllegalStateException) {
            Log.w(TAG, "Cookie manager unavailable in current process context")
        } catch (_: SecurityException) {
            Log.w(TAG, "Cookie access restricted by runtime or profile policy")
        }
        return false
    }

    fun getItem(key: String): String? {
        val cookieName = normalizeKey(key)

        try {
            val cookieManager = cookieManagerProvider()
            val cookieHeader = cookieManager.getCookie(storageUrl) ?: return null
            val encryptedValue = extractCookieValue(cookieHeader, cookieName) ?: return null

            val plainValue = cryptoEngine.decrypt(encryptedValue)
            if (plainValue == null) {
                clearCookie(cookieManager, cookieName)
            }
            return plainValue
        } catch (_: SQLiteFullException) {
            Log.w(TAG, "Cookie storage full during read; personalization disabled")
        } catch (_: IOException) {
            Log.w(TAG, "Cookie storage I/O failure during read; personalization disabled")
        } catch (_: IllegalStateException) {
            Log.w(TAG, "Cookie manager unavailable in current process context")
        } catch (_: SecurityException) {
            Log.w(TAG, "Cookie access restricted by runtime or profile policy")
        }
        return null
    }

    private fun clearCookie(cookieManager: CookieManager, cookieName: String) {
        val expiredCookie = buildCookie(cookieName, "", 0)
        cookieManager.setCookie(storageUrl, expiredCookie)
        cookieManager.flush()
    }

    private fun normalizeKey(key: String): String {
        val trimmedKey = key.trim()
        if (trimmedKey.matches(Regex(KEY_TOKEN_PATTERN))) {
            return trimmedKey
        }

        val encodedKey = Base64.encodeToString(
            trimmedKey.toByteArray(StandardCharsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
        return "$COOKIE_KEY_PREFIX$encodedKey"
    }

    private fun extractCookieValue(cookieHeader: String, cookieName: String): String? {
        val cookieParts = cookieHeader.split(';')
        for (part in cookieParts) {
            val trimmedPart = part.trim()
            val equalSignIndex = trimmedPart.indexOf('=')
            if (equalSignIndex <= 0) {
                continue
            }

            val name = trimmedPart.substring(0, equalSignIndex)
            if (name == cookieName) {
                return trimmedPart.substring(equalSignIndex + 1).ifBlank { null }
            }
        }
        return null
    }

    private fun buildCookie(cookieName: String, value: String, maxAgeSeconds: Int): String {
        return "$cookieName=$value; Max-Age=$maxAgeSeconds; Path=/; Secure; HttpOnly; SameSite=Strict"
    }

    private companion object {
        const val TAG = "TrustlyStorageClient"
        const val DEFAULT_STORAGE_URL = "https://storage.trustly.com"
        const val COOKIE_MAX_AGE_SECONDS = 31536000
        const val COOKIE_KEY_PREFIX = "tk_"
        const val KEY_TOKEN_PATTERN = "^[A-Za-z0-9_-]+$"
    }
}

