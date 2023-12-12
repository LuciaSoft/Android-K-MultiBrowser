package com.luciasoft.browserjavatokotlin

import android.view.Menu
import android.view.MenuItem
import com.luciasoft.browserjavatokotlin.MyMessageBox.Companion.show
import java.io.File

internal object OptionsMenu
{
    fun onMenuOpened(act: MultiBrowserActivity, menu: Menu)
    {
        var testVisible = true

        var newFolderOptionVisible = false
        var listViewOptionVisible = false
        var tilesViewOptionVisible = false
        var galleryViewOptionVisible = false
        var columnCountOptionVisible = false
        var resetDirectoryOptionVisible = false
        var sortOrderOptionVisible = false
        var showHideFileNamesOptionVisible = false
        if (act.isListView)
        {
            newFolderOptionVisible = true
            listViewOptionVisible = false
            tilesViewOptionVisible = true
            galleryViewOptionVisible = true
            columnCountOptionVisible = false
            resetDirectoryOptionVisible = true
            sortOrderOptionVisible = true
        }
        else if (act.isTilesView)
        {
            newFolderOptionVisible = true
            listViewOptionVisible = true
            tilesViewOptionVisible = false
            galleryViewOptionVisible = true
            columnCountOptionVisible = true
            resetDirectoryOptionVisible = true
            sortOrderOptionVisible = true
        }
        else if (act.isGalleryView)
        {
            newFolderOptionVisible = false
            listViewOptionVisible = true
            tilesViewOptionVisible = true
            galleryViewOptionVisible = false
            columnCountOptionVisible = true
            resetDirectoryOptionVisible = false
            showHideFileNamesOptionVisible = true
            sortOrderOptionVisible = true
        }
        if (act.OPT.browseMode == BrowseMode.LoadFilesAndOrFolders)
        {
            newFolderOptionVisible = false
        }
        if (!act.ADV.menuOptionListViewEnabled) listViewOptionVisible = false
        if (!act.ADV.menuOptionTilesViewEnabled) tilesViewOptionVisible = false
        if (!act.ADV.menuOptionGalleryViewEnabled) galleryViewOptionVisible = false
        if (!act.ADV.menuOptionColumnCountEnabled) columnCountOptionVisible = false
        if (!act.ADV.menuOptionSortOrderEnabled) sortOrderOptionVisible = false
        if (!act.ADV.menuOptionResetDirectoryEnabled) resetDirectoryOptionVisible = false
        if (!act.ADV.menuOptionShowHideFileNamesEnabled) showHideFileNamesOptionVisible = false
        if (!act.ADV.menuOptionNewFolderEnabled) newFolderOptionVisible = false
        menu.findItem(R.id.menuItemNewFolder).isVisible = newFolderOptionVisible
        menu.findItem(R.id.menuItemListView).isVisible = listViewOptionVisible
        menu.findItem(R.id.menuItemTilesView).isVisible = tilesViewOptionVisible
        menu.findItem(R.id.menuItemGalleryView).isVisible = galleryViewOptionVisible
        menu.findItem(R.id.menuItemColumnCount).isVisible = columnCountOptionVisible
        menu.findItem(R.id.menuItemResetDir).isVisible = resetDirectoryOptionVisible
        menu.findItem(R.id.menuItemSortOrder).isVisible = sortOrderOptionVisible
        menu.findItem(R.id.menuItemShowHideFileNames).isVisible = showHideFileNamesOptionVisible
        menu.findItem(R.id.menuItemTest).isVisible = testVisible
    }

    fun onOptionsItemSelected(act: MultiBrowserActivity, item: MenuItem): Boolean
    {
        val itemId = item.itemId

        if (itemId == R.id.menuItemTest)
        {
            if (Permissions.checkExternalStoragePermission(act))
            {
                var filePath = Options.extStoragePath
                if (!filePath!!.endsWith(File.separatorChar)) filePath += File.separatorChar
                filePath += "xml-file.xml"
                XmlOperations.saveXml(act, filePath)

                val array = XmlOperations.loadXml(act, filePath)
                Utils.toastLong(act, "NUM SET=" + array[0] + ", NUM SKIPPED=" + array[1])
            }

            return true
        }

        if (itemId == R.id.menuItemNewFolder)
        {
            var dir = act.DAT.currentDir ?: return true
            val dlg = MyInputDialog(
                act,
                "Create New Folder",
                "New Folder Name",
                object : MyInputDialog.DoSomethingWithResult
                {
                    override fun doSomething(result: String)
                    {
                        var result = result
                        result = result.trim { it <= ' ' }
                        if (result.isEmpty()) return
                        if (!dir.endsWith("/")) dir += "/"
                        dir += result
                        try
                        {
                            if (File(dir).exists())
                            {
                                show(
                                    act,
                                    "Directory Exists",
                                    "The directory already exists.",
                                    MyMessageBox.ButtonsType.Ok,
                                    null, null
                                )
                                return
                            }
                            val success = File(dir).mkdirs()
                            if (!success)
                            {
                                show(
                                    act,
                                    "Error",
                                    "Could not create directory.",
                                    MyMessageBox.ButtonsType.Ok,
                                    null, null
                                )
                                return
                            }
                            act.refreshView(dir, true, false)
                        }
                        catch (ex: Exception)
                        {
                        }
                    }
                })
            dlg.show()
            return true
        }
        if (itemId == R.id.menuItemListView)
        {
            if (act.isListView) return false
            act.OPT.browserViewType = BrowserViewType.List
            act.refreshView(true, true)
            return true
        }
        if (itemId == R.id.menuItemTilesView)
        {
            if (act.isTilesView) return false
            act.OPT.browserViewType = BrowserViewType.Tiles
            act.refreshView(true, true)
            return true
        }
        if (itemId == R.id.menuItemGalleryView)
        {
            if (act.isGalleryView) return false
            act.OPT.browserViewType = BrowserViewType.Gallery
            act.refreshView(true, true)
            return true
        }
        if (itemId == R.id.menuItemColumnCount)
        {
            val counts = Array(10) { "" + (it + 1) }
            val listDlg = MyListDialog()
            val columnCount =
                if (act.isGalleryView) act.OPT.galleryViewColumnCount else act.OPT.normalViewColumnCount
            listDlg.show(act, "Column Count", counts, columnCount - 1) { dialog, which ->
                val count = listDlg.choice + 1
                var refresh = false
                if (act.isGalleryView)
                {
                    if (count != act.OPT.galleryViewColumnCount)
                    {
                        act.OPT.galleryViewColumnCount = count
                        refresh = true
                    }
                }
                else
                {
                    if (count != act.OPT.normalViewColumnCount)
                    {
                        act.OPT.normalViewColumnCount = count
                        refresh = true
                    }
                }
                if (refresh) act.refreshView(false, true)
            }
            return true
        }
        if (itemId == R.id.menuItemResetDir)
        {
            act.DAT.currentDir = act.OPT.defaultDir
            act.refreshView(true, false)
            return true
        }
        if (itemId == R.id.menuItemSortOrder)
        {
            val index: Int
            index = when (act.sortOrder)
            {
                SortOrder.PathAscending -> 0
                SortOrder.PathDescending -> 1
                SortOrder.DateAscending -> 2
                SortOrder.DateDescending -> 3
                SortOrder.SizeAscending -> 4
                SortOrder.SizeDescending -> 5
                else -> 0
            }
            val options = arrayOf(
                "Path Ascending", "Path Descending",
                "Date Ascending", "Date Descending",
                "Size Ascending", "Size Descending"
            )
            val listDlg = MyListDialog()
            listDlg.show(act, "Sort Order", options, index) { dialog, which ->
                val option = listDlg.choice
                val order: SortOrder? = when (option)
                {
                    0 -> SortOrder.PathAscending
                    1 -> SortOrder.PathDescending
                    2 -> SortOrder.DateAscending
                    3 -> SortOrder.DateDescending
                    4 -> SortOrder.SizeAscending
                    5 -> SortOrder.SizeDescending
                    else -> null
                }
                var refresh = false
                if (order != null)
                {
                    if (act.sortOrder != order)
                    {
                        act.sortOrder = order
                        refresh = true
                    }
                }
                if (refresh) act.refreshView(true, false)
            }
            return true
        }
        if (itemId == R.id.menuItemShowHideFileNames)
        {
            act.OPT.showFileNamesInGalleryView = !act.OPT.showFileNamesInGalleryView
            act.refreshView(false, false)
            return true
        }
        return false
    }
}