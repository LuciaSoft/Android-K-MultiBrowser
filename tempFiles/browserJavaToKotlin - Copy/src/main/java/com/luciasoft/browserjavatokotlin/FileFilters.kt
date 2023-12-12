package com.luciasoft.browserjavatokotlin

import java.util.Arrays
import java.util.Locale

object FileFilters
{
    fun getFileFilterArrays(filterString: String): Pair<Array<Array<String>>, Array<String>>
    {
        val array = filterString.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(array.size % 2 == 0) { "The filter string must be divisible by 2." }
        val filters = Array(array.size / 2) { "" }
        val descriptions = Array(array.size / 2) { "" }
        for (i in array.indices step(2))
        {
            descriptions[i / 2] = array[i]
            filters[i / 2] = array[i + 1]
        }

        val filterArray: Array<Array<String>> = Array(filters.size) { emptyArray() }
        for (i in filters.indices)
        {
            val list = ArrayList<String>()
            val exts = filters[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (j in exts.indices)
            {
                var ext = exts[j].trim { it <= ' ' }.lowercase(Locale.getDefault())
                if (ext == "*" || ext == "*.*")
                {
                    list.clear()
                    list.add("*")
                    break
                }
                while (ext.startsWith("*")) ext = ext.substring(1)
                if (ext.isEmpty()) continue
                if (!ext.startsWith(".")) ext = ".$ext"
                list.add(ext)
            }
            filterArray[i] = list.toTypedArray()
        }
        val newDescrips = Arrays.copyOf(descriptions, descriptions.size)
        for (i in newDescrips.indices) newDescrips[i] = " " + newDescrips[i].trim { it <= ' ' } + " "

        return Pair(filterArray, newDescrips)
    }

    // UNUSED
    fun getFileFilterString(filterArray: Array<Array<String>>, descripArray: Array<String>): String
    {
        var result = ""
        for (i in filterArray.indices)
        {
            val filters = filterArray[i]
            val descrip = descripArray[i].trim { it <= ' ' }
            if (i > 0) result += "|"
            result += " $descrip |"
            for (j in filters.indices)
            {
                var filter = filters[j].trim { it <= ' ' }
                if (!filter.startsWith("*"))
                {
                    filter = if (!filter.startsWith(".")) "*.$filter" else "*$filter"
                }
                if (j > 0) result += ","
                result += filter
            }
        }
        return result
    }

}