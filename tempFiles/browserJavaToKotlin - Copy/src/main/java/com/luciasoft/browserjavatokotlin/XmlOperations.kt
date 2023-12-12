package com.luciasoft.browserjavatokotlin

import com.luciasoft.collections.Mutability
import com.luciasoft.collections.PropertyInfo
import com.luciasoft.collections.PropertyInfo.Companion.getPropertyInfoTree
import com.luciasoft.xml.XmlUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import kotlin.reflect.KMutableProperty1

internal object XmlOperations
{
    fun saveXml(act: MultiBrowserActivity, filePath: String)
    {
        val doc = getXml(act)
        XmlUtils.saveXml(doc, filePath)
    }

    fun loadXml(act: MultiBrowserActivity, filePath: String): Array<Int>
    {
        val doc = XmlUtils.loadXmlFile(filePath)
        return loadXml(act, doc)
    }

    private fun getXml(act: MultiBrowserActivity): Document
    {
        val doc = XmlUtils.createXmlDocument("options")
        val root = doc.documentElement
        val heads = arrayOf("opt", "adv", "thm")
        val trees = arrayOf(
            getPropertyInfoTree(act.OPT, Mutability.Mutable),
            getPropertyInfoTree(act.ADV, Mutability.Mutable),
            getPropertyInfoTree(act.THM, Mutability.Mutable))
        
        for (i in heads.indices)
        {
            val head = heads[i]
            val tree = trees[i]
        
            for (info in tree)
            {
                val element = doc.createElement("$head.${info.name}")
                val type = getType("" + info.type)
                if (type.lowercase().startsWith("array")) continue
                element.setAttribute("type", type)
                element.setAttribute("value", "" + info.value)
                root.appendChild(element)
            }
        }
        return doc
    }

    private fun loadXml(act: MultiBrowserActivity, doc: Document): Array<Int>
    {
        val root = doc.documentElement
        val heads = arrayOf("opt", "adv", "thm")
        val trees = arrayOf(
            getPropertyInfoTree(act.OPT, Mutability.Mutable),
            getPropertyInfoTree(act.ADV, Mutability.Mutable),
            getPropertyInfoTree(act.THM, Mutability.Mutable))
        
        var count = 0
        var skipped = 0

        for (i in heads.indices)
        {
            val head = heads[i]
            val tree = trees[i]
            
            for (info in tree)
            {
                val element = try { root.getElementsByTagName("$head.${info.name}").item(0) as Element } catch (ex: Exception) { continue }
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
                val value = BrowseMode.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, BrowseMode>
                prop.set(info.instance, value)
                return true
            }

            "browserviewtype" -> {
                val value = BrowserViewType.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, BrowserViewType>
                prop.set(info.instance, value)
                return true
            }

            "sortorder" -> {
                val value = SortOrder.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, SortOrder>
                prop.set(info.instance, value)
                return true
            }

            "savefilebehavior" -> {
                val value = SaveFileBehavior.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, SaveFileBehavior>
                prop.set(info.instance, value)
                return true
            }

            "screenmode" -> {
                val value = ScreenMode.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, ScreenMode>
                prop.set(info.instance, value)
                return true
            }

            "fontmode" -> {
                val value = FontMode.valueOf(getNumFromVal(valStr))
                val prop = info.prop as KMutableProperty1<Any, FontMode>
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