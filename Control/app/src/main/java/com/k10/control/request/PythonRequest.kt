package com.k10.control.request

import com.k10.control.utils.KeyTypeValuePair
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PythonRequest @Inject constructor() : Request() {
    override fun generateRequest(service: Int, subService: Int): String {
        return "0"
    }

    override fun generateRequest(service: Int, subService: Int, vararg data: Int): String {
        return "0"
    }

    /**
     * Construct a JSON object of the service, sub_service,
     * value according to the type of service and sub_service.
     */
    fun generateRequestJSON(service: Int, subServiceType: Int, value: Any = ""): JSONObject {
        val requestData = JSONObject()
        requestData.put(Services.SERVICE, service)
        requestData.put(Services.SUB_SERVICE, subServiceType)

        if (service == Services.SERVICE_KEYBOARD) {
            when (subServiceType) {
                //Type Some String
                Services.SERVICE_KEYBOARD_TYPE -> {
                    if (value is String) {
                        requestData.put(Services.VALUE, value)
                    } else {
                        throw IllegalArgumentException("Expected a String")
                    }
                }
                //Press Keys one by one
                Services.SERVICE_KEYBOARD_PRESS_KEYS -> {
                    if (value is ArrayList<*>) {
                        val pairArray = generateKeyTypeValueJSONArray(value)
                        //Add the list of keys in return JSON-Object
                        requestData.put(Services.KEY_LIST, pairArray)
                    } else {
                        throw IllegalArgumentException("Expected ArrayList<KeyType>")
                    }
                }
                //press Hot key combination
                Services.SERVICE_KEYBOARD_PRESS_HOT_KEYS -> {
                    if (value is ArrayList<*>) {
                        val pairArray = generateKeyTypeValueJSONArray(value)
                        //Add the list of keys in return JSON-Object
                        requestData.put(Services.KEY_LIST, pairArray)
                    } else {
                        throw IllegalArgumentException("Expected ArrayList<KeyType>")
                    }
                }
            }

        } else if (service == Services.SERVICE_MOUSE) {
            when (subServiceType) {
                Services.SERVICE_MOUSE_LEFT_CLICK -> {
                }
                Services.SERVICE_MOUSE_LEFT_DOUBLE_CLICK -> {
                }
                Services.SERVICE_MOUSE_RIGHT_CLICK -> {
                }
                Services.SERVICE_MOUSE_MOVE_POINTER_BY -> {
                    if (value is Array<*>) {
                        val displacement = generateDisplacementJSONObject(value)
                        requestData.put(Services.VALUE, displacement)
                    } else {
                        throw IllegalArgumentException("Excepted Array of size 2")
                    }
                }
                Services.SERVICE_MOUSE_SCROLL_BY -> {
                    if (value is Float) {
                        requestData.put(Services.VALUE, value)
                    } else {
                        throw IllegalArgumentException("Expected  Float value to scroll")
                    }
                }
            }
        }

        return requestData
    }

    private fun generateKeyTypeValueJSONArray(keyList: ArrayList<*>): JSONArray {
        val keyStrokes = JSONArray()
        //Loop every entry and convert it to JSONArray
        for (key in keyList) {
            if (key is KeyTypeValuePair) {
                //create object for json array
                val x = JSONObject()
                x.put(Services.TYPE, key.type)
                x.put(Services.VALUE, key.value)

                //Add this key pair in the json array
                keyStrokes.put(x)
            } else {
                throw IllegalArgumentException("Expected KeyTypeValuePair object in the ArrayList")
            }
        }
        return keyStrokes
    }

    private fun generateDisplacementJSONObject(value: Array<*>): JSONObject {
        val displacement = JSONObject()
        if (value[0] is Float) {
            displacement.put("x", value[0] as Float)
        } else {
            throw IllegalArgumentException("Expected Array of Float value")
        }
        if (value[1] is Float) {
            displacement.put("y", value[1] as Float)
        } else {
            throw IllegalArgumentException("Expected Array of Float")
        }
        return displacement
    }
}