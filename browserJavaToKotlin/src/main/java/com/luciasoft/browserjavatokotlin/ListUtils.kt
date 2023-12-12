package com.luciasoft.browserjavatokotlin

import android.database.Cursor
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.luciasoft.browserjavatokotlin.Utils2.directoryIsReadable
import com.luciasoft.browserjavatokotlin.Utils.filePassesFilter
import com.luciasoft.browserjavatokotlin.Utils.getDateString
import com.luciasoft.browserjavatokotlin.Utils.getFileSizeString
import com.luciasoft.browserjavatokotlin.Utils.getValidExts
import java.io.File
import java.util.Collections
import java.util.Random

internal object ListUtils
{
    fun getImageInfos(act: MultiBrowserActivity): ArrayList<DirectoryItem>?
    {
        val sortOrder = act.sortOrder
        val sortOrderString: String = when (sortOrder)
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
        val cursor: Cursor =
            try
            {
                val cols = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
                with(CursorLoader(act))
                {
                    this.uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    this.projection = cols
                    this.selection = null
                    this.sortOrder = sortOrderString
                    this.loadInBackground()
                }
            }
            catch (ex: Exception)
            {
                null
            } ?: return null
        val list = ArrayList<DirectoryItem>()
        val exts = getValidExts(act.ADV.mediaStoreImageExts)
        for (i in 0 until cursor.count)
        {
            var imagePath = try
            {
                cursor.moveToPosition(i)
                val imagePathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                cursor.getString(imagePathCol)
            }
            catch (ex: Exception)
            {
                continue
            }
            val imageFile = try
            {
                File(imagePath)
            }
            catch (ex: Exception)
            {
                null
            }
            if (imageFile != null)
            {
                var ipath = try
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
            if (!filePassesFilter(exts, imagePath)) continue
            try
            {
                val imageIdCol = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val imageId = cursor.getInt(imageIdCol)
                list.add(FileItem(imagePath, imageId = imageId))
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
        if (act.DAT.mediaStoreImageInfoTree == null) act.DAT.mediaStoreImageInfoTree =
            MediaStoreImageInfoTree()
        else act.DAT.mediaStoreImageInfoTree!!.reset()
        for (info in infoArray) act.DAT.mediaStoreImageInfoTree!!.add(info as FileItem)
        return list
    }

    fun getDirectoryItemsFromFileSystem(
        act: MultiBrowserActivity,
        directory: String,
        exts: Array<String>
    ): ArrayList<DirectoryItem>?
    {
        var exts = exts
        val dirItemList = ArrayList<DirectoryItem>()
        val items = try
        {
            File(directory).listFiles()
        }
        catch (ex: Exception)
        {
            return if (directoryIsReadable(act, directory)) dirItemList else null
        }
        if (!act.ADV.showFilesInNormalView && !act.ADV.showFoldersInNormalView) return dirItemList
        exts = getValidExts(exts)
        for (item in items)
        {
            val path = try
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
            val isFile = try
            {
                item.isFile
            }
            catch (ex: Exception)
            {
                continue
            }
            val isDirectory = try
            {
                item.isDirectory
            }
            catch (ex: Exception)
            {
                continue
            }
            if (!isFile && !isDirectory) continue
            if (isFile &&
                (!act.ADV.showFilesInNormalView || act.OPT.browseMode == Options.BrowseMode.LoadFolders || act.OPT.browseMode == Options.BrowseMode.SaveFolders)) continue
            if (isDirectory && !act.ADV.showFoldersInNormalView) continue
            val isHidden = try
            {
                item.isHidden
            }
            catch (ex: Exception)
            {
                continue
            }
            if (isHidden && isFile && !act.OPT.showHiddenFiles) continue
            if (isHidden && isDirectory && !act.OPT.showHiddenFolders) continue
            val date = try
            {
                item.lastModified()
            }
            catch (ex: Exception)
            {
                null
            }
            var info = ""
            val showDate = isFile && act.ADV.showFileDatesInListView ||
                isDirectory && act.ADV.showFolderDatesInListView
            if (date != null && showDate)
            {
                info += getDateString(date) + ", "
            }
            if (isDirectory)
            {
                val subItemCount = try
                {
                    item.listFiles()?.size
                }
                catch (ex: Exception)
                {
                    null
                }
                if (subItemCount == null || !act.ADV.showFolderCountsInListView)
                {
                    info += "folder"
                }
                else
                {
                    info += "$subItemCount item"
                    if (subItemCount != 1) info += "s"
                }
                dirItemList.add(FolderItem(path, date, info))
            }
            else // if (isFile)
            {
                if (!filePassesFilter(exts, path)) continue
                var size = try
                {
                    item.length()
                }
                catch (ex: Exception)
                {
                    null
                }
                info += if (size == null || !act.ADV.showFileSizesInListView) "file"
                else getFileSizeString(size)
                val imageId =
                    if (act.OPT.showImagesWhileBrowsingNormal && act.DAT.mediaStoreImageInfoTree != null) act.DAT.mediaStoreImageInfoTree!!.getImageId(
                        path
                    )
                    else null
                dirItemList.add(FileItem(path, date, size, info, imageId))
            }
        }
        Collections.sort(dirItemList, DirItemComparator(act.sortOrder))
        return dirItemList
    }
}

internal class DirItemComparator(private var order: Options.SortOrder) : Comparator<DirectoryItem>
{
    override fun compare(item1: DirectoryItem, item2: DirectoryItem): Int
    {
        val item1isDir = item1 is FolderItem
        val item2isDir = item2 is FolderItem
        if (item1isDir && !item2isDir) return -1 else if (item2isDir && !item1isDir) return 1
        var path = order == Options.SortOrder.PathAscending ||
            order == Options.SortOrder.PathDescending
        var date = order == Options.SortOrder.DateAscending ||
            order == Options.SortOrder.DateDescending
        var size = order == Options.SortOrder.SizeAscending ||
            order == Options.SortOrder.SizeDescending
        var desc =
            order == Options.SortOrder.PathDescending || order == Options.SortOrder.DateDescending || order == Options.SortOrder.SizeDescending
        var compare = 0
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
        if (desc) compare = -compare
        return compare
    }
}