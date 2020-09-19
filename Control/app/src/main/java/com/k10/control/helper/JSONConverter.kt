package com.k10.control.helper

import org.json.JSONObject

class JSONConverter {

    companion object{
        fun stringToJSON(jsonString: String): JSONObject{
            return JSONObject(jsonString)
        }

        fun JSONtoString(jsonObject: JSONObject): String{
            return jsonObject.toString()
        }
    }

}