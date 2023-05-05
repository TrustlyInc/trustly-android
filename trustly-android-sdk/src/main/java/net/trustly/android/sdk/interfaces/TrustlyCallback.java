/*  ___________________________________________________________________________________________________________
 *
 *    TRUSTLY CONFIDENTIAL AND PROPRIETARY INFORMATION
 *  ___________________________________________________________________________________________________________
 *
 *      Copyright (c) 2012 - 2020 Trustly
 *      All Rights Reserved.
 *
 *   NOTICE:  All information contained herein is, and remains, the confidential and proprietary property of
 *   Trustly and its suppliers, if any. The intellectual and technical concepts contained herein are the
 *   confidential and proprietary property of Trustly and its suppliers and  may be covered by U.S. and
 *   Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of
 *   this information or reproduction of this material is strictly forbidden unless prior written permission is
 *   obtained from Trustly.
 *   ___________________________________________________________________________________________________________
 */
package net.trustly.android.sdk.interfaces;

/**
 * Trustly callback function definition
 *
 * @param <T> The object instance type that is calling the handler
 * @param <V> The returned data type
 */
public interface TrustlyCallback<T, V> {

    /**
     * @param trustly    The Trustly instance object that is calling the callback handler
     * @param returnParameters The map containing the returned parameters from the TrustlyView.
     */
    void handle(T trustly, V returnParameters);
}
