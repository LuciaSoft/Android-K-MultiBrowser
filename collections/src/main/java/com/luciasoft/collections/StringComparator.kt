package com.luciasoft.collections

class StringComparator(private val ignoreCase: Boolean, private val special: Boolean) : Comparator<String>
{
    override fun compare(s1: String?, s2: String?): Int
    {
        checkNotNull(s1)
        checkNotNull(s2)

        if (ignoreCase || special)
        {
            val compare = s1.compareTo(s2, true)
            if (ignoreCase || compare != 0) return compare
        }

        return s1.compareTo(s2, false)
    }
}