package com.k10.control.network

abstract class AbstractSocket {

    abstract suspend fun createSocket(ipAddress: String, port: Int)

    abstract suspend fun sendData(data: String)

    abstract suspend fun closeSocket()

}