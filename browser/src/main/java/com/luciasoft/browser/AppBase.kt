package com.luciasoft.browser

import android.app.Application

open class AppBase: Application()
{
    open fun initOpts(opt: Options, adv: AdvancedOptions, thm: ThemeOptions) { }
}