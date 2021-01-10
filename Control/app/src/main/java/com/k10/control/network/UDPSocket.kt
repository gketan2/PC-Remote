package com.k10.control.network

import androidx.lifecycle.MediatorLiveData
import com.k10.control.network.wrapper.SocketStatus
import java.net.*
import java.util.regex.Pattern
import javax.inject.Inject

class UDPSocket @Inject constructor() : AbstractSocket(){

    private var ip: InetAddress? = null
    private var port: Int = 5000

    private var socket: DatagramSocket? = null
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
            socket = DatagramSocket()
            ip = InetAddress.getByName(ipAddress)
            this.port = port
            isInitial = false
            currentState.postValue(SocketStatus.connected(ipAddress, port))
        } catch (e: SocketException) {
            currentState.postValue(SocketStatus.failed(e.localizedMessage!!))
        } catch (e: SecurityException) {
            currentState.postValue(SocketStatus.failed(e.localizedMessage!!))
        } catch (e: UnknownHostException) {
            currentState.postValue(SocketStatus.failed(e.localizedMessage!!))
        }
    }

    override suspend fun sendData(data: String) {
        if(ip == null){
            closeSocket()
            return
        }
        socket?.let {
            try {
                it.send(
                    DatagramPacket(
                        data.toByteArray(charset = Charsets.UTF_8),
                        data.length,
                        ip,
                        port
                    )
                )
            } catch (e: Exception) {
                currentState.postValue(SocketStatus.failed(e.localizedMessage!!))
                closeSocket()
            }
        }
    }

    override suspend fun closeSocket() {
        socket?.close()
        socket = null
        isInitial = true
        currentState.postValue(SocketStatus.disconnected())
    }

}