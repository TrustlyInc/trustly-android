package net.trustly.android.sdk.data.model

data class Settings(
    val settings: StrategySetting
)

data class StrategySetting(
    val integrationStrategy: String
)