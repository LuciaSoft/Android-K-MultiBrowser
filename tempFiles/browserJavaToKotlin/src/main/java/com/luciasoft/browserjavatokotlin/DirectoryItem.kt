package com.luciasoft.browserjavatokotlin

import com.luciasoft.utils.getShortName

abstract class DirectoryItem(val path: String, var date: Long? = null, var info: String? = null)
{
    val name = getShortName(path)
}

class FileItem(path: String, date: Long? = null, var size: Long? = null, info: String? = null, var imageId: Int? = null)
    : DirectoryItem(path, date, info)

class FolderItem(path: String, date: Long? = null, info: String? = null)
    : DirectoryItem(path, date, info)

object FileItemPathComparator : Comparator<FileItem>
{
    override fun compare(fi1: FileItem?, fi2: FileItem?): Int
    {
        checkNotNull(fi1)
        checkNotNull(fi2)
        return fi1.path.compareTo(fi2.path, true)
    }
}