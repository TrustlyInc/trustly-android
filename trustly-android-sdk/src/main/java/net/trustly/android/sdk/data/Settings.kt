package net.trustly.android.sdk.data

data class Settings(
    val settings: StrategySetting,
    /**
     * Cross-origin / cross-merchant persistent storage fields, delivered by the
     * server in the setup response when the merchant has the feature enabled
     * (ENABLE_CROSS_ORIGIN_STORAGE). All nullable and additive: when the server
     * does not send them, Gson leaves them null and behavior is unchanged.
     *
     * The actual storage read/write happens in the web layer (localStorage on
     * trustly.com within the shared Custom Tabs browser); these fields only let
     * the SDK detect that the feature is on and reconcile legacy signaling.
     */
    val crossOriginStorage: Boolean? = null,
    val crossOriginEncryptionKey: String? = null,
    val crossOriginStorageUrl: String? = null,
    val crossOriginData: String? = null
) {

    /**
     * True when the merchant has cross-origin storage enabled and the server
     * supplied the storage URL. Mirrors the web `isEnabled`/`canFetchRawBlob`
     * gating: the encryption key may be absent on requests without a shopper
     * identifier, so it is not required here.
     */
    fun isCrossOriginStorageEnabled(): Boolean =
        crossOriginStorage == true && !crossOriginStorageUrl.isNullOrBlank()

}

data class StrategySetting(
    val integrationStrategy: String
)
