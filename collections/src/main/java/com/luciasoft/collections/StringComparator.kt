package com.luciasoft.collections

class StringComparator(private val ignoreCase: Boolean, private val special: Boolean) : Comparator<String>
{
    override fun compare(s1: String?, s2: String?): Int
    {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1
        if (s2 == null) return 1

        if (special || ignoreCase)
        {
            val compare = s1.compareTo(s2, true)
            if (ignoreCase || compare != 0) return compare
        }

        return s1.compareTo(s2, false)
    }
}