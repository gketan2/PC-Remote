package com.k10.control.utils

object SpecialKeyCodes {

    val codes: ArrayList<KeyCodeValuePair> by lazy {
        arrayListOf(
            KeyCodeValuePair("esc","0"),
            KeyCodeValuePair("enter","1"),
            KeyCodeValuePair("shiftleft","2"),
            KeyCodeValuePair("shiftright","3"),
            KeyCodeValuePair("ctrlleft","4"),
            KeyCodeValuePair("ctrlright","5"),
            KeyCodeValuePair("tab","6"),
            KeyCodeValuePair("backspace","7"),
            KeyCodeValuePair("delete","8"),
            KeyCodeValuePair("pageup","9"),
            KeyCodeValuePair("pagedown","10"),
            KeyCodeValuePair("home","11"),
            KeyCodeValuePair("end","12"),
            KeyCodeValuePair("up","13"),
            KeyCodeValuePair("down","14"),
            KeyCodeValuePair("left","15"),
            KeyCodeValuePair("right","16"),
            KeyCodeValuePair("volumemute","17"),
            KeyCodeValuePair("volumeup","18"),
            KeyCodeValuePair("volumedown","19"),
            KeyCodeValuePair("pause","20"),
            KeyCodeValuePair("capslock","21"),
            KeyCodeValuePair("numlock","22"),
            KeyCodeValuePair("scrolllock","23"),
            KeyCodeValuePair("insert","24"),
            KeyCodeValuePair("printscreen","25"),
            KeyCodeValuePair("winleft","26"),
            KeyCodeValuePair("winright","27"),
            KeyCodeValuePair("command (macOs)","28"), //macOs
            KeyCodeValuePair("option (macOs)","29"), //macOs
//            KeyCodeValuePair("esc","30"),
            KeyCodeValuePair("f1","31"),
            KeyCodeValuePair("f2","32"),
            KeyCodeValuePair("f3","33"),
            KeyCodeValuePair("f4","34"),
            KeyCodeValuePair("f5","35"),
            KeyCodeValuePair("f6","36"),
            KeyCodeValuePair("f7","37"),
            KeyCodeValuePair("f8","38"),
            KeyCodeValuePair("f9","39"),
            KeyCodeValuePair("f10","40"),
            KeyCodeValuePair("f11","41"),
            KeyCodeValuePair("f12","42"),
        )
    }

    const val esc = "0"
    const val enter = "1"
    const val shiftleft = "2"
    const val shiftright = "3"
    const val ctrlleft = "4"
    const val ctrlright = "5"
    const val tab = "6"
    const val backspace = "7"
    const val delete = "8"
    const val pageup = "9"
    const val pagedown = "10"
    const val home = "11"
    const val end = "12"
    const val up = "13"
    const val down = "14"
    const val left = "15"
    const val right = "16"
    const val volumemute = "17"
    const val volumeup = "18"
    const val volumedown = "19"
    const val pause = "20"
    const val capslock = "21"
    const val numlock = "22"
    const val scrolllock = "23"
    const val insert = "24"
    const val printscreen = "25"
    const val winleft = "26"
    const val winright = "27"
    const val command = "28" // macOs
    const val option = "29" //  macOs
    //const val leaving for number match for f1, f2 = 30
    const val f1 = "31"
    const val f2 = "32"
    const val f3 = "33"
    const val f4 = "34"
    const val f5 = "35"
    const val f6 = "36"
    const val f7 = "37"
    const val f8 = "38"
    const val f9 = "39"
    const val f10 = "40"
    const val f11 = "41"
    const val f12 = "42"
}