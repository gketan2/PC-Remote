package com.k10.control.network.wrapper

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class SocketStatus {

    @NonNull
    var state: SocketState

    @Nullable
    var message: String

    @Nullable
    var ip: String

    @Nullable
    var port: Int

    constructor(@NonNull state: SocketState, @Nullable message: String) {
        this.state = state
        this.message = message
        ip = "0.0.0.0"
        port = -1
    }

    constructor(
        @NonNull state: SocketState,
        @Nullable message: String,
        @Nullable ip: String,
        @Nullable port: Int
    ) {
        this.state = state
        this.message = message
        this.ip = ip
        this.port = port
    }

    companion object {
        fun connecting(ip: String, port: Int): SocketStatus {
            return SocketStatus(
                SocketState.CONNECTING,
                "Connecting...",
                ip,
                port
            )
        }

        fun connected(ip: String, port: Int): SocketStatus {
            return SocketStatus(
                SocketState.CONNECTED,
                "Connected",
                ip,
                port
            )
        }

        fun failed(message: String): SocketStatus {
            return SocketStatus(
                SocketState.FAILED,
                message
            )
        }

        fun disconnected(): SocketStatus {
            return SocketStatus(
                SocketState.DISCONNECTED,
                "Disconnected"
            )
        }

    }
}