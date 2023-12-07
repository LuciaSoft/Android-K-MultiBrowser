package com.lucerta.multibrowser;

import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import com.luciasoft.browser.*;

import java.io.File;

class OptionsMenu
{
    static void onMenuOpened(MultiBrowserActivity act, Menu menu)
    {
        boolean newFolderOptionVisible = false;
        boolean listViewOptionVisible = false;
        boolean tilesViewOptionVisible = false;
        boolean galleryViewOptionVisible = false;
        boolean columnCountOptionVisible = false;
        boolean resetDirectoryOptionVisible = false;
        boolean sortOrderOptionVisible = false;
        boolean showHideFileNamesOptionVisible = false;

        if (act.OPT().browserViewType == Options.BrowserViewType.List)
        {
            newFolderOptionVisible = true;
            listViewOptionVisible = false;
            tilesViewOptionVisible = true;
            galleryViewOptionVisible = true;
            columnCountOptionVisible = false;
            resetDirectoryOptionVisible = true;
            sortOrderOptionVisible = true;
        }
        else if (act.OPT().browserViewType == Options.BrowserViewType.Tiles)
        {
            newFolderOptionVisible = true;
            listViewOptionVisible = true;
            tilesViewOptionVisible = false;
            galleryViewOptionVisible = true;
            columnCountOptionVisible = true;
            resetDirectoryOptionVisible = true;
            sortOrderOptionVisible = true;
        }
        else if (act.OPT().browserViewType == Options.BrowserViewType.Gallery)
        {
            newFolderOptionVisible = false;
            listViewOptionVisible = true;
            tilesViewOptionVisible = true;
            galleryViewOptionVisible = false;
            columnCountOptionVisible = true;
            resetDirectoryOptionVisible = false;
            showHideFileNamesOptionVisible = true;
            sortOrderOptionVisible = true;
        }

        if (act.OPT().browseMode == Options.BrowseMode.LoadFilesAndOrFolders)
        {
            newFolderOptionVisible = false;
        }

        if (!act.ADV().menuOptionListViewEnabled) listViewOptionVisible = false;
        if (!act.ADV().menuOptionTilesViewEnabled) tilesViewOptionVisible = false;
        if (!act.ADV().menuOptionGalleryViewEnabled) galleryViewOptionVisible = false;
        if (!act.ADV().menuOptionColumnCountEnabled) columnCountOptionVisible = false;
        if (!act.ADV().menuOptionSortOrderEnabled) sortOrderOptionVisible = false;
        if (!act.ADV().menuOptionResetDirectoryEnabled) resetDirectoryOptionVisible = false;
        if (!act.ADV().menuOptionShowHideFileNamesEnabled) showHideFileNamesOptionVisible = false;
        if (!act.ADV().menuOptionNewFolderEnabled) newFolderOptionVisible = false;

        menu.findItem(R.id.menuItemNewFolder).setVisible(newFolderOptionVisible);
        menu.findItem(R.id.menuItemListView).setVisible(listViewOptionVisible);
        menu.findItem(R.id.menuItemTilesView).setVisible(tilesViewOptionVisible);
        menu.findItem(R.id.menuItemGalleryView).setVisible(galleryViewOptionVisible);
        menu.findItem(R.id.menuItemColumnCount).setVisible(columnCountOptionVisible);
        menu.findItem(R.id.menuItemResetDir).setVisible(resetDirectoryOptionVisible);
        menu.findItem(R.id.menuItemSortOrder).setVisible(sortOrderOptionVisible);
        menu.findItem(R.id.menuItemShowHideFileNames).setVisible(showHideFileNamesOptionVisible);
    }

    static boolean onOptionsItemSelected(MultiBrowserActivity act, MenuItem item)
    {
        int itemId = item.getItemId();

        if (itemId == R.id.menuItemNewFolder)
        {
            MyInputDialog dlg = new MyInputDialog(act, "Create New Folder", "New Folder Name", new MyInputDialog.DoSomethingWithResult()
            {
                @Override
                public void doSomething(String result)
                {
                    result = result.trim();

                    if (result.isEmpty()) return;

                    String dir = act.OPT().currentDir;

                    if (!dir.endsWith("/")) dir += "/";

                    dir += result;

                    try
                    {
                        if (new File(dir).exists())
                        {
                            MyMessageBox.Companion.show(
                                    act,
                                    "Directory Exists",
                                    "The directory already exists.",
                                    MyMessageBox.ButtonsType.Ok,
                                    null, null);

                            return;
                        }

                        boolean success = new File(dir).mkdirs();

                        if (!success)
                        {
                            MyMessageBox.Companion.show(
                                    act,
                                    "Error",
                                    "Could not create directory.",
                                    MyMessageBox.ButtonsType.Ok,
                                    null, null);

                            return;
                        }

                        act.refreshView(dir, true, false);
                    }
                    catch (Exception ex) { }
                }
            });

            dlg.show();

            return true;
        }

        if (itemId == R.id.menuItemListView)
        {
            if (act.OPT().browserViewType == Options.BrowserViewType.List) return false;

            act.OPT().browserViewType = Options.BrowserViewType.List;

            act.refreshView(true, true);

            return true;
        }

        if (itemId == R.id.menuItemTilesView)
        {
            if (act.OPT().browserViewType == Options.BrowserViewType.Tiles) return false;

            act.OPT().browserViewType = Options.BrowserViewType.Tiles;

            act.refreshView(true, true);

            return true;
        }

        if (itemId == R.id.menuItemGalleryView)
        {
            if (act.OPT().browserViewType == Options.BrowserViewType.Gallery) return false;

            act.OPT().browserViewType = Options.BrowserViewType.Gallery;

            act.refreshView(true, true);

            return true;
        }

        if (itemId == R.id.menuItemColumnCount)
        {
            String[] counts = new String[10];

            for (int i = 0; i < 10; i++) counts[i] = "" + (i + 1);

            MyListDialog listDlg = new MyListDialog();

            boolean galleryView = act.OPT().browserViewType == Options.BrowserViewType.Gallery;

            int columnCount = galleryView ? act.OPT().galleryViewColumnCount : act.OPT().normalViewColumnCount;

            listDlg.show(act, "Column Count", counts, columnCount - 1, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    int count = listDlg.getChoice() + 1;

                    boolean refresh = false;

                    if (galleryView)
                    {
                        if (count != act.OPT().galleryViewColumnCount)
                        {
                            act.OPT().galleryViewColumnCount = count;
                            refresh = true;
                        }
                    }
                    else
                    {
                        if (count != act.OPT().normalViewColumnCount)
                        {
                            act.OPT().normalViewColumnCount = count;
                            refresh = true;
                        }
                    }

                    if (refresh) act.refreshView(false, true);
                }
            });

            return true;
        }

        if (itemId == R.id.menuItemResetDir)
        {
            act.OPT().currentDir = act.OPT().defaultDir;

            act.refreshView(true, false);

            return true;
        }

        if (itemId == R.id.menuItemSortOrder)
        {
            int index;

            Options.SortOrder sortOrder;

            if (act.OPT().browserViewType == Options.BrowserViewType.Gallery)
                sortOrder = act.OPT().galleryViewSortOrder;
            else sortOrder = act.OPT().normalViewSortOrder;

            switch (sortOrder)
            {
                case PathAscending: index = 0; break;
                case PathDescending: index = 1; break;
                case DateAscending: index = 2; break;
                case DateDescending: index = 3; break;
                case SizeAscending: index = 4; break;
                case SizeDescending: index = 5; break;
                default: index = 0;
            }

            String[] options = new String[] {
                    "Path Ascending", "Path Descending",
                    "Date Ascending", "Date Descending",
                    "Size Ascending", "Size Descending" };

            MyListDialog listDlg = new MyListDialog();
            listDlg.show(act, "Sort Order", options, index, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    int option = listDlg.getChoice();

                    Options.SortOrder sortOrder = null;

                    switch (option)
                    {
                        case 0:
                        {
                            sortOrder = Options.SortOrder.PathAscending;
                            break;
                        }
                        case 1:
                        {
                            sortOrder = Options.SortOrder.PathDescending;
                            break;
                        }
                        case 2:
                        {
                            sortOrder = Options.SortOrder.DateAscending;
                            break;
                        }
                        case 3:
                        {
                            sortOrder = Options.SortOrder.DateDescending;
                            break;
                        }
                        case 4:
                        {
                            sortOrder = Options.SortOrder.SizeAscending;
                            break;
                        }
                        case 5:
                        {
                            sortOrder = Options.SortOrder.SizeDescending;
                            break;
                        }
                    }

                    boolean refresh = false;

                    if (sortOrder != null)
                    {
                        if (act.OPT().browserViewType == Options.BrowserViewType.Gallery)
                        {
                            if (act.OPT().galleryViewSortOrder != sortOrder)
                            {
                                act.OPT().galleryViewSortOrder = sortOrder;
                                refresh = true;
                            }
                        }
                        else
                        {
                            if (act.OPT().normalViewSortOrder != sortOrder)
                            {
                                act.OPT().normalViewSortOrder = sortOrder;
                                refresh = true;
                            }
                        }
                    }

                    if (refresh) act.refreshView(true, false);
                }
            });

            return true;
        }

        if (itemId == R.id.menuItemShowHideFileNames)
        {
            act.OPT().showFileNamesInGalleryView = !act.OPT().showFileNamesInGalleryView;

            act.refreshView(false, false);

            return true;
        }

        return false;
    }
}
