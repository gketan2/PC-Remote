package com.k10.control.request

/**These should be same as implemented on Server-Side.
 *
 * Only the respective command will run with the command type.
 *
 * If data required with each command is not satisfied then also command will not run.
 */
object Services {
    const val SERVICE = "service"
    const val SUB_SERVICE = "sub_service"

    const val TYPE = "type"
    const val VALUE = "value"
    const val KEY_LIST = "key_list"

    const val TYPE_SPECIAL = 1
    const val TYPE_NORMAL = 0

    const val SERVICE_KEYBOARD = 1
    const val SERVICE_MOUSE = 2

    const val SERVICE_KEYBOARD_TYPE = 0
    const val SERVICE_KEYBOARD_PRESS_KEYS = 1
    const val SERVICE_KEYBOARD_PRESS_HOT_KEYS = 2

    const val SERVICE_MOUSE_LEFT_CLICK = 0
    const val SERVICE_MOUSE_LEFT_DOUBLE_CLICK = 1
    const val SERVICE_MOUSE_RIGHT_CLICK = 2
    const val SERVICE_MOUSE_MOVE_POINTER_BY = 3
    const val SERVICE_MOUSE_SCROLL_BY = 4



    const val PASSWORD = "password"
}