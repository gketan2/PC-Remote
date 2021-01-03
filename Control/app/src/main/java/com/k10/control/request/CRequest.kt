package com.k10.control.request

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CRequest @Inject constructor() : Request() {
    override fun generateRequest(service: Int, subService: Int): String {
        val sb = StringBuilder()
        sb.append(service)
        sb.append('m')
        sb.append(subService)
        return sb.toString()
    }

    override fun generateRequest(service: Int, subService: Int, vararg data: Int): String {
        val sb = StringBuilder()
        sb.append(service)
        sb.append('m')
        sb.append(subService)
        sb.append('m')
        for (i in data) {
            sb.append(i)
            sb.append('m')
        }
        return sb.toString()
    }

    fun generateRequest(vararg data: Any): String {
        val sb = StringBuilder()
        for (i in data) {
            sb.append(i)
            sb.append(':')
        }
        return sb.toString()
    }
}