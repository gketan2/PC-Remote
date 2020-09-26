package com.k10.control.request

import com.k10.control.utils.KeyTypeValuePair
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Request @Inject constructor() {

    /**
     * Construct a JSON object of the service, sub_service,
     * value according to the type of service and sub_service.
     */
    fun generateRequestJSON(service: Int, subServiceType: Int, value: Any): JSONObject {
        val requestData = JSONObject()
        requestData.put(Services.SERVICE, service)
        requestData.put(Services.SUB_SERVICE, subServiceType)

        if (service == Services.SERVICE_KEYBOARD) {
            when (subServiceType) {
                Services.SERVICE_KEYBOARD_TYPE -> {
                    if (value is String) {
                        requestData.put(Services.VALUE, value)
                    } else {
                        throw IllegalArgumentException("Expected a String")
                    }
                }
                Services.SERVICE_KEYBOARD_PRESS_KEYS -> {
                    if (value is ArrayList<*>) {
                        val pairArray = generateKeyTypeValueJSONArray(value)
                        //Add the list of keys in return JSON-Object
                        requestData.put(Services.KEY_LIST, pairArray)
                    } else {
                        throw IllegalArgumentException("Expected ArrayList<KeyType>")
                    }
                }
                Services.SERVICE_KEYBOARD_PRESS_HOT_KEYS -> {
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
            }
        }

//        println(requestData.toString())
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
}