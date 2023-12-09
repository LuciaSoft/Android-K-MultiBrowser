package com.luciasoft.browserjavatokotlin.multibrowser;

import android.database.Cursor;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

class ListUtils
{
    static ArrayList<DirectoryItem> getImageInfos(MultiBrowserActivity act)
    {
        MultiBrowserOptions.SortOrder sortOrder;
        if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery)
            sortOrder = act.OPT().mGalleryViewSortOrder;
        else sortOrder = act.OPT().mNormalViewSortOrder;

        String sortOrderString;
        switch (sortOrder)
        {
            case PathAscending: sortOrderString = MediaStore.Images.Media.DATA; break;
            case PathDescending: sortOrderString = MediaStore.Images.Media.DATA + " DESC"; break;
            case DateAscending: sortOrderString = MediaStore.Images.Media.DATE_MODIFIED; break;
            case DateDescending: sortOrderString = MediaStore.Images.Media.DATE_MODIFIED + " DESC"; break;
            case SizeAscending: sortOrderString = MediaStore.Images.Media.SIZE; break;
            case SizeDescending: sortOrderString = MediaStore.Images.Media.SIZE + " DESC"; break;
            default: sortOrderString = MediaStore.Images.Media.DATA; break;
        }

        //Cursor cursor = context.managedQuery( // DEPRICATED
        //MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cols, null,
        //null, sortOrder); // GET DATA IN CURSOR IN DESC ORDER

        Cursor cursor;
        try
        {
            final String[] cols = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, };

            CursorLoader loader = new CursorLoader(act);
            loader.setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            loader.setProjection(cols);
            loader.setSelection(null);
            loader.setSortOrder(sortOrderString);
            cursor = loader.loadInBackground();
        }
        catch (Exception ex) { cursor = null; }
        if (cursor == null) return null;

        ArrayList<DirectoryItem> list = new ArrayList<>();

        String[] exts = Utils.getValidExts(act.ADV().mMediaStoreImageExts);

        for (int i = 0; i < cursor.getCount(); i++)
        {
            String imagePath;
            try
            {
                cursor.moveToPosition(i);
                int imagePathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imagePath = cursor.getString(imagePathCol);
            }
            catch (Exception ex) { continue; }

            File imageFile;
            try { imageFile = new File(imagePath); }
            catch (Exception ex) { imageFile = null; }
            if (imageFile != null)
            {
                String ipath;
                try { ipath = imageFile.getCanonicalPath(); }
                catch (Exception ex2)
                {
                    try { ipath = imageFile.getAbsolutePath(); }
                    catch (Exception ex3) { ipath = null; }
                }
                if (ipath != null) imagePath = ipath;
            }

            if (!Utils.filePassesFilter(exts, imagePath)) continue;

            try
            {
                int imageIdCol = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                int imageId = cursor.getInt(imageIdCol);
                list.add(new FileItem(imagePath, imageId));
            }
            catch (Exception ex) { }
        }
            
        DirectoryItem[] infoArray = list.toArray(new DirectoryItem[0]);
            
        Random r = new Random();
        DirectoryItem tmp;
        for (int i = 0; i < infoArray.length; i++)
        {
            int pos = (int)(r.nextDouble() * infoArray.length);

            tmp = infoArray[i];
            infoArray[i] = infoArray[pos];
            infoArray[pos] = tmp;
        }

        if (act.DAT().mMediaStoreImageInfoTree == null)
            act.DAT().mMediaStoreImageInfoTree = new MediaStoreImageInfoTree();
        else act.DAT().mMediaStoreImageInfoTree.reset();

        for (DirectoryItem info : infoArray) act.DAT().mMediaStoreImageInfoTree.add((FileItem)info);

        return list;
    }

    static ArrayList<DirectoryItem> getDirectoryItemsFromFileSystem(MultiBrowserActivity act, String directory, String[] exts)
    {
        ArrayList<DirectoryItem> dirItemList = new ArrayList<>();

        File[] items;
        try { items = new File(directory).listFiles(); }
        catch (Exception ex) { return null; }
        if (items == null)
        {
            if (Utils.directoryIsReadable(act, directory)) return dirItemList;
            return null;
        }
        
        if (!act.ADV().mShowFilesInNormalView && !act.ADV().mShowFoldersInNormalView) return dirItemList;

        exts = Utils.getValidExts(exts);

        for (File item : items)
        {
            String path;
            try { path = item.getCanonicalPath(); }
            catch (Exception ex)
            {
                try { path = item.getAbsolutePath(); }
                catch (Exception ex2) { continue; }
            }

            boolean isFile;
            try { isFile = item.isFile(); }
            catch (Exception ex) { continue; }

            boolean isDirectory;
            try { isDirectory = item.isDirectory(); }
            catch (Exception ex) { continue; }

            if (!isFile && !isDirectory) continue;
            if (isFile &&
                (!act.ADV().mShowFilesInNormalView ||
                act.OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.LoadFolders ||
                act.OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.SaveFolders))
                continue;
            if (isDirectory && !act.ADV().mShowFoldersInNormalView) continue;

            boolean isHidden;
            try { isHidden = item.isHidden(); }
            catch (Exception ex) { continue; }

            if (isHidden && isFile && !act.OPT().mShowHiddenFiles) continue;
            if (isHidden && isDirectory && !act.OPT().mShowHiddenFolders) continue;
            
            Long date;
            try { date = item.lastModified(); }
            catch (Exception ex) { date = null; }

            String info = "";

            boolean showDate = isFile && act.ADV().mShowFileDatesInListView ||
                    isDirectory && act.ADV().mShowFolderDatesInListView;

            if (date != null && showDate)
            {
                if (showDate) info += Utils.getDateString(date) + ", ";
            }

            if (isDirectory)
            {
                Integer subItemCount;
                try { subItemCount = item.listFiles().length; }
                catch (Exception ex) { subItemCount = null; }

                if (subItemCount == null || !act.ADV().mShowFolderCountsInListView)
                {
                    info += "folder";
                }
                else
                {
                    info += "" + subItemCount + " item";
                    if (subItemCount != 1) info += "s";
                }

                dirItemList.add(new FolderItem(path, date, info));
            }
            else if (isFile)
            {
                if (!Utils.filePassesFilter(exts, path)) continue;

                Long size;
                try { size = item.length(); }
                catch (Exception ex) { size = null; }

                if (size == null || !act.ADV().mShowFileSizesInListView) info += "file";
                else info += Utils.getFileSizeString(size);

                Integer imageId = null;

                if (act.OPT().mShowImagesWhileBrowsingNormal && act.DAT().mMediaStoreImageInfoTree != null)
                {
                    imageId = act.DAT().mMediaStoreImageInfoTree.getImageId(path);
                }

                dirItemList.add(new FileItem(path, date, size, info, imageId));
            }
        }

        Collections.sort(dirItemList, DirItemComparator.getComparator(act));

        return dirItemList;
    }
}

class DirItemComparator implements Comparator<DirectoryItem>
{
    DirItemComparator(MultiBrowserActivity act)
    {
        this.act = act;
    }

    MultiBrowserActivity act;

    @Override
    public int compare(DirectoryItem item1, DirectoryItem item2)
    {
        boolean item1isDir = item1 instanceof FolderItem;
        boolean item2isDir = item2 instanceof FolderItem;

        if (item1isDir && !item2isDir) return -1;
        else if (item2isDir && !item1isDir) return 1;

        Integer compare = null;

        MultiBrowserOptions.SortOrder sortOrder;

        if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery)
            sortOrder = act.OPT().mGalleryViewSortOrder;
        else sortOrder = act.OPT().mNormalViewSortOrder;

        boolean path =
                sortOrder == MultiBrowserOptions.SortOrder.PathAscending ||
                sortOrder == MultiBrowserOptions.SortOrder.PathDescending;

        boolean date =
                sortOrder == MultiBrowserOptions.SortOrder.DateAscending ||
                sortOrder == MultiBrowserOptions.SortOrder.DateDescending;

        boolean size =
                sortOrder == MultiBrowserOptions.SortOrder.SizeAscending ||
                sortOrder == MultiBrowserOptions.SortOrder.SizeDescending;

        boolean desc =
                sortOrder == MultiBrowserOptions.SortOrder.PathDescending ||
                sortOrder == MultiBrowserOptions.SortOrder.DateDescending ||
                sortOrder == MultiBrowserOptions.SortOrder.SizeDescending;
 
        if (size && item1isDir)
        {
            size = false;
            path = true;
            desc = false;
        }
 
        if (size)
        {
            Long size1 = ((FileItem)item1).getSize();
            Long size2 = ((FileItem)item2).getSize();

            if (size1 == null || size2 == null)
            {
                size = false;
                path = true;
                desc = false;
            }
            else
            {
                long cmp = size1 - size2;
                if (cmp < 0) compare = -1;
                else if (cmp > 0) compare = 1;
                else compare = 0;
            }
        }

        if (date)
        {
            Long date1 = ((FileItem)item1).getDate();
            Long date2 = ((FileItem)item2).getDate();

            if (date1 == null || date2 == null)
            {
                date = false;
                path = true;
                desc = false;
            }
            else
            {
                long cmp = date1 - date2;
                if (cmp < 0) compare = -1;
                else if (cmp > 0) compare = 1;
                else compare = 0;
            }
        }

        if (path)
        {
            compare = item1.getPath().compareToIgnoreCase(item2.getPath());
        }

        if (desc) compare = -compare;

        return compare;
    }

    static DirItemComparator comparator = null;

    static DirItemComparator getComparator(MultiBrowserActivity act)
    {
        if (comparator == null) comparator = new DirItemComparator(act);

        return comparator;
    }
}
