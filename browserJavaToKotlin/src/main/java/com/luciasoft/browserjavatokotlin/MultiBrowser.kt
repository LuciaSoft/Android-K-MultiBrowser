package com.luciasoft.browserjavatokotlin

import android.app.Application

class MultiBrowser
    : Application()
{
    override fun onCreate()
    {
        super.onCreate()
    }

    var OPT = Options()
    var ADV = AdvancedOptions()
    var THM = ThemeOptions()
    var DAT = Data(this)
}