package net.trustly.android.sdk.data.model

data class Tracking(
    val trustlySdkVersion: String,
    val deviceSystem: String,
    val deviceVersion: String,
    val deviceModel: String?,
    val type: String,
    val message: String,
)