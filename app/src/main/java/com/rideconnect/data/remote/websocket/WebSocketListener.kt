package com.rideconnect.data.remote.websocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

abstract class WebSocketListener : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket", "Connection established")
        onConnectionEstablished()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Receiving message: $text")
        onMessageReceived(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("WebSocket", "Receiving bytes message")
        // Usually not used in this application
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Connection closing: $reason")
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Connection closed: $reason")
        onConnectionClosed()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "Connection failure: ${t.message}")
        onConnectionFailed(t)
    }

    abstract fun onConnectionEstablished()
    abstract fun onMessageReceived(message: String)
    abstract fun onConnectionClosed()
    abstract fun onConnectionFailed(throwable: Throwable)
}
