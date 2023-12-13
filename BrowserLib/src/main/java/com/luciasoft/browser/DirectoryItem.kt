package com.luciasoft.browser

import com.luciasoft.utils.getShortName

// OPTIMIZED
internal abstract class DirectoryItem(val path: String, var date: Long?, var info: String?)
{
    val name = getShortName(path)
}

// OPTIMIZED
internal class FileItem(path: String, date: Long?, var size: Long?, info: String?, var imageId: Int?)
    : DirectoryItem(path, date, info)

// OPTIMIZED
internal class FolderItem(path: String, date: Long?, info: String?)
    : DirectoryItem(path, date, info)

