package com.luciasoft.browserjavatokotlin

import com.luciasoft.collections.Mutability
import com.luciasoft.collections.PropertyInfo
import com.luciasoft.collections.PropertyInfo.Companion.getPropertyInfoTree
import com.luciasoft.xml.XmlUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

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
                val typeStr = getTypeStr(info.type)
                val typeStrLower = typeStr.lowercase()
                if (typeStrLower.startsWith("array")) continue
                val element = doc.createElement("$head.${info.name}")
                element.setAttribute("type", typeStr)
                val value = info.value
                if (value == null)
                {
                    element.setAttribute("is_null", "" + true)
                }
                else
                {
                    val valStr = when (typeStrLower)
                    {
                        "char", "char?" -> "" + (value as Char).code
                        else -> "" + value
                    }
                    element.setAttribute("value", valStr)
                }
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
                val typeStr = element.getAttribute("type").lowercase()
                val valStr = if (element.hasAttribute("value")) element.getAttribute("value") else null
                if (valStr == null)
                {
                    if (!element.hasAttribute("is_null")
                        || element.getAttribute("is_null") != "" + true) throw Exception()
                }
                if (setProperty(typeStr, valStr, info)) count++
                else skipped++
            }
        }

        return arrayOf(count, skipped)
    }

    private fun setProperty(type: String, valStr: String?, info: PropertyInfo): Boolean
    {
        when (type)
        {
            "char" -> {
                val prop = info.prop as KMutableProperty1<Any, Char>
                prop.set(info.instance, valStr!!.toInt().toChar())
                return true
            }

            "char?" -> {
                val prop = info.prop as KMutableProperty1<Any, Char?>
                prop.set(info.instance, valStr?.toInt()?.toChar())
                return true
            }

            "byte" -> {
                val prop = info.prop as KMutableProperty1<Any, Byte>
                prop.set(info.instance, valStr!!.toByte())
                return true
            }

            "byte?" -> {
                val prop = info.prop as KMutableProperty1<Any, Byte?>
                prop.set(info.instance, valStr?.toByte())
                return true
            }

            "short" -> {
                val prop = info.prop as KMutableProperty1<Any, Short>
                prop.set(info.instance, valStr!!.toShort())
                return true
            }

            "short?" -> {
                val prop = info.prop as KMutableProperty1<Any, Short?>
                prop.set(info.instance, valStr?.toShort())
                return true
            }

            "int" -> {
                val prop = info.prop as KMutableProperty1<Any, Int>
                prop.set(info.instance, valStr!!.toInt())
                return true
            }

            "int?" -> {
                val prop = info.prop as KMutableProperty1<Any, Int?>
                prop.set(info.instance, valStr?.toInt())
                return true
            }

            "long" -> {
                val prop = info.prop as KMutableProperty1<Any, Long>
                prop.set(info.instance, valStr!!.toLong())
                return true
            }

            "long?" -> {
                val prop = info.prop as KMutableProperty1<Any, Long?>
                prop.set(info.instance, valStr?.toLong())
                return true
            }

            "float" -> {
                val prop = info.prop as KMutableProperty1<Any, Float>
                prop.set(info.instance, valStr!!.toFloat())
                return true
            }

            "float?" -> {
                val prop = info.prop as KMutableProperty1<Any, Float?>
                prop.set(info.instance, valStr?.toFloat())
                return true
            }

            "double" -> {
                val prop = info.prop as KMutableProperty1<Any, Double>
                prop.set(info.instance, valStr!!.toDouble())
                return true
            }

            "double?" -> {
                val prop = info.prop as KMutableProperty1<Any, Double?>
                prop.set(info.instance, valStr?.toDouble())
                return true
            }

            "boolean" -> {
                val prop = info.prop as KMutableProperty1<Any, Boolean>
                prop.set(info.instance, valStr!!.toBoolean())
                return true
            }

            "boolean?" -> {
                val prop = info.prop as KMutableProperty1<Any, Boolean?>
                prop.set(info.instance, valStr?.toBoolean())
                return true
            }

            "string" -> {
                val prop = info.prop as KMutableProperty1<Any, String>
                prop.set(info.instance, valStr!!)
                return true
            }

            "string?" -> {
                val prop = info.prop as KMutableProperty1<Any, String?>
                prop.set(info.instance, valStr)
                return true
            }

            "browsemode" -> {
                val prop = info.prop as KMutableProperty1<Any, BrowseMode>
                val value = BrowseMode.valueOf(getNumFromVal(valStr!!))
                prop.set(info.instance, value)
                return true
            }

            "browserviewtype" -> {
                val prop = info.prop as KMutableProperty1<Any, BrowserViewType>
                val value = BrowserViewType.valueOf(getNumFromVal(valStr!!))
                prop.set(info.instance, value)
                return true
            }

            "sortorder" -> {
                val prop = info.prop as KMutableProperty1<Any, SortOrder>
                val value = SortOrder.valueOf(getNumFromVal(valStr!!))
                prop.set(info.instance, value)
                return true
            }

            "savefilebehavior" -> {
                val prop = info.prop as KMutableProperty1<Any, SaveFileBehavior>
                val value = SaveFileBehavior.valueOf(getNumFromVal(valStr!!))
                prop.set(info.instance, value)
                return true
            }

            "screenmode" -> {
                val prop = info.prop as KMutableProperty1<Any, ScreenMode>
                val value = ScreenMode.valueOf(getNumFromVal(valStr!!))
                prop.set(info.instance, value)
                return true
            }

            "fontmode" -> {
                val prop = info.prop as KMutableProperty1<Any, FontMode>
                val value = FontMode.valueOf(getNumFromVal(valStr!!))
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

    private fun getTypeStr(type: KType): String
    {
        var typeStr = "" + type.jvmErasure.simpleName
        if (type.isMarkedNullable) typeStr += "?"
        typeStr = typeStr.replace('<', '[').replace('>', ']')
        return typeStr
    }

}