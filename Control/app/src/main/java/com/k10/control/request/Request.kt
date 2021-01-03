package com.k10.control.request

abstract class Request {
    abstract fun generateRequest(service: Int, subService: Int): String
    abstract fun generateRequest(service: Int, subService: Int, vararg data: Int): String
}