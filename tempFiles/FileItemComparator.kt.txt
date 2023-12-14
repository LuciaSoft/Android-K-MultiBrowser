package com.luciasoft.browser

// OPTIMIZED
internal object FileItemPathComparator : Comparator<FileItem>
{
    override fun compare(fi1: FileItem?, fi2: FileItem?): Int
    {
        checkNotNull(fi1)
        checkNotNull(fi2)
        return fi1.path.compareTo(fi2.path, true)
    }
}