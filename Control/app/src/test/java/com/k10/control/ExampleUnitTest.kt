package com.k10.control

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun stringManipulation(){
        var a = "25"
        for(i in a.length until 10){
            a += "k"
        }
        assertEquals("25kkkkkkkk", a)
    }
}