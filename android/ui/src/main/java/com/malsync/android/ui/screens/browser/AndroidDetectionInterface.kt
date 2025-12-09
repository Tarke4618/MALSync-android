package com.malsync.android.ui.screens.browser

import android.webkit.JavascriptInterface

/**
 * Interface injected into WebView to receive video events from JavaScript
 */
class AndroidDetectionInterface(
    private val onVideoFound: (Float) -> Unit,
    private val onVideoProgress: (Float, Float) -> Unit
) {

    @JavascriptInterface
    fun onVideoFound(duration: Float) {
        onVideoFound.invoke(duration)
    }

    @JavascriptInterface
    fun onVideoProgress(currentTime: Float, duration: Float) {
        onVideoProgress.invoke(currentTime, duration)
    }
}
