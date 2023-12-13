package com.luciasoft.browser

import com.luciasoft.utils.getShortName

// OPTIMIZED
abstract class DirectoryItem(val path: String, var date: Long?, var info: String?)
{
    val name = getShortName(path)
}

// OPTIMIZED
class FileItem(path: String, date: Long?, var size: Long?, info: String?, var imageId: Int?)
    : DirectoryItem(path, date, info)

// OPTIMIZED
class FolderItem(path: String, date: Long?, info: String?)
    : DirectoryItem(path, date, info)

