package com.luciasoft.browser

import java.io.File
import java.util.Collections

// OPTIMIZED
internal object GetDirectoryItems
{
    fun getDirectoryItemsFromFileSystem(
        act: MultiBrowserActivity,
        directory: String,
        exts: Array<String>
    ): ArrayList<DirectoryItem>?
    {
        var exts = exts
        val dirItemList = ArrayList<DirectoryItem>()
        val items = try { File(directory).listFiles() }
        catch (ex: Exception)
        { return if (Utils2.directoryIsReadable(act, directory)) dirItemList else null }

        if (!act.ADV.showFilesInNormalView && !act.ADV.showFoldersInNormalView) return dirItemList

        exts = Utils.getValidExts(exts)
        for (item in items)
        {
            val path = try { item.canonicalPath }
            catch (ex: Exception) { try { item.absolutePath }
            catch (ex2: Exception) { continue } }

            val isFile = try { item.isFile } catch (ex: Exception) { continue }
            val isDirectory = try { item.isDirectory } catch (ex: Exception) { continue }

            if (!isFile && !isDirectory) continue

            if (isFile && (!act.ADV.showFilesInNormalView ||
                    act.OPT.browseMode == BrowseMode.LoadFolders ||
                    act.OPT.browseMode == BrowseMode.SaveFolders)) continue
            if (isDirectory && !act.ADV.showFoldersInNormalView) continue

            val isHidden = try { item.isHidden } catch (ex: Exception) { continue }

            if (isHidden && isFile && !act.OPT.showHiddenFiles) continue
            if (isHidden && isDirectory && !act.OPT.showHiddenFolders) continue

            val date = try { item.lastModified() } catch (ex: Exception) { null }

            var info = ""

            val showDate = isFile && act.ADV.showFileDatesInListView ||
                isDirectory && act.ADV.showFolderDatesInListView

            if (date != null && showDate) info += Utils.getDateString(date) + ", "

            if (isDirectory)
            {
                val subItemCount = try { item.listFiles()?.size } catch (ex: Exception) { null }

                info +=
                    if (subItemCount == null || !act.ADV.showFolderCountsInListView) "folder"
                    else "$subItemCount item" + if (subItemCount > 0) "s" else ""

                dirItemList.add(FolderItem(path, date, info))
            }
            else // if (isFile)
            {
                if (!Utils.filePassesFilter(exts, path)) continue

                val size = try { item.length() } catch (ex: Exception) { null }

                info +=
                    if (size == null || !act.ADV.showFileSizesInListView) "file"
                    else Utils.getFileSizeString(size)

                val imageId =
                    if (act.OPT.showImagesWhileBrowsingNormal) act.DAT.mediaStoreImageInfoMap[path]
                    else null

                dirItemList.add(FileItem(path, date, size, info, imageId))
            }
        }

        Collections.sort(dirItemList, DirectoryItemComparator(act.sortOrder))

        return dirItemList
    }
}