package com.example.myapplication.socket

import android.util.Log
import com.neovisionaries.ws.client.WebSocketFactory
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class SocketManager(accessToken: String) {
    companion object {
        private const val BASE_URL_DOMAIN = "https://yochat.4qcteam.com/JUMxUJlSGkdeYogAolfh3Sny"
//        private const val BASE_URL_DOMAIN = "https://beta.bitti.com:3003/"

        private const val SOCKET_URL = BASE_URL_DOMAIN
        private const val TAG = "SOCKET_MANAGER"

        private const val EVENT_CONNECT = "socketConnected"

        private var INSTANCE: SocketManager? = null

        fun getInstance(accessToken: String) = INSTANCE ?: synchronized(SocketManager::class.java) {
            INSTANCE ?: SocketManager(accessToken)
                .also {
                    INSTANCE = it
                }
        }

        fun destroy() {
            Log.d(TAG, "Destroying socket instance")
            INSTANCE?.disconnect()
            INSTANCE = null
        }
    }

    private val options by lazy {
        IO.Options().apply {
            secure = false
            SocketSSL.set(this)
//            reconnection = true
//            forceNew = true
//            query = "authentication=${accessToken}"
        }
    }

    private val socket by lazy { IO.socket(SOCKET_URL, options) }

    private fun createWebSocketClient(coinbaseUri: URI?) {

    }

    fun connect() {
        if (socket.connected()) {
            Log.d(TAG, "Socket is already connected")
            return
        }

        if (!socket.hasListeners(EVENT_CONNECT)) {
            socket.on(EVENT_CONNECT) { Log.d(TAG, "Socket Connect ${socket.id()}") }
            socket.on(Socket.EVENT_DISCONNECT) { Log.d(TAG, "Socket Disconnect") }
            socket.on(Socket.EVENT_CONNECT_TIMEOUT) { args ->
                Log.w(
                    TAG,
                    "Socket Connect timeout : ${args.firstOrNull()}"
                )
            }
            socket.on(Socket.EVENT_ERROR) { args ->
                Log.w(
                    TAG,
                    "Socket error : ${args.firstOrNull()}"
                )
            }
            socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.w(
                    TAG,
                    "Socket connect error : ${args.firstOrNull()}"
                )
            }
        }

        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
        socket.off()
        Log.d(TAG, "Disconnect")
    }

    fun on(event: String, listener: Emitter.Listener) {
        socket.on(event, listener)
    }

    fun off(event: String, listener: Emitter.Listener) {
        socket.off(event, listener)
    }

    fun emit(event: String, args: Any, acknowledge: Ack) {
        if (socket.connected()) {
            socket.emit(event, args, acknowledge)
        }
    }
}