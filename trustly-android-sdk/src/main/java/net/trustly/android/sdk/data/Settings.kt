package net.trustly.android.sdk.data

data class Settings(
    val settings: StrategySetting
)

data class StrategySetting(
    val integrationStrategy: String
)