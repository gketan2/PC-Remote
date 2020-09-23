package com.k10.control.network

/**These should be same as implemented on Server-Side.
 *
 * Only the respective command will run with the command type.
 *
 * If data required with each command is not satisfied then also command will not run.
 */
object COMMANDS {
    const val COMMAND_PASSWORD: Int = 0
    const val COMMAND_POINTER_POSITION: Int = 1
    const val COMMAND_SCREEN_SIZE: Int = 2

    const val COMMAND = "command"
    const val PASSWORD = "password"
}