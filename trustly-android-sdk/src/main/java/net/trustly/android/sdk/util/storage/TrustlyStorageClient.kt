package net.trustly.android.sdk.util.storage

import android.content.Context
import android.os.Build
import android.util.Base64
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import net.trustly.android.sdk.util.error.TrustlyExceptionHandler
import java.nio.charset.StandardCharsets

class TrustlyStorageClient(
    context: Context,
    private val storageUrl: String = DEFAULT_STORAGE_URL,
    private val cryptoEngine: TrustlyCryptoEngine = TrustlyCryptoEngine(context),
    private val cookieManagerProvider: () -> CookieManager = { CookieManager.getInstance() },
    private val legacyCookieSync: (() -> Unit)? = null
) {
    private val appContext = context.applicationContext ?: context

    fun setItem(key: String, value: String): Boolean {
        val encryptedValue = cryptoEngine.encrypt(value) ?: return false
        val cookieName = normalizeKey(key)
        val cookieValue = buildCookie(cookieName, encryptedValue, COOKIE_MAX_AGE_SECONDS)

        try {
            val cookieManager = cookieManagerProvider()
            cookieManager.setCookie(storageUrl, cookieValue)
            persistCookies(cookieManager)
            return true
        } catch (e: Exception) {
            showLogError("Unexpected error during cookie write: ${e.message}")
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
        } catch (e: Exception) {
            showLogError("Unexpected error during cookie read: ${e.message}")
        }
        return null
    }

    private fun clearCookie(cookieManager: CookieManager, cookieName: String) {
        val expiredCookie = buildCookie(cookieName, "", 0)
        cookieManager.setCookie(storageUrl, expiredCookie)
        persistCookies(cookieManager)
    }

    private fun persistCookies(cookieManager: CookieManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
            return
        }

        val syncAction = legacyCookieSync ?: {
            CookieSyncManager.createInstance(appContext)
            CookieSyncManager.getInstance().sync()
        }
        syncAction.invoke()
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

    private fun showLogError(message: String) {
        TrustlyExceptionHandler().uncaughtException(
            Thread.currentThread(),
            Exception(message)
        )
    }

    private companion object {
        const val DEFAULT_STORAGE_URL = "https://storage.trustly.com"
        const val COOKIE_MAX_AGE_SECONDS = 31536000
        const val COOKIE_KEY_PREFIX = "tk_"
        const val KEY_TOKEN_PATTERN = "^[A-Za-z0-9_-]+$"
    }
}
