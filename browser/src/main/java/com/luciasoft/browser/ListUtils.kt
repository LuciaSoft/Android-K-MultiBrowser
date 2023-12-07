package com.luciasoft.browser

import android.database.Cursor
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.FileItem
import com.luciasoft.collections.FolderItem
import com.luciasoft.collections.MediaStoreImageInfoTree
import java.io.File
import java.util.Collections
import java.util.Random

internal object ListUtils
{
    fun getImageInfos(act: MultiBrowserActivity): ArrayList<DirectoryItem>?
    {
        val sortOrder: Options.SortOrder?
        sortOrder =
            if (act.OPT.browserViewType === Options.BrowserViewType.Gallery) act.OPT.galleryViewSortOrder else act.OPT.normalViewSortOrder
        val sortOrderString: String
        sortOrderString = when (sortOrder)
        {
            Options.SortOrder.PathAscending -> MediaStore.Images.Media.DATA
            Options.SortOrder.PathDescending -> MediaStore.Images.Media.DATA + " DESC"
            Options.SortOrder.DateAscending -> MediaStore.Images.Media.DATE_MODIFIED
            Options.SortOrder.DateDescending -> MediaStore.Images.Media.DATE_MODIFIED + " DESC"
            Options.SortOrder.SizeAscending -> MediaStore.Images.Media.SIZE
            Options.SortOrder.SizeDescending -> MediaStore.Images.Media.SIZE + " DESC"
            else -> MediaStore.Images.Media.DATA
        }

        //Cursor cursor = context.managedQuery( // DEPRICATED
        //MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cols, null,
        //null, sortOrder); // GET DATA IN CURSOR IN DESC ORDER
        var cursor: Cursor?
        try
        {
            val cols = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
            val loader = CursorLoader(act)
            loader.uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            loader.projection = cols
            loader.selection = null
            loader.sortOrder = sortOrderString
            cursor = loader.loadInBackground()
        }
        catch (ex: Exception)
        {
            cursor = null
        }
        if (cursor == null) return null
        val list = ArrayList<DirectoryItem>()
        val exts = getValidExts(AdvancedOptions.mediaStoreImageExts)
        for (i in 0 until cursor.count)
        {
            var imagePath: String?
            imagePath = try
            {
                cursor.moveToPosition(i)
                val imagePathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                cursor.getString(imagePathCol)
            }
            catch (ex: Exception)
            {
                continue
            }
            var imageFile: File?
            imageFile = try
            {
                File(imagePath)
            }
            catch (ex: Exception)
            {
                null
            }
            if (imageFile != null)
            {
                var ipath: String?
                ipath = try
                {
                    imageFile.canonicalPath
                }
                catch (ex2: Exception)
                {
                    try
                    {
                        imageFile.absolutePath
                    }
                    catch (ex3: Exception)
                    {
                        null
                    }
                }
                if (ipath != null) imagePath = ipath
            }
            if (!filePassesFilter(exts, imagePath!!)) continue
            try
            {
                val imageIdCol = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val imageId = cursor.getInt(imageIdCol)
                list.add(FileItem(imagePath, null, null, null, imageId))
            }
            catch (ex: Exception)
            {
            }
        }
        val infoArray = list.toTypedArray()
        val r = Random()
        var tmp: DirectoryItem
        for (i in infoArray.indices)
        {
            val pos = (r.nextDouble() * infoArray.size).toInt()
            tmp = infoArray[i]
            infoArray[i] = infoArray[pos]
            infoArray[pos] = tmp
        }
        if (Data.mMediaStoreImageInfoTree == null) Data.mMediaStoreImageInfoTree = MediaStoreImageInfoTree()
        else Data.mMediaStoreImageInfoTree!!.reset()
        for (info in infoArray) Data.mMediaStoreImageInfoTree!!.add(info as FileItem)
        return list
    }

    fun getDirectoryItemsFromFileSystem(
        act: MultiBrowserActivity,
        directory: String?,
        exts: Array<String>?
    ): ArrayList<DirectoryItem>?
    {
        var exts = exts
        val dirItemList = ArrayList<DirectoryItem>()
        val items: Array<File>?
        items = try
        {
            File(directory).listFiles()
        }
        catch (ex: Exception)
        {
            return null
        }
        if (items == null)
        {
            return if (directoryIsReadable(act, directory!!)) dirItemList else null
        }
        if (!AdvancedOptions.showFilesInNormalView && !AdvancedOptions.showFoldersInNormalView) return dirItemList
        exts = getValidExts(exts)
        for (item in items)
        {
            var path: String?
            path = try
            {
                item.canonicalPath
            }
            catch (ex: Exception)
            {
                try
                {
                    item.absolutePath
                }
                catch (ex2: Exception)
                {
                    continue
                }
            }
            var isFile: Boolean
            isFile = try
            {
                item.isFile
            }
            catch (ex: Exception)
            {
                continue
            }
            var isDirectory: Boolean
            isDirectory = try
            {
                item.isDirectory
            }
            catch (ex: Exception)
            {
                continue
            }
            if (!isFile && !isDirectory) continue
            if (isFile &&
                (!AdvancedOptions.showFilesInNormalView || act.OPT.browseMode === Options.BrowseMode.LoadFolders || act.OPT.browseMode === Options.BrowseMode.SaveFolders)) continue
            if (isDirectory && !AdvancedOptions.showFoldersInNormalView) continue
            var isHidden: Boolean
            isHidden = try
            {
                item.isHidden
            }
            catch (ex: Exception)
            {
                continue
            }
            if (isHidden && isFile && !act.OPT.showHiddenFiles) continue
            if (isHidden && isDirectory && !act.OPT.showHiddenFolders) continue
            var date: Long?
            date = try
            {
                item.lastModified()
            }
            catch (ex: Exception)
            {
                null
            }
            var info = ""
            val showDate = isFile && AdvancedOptions.showFileDatesInListView ||
                isDirectory && AdvancedOptions.showFolderDatesInListView
            if (date != null && showDate)
            {
                if (showDate) info += getDateString(date) + ", "
            }
            if (isDirectory)
            {
                var subItemCount: Int?
                subItemCount = try
                {
                    item.listFiles().size
                }
                catch (ex: Exception)
                {
                    null
                }
                if (subItemCount == null || !AdvancedOptions.showFolderCountsInListView)
                {
                    info += "folder"
                }
                else
                {
                    info += "$subItemCount item"
                    if (subItemCount != 1) info += "s"
                }
                dirItemList.add(FolderItem(path!!, date, info))
            }
            else if (isFile)
            {
                if (!filePassesFilter(exts, path!!)) continue
                var size: Long?
                size = try
                {
                    item.length()
                }
                catch (ex: Exception)
                {
                    null
                }
                info += if (size == null || !AdvancedOptions.showFileSizesInListView) "file"
                else getFileSizeString(
                    size
                )
                var imageId: Int? = null
                if (act.OPT.showImagesWhileBrowsingNormal && Data.mMediaStoreImageInfoTree != null)
                {
                    imageId = Data.mMediaStoreImageInfoTree!!.getImageId(path)
                }
                dirItemList.add(FileItem(path, date, size, info, imageId))
            }
        }
        Collections.sort(dirItemList, DirItemComparator.getComparator(act))
        return dirItemList
    }
}

internal class DirItemComparator(var act: MultiBrowserActivity) : Comparator<DirectoryItem>
{
    override fun compare(item1: DirectoryItem, item2: DirectoryItem): Int
    {
        val item1isDir = item1 is FolderItem
        val item2isDir = item2 is FolderItem
        if (item1isDir && !item2isDir) return -1 else if (item2isDir && !item1isDir) return 1
        var compare: Int? = null
        val sortOrder: Options.SortOrder?
        sortOrder =
            if (act.OPT.browserViewType === Options.BrowserViewType.Gallery) act.OPT.galleryViewSortOrder else act.OPT.normalViewSortOrder
        var path = sortOrder === Options.SortOrder.PathAscending ||
            sortOrder === Options.SortOrder.PathDescending
        var date = sortOrder === Options.SortOrder.DateAscending ||
            sortOrder === Options.SortOrder.DateDescending
        var size = sortOrder === Options.SortOrder.SizeAscending ||
            sortOrder === Options.SortOrder.SizeDescending
        var desc =
            sortOrder === Options.SortOrder.PathDescending || sortOrder === Options.SortOrder.DateDescending || sortOrder === Options.SortOrder.SizeDescending
        if (size && item1isDir)
        {
            size = false
            path = true
            desc = false
        }
        if (size)
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
        if (date)
        {
            val date1 = (item1 as FileItem).date
            val date2 = (item2 as FileItem).date
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
        if (path)
        {
            compare = item1.path.compareTo(item2.path, ignoreCase = true)
        }
        if (desc) compare = -compare!!
        return compare!!
    }

    companion object
    {
        var comparator: DirItemComparator? = null
        fun getComparator(act: MultiBrowserActivity): DirItemComparator?
        {
            if (comparator == null) comparator = DirItemComparator(act)
            return comparator
        }
    }
}