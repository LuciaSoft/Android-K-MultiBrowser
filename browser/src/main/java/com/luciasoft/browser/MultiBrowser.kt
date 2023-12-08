package com.luciasoft.browser

import android.app.Application

class MultiBrowser : Application()
{
    override fun onCreate()
    {
        super.onCreate()
    }

    val DAT = Data()
    val OPT = Options()
    val ADV = AdvancedOptions()
    val THM = ThemeOptions()
}