package com.luciasoft.browser

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.text.format.Time
import android.widget.Toast
import androidx.compose.animation.core.estimateAnimationDurationMillis
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toLowerCase
import java.io.File
import java.io.FileOutputStream

fun copyFileFromAssets(assets: AssetManager, inputFilePath: String, outputFilePath: String)
{
    val inp = assets.open(inputFilePath)
    val fos = FileOutputStream(outputFilePath)
    val buffer = ByteArray(10240)
    while (true)
    {
        val count = inp.read(buffer, 0, 10240)
        if (count == -1) break
        fos.write(buffer, 0, count)
    }
    inp.close()
    fos.flush()
    fos.close()
}

fun getValidExts(exts: Array<String>? = null): Array<String>
{
    if (exts == null) return Array<String>(1) { "*" }

    val list = ArrayList<String>()

    for (ext in exts)
    {
        var ext = ext
        ext = ext.trim().lowercase()
        if (ext.isBlank()) continue
        if (ext.equals("*")) return Array<String>(1) { "*" }
        if (!ext.startsWith(".")) ext = ".$ext"
        list.add(ext)
    }

    if (list.isEmpty()) return Array<String>(1) { "*" }

    return list.toTypedArray()
}

fun getValidExts(exts: String? = null): Array<String>
{
    if (exts == null) return Array<String>(1) { "*" }

    val extArray: Array<String> = exts.split(",").toTypedArray()

    return getValidExts(extArray)
}

fun getDateString(dateMs: Long): String
{
    val t = Time()
    t.set(dateMs)
    return t.format("%m/%d/%Y")
}

fun getFileExtensionLowerCaseWithDot(fileNameOrPath: String): String
{
    val pos1 = fileNameOrPath.lastIndexOf('.')
    if (pos1 == -1) return ""
    val pos2 = fileNameOrPath.lastIndexOf(File.separator, pos1)
    if (pos2 > pos1) return ""

    return fileNameOrPath.substring(pos1).lowercase()
}

fun getParentDir(path: String): String
{
    var path = path
    if (path.equals(File.separator)) return ""
    if (path.endsWith(File.separator)) path = path.substring(0, path.length-1)
    val pos = path.lastIndexOf(File.separator)
    if (pos == -1) return ""
    if (pos == 0) return File.separator
    return path.substring(0, pos)
}

fun getShortName(path: String): String
{
    var path = path
    if (path.endsWith(File.separator)) path = path.substring(0, path.length-1)
    val pos = path.lastIndexOf(File.separator)
    if (pos == -1) return path
    return path.substring(pos+1)
}

fun trimStart(str: String, trim: String): String
{
    var str = str
    while (str.startsWith(trim)) str = str.substring(trim.length)
    return str
}

fun trimEmd(str: String, trim: String): String
{
    var str = str
    while (str.endsWith(trim)) str = str.substring(0, str.length - trim.length)
    return str
}

fun toastShort(context: Context, message: String)
{
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun toastLong(context: Context, message: String)
{
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun<T> arrayContains(array: Array<T>, item: T): Boolean
{
    return item in array
}

fun charCount(str: String, ch: Char): Int
{
    var count = 0

    for (c in str) if (c == ch) count++

    return count
}

@SuppressLint("DefaultLocale")
fun getFileSizeString(fileSize: Long): String
{
    val kb: Float = fileSize / 1024f
    val mb: Float = if (kb >= 1024) kb / 1024 else 0f
    val gb: Float = if (mb >= 1024) mb / 1024 else 0f

    if (gb >= 1) return String.format("%.2f", gb) + " Gb";
    if (mb >= 1) return String.format("%.2f", mb) + " Mb";
    if (kb >= 1) return String.format("%.2f", kb) + " Kb";

    if (fileSize == 1.toLong()) return "1 byte"
    return "$fileSize bytes"
}

fun filePassesFilter(exts: Array<String>, fileNameOrPath: String): Boolean
{
    var exts = exts
    if (exts.isEmpty()) return true
    exts = getValidExts(exts)
    val fileExt = getFileExtensionLowerCaseWithDot(fileNameOrPath)
    exts.forEach {
        if (it == "*" || it == fileExt) return true
    }
    return false
}