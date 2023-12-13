package com.luciasoft.browserjavatokotlin

import com.luciasoft.collections.BinarySearchTree

class MediaStoreImageInfoTree()
    : BinarySearchTree<FileItem>(false, FileItemPathComparator)
{
    private fun getFileItem(path: String): FileItem?
    {
        return super.getSameAs(FileItem(path))
    }

    fun getImageId(path: String): Int?
    {
        val item = getFileItem(path) ?: return null
        return item.imageId
    }
}