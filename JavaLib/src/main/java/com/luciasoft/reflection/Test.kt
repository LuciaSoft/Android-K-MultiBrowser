package com.luciasoft.reflection

import kotlin.reflect.KFunction0

class TestInfo(val fn: KFunction0<Unit>, val ex: Exception)
{
    val message = "${fn.name}: ${ex.message}"
}

fun KFunction0<Unit>.test(): TestInfo?
{
    try { this.invoke(); }
    catch (ex: Exception)
    { return TestInfo(this, ex) }
    return null
}

