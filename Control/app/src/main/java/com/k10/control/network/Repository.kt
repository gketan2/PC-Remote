package com.k10.control.network

import androidx.lifecycle.MediatorLiveData
import com.k10.control.helper.Headers
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val sockets: Sockets) {

    /**TODO
     * Make a List of possible commands.
     * Also make function that accepts custom commands
     */

    val socketStatus: MediatorLiveData<SocketStatus> = MediatorLiveData()

    /**
     * Send current position(x, y) to the server.
     *
     * If aspect-ratio is not set then server moves the pointer to this position only.
     *
     * If aspect-ratio is set then server converts the given point according to its screen size.
     * So it is recommended to set aspect-ratio.
     */
    suspend fun sendPointerPosition(x: Int, y: Int) {
    }

    /**
     * Send the tracker window size(not the screen size, just the view which is the tracker region).
     *
     * This will help in moving the pointer for different screen sizes with same speed.
     */
    suspend fun sendScreenSize(screenWidth: Int, screenHeight: Int) {
    }

    /**
     * Close the persistent http / socket (not decided yet) by which we contact the server.
     *
     * Call it when application is closing otherwise the connection will be open forever
     */
    suspend fun closeConnection() {
        sockets.closeSocket()
    }

    /**
     * Start the connection with given ip and port
     */
    suspend fun startConnection(ipAddress: String, port: Int) {
        socketStatus.postValue(sockets.createSocket(ipAddress, port))
    }

    /**
     * Should be the first to call.
     *
     * No other command will run if the correct password is not set.
     *
     * If called after the auth done, with wrong password, no command will run until you set correct password again.
     */
    suspend fun sendPassword(password: String) {
        val jsonObject = JSONObject()
        jsonObject.put(COMMANDS.COMMAND, COMMANDS.COMMAND_PASSWORD)
        jsonObject.put(COMMANDS.PASSWORD, password)

        val data = Headers.addHeader(jsonObject.toString())

        println("-------------------"+data)

        sockets.sendStringData(data)
    }

    /**
     * If the client sdk is not up to date with server.
     * Client will have not have latest commands.
     * You can send custom command and data in the format of JSON.
     *
     * Note: Data should only be in json format,
     * otherwise it will not work(will be discarded on server side)
     */
    suspend fun sendCustomCommand(command: Int, data: String) {

    }
}