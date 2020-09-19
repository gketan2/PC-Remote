package com.k10.control.network

class Repository {

    /**TODO
     * Make a List of possible commands.
     * Also make function that accepts custom commands
     */

    companion object {
        private var repository: Repository? = null

        /**
         * Returns instance of this class
         *
         * Following Singleton pattern
         */
        fun getInstance(): Repository {
            if (repository == null) {
                repository = Repository()
            }
            return repository!!
        }
    }

    /**
     * Send current position(x, y) to the server.
     *
     * If aspect-ratio is not set then server moves the pointer to this position only.
     *
     * If aspect-ratio is set then server converts the given point according to its screen size.
     * So it is recommended to set aspect-ratio.
     */
    fun sendPointerPosition(x: Int, y: Int) {

    }

    /**
     * Send the tracker window size(not the screen size, just the view which is the tracker region).
     *
     * This will help in moving the pointer for different screen sizes with same speed.
     */
    fun sendScreenSize(screenWidth: Int, screenHeight: Int) {

    }

    /**
     * Close the persistent http / socket (not decided yet) by which we contact the server.
     *
     * Call it when application is closing otherwise the connection will be open forever
     */
    fun closeConnection() {

    }

    /**
     * Start the connection to given ip and port
     */
    fun startConnection(ipAddress: String, port: Int) {

    }

    /**
     * Should be the first to call.
     *
     * No other command will run if the correct password is not set.
     *
     * If called after the auth done, with wrong password, no command will run until you set correct password again.
     */
    fun sendPassword(password: String) {

    }

    /**
     * If the client sdk is not up to date with server.
     * Client will have not have latest commands.
     * You can send custom command and data in the format of JSON.
     *
     * Note: Data should only be in json format,
     * otherwise it will not work(will be discarded on server side)
     */
    fun sendCustomCommand(command: Int, data: String) {

    }
}