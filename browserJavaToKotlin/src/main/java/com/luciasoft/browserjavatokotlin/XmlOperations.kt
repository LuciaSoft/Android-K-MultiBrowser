package com.luciasoft.browserjavatokotlin

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
        var el = doc.createElement("opt")
        doc.documentElement.appendChild(el)
        for (prop in getPropertyInfoTree(opt))
        {
            if (prop.name == opt::mAdvancedOptions.name) continue
            if (prop.name == opt::mThemeOptions.name) continue

            val element = doc.createElement(prop.name)
            element.setAttribute("type", getType("" + prop.type))
            element.setAttribute("value", "" + prop.value)
            el.appendChild(element)
        }
        el = doc.createElement("adv")
        doc.documentElement.appendChild(el)
        for (prop in getPropertyInfoTree(opt.mAdvancedOptions))
        {
            val element = doc.createElement(prop.name)
            element.setAttribute("type", getType("" + prop.type))
            element.setAttribute("value", "" + prop.value)
            el.appendChild(element)
        }
        el = doc.createElement("thm")
        doc.documentElement.appendChild(el)
        for (prop in getPropertyInfoTree(opt.mThemeOptions))
        {
            val element = doc.createElement(prop.name)
            element.setAttribute("type", getType("" + prop.type))
            element.setAttribute("value", "" + prop.value)
            el.appendChild(element)
        }
        return doc
    }

    private fun loadXml(doc: Document, options: Options): Array<Int>
    {
        val root = doc.documentElement

        val optEl = try { root.getElementsByTagName("opt").item(0) as Element } catch (ex: Exception) { null }
        val advEl = try { root.getElementsByTagName("adv").item(0) as Element } catch (ex: Exception) { null }
        var thmEl = try { root.getElementsByTagName("thm").item(0) as Element } catch (ex: Exception) { null }

        val elList = ArrayList<Element>()
        val treeList = ArrayList<PropertyInfoTree>()

        if (optEl != null)
        {
            elList.add(optEl)
            treeList.add(getPropertyInfoTree(options))
        }
        if (advEl != null)
        {
            elList.add(advEl)
            treeList.add(getPropertyInfoTree(options.mAdvancedOptions))
        }
        if (thmEl != null)
        {
            elList.add(thmEl)
            treeList.add(getPropertyInfoTree(options.mThemeOptions))
        }

        var count = 0
        var skipped = 0

        for (i in elList.indices)
        {
            val tree = treeList[i]
            for (info in tree)
            {
                if (info.prop !is KMutableProperty1<Any, *>) continue
                val element = try { elList[i].getElementsByTagName(info.name).item(0) as Element } catch (ex: Exception) { continue }
                val type = element.getAttribute("type").lowercase()
                val valStr = element.getAttribute("value")
                when (type)
                {
                    "int" -> {
                        val value = valStr.toInt()
                        val prop = info.prop as KMutableProperty1<Any, Int>
                        prop.set(info.instance, value)
                        count++
                    }

                    "int?" -> {
                        val value = if (valStr == "null") null else valStr.toInt()
                        val prop = info.prop as KMutableProperty1<Any, Int?>
                        prop.set(info.instance, value)
                        count++
                    }

                    "long" -> {
                        val value = valStr.toLong()
                        val prop = info.prop as KMutableProperty1<Any, Long>
                        prop.set(info.instance, value)
                        count++
                    }

                    "long?" -> {
                        val value = if (valStr == "null") null else valStr.toLong()
                        val prop = info.prop as KMutableProperty1<Any, Long?>
                        prop.set(info.instance, value)
                        count++
                    }

                    "boolean" -> {
                        val value = valStr.toBoolean()
                        val prop = info.prop as KMutableProperty1<Any, Boolean>
                        prop.set(info.instance, value)
                        count++
                    }

                    "boolean?" -> {
                        val value = if (valStr == "null") null else valStr.toBoolean()
                        val prop = info.prop as KMutableProperty1<Any, Boolean?>
                        prop.set(info.instance, value)
                        count++
                    }

                    "string" -> {
                        val value = valStr
                        val prop = info.prop as KMutableProperty1<Any, String>
                        prop.set(info.instance, value)
                        count++
                    }

                    "string?" -> {
                        val value = if (valStr == "null") null else valStr
                        val prop = info.prop as KMutableProperty1<Any, String?>
                        prop.set(info.instance, value)
                        count++
                    }

                    else -> {
                        skipped++
                        continue
                    }
                }
            }
        }

        return arrayOf(count, skipped)
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