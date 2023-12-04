package com.luciasoft.collections

open class DirectoryItem(val path: String, var date: Long? = null, var info: String? = null)
{
    val name: String = getShortName(path)
}

class FileItem(path: String, date: Long? = null, var size: Long? = null, info: String? = null, var imageId: Int? = null) : DirectoryItem(path, date, info)

class FolderItem(path: String, date: Long? = null, info: String? = null) : DirectoryItem(path, date, info)

class MediaStoreImageInfoTree() : BinarySearchTree<FileItem>(false, FileItemPathComparator.getComparator())
{
    fun getFileItem(path: String): FileItem?
    {
        return super.getSameAs(FileItem(path))
    }

    fun getImageId(path: String): Int?
    {
        val item = getFileItem(path) ?: return null
        return item.imageId
    }
}

class FileItemPathComparator : Comparator<FileItem>
{
    override fun compare(fi1: FileItem?, fi2: FileItem?): Int
    {
        checkNotNull(fi1)
        checkNotNull(fi2)
        return fi1.path.compareTo(fi2.path, true)
    }

    companion object
    {
        private var comparator: FileItemPathComparator? = null

        fun getComparator(): FileItemPathComparator
        {
            if (comparator == null) comparator = FileItemPathComparator()
            return comparator as FileItemPathComparator
        }
    }
}