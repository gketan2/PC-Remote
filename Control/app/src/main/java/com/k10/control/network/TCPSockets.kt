package com.k10.control.network

import androidx.lifecycle.MediatorLiveData
import com.k10.control.helper.Headers
import com.k10.control.network.wrapper.SocketStatus
import java.io.IOException
import java.io.OutputStream
import java.lang.IllegalArgumentException
import java.net.*
import java.util.regex.Pattern
import javax.inject.Inject

class TCPSockets @Inject constructor() : AbstractSocket() {

    companion object {
        private const val DEFAULT_IP = "192.168.43.143"
        private const val DEFAULT_PORT = 5000
        private const val TIMEOUT_IN_SEC = 2
    }

    private var socket: Socket? = null
    private var writerStream: OutputStream? = null

    private var isInitial = true

    val currentState: MediatorLiveData<SocketStatus> = MediatorLiveData()

    private val zeroTo255 = ("(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])")

    private val regex: String = "$zeroTo255\\.$zeroTo255\\.$zeroTo255\\.$zeroTo255"

    private var ipPattern: Pattern = Pattern.compile(regex)

    override suspend fun createSocket(ipAddress: String, port: Int) {
        currentState.postValue(SocketStatus.connecting(ipAddress, port))
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

        val matcher = ipPattern.matcher(ipAddress)
        if (!matcher.matches()) {
            currentState.postValue(SocketStatus.failed("Wrong IP Address Format!"))
            return
        }

        try {
//            socket = Socket(ipAddress, port)
            val v = InetSocketAddress(ipAddress, port)
            socket = Socket()
            socket?.connect(v, TIMEOUT_IN_SEC * 1000)
            isInitial = false
            currentState.postValue(SocketStatus.connected(ipAddress, port))
        } catch (e: UnknownHostException) {
            isInitial = true
            currentState.postValue(SocketStatus.failed("Server not found!"))
            return
        } catch (e: SocketTimeoutException) {
            isInitial = true
            currentState.postValue(SocketStatus.failed("$TIMEOUT_IN_SEC sec Time Out!"))
            return
        } catch (e: IOException) {
            isInitial = true
            currentState.postValue(SocketStatus.failed("Something Went Wrong."))
            return
        } catch (e: SecurityException) {
            isInitial = true
            currentState.postValue(SocketStatus.failed("Network not secure."))
            return
        } catch (e: IllegalArgumentException) {
            isInitial = true
            currentState.postValue(SocketStatus.failed("Illegal Argument(developer error)."))
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
        currentState.postValue(SocketStatus.disconnected())
    }

    /**
     * input: String - data to be sent
     *
     * Add the HEADER to the input string and then send it to the connected socket server.
     */
    override suspend fun sendData(data: String) {
        socket?.let {
            //adding Header to the data
            val message = Headers.addHeader(data)
            try {
                writerStream?.write(message.toByteArray(charset = Charsets.UTF_8))
            } catch (e: Exception) {
                currentState.postValue(SocketStatus.failed(e.localizedMessage!!))
                closeSocket()
            }
        }
    }

    override suspend fun closeSocket() {
        try {
            socket?.close()
        } catch (e: Exception) {
        }
        socket = null
        writerStream = null
        socket = null
        isInitial = true
        currentState.postValue(SocketStatus.disconnected())
    }
}