package com.luciasoft.browser

// OPTIMIZED
internal class DirectoryItemComparator(private val order: SortOrder) : Comparator<DirectoryItem>
{
    override fun compare(item1: DirectoryItem, item2: DirectoryItem): Int
    {
        val item1isDir = item1 is FolderItem
        val item2isDir = item2 is FolderItem
        if (item1isDir && !item2isDir) return -1
        if (item2isDir && !item1isDir) return 1

        var path = order == SortOrder.PathAscending || order == SortOrder.PathDescending
        var date = order == SortOrder.DateAscending || order == SortOrder.DateDescending
        var size = order == SortOrder.SizeAscending || order == SortOrder.SizeDescending
        var desc = order == SortOrder.PathDescending || order == SortOrder.DateDescending || order == SortOrder.SizeDescending

        var compare = 0

        if (size)
        {
            if (item1isDir)
            {
                size = false
                path = true
                desc = false
            }
            else
            {
                val size1 = (item1 as FileItem).size
                val size2 = (item2 as FileItem).size
                if (size1 == null || size2 == null)
                {
                    size = false
                    path = true
                    desc = false
                }
                else
                {
                    val cmp = size1 - size2
                    compare = if (cmp < 0) -1 else if (cmp > 0) 1 else 0
                }
            }
        }

        if (date)
        {
            val date1 = item1.date
            val date2 = item2.date
            if (date1 == null || date2 == null)
            {
                date = false
                path = true
                desc = false
            }
            else
            {
                val cmp = date1 - date2
                compare = if (cmp < 0) -1 else if (cmp > 0) 1 else 0
            }
        }

        if (path) compare = item1.path.compareTo(item2.path, ignoreCase = true)

        if (desc) compare = -compare

        return compare
    }
}