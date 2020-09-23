package com.k10.control.helper

class Headers {
    companion object{
        private const val HEADER_SIZE = 10

        fun addHeader(data: String): String{
            val length = data.length
            var prefix = "$length"

            for(i in prefix.length until HEADER_SIZE)
                prefix += " "

            return prefix + data
        }

    }
}