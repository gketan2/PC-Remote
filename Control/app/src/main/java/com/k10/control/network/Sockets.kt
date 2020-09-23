package com.k10.control.network

import java.io.IOException
import java.io.OutputStream
import java.lang.IllegalArgumentException
import java.net.Socket
import java.net.UnknownHostException
import javax.inject.Inject

class Sockets @Inject constructor() {

    companion object {
        private const val DEFAULT_IP = "192.168.43.143"
        private const val DEFAULT_PORT = 5000
    }

    private var socket: Socket? = null
    private var sender: OutputStream? = null

    private var isInitial = true

    suspend fun createSocket(
        ipAddress: String = DEFAULT_IP,
        port: Int = DEFAULT_PORT
    ): SocketStatus {
        if (!isInitial)
            socket?.close()

        try {
            socket = Socket(ipAddress, port)
        } catch (e: UnknownHostException) {
            return SocketStatus(false, "Server not found")
        } catch (e: IOException) {
            println(e.message)
            return SocketStatus(false, "Something Went Wrong")
        } catch (e: SecurityException) {
            return SocketStatus(false, "Network not secure")
        } catch (e: IllegalArgumentException) {
            return SocketStatus(false, "Illegal Argument")
        }

        socket?.let {
            if (it.isConnected) {
                sender = it.getOutputStream()
                isInitial = false
                println("socket connected")
                return SocketStatus(true, "Success")
            }
        }

        socket?.close()
        socket = null
        isInitial = true
        return SocketStatus(false, "Something went")
    }

    suspend fun sendStringData(data: String) {
        socket?.let {
            println("-------not null socket")
            if (it.isConnected) {
                println("----------is connected")
                sender?.write(data.toByteArray(charset = Charsets.UTF_8))
            }
        }
    }

    suspend fun closeSocket(): Boolean {
        socket?.let {
            it.close()
            sender = null
            isInitial = false
            return true
        }
        isInitial = false
        return false
    }
}