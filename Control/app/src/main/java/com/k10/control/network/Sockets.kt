package com.k10.control.network

import androidx.lifecycle.MediatorLiveData
import com.k10.control.helper.Headers
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
    private var writerStream: OutputStream? = null

    private var isInitial = true

    val currentState: MediatorLiveData<SocketStatus> = MediatorLiveData()

    suspend fun createSocket(
        ipAddress: String = DEFAULT_IP,
        port: Int = DEFAULT_PORT
    ) {
        currentState.postValue(SocketStatus(false, "Connecting..."))
        try {
            if (isInitial) {
                socket = null
            } else {
                socket?.close()
                socket = null
            }
        } catch (e: Exception) {
            socket = null
        }

        try {
            socket = Socket(ipAddress, port)
            isInitial = false
            currentState.postValue(SocketStatus(true, "Connected"))
            println("connected")
        } catch (e: UnknownHostException) {
            isInitial = true
            currentState.postValue(SocketStatus(false, "Server not found"))
            return
        } catch (e: IOException) {
            isInitial = true
            currentState.postValue(SocketStatus(false, "Something Went Wrong"))
            return
        } catch (e: SecurityException) {
            isInitial = true
            currentState.postValue(SocketStatus(false, "Network not secure"))
            return
        } catch (e: IllegalArgumentException) {
            isInitial = true
            currentState.postValue(SocketStatus(false, "Illegal Argument"))
            return
        }

        socket?.let {
            if (it.isConnected) {
                writerStream = it.getOutputStream()
                isInitial = false
                return
            }
        }

        try {
            socket?.close()
        } catch (e: Exception) {
        }
        socket = null
        isInitial = true
        currentState.postValue(SocketStatus(false, "Socket Disconnected"))
    }

    /**
     * input: String - data to be sent
     *
     * Add the HEADER to the input string and then send it to the connected socket server.
     */
    suspend fun sendStringData(data: String) {
        socket?.let {
            println("-------not null socket")
            //adding Header to the data
            val message = Headers.addHeader(data)
            try {
                println("Sending Data:$data")
                writerStream?.write(message.toByteArray(charset = Charsets.UTF_8))
            } catch (e: Exception) {
                currentState.postValue(SocketStatus(false, e.localizedMessage!!))
                closeSocket()
            }
        }
    }

    suspend fun closeSocket() {
        try {
            socket?.close()
        } catch (e: Exception) {
        }
        socket = null
        writerStream = null
        socket = null
        isInitial = true
        currentState.postValue(SocketStatus(false, "Socket Disconnected"))
    }
}