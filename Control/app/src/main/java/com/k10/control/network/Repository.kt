package com.k10.control.network

import androidx.lifecycle.MediatorLiveData
import com.k10.control.request.Request
import com.k10.control.request.Services
import com.k10.control.utils.KeyTypeValuePair
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val sockets: Sockets, private val request: Request) {

    /**TODO
     * Make a List of possible commands.
     * Also make function that accepts custom commands
     */

    fun getSocketStatusLiveData() = sockets.currentState

    val passwordSet: MediatorLiveData<String> = MediatorLiveData()

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
     * Close the socket by which we contact the server.
     *
     * Call it when application is closing otherwise the connection will be open forever
     */
    suspend fun closeConnection() {
        passwordSet.postValue("")
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

        passwordSet.postValue(password)
        val data = "password=$password"

        sockets.sendStringData(data)
    }

    /**
     * Send command to server to perform a left click.
     */
    suspend fun leftClick() {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_MOUSE,
            Services.SERVICE_MOUSE_LEFT_CLICK
        )
        sockets.sendStringData(jsonObject.toString())
    }

    /**
     * Send command to server to perform a left double click.
     */
    suspend fun doubleClick() {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_MOUSE,
            Services.SERVICE_MOUSE_LEFT_DOUBLE_CLICK
        )
        sockets.sendStringData(jsonObject.toString())
    }

    /**
     * Send command to server to perform a right click.
     */
    suspend fun rightClick() {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_MOUSE,
            Services.SERVICE_MOUSE_RIGHT_CLICK
        )
        sockets.sendStringData(jsonObject.toString())
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
    suspend fun movePointerBy(x: Float, y: Float) {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_MOUSE,
            Services.SERVICE_MOUSE_MOVE_POINTER_BY,
            arrayOf(x, y)
        )
        sockets.sendStringData(jsonObject.toString())
    }

    suspend fun scrollBy(y: Float) {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_MOUSE,
            Services.SERVICE_MOUSE_SCROLL_BY,
            y
        )
        sockets.sendStringData(jsonObject.toString())
    }

    /**
     * input: String - to be written(buttons to be pressed)
     *
     * Given string will be typed out on the pc,
     * it is not necessary that it will write it in the text box,
     * it just simulate the button press if the view(in PC)
     * is not in focus string will not be typed.
     * */
    suspend fun typeString(data: String) {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_KEYBOARD,
            Services.SERVICE_KEYBOARD_TYPE,
            data
        )
        sockets.sendStringData(jsonObject.toString())
    }

    suspend fun sendSpecialKeys(data: ArrayList<KeyTypeValuePair>) {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_KEYBOARD,
            Services.SERVICE_KEYBOARD_PRESS_KEYS,
            data
        )
        sockets.sendStringData(jsonObject.toString())
    }

    suspend fun sendHotKeys(data: ArrayList<KeyTypeValuePair>) {
        val jsonObject: JSONObject = request.generateRequestJSON(
            Services.SERVICE_KEYBOARD,
            Services.SERVICE_KEYBOARD_PRESS_HOT_KEYS,
            data
        )
        sockets.sendStringData(jsonObject.toString())
    }

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