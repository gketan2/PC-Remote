package com.k10.control.network

/**These should be same as implemented on Server-Side.
 *
 * Only the respective command will run with the command type.
 *
 * If data required with each command is not satisfied then also command will not run.
 */
object COMMANDS {
    const val PASSWORD: Int = 0
    const val POINTER_POSITION: Int = 1
    const val SCREEN_SIZE: Int = 2
}