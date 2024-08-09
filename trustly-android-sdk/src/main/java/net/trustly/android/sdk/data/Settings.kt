package net.trustly.android.sdk.data

data class Settings(
    val settings: LightBoxSetting
)

data class LightBoxSetting(
    val lightbox: LightBoxContext
)

data class LightBoxContext(
    val context: String
)