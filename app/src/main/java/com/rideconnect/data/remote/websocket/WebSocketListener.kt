package com.rideconnect.data.remote.websocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

abstract class WebSocketListener : WebSocketListener() {
    private val TAG = "WebSocketListener"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "WebSocket connection opened")
        onConnectionEstablished()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d(TAG, "WebSocket message received: $text")
        onMessageReceived(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d(TAG, "WebSocket binary message received: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "WebSocket closing: $code, $reason")
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "WebSocket closed: $code, $reason")
        onConnectionClosed()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e(TAG, "WebSocket failure: ${t.message}", t)
        onConnectionFailed(t)
    }

    abstract fun onConnectionEstablished()
    abstract fun onMessageReceived(message: String)
    abstract fun onConnectionClosed()
    abstract fun onConnectionFailed(throwable: Throwable)
}
