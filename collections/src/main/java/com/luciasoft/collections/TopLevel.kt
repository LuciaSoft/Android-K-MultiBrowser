package com.luciasoft.collections

import java.io.File
import kotlin.random.Random

fun<T> randomize(data: Collection<T>): ArrayList<T>
{
    val r = Random(0)
    val len = data.size
    val list = ArrayList(data)
    for (i in 0..len)
    {
        val pos = (r.nextDouble() * len).toInt()
        val tmp = list[i];
        list[i] = list[pos]
        list[pos] = tmp
    }
    return list
}

fun getShortName(path: String): String
{
    var p = path
    if (p.endsWith(File.separator)) p = p.substring(0, p.length-1)
    val pos = p.lastIndexOf(File.separator);
    if (pos == -1) return p;
    return p.substring(pos+1)
}