package com.luciasoft.browserjavatokotlin

import Mutability
import PropertyInfo
import PropertyInfo.Companion.getPropertyInfoTree
import PropertyInfoTree
import com.luciasoft.xml.XmlUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import kotlin.reflect.KMutableProperty1

internal object XmlOperations
{
    fun saveXml(filePath: String, options: Options)
    {
        val doc = getXml(options)
        XmlUtils.saveXml(doc, filePath)
    }

    fun loadXml(filePath: String, options: Options): Array<Int>
    {
        val doc = XmlUtils.loadXmlFile(filePath)
        return loadXml(doc, options)
    }

    private fun getXml(opt: Options): Document
    {
        val doc = XmlUtils.createXmlDocument("options")
        var root = doc.documentElement
        for (prop in getPropertyInfoTree(opt, Mutability.Mutable))
        {
            if (prop.name == opt::mAdvancedOptions.name) continue
            if (prop.name == opt::mThemeOptions.name) continue

            val element = doc.createElement("opt.${prop.name}")
            val type = getType("" + prop.type)
            if (type.lowercase().startsWith("array")) continue
            element.setAttribute("type", type)
            element.setAttribute("value", "" + prop.value)
            root.appendChild(element)
        }
        for (prop in getPropertyInfoTree(opt.mAdvancedOptions, Mutability.Mutable))
        {
            val element = doc.createElement("adv.${prop.name}")
            val type = getType("" + prop.type)
            if (type.lowercase().startsWith("array")) continue
            element.setAttribute("type", type)
            element.setAttribute("value", "" + prop.value)
            root.appendChild(element)
        }
        for (prop in getPropertyInfoTree(opt.mThemeOptions, Mutability.Mutable))
        {
            val element = doc.createElement("thm.${prop.name}")
            val type = getType("" + prop.type)
            if (type.lowercase().startsWith("array")) continue
            element.setAttribute("type", type)
            element.setAttribute("value", "" + prop.value)
            root.appendChild(element)
        }
        return doc
    }

    private fun loadXml(doc: Document, options: Options): Array<Int>
    {
        val root = doc.documentElement
        val heads = arrayOf("opt", "adv", "thm")
        val trees = arrayOf(
            getPropertyInfoTree(options, Mutability.Mutable),
            getPropertyInfoTree(options.mAdvancedOptions, Mutability.Mutable),
            getPropertyInfoTree(options.mThemeOptions, Mutability.Mutable))
        
        var count = 0
        var skipped = 0

        for (i in heads.indices)
        {
            val head = heads[i]
            val tree = trees[i]
            for (info in tree)
            {
                val element = try { elList[i].getElementsByTagName("$head.${info.name}").item(0) as Element } catch (ex: Exception) { continue }
                val type = element.getAttribute("type").lowercase()
                val valStr = element.getAttribute("value")

                if (setProperty(type, valStr, info)) count++
                else skipped++
            }
        }

        return arrayOf(count, skipped)
    }

    private fun setProperty(type: String, valStr: String, info: PropertyInfo): Boolean
    {
        when (type)
        {
            "char" -> {
                val value = valStr[0]
                val prop = info.prop as KMutableProperty1<Any, Char>
                prop.set(info.instance, value)
                return true
            }

            "char?" -> {
                val value = if (valStr == "null") null else valStr[0]
                val prop = info.prop as KMutableProperty1<Any, Char?>
                prop.set(info.instance, value)
                return true
            }

            "byte" -> {
                val value = valStr.toByte()
                val prop = info.prop as KMutableProperty1<Any, Byte>
                prop.set(info.instance, value)
                return true
            }

            "byte?" -> {
                val value = if (valStr == "null") null else valStr.toByte()
                val prop = info.prop as KMutableProperty1<Any, Byte?>
                prop.set(info.instance, value)
                return true
            }

            "short" -> {
                val value = valStr.toShort()
                val prop = info.prop as KMutableProperty1<Any, Short>
                prop.set(info.instance, value)
                return true
            }

            "short?" -> {
                val value = if (valStr == "null") null else valStr.toShort()
                val prop = info.prop as KMutableProperty1<Any, Short?>
                prop.set(info.instance, value)
                return true
            }

            "int" -> {
                val value = valStr.toInt()
                val prop = info.prop as KMutableProperty1<Any, Int>
                prop.set(info.instance, value)
                return true
            }

            "int?" -> {
                val value = if (valStr == "null") null else valStr.toInt()
                val prop = info.prop as KMutableProperty1<Any, Int?>
                prop.set(info.instance, value)
                return true
            }

            "long" -> {
                val value = valStr.toLong()
                val prop = info.prop as KMutableProperty1<Any, Long>
                prop.set(info.instance, value)
                return true
            }

            "long?" -> {
                val value = if (valStr == "null") null else valStr.toLong()
                val prop = info.prop as KMutableProperty1<Any, Long?>
                prop.set(info.instance, value)
                return true
            }

            "float" -> {
                val value = valStr.toFloat()
                val prop = info.prop as KMutableProperty1<Any, Float>
                prop.set(info.instance, value)
                return true
            }

            "float?" -> {
                val value = if (valStr == "null") null else valStr.toFloat()
                val prop = info.prop as KMutableProperty1<Any, Float?>
                prop.set(info.instance, value)
                return true
            }

            "double" -> {
                val value = valStr.toDouble()
                val prop = info.prop as KMutableProperty1<Any, Double>
                prop.set(info.instance, value)
                return true
            }

            "double?" -> {
                val value = if (valStr == "null") null else valStr.toDouble()
                val prop = info.prop as KMutableProperty1<Any, Double?>
                prop.set(info.instance, value)
                return true
            }

            "boolean" -> {
                val value = valStr.toBoolean()
                val prop = info.prop as KMutableProperty1<Any, Boolean>
                prop.set(info.instance, value)
                return true
            }

            "boolean?" -> {
                val value = if (valStr == "null") null else valStr.toBoolean()
                val prop = info.prop as KMutableProperty1<Any, Boolean?>
                prop.set(info.instance, value)
                return true
            }

            "string" -> {
                val value = valStr
                val prop = info.prop as KMutableProperty1<Any, String>
                prop.set(info.instance, value)
                return true
            }

            "string?" -> {
                val value = if (valStr == "null") null else valStr
                val prop = info.prop as KMutableProperty1<Any, String?>
                prop.set(info.instance, value)
                return true
            }

            "browsemode" -> {
                val value = Options.BrowseMode.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, Options.BrowseMode>
                prop.set(info.instance, value)
                return true
            }

            "browserviewtype" -> {
                val value = Options.BrowserViewType.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, Options.BrowserViewType>
                prop.set(info.instance, value)
                return true
            }

            "sortorder" -> {
                val value = Options.SortOrder.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, Options.SortOrder>
                prop.set(info.instance, value)
                return true
            }

            "savefilebehavior" -> {
                val value = Options.SaveFileBehavior.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, Options.SaveFileBehavior>
                prop.set(info.instance, value)
                return true
            }

            "screenmode" -> {
                val value = Options.ScreenMode.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, Options.ScreenMode>
                prop.set(info.instance, value)
                return true
            }

            "fontmode" -> {
                val value = Options.FontMode.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, Options.FontMode>
                prop.set(info.instance, value)
                return true
            }

            else -> return false
        }
    }

    private fun getNumFromVal(valStr: String): Int
    {
        val pos = valStr.indexOf(':');
        if (pos == -1) throw Exception()
        return valStr.substring(0, pos).toInt()
    }

    private fun getType(type: String): String
    {
        val type = type
            .replace("kotlin.", "")
            .replace("<", "[")
            .replace(">", "]")
        val pos = type.lastIndexOf('.')
        if (pos == -1 || pos == type.length - 1) return type
        return type.substring(pos + 1)
    }

}