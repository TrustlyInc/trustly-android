package net.trustly.android.sdk.interfaces

/**
 * Trustly callback function definition
 *
 * @param <T> The object instance type that is calling the handler
 * @param <V> The returned data type
 */
fun interface TrustlyCallback<T, V> {

    /**
     * @param trustly    The Trustly instance object that is calling the callback handler
     * @param returnParameters The map containing the returned parameters from the TrustlyView.
     */
    fun handle(trustly: T?, returnParameters: V?)

}