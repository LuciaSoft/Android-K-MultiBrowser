package com.luciasoft.browserjavatokotlin

import com.luciasoft.browserjavatokotlin.Utils.toastLong
import kotlin.reflect.KFunction0

fun test(fn: KFunction0<Unit>, act: MultiBrowserActivity)
{
    try { fn.invoke(); }
    catch (ex: Exception) { toastLong(act, "FUNCTION=" + fn.name + " ERROR=" + ex.message) }
}