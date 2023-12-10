package com.luciasoft.browserjavatokotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.text.format.Time
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

internal object Utils
{
    @Throws(IOException::class)
    fun copyFileFromAssets(assets: AssetManager, inputFilePath: String, outputFilePath: String)
    {
        val op = assets.open(inputFilePath)
        val fos = FileOutputStream(outputFilePath)
        val buffer = ByteArray(10240)
        while (true)
        {
            val count = op.read(buffer, 0, 10240)
            if (count == -1) break
            fos.write(buffer, 0, count)
        }
        op.close()
        fos.flush()
        fos.close()
    }

    @JvmStatic
    fun getValidExts(exts: Array<String>?): Array<String>
    {
        if (exts == null) return arrayOf("*")
        val list = ArrayList<String>()
        for (ext in exts)
        {
            var ext = ext
            ext = ext.trim { it <= ' ' }.lowercase(Locale.getDefault())
            if (ext.isEmpty()) continue
            if (ext == "*") return arrayOf("*")
            if (!ext.startsWith(".")) ext = ".$ext"
            list.add(ext)
        }
        return if (list.size == 0) arrayOf("*") else list.toTypedArray()
    }

    @JvmStatic
    fun getValidExts(exts: String?): Array<String>
    {
        if (exts == null) return arrayOf("*")
        val extArray = exts.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return getValidExts(extArray)
    }

    @JvmStatic
    fun getDateString(dateMs: Long): String
    {
        val t = Time()
        t.set(dateMs)
        return t.format("%m/%d/%Y")
    }

    @JvmStatic
    fun getFileExtensionLowerCaseWithDot(fileNameOrPath: String): String
    {
        val pos1 = fileNameOrPath.lastIndexOf('.')
        if (pos1 == -1) return ""
        val pos2 = fileNameOrPath.lastIndexOf(File.separatorChar, pos1)
        return if (pos2 > pos1) ""
        else fileNameOrPath.substring(pos1).lowercase(Locale.getDefault())
    }

    @JvmStatic
    fun getParentDir(path: String): String
    {
        var path = path
        if (path == File.separator) return ""
        if (path.endsWith(File.separatorChar)) path = path.substring(0, path.length - 1)
        val pos = path.lastIndexOf(File.separatorChar)
        if (pos == -1) return ""
        return if (pos == 0) File.separator else path.substring(0, pos)
    }

    @JvmStatic
    fun getShortName(path: String): String
    {
        var path = path
        if (path.endsWith(File.separatorChar)) path = path.substring(0, path.length - 1)
        val pos = path.lastIndexOf(File.separatorChar)
        return if (pos == -1) path else path.substring(pos + 1)
    }

    fun trimStart(str: String, trim: String): String
    {
        var str = str
        while (str.startsWith(trim))
        {
            str = str.substring(trim.length)
        }
        return str
    }

    fun trimEnd(str: String, trim: String): String
    {
        var str = str
        while (str.endsWith(trim))
        {
            str = str.substring(0, str.length - trim.length)
        }
        return str
    }

    fun toastShort(context: Context, message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun toastLong(context: Context, message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    @JvmStatic
    fun <T> arrayContains(array: Array<T>, item: T): Boolean
    {
        return item in array
    }

    fun charCount(str: String, ch: Char): Int
    {
        var count = 0
        for (c in str)
        {
            if (c == ch) count++
        }
        return count
    }

    @JvmStatic
    @SuppressLint("DefaultLocale")
    fun getFileSizeString(fileSize: Long): String
    {
        val kb = fileSize / 1024f
        val mb: Float = if (kb >= 1024) kb / 1024 else 0f
        val gb: Float = if (mb >= 1024) mb / 1024 else 0f
        if (gb >= 1) return String.format("%.2f", gb) + " Gb"
        if (mb >= 1) return String.format("%.2f", mb) + " Mb"
        if (kb >= 1) return String.format("%.2f", kb) + " Kb"
        return if (fileSize == 1L) "1 byte" else "$fileSize bytes"
    }

    @JvmStatic
    fun filePassesFilter(exts: Array<String>?, fileNameOrPath: String): Boolean
    {
        var exts = exts
        if (exts.isNullOrEmpty()) return true
        exts = getValidExts(exts)
        val fileExt = getFileExtensionLowerCaseWithDot(fileNameOrPath)
        for (ext in exts)
        {
            if (ext == "*" || ext == fileExt) return true
        }
        return false
    }
}