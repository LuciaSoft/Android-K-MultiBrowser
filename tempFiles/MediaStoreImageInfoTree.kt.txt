package com.luciasoft.browser

import com.luciasoft.collections.BinarySearchTree

// OPTIMIZED
internal class MediaStoreImageInfoTree()
    : BinarySearchTree<FileItem>(false, FileItemPathComparator)
{
    private fun getFileItem(path: String): FileItem?
    {
        return super.getSameAs(FileItem(path, null, null, null, null))
    }

    fun getImageId(path: String): Int?
    {
        val item = getFileItem(path) ?: return null
        return item.imageId
    }
}