package com.k10.control.network

import com.k10.control.request.CRequest
import com.k10.control.request.Services
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val sockets: UDPSocket,
    private val request: CRequest
) {

    /**TODO
     * Make a List of possible commands.
     * Also make function that accepts custom commands
     */

    fun getSocketStatusLiveData() = sockets.currentState

    /**
     * Close the socket by which we contact the server.
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
        sockets.createSocket(ipAddress, port)
    }

    /**
     * Should be the first to call.
     *
     * No other command will make effect on server if the correct password is not set.
     *
     * If called after the auth done, with wrong password, no command will run until you set correct password again.
     */
    suspend fun sendPassword(password: String) {
        val data = "password=$password"

        sockets.sendData(data)
    }

    /**
     * input:
     *
     * x: Float, x direction
     *
     * y: Float, y direction
     *
     * send distance to move, to the server.
     */
    suspend fun movePointerBy(x: Int, y: Int) {
        val v = request.generateRequest(Services.MOUSE_SERVICE, Services.MOUSE_MOVE, x, y)
        sockets.sendData(v)
    }

    /**
     * Send mouse-click event based on service selected in viewmodel
     */
    suspend fun mouseClick(mouseClick: Int) {
        val v = request.generateRequest(Services.MOUSE_SERVICE, mouseClick)
        sockets.sendData(v)
    }

    /**
     * Send scroll-event based on service selected in viewmodel.
     */
    suspend fun scroll(mouseService: Int) {
        val v = request.generateRequest(Services.MOUSE_SERVICE, mouseService)
        sockets.sendData(v)
    }

    /**
     * input: String - to be written(buttons to be pressed)
     *
     * Given string will be typed out on the pc,
     * it is not necessary that it will write it in the text box,
     * it just simulate the button press if the view(in PC)
     * is not in focus string will not be typed.
     * */
//    suspend fun typeString(data: String) {
//        val jsonObject: JSONObject = request.generateRequestJSON(
//            Services.SERVICE_KEYBOARD,
//            Services.SERVICE_KEYBOARD_TYPE,
//            data
//        )
//        sockets.sendStringData(jsonObject.toString())
//    }

//    suspend fun sendSpecialKeys(data: ArrayList<KeyTypeValuePair>) {
//        val jsonObject: JSONObject = request.generateRequestJSON(
//            Services.SERVICE_KEYBOARD,
//            Services.SERVICE_KEYBOARD_PRESS_KEYS,
//            data
//        )
//        sockets.sendStringData(jsonObject.toString())
//}

//    suspend fun sendHotKeys(data: ArrayList<KeyTypeValuePair>) {
//        val jsonObject: JSONObject = request.generateRequestJSON(
//            Services.SERVICE_KEYBOARD,
//            Services.SERVICE_KEYBOARD_PRESS_HOT_KEYS,
//            data
//        )
//        sockets.sendStringData(jsonObject.toString())
//    }

    /**
     * If the client sdk is not up to date with server.
     * Client will have not have latest commands.
     * You can send custom command and data in the format of JSON.
     *
     * Note: Data should only be in json format,
     * otherwise it will not work(will be discarded on server side)

    suspend fun sendCustomCommand(command: Int, data: String) {

    }
     */
}