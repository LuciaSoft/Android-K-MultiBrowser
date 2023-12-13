package com.luciasoft.browser.multibrowser;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.TypedValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class MultiBrowserOptions
{
    public final String ALL_FILES_FILTER = " All Files ( * ) |*";

    public void saveXml(String filePath) throws IOException, TransformerException, ParserConfigurationException
    {
        XmlOperations.saveOptions(this, filePath);
    }

    public static String getExtStoragePath()
    {
        try { return Environment.getExternalStorageDirectory().getCanonicalPath(); }
        catch (Exception ex) { return null; }
    }

    public enum FontMode
    {
        System(1), AppDefault(2), CustomOrSystem(3), CustomOrAppDefault(4);

        FontMode(int value)
        {
            this.value = value;
        }

        private final int value;

        public static FontMode valueOf(int fontMode)
        {
            switch (fontMode)
            {
                case 1: return System;
                case 2: return AppDefault;
                case 3: return CustomOrSystem;
                case 4: return CustomOrAppDefault;
                default: return null;
            }
        }

        public int getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return "" + value + ":" + name();
        }
    }

    public enum ScreenMode
    {
        NotSpecified(1), SystemDefault(2), AllowPortraitUprightAndLandscape(3), AllowPortraitUprightOnly(4), AllowLandscapeOnly(5), AllowAll(6);

        ScreenMode(int value)
        {
            this.value = value;
        }

        private final int value;

        public static ScreenMode valueOf(int screenMode)
        {
            switch (screenMode)
            {
                case 1: return NotSpecified;
                case 2: return SystemDefault;
                case 3: return AllowPortraitUprightAndLandscape;
                case 4: return AllowPortraitUprightOnly;
                case 5: return AllowLandscapeOnly;
                case 6: return AllowAll;
                default: return null;
            }
        }

        public int getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return "" + value + ":" + name();
        }
    }

    public enum BrowseMode
    {
        LoadFilesAndOrFolders(1), SaveFilesAndOrFolders(2), LoadFolders(3), SaveFolders(4);

        BrowseMode(int value)
        {
            this.value = value;
        }

        private final int value;

        public static BrowseMode valueOf(int browseMode)
        {
            switch (browseMode)
            {
                case 1: return LoadFilesAndOrFolders;
                case 2: return SaveFilesAndOrFolders;
                case 3: return LoadFolders;
                case 4: return SaveFolders;
                default: return null;
            }
        }

        public int getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return "" + value + ":" + name();
        }
    }

    public enum BrowserViewType
    {
        List(1), Tiles(2), Gallery(3);

        BrowserViewType(int value)
        {
            this.value = value;
        }

        private final int value;


        public static BrowserViewType valueOf(int browserViewType)
        {
            switch (browserViewType)
            {
                case 1: return List;
                case 2: return Tiles;
                case 3: return Gallery;
                default: return null;
            }
        }

        public int getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return "" + value + ":" + name();
        }
    }

    public enum SortOrder
    {
        PathAscending(1), PathDescending(2), DateAscending(3), DateDescending(4), SizeAscending(5), SizeDescending(6);

        SortOrder(int value)
        {
            this.value = value;
        }

        private final int value;

        public static SortOrder valueOf(int sortOrder)
        {
            switch (sortOrder)
            {
                case 1: return PathAscending;
                case 2: return PathDescending;
                case 3: return DateAscending;
                case 4: return DateDescending;
                case 5: return SizeAscending;
                case 6: return SizeDescending;
                default: return null;
            }
        }

        public int getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return "" + value + ":" + name();
        }
    }

    public enum SaveFileBehavior
    {
        SaveFile(1), SendNameToSaveBoxOrSaveFile(2), SendNameToSaveBoxAndSaveFile(3);

        SaveFileBehavior(int value)
        {
            this.value = value;
        }

        private final int value;

        public static SaveFileBehavior valueOf(int saveFileBehavior)
        {
            switch (saveFileBehavior)
            {
                case 1: return SaveFile;
                case 2: return SendNameToSaveBoxOrSaveFile;
                case 3: return SendNameToSaveBoxAndSaveFile;
                default: return null;
            }
        }

        public int getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return "" + value + ":" + name();
        }
    }

    Advanced mAdvancedOptions;
    Theme mThemeOptions;

    public Advanced advanced()
    {
        return mAdvancedOptions;
    }
    public Theme theme() { return mThemeOptions; }

    public MultiBrowserOptions()
    {
        mAdvancedOptions = new Advanced();
        mThemeOptions = new Theme();
        reset();
    }

    public void reset()
    {
        setFileFilter(ALL_FILES_FILTER);

        mBrowserTitle = "Multi Browser";
        mBrowseMode = BrowseMode.SaveFilesAndOrFolders;
        mBrowserViewType = BrowserViewType.List;
        mNormalViewSortOrder = SortOrder.PathAscending;
        mGalleryViewSortOrder = SortOrder.DateDescending;
        mNormalViewColumnCount = 4;
        mGalleryViewColumnCount = 3;
        mCurrentDir = getExtStoragePath();
        mDefaultDir = null;
        mDefaultSaveFileName = "";
        mCreateDirOnActivityStart = false;
        mFileFilterIndex = 0;
        mAlwaysShowDialogForSavingFile = true;
        mAlwaysShowDialogForSavingFolder = true;
        mAlwaysShowDialogForSavingGalleryItem = true;
        mShowOverwriteDialogForSavingFileIfExists = true;
        mShowHiddenFiles = true;
        mShowHiddenFolders = true;
        mShowFileNamesInGalleryView = false;
        mShowImagesWhileBrowsingNormal = true;
        mShowImagesWhileBrowsingGallery = true;
        mAllowAccessToRestrictedFolders = false;
        mOnSelectFileForLoad = null;
        mOnSelectFileForSave = null;
        mOnSelectFolderForLoad = null;
        mOnSelectFolderForSave = null;

        mAdvancedOptions.reset();
        mThemeOptions.reset();
    }

    // ENCAP BEGIN PUBLIC (DONE)
    String mBrowserTitle;

    BrowseMode mBrowseMode;
    BrowserViewType mBrowserViewType;
    SortOrder mNormalViewSortOrder;
    SortOrder mGalleryViewSortOrder;

    int mNormalViewColumnCount;
    int mGalleryViewColumnCount;

    String mCurrentDir;
    String mDefaultDir;
    String mDefaultSaveFileName;
    boolean mCreateDirOnActivityStart;
    int mFileFilterIndex;

    boolean mAlwaysShowDialogForSavingFile;
    boolean mAlwaysShowDialogForSavingFolder;
    boolean mAlwaysShowDialogForSavingGalleryItem;
    boolean mShowOverwriteDialogForSavingFileIfExists;

    boolean mShowHiddenFiles;
    boolean mShowHiddenFolders;
    boolean mShowFileNamesInGalleryView;
    boolean mShowImagesWhileBrowsingNormal;
    boolean mShowImagesWhileBrowsingGallery;

    boolean mAllowAccessToRestrictedFolders;

    OnSelectItem mOnSelectFileForLoad;
    OnSelectItem mOnSelectFileForSave;
    OnSelectItem mOnSelectFolderForLoad;
    OnSelectItem mOnSelectFolderForSave;
    // ENCAP END (DONE)

    String[][] mFileFilters;
    String[] mFileFilterDescrips;

    // ENCAP HERE (DONE)
    public String getBrowserTitle()
    {
        return mBrowserTitle;
    }

    public void setBrowserTitle(String browserTitle)
    {
        this.mBrowserTitle = browserTitle;
    }

    public BrowseMode getBrowseMode()
    {
        return mBrowseMode;
    }

    public void setBrowseMode(BrowseMode browseMode)
    {
        this.mBrowseMode = browseMode;
    }

    public BrowserViewType getBrowserViewType()
    {
        return mBrowserViewType;
    }

    public void setBrowserViewType(BrowserViewType browserViewType)
    {
        this.mBrowserViewType = browserViewType;
    }

    public SortOrder getNormalViewSortOrder()
    {
        return mNormalViewSortOrder;
    }

    public void setNormalViewSortOrder(SortOrder normalViewSortOrder)
    {
        this.mNormalViewSortOrder = normalViewSortOrder;
    }

    public SortOrder getGalleryViewSortOrder()
    {
        return mGalleryViewSortOrder;
    }

    public void setGalleryViewSortOrder(SortOrder galleryViewSortOrder)
    {
        this.mGalleryViewSortOrder = galleryViewSortOrder;
    }

    public int getNormalViewColumnCount()
    {
        return mNormalViewColumnCount;
    }

    public void setNormalViewColumnCount(int normalViewColumnCount)
    {
        this.mNormalViewColumnCount = normalViewColumnCount;
    }

    public int getGalleryViewColumnCount()
    {
        return mGalleryViewColumnCount;
    }

    public void setGalleryViewColumnCount(int galleryViewColumnCount)
    {
        this.mGalleryViewColumnCount = galleryViewColumnCount;
    }

    public String getCurrentDir()
    {
        return mCurrentDir;
    }

    public void setCurrentDir(String currentDir)
    {
        this.mCurrentDir = currentDir;
    }

    public String getDefaultDir()
    {
        return mDefaultDir;
    }

    public void setDefaultDir(String defaultDir)
    {
        this.mDefaultDir = defaultDir;
    }

    public String getDefaultSaveFileName()
    {
        return mDefaultSaveFileName;
    }

    public void setDefaultSaveFileName(String defaultSaveFileName)
    {
        this.mDefaultSaveFileName = defaultSaveFileName;
    }

    public boolean getCreateDirOnActivityStart()
    {
        return mCreateDirOnActivityStart;
    }

    public void setCreateDirOnActivityStart(boolean createDirOnActivityStart)
    {
        this.mCreateDirOnActivityStart = createDirOnActivityStart;
    }

    public int getFileFilterIndex()
    {
        return mFileFilterIndex;
    }

    public void setFileFilterIndex(int fileFilterIndex)
    {
        this.mFileFilterIndex = fileFilterIndex;
    }

    public boolean getAlwaysShowDialogForSavingFile()
    {
        return mAlwaysShowDialogForSavingFile;
    }

    public void setAlwaysShowDialogForSavingFile(boolean alwaysShowDialogForSavingFile)
    {
        this.mAlwaysShowDialogForSavingFile = alwaysShowDialogForSavingFile;
    }

    public boolean getAlwaysShowDialogForSavingFolder()
    {
        return mAlwaysShowDialogForSavingFolder;
    }

    public void setAlwaysShowDialogForSavingFolder(boolean alwaysShowDialogForSavingFolder)
    {
        this.mAlwaysShowDialogForSavingFolder = alwaysShowDialogForSavingFolder;
    }

    public boolean getAlwaysShowDialogForSavingGalleryItem()
    {
        return mAlwaysShowDialogForSavingGalleryItem;
    }

    public void setAlwaysShowDialogForSavingGalleryItem(boolean alwaysShowDialogForSavingGalleryItem)
    {
        this.mAlwaysShowDialogForSavingGalleryItem = alwaysShowDialogForSavingGalleryItem;
    }

    public boolean getShowOverwriteDialogForSavingFileIfExists()
    {
        return mShowOverwriteDialogForSavingFileIfExists;
    }

    public void setShowOverwriteDialogForSavingFileIfExists(boolean showOverwriteDialogForSavingFileIfExists)
    {
        this.mShowOverwriteDialogForSavingFileIfExists = showOverwriteDialogForSavingFileIfExists;
    }

    public boolean getShowHiddenFiles()
    {
        return mShowHiddenFiles;
    }

    public void setShowHiddenFiles(boolean showHiddenFiles)
    {
        this.mShowHiddenFiles = showHiddenFiles;
    }

    public boolean getShowHiddenFolders()
    {
        return mShowHiddenFolders;
    }

    public void setShowHiddenFolders(boolean showHiddenFolders)
    {
        this.mShowHiddenFolders = showHiddenFolders;
    }

    public boolean getShowFileNamesInGalleryView()
    {
        return mShowFileNamesInGalleryView;
    }

    public void setShowFileNamesInGalleryView(boolean showFileNamesInGalleryView)
    {
        this.mShowFileNamesInGalleryView = showFileNamesInGalleryView;
    }

    public boolean getShowImagesWhileBrowsingNormal()
    {
        return mShowImagesWhileBrowsingNormal;
    }

    public void setShowImagesWhileBrowsingNormal(boolean showImagesWhileBrowsingNormal)
    {
        this.mShowImagesWhileBrowsingNormal = showImagesWhileBrowsingNormal;
    }

    public boolean getShowImagesWhileBrowsingGallery()
    {
        return mShowImagesWhileBrowsingGallery;
    }

    public void setShowImagesWhileBrowsingGallery(boolean showImagesWhileBrowsingGallery)
    {
        this.mShowImagesWhileBrowsingGallery = showImagesWhileBrowsingGallery;
    }

    public boolean getAllowAccessToRestrictedFolders()
    {
        return mAllowAccessToRestrictedFolders;
    }

    public void setAllowAccessToRestrictedFolders(boolean allowAccessToRestrictedFolders)
    {
        this.mAllowAccessToRestrictedFolders = allowAccessToRestrictedFolders;
    }

    public OnSelectItem getOnSelectFileForLoad()
    {
        return mOnSelectFileForLoad;
    }

    public void setOnSelectFileForLoad(OnSelectItem onSelectFileForLoad)
    {
        this.mOnSelectFileForLoad = onSelectFileForLoad;
    }

    public OnSelectItem getOnSelectFileForSave()
    {
        return mOnSelectFileForSave;
    }

    public void setOnSelectFileForSave(OnSelectItem onSelectFileForSave)
    {
        this.mOnSelectFileForSave = onSelectFileForSave;
    }

    public OnSelectItem getOnSelectFolderForLoad()
    {
        return mOnSelectFolderForLoad;
    }

    public void setOnSelectFolderForLoad(OnSelectItem onSelectFolderForLoad)
    {
        this.mOnSelectFolderForLoad = onSelectFolderForLoad;
    }

    public OnSelectItem getOnSelectFolderForSave()
    {
        return mOnSelectFolderForSave;
    }

    public void setOnSelectFolderForSave(OnSelectItem onSelectFolderForSave)
    {
        this.mOnSelectFolderForSave = onSelectFolderForSave;
    }
    // ENCAPSULATED (DONE)

    public void setFileFilter(String filterString)
    {
        String[] array = filterString.split("\\|");

        if (array.length % 2 != 0)
            throw new IllegalArgumentException("The filter string must be divisible by 2.");

        String[] filters = new String[array.length / 2];
        String[] descriptions = new String[array.length / 2];

        for (int i = 0; i < array.length; i += 2)
        {
            descriptions[i / 2] = array[i];
            filters[i / 2] = array[i + 1];
        }

        setFileFilter(filters, descriptions);
    }

    public void setFileFilter(String[] filters, String[] descriptions) throws IllegalArgumentException
    {
        if (filters.length != descriptions.length)
            throw new IllegalArgumentException("The filters and the descriptions must have the same length.");

        String[][] filterArray = new String[filters.length][];

        for (int i = 0; i < filters.length; i++)
        {
            ArrayList<String> list = new ArrayList<>();

            String[] exts = filters[i].split(",");

            for (int j = 0; j < exts.length; j++)
            {
                String ext = exts[j].trim().toLowerCase();

                if (ext.equals("*") || ext.equals("*.*"))
                {
                    list.clear();
                    list.add("*");
                    break;
                }
                while (ext.startsWith("*")) ext = ext.substring(1);
                if (ext.isEmpty()) continue;
                if (!ext.startsWith(".")) ext = "." + ext;
                list.add(ext);
            }

            filterArray[i] = list.toArray(new String[0]);
        }

        String[] newDescrips = Arrays.copyOf(descriptions, descriptions.length);
        for (int i = 0; i < newDescrips.length; i++)
            newDescrips[i] = " " + newDescrips[i].trim() + " ";

        mFileFilters = filterArray;
        mFileFilterDescrips = newDescrips;
    }

    public String getFileFilterString()
    {
        String result = "";

        for (int i = 0; i < mFileFilters.length; i++)
        {
            String[] filters = mFileFilters[i];
            String descrip = mFileFilterDescrips[i].trim();

            if (i > 0) result += "|";

            result += " " + descrip + " |";
            for (int j = 0; j < filters.length; j++)
            {
                String filter = filters[j].trim();
                if (!filter.startsWith("*"))
                {
                    if (!filter.startsWith(".")) filter = "*." + filter;
                    else filter = "*" + filter;
                }
                if (j > 0) result += ",";
                result += filter;
            }
        }

        return result;
    }

    public class Advanced
    {
        Advanced()
        {
            reset();
        }

        void reset()
        {
            mDebugMode = false;
            mScreenRotationMode = ScreenMode.NotSpecified;
            mShortClickSaveFileBehavior = SaveFileBehavior.SendNameToSaveBoxOrSaveFile;
            mLongClickSaveFileBehavior = SaveFileBehavior.SendNameToSaveBoxAndSaveFile;
            mAllowShortClickFileForLoad = true;
            mAllowShortClickFileForSave = true;
            mAllowLongClickFileForLoad = false;
            mAllowLongClickFileForSave = false;
            mAllowLongClickFolderForLoad = true;
            mAllowLongClickFolderForSave = true;
            mMenuEnabled = true;
            mMenuOptionListViewEnabled = true;
            mMenuOptionTilesViewEnabled = true;
            mMenuOptionGalleryViewEnabled = true;
            mMenuOptionColumnCountEnabled = true;
            mMenuOptionSortOrderEnabled = true;
            mMenuOptionResetDirectoryEnabled = true;
            mMenuOptionRefreshDirectoryEnabled = false;
            mMenuOptionShowHideFileNamesEnabled = true;
            mMenuOptionNewFolderEnabled = true;
            mShowCurrentDirectoryLayoutIfAvailable = true;
            mShowParentDirectoryLayoutIfAvailable = true;
            mShowSaveFileLayoutIfAvailable = true;
            mShowFileFilterLayoutIfAvailable = true;
            mShowFilesInNormalView = true;
            mShowFoldersInNormalView = true;
            mShowFileDatesInListView = true;
            mShowFileSizesInListView = true;
            mShowFolderDatesInListView = false;
            mShowFolderCountsInListView = true;
            mAutoRefreshDirectorySource = true;
            mMediaStoreImageExts = "*";
        }

        // ENCAP BEGIN PUBLIC (DONE)
        boolean mDebugMode;
        ScreenMode mScreenRotationMode;

        SaveFileBehavior mShortClickSaveFileBehavior;
        SaveFileBehavior mLongClickSaveFileBehavior;

        boolean mAllowShortClickFileForLoad;
        boolean mAllowShortClickFileForSave;
        boolean mAllowLongClickFileForLoad;
        boolean mAllowLongClickFileForSave;
        boolean mAllowLongClickFolderForLoad;
        boolean mAllowLongClickFolderForSave;

        boolean mMenuEnabled;
        boolean mMenuOptionListViewEnabled;
        boolean mMenuOptionTilesViewEnabled;
        boolean mMenuOptionGalleryViewEnabled;
        boolean mMenuOptionColumnCountEnabled;
        boolean mMenuOptionSortOrderEnabled;
        boolean mMenuOptionResetDirectoryEnabled;
        boolean mMenuOptionRefreshDirectoryEnabled;
        boolean mMenuOptionShowHideFileNamesEnabled;
        boolean mMenuOptionNewFolderEnabled;

        boolean mShowCurrentDirectoryLayoutIfAvailable;
        boolean mShowParentDirectoryLayoutIfAvailable;
        boolean mShowSaveFileLayoutIfAvailable;
        boolean mShowFileFilterLayoutIfAvailable;

        boolean mShowFilesInNormalView;
        boolean mShowFoldersInNormalView;

        // ENCAP BEGIN PUBLIC (DONE) NEW OPTIONS!!!
        boolean mShowFileDatesInListView;
        boolean mShowFileSizesInListView;
        boolean mShowFolderDatesInListView;
        boolean mShowFolderCountsInListView;
        // ENCAP END (DONE)

        boolean mAutoRefreshDirectorySource;

        String mMediaStoreImageExts;
        // ENCAP END (DONE)

        // ENCAP HERE (DONE)
        public boolean getShowFileDatesInListView()
        {
            return mShowFileDatesInListView;
        }

        public void setShowFileDatesInListView(boolean showFileDatesInListView)
        {
            this.mShowFileDatesInListView = showFileDatesInListView;
        }

        public boolean getShowFileSizesInListView()
        {
            return mShowFileSizesInListView;
        }

        public void setShowFileSizesInListView(boolean showFileSizesInListView)
        {
            this.mShowFileSizesInListView = showFileSizesInListView;
        }

        public boolean getShowFolderDatesInListView()
        {
            return mShowFolderDatesInListView;
        }

        public void setShowFolderDatesInListView(boolean showFolderDatesInListView)
        {
            this.mShowFolderDatesInListView = showFolderDatesInListView;
        }

        public boolean getShowFolderCountsInListView()
        {
            return mShowFolderCountsInListView;
        }

        public void setShowFolderCountsInListView(boolean showFolderCountsInListView)
        {
            this.mShowFolderCountsInListView = showFolderCountsInListView;
        }
        // ENCAPSULATED (DONE)

        public boolean getDebugMode()
        {
            return mDebugMode;
        }

        public void setDebugMode(boolean debugMode)
        {
            this.mDebugMode = debugMode;
        }

        public ScreenMode getScreenRotationMode()
        {
            return mScreenRotationMode;
        }

        public void setScreenRotationMode(ScreenMode screenRotationMode)
        {
            this.mScreenRotationMode = screenRotationMode;
        }

        public SaveFileBehavior getShortClickSaveFileBehavior()
        {
            return mShortClickSaveFileBehavior;
        }

        public void setShortClickSaveFileBehavior(SaveFileBehavior shortClickSaveFileBehavior)
        {
            this.mShortClickSaveFileBehavior = shortClickSaveFileBehavior;
        }

        public SaveFileBehavior getLongClickSaveFileBehavior()
        {
            return mLongClickSaveFileBehavior;
        }

        public void setLongClickSaveFileBehavior(SaveFileBehavior longClickSaveFileBehavior)
        {
            this.mLongClickSaveFileBehavior = longClickSaveFileBehavior;
        }

        public boolean getAllowShortClickFileForLoad()
        {
            return mAllowShortClickFileForLoad;
        }

        public void setAllowShortClickFileForLoad(boolean allowShortClickFileForLoad)
        {
            this.mAllowShortClickFileForLoad = allowShortClickFileForLoad;
        }

        public boolean getAllowShortClickFileForSave()
        {
            return mAllowShortClickFileForSave;
        }

        public void setAllowShortClickFileForSave(boolean allowShortClickFileForSave)
        {
            this.mAllowShortClickFileForSave = allowShortClickFileForSave;
        }

        public boolean getAllowLongClickFileForLoad()
        {
            return mAllowLongClickFileForLoad;
        }

        public void setAllowLongClickFileForLoad(boolean allowLongClickFileForLoad)
        {
            this.mAllowLongClickFileForLoad = allowLongClickFileForLoad;
        }

        public boolean getAllowLongClickFileForSave()
        {
            return mAllowLongClickFileForSave;
        }

        public void setAllowLongClickFileForSave(boolean allowLongClickFileForSave)
        {
            this.mAllowLongClickFileForSave = allowLongClickFileForSave;
        }

        public boolean getAllowLongClickFolderForLoad()
        {
            return mAllowLongClickFolderForLoad;
        }

        public void setAllowLongClickFolderForLoad(boolean allowLongClickFolderForLoad)
        {
            this.mAllowLongClickFolderForLoad = allowLongClickFolderForLoad;
        }

        public boolean getAllowLongClickFolderForSave()
        {
            return mAllowLongClickFolderForSave;
        }

        public void setAllowLongClickFolderForSave(boolean allowLongClickFolderForSave)
        {
            this.mAllowLongClickFolderForSave = allowLongClickFolderForSave;
        }

        public boolean getMenuEnabled()
        {
            return mMenuEnabled;
        }

        public void setMenuEnabled(boolean menuEnabled)
        {
            this.mMenuEnabled = menuEnabled;
        }

        public boolean getMenuOptionListViewEnabled()
        {
            return mMenuOptionListViewEnabled;
        }

        public void setMenuOptionListViewEnabled(boolean menuOptionListViewEnabled)
        {
            this.mMenuOptionListViewEnabled = menuOptionListViewEnabled;
        }

        public boolean getMenuOptionTilesViewEnabled()
        {
            return mMenuOptionTilesViewEnabled;
        }

        public void setMenuOptionTilesViewEnabled(boolean menuOptionTilesViewEnabled)
        {
            this.mMenuOptionTilesViewEnabled = menuOptionTilesViewEnabled;
        }

        public boolean getMenuOptionGalleryViewEnabled()
        {
            return mMenuOptionGalleryViewEnabled;
        }

        public void setMenuOptionGalleryViewEnabled(boolean menuOptionGalleryViewEnabled)
        {
            this.mMenuOptionGalleryViewEnabled = menuOptionGalleryViewEnabled;
        }

        public boolean getMenuOptionColumnCountEnabled()
        {
            return mMenuOptionColumnCountEnabled;
        }

        public void setMenuOptionColumnCountEnabled(boolean menuOptionColumnCountEnabled)
        {
            this.mMenuOptionColumnCountEnabled = menuOptionColumnCountEnabled;
        }

        public boolean getMenuOptionSortOrderEnabled()
        {
            return mMenuOptionSortOrderEnabled;
        }

        public void setMenuOptionSortOrderEnabled(boolean menuOptionSortOrderEnabled)
        {
            this.mMenuOptionSortOrderEnabled = menuOptionSortOrderEnabled;
        }

        public boolean getMenuOptionResetDirectoryEnabled()
        {
            return mMenuOptionResetDirectoryEnabled;
        }

        public void setMenuOptionResetDirectoryEnabled(boolean menuOptionResetDirectoryEnabled)
        {
            this.mMenuOptionResetDirectoryEnabled = menuOptionResetDirectoryEnabled;
        }

        public boolean getMenuOptionRefreshDirectoryEnabled()
        {
            return mMenuOptionRefreshDirectoryEnabled;
        }

        public void setMenuOptionRefreshDirectoryEnabled(boolean menuOptionRefreshDirectoryEnabled)
        {
            this.mMenuOptionRefreshDirectoryEnabled = menuOptionRefreshDirectoryEnabled;
        }

        public boolean getMenuOptionShowHideFileNamesEnabled()
        {
            return mMenuOptionShowHideFileNamesEnabled;
        }

        public void setMenuOptionShowHideFileNamesEnabled(boolean menuOptionShowHideFileNamesEnabled)
        {
            this.mMenuOptionShowHideFileNamesEnabled = menuOptionShowHideFileNamesEnabled;
        }

        public boolean getMenuOptionNewFolderEnabled()
        {
            return mMenuOptionNewFolderEnabled;
        }

        public void setMenuOptionNewFolderEnabled(boolean menuOptionNewFolderEnabled)
        {
            this.mMenuOptionNewFolderEnabled = menuOptionNewFolderEnabled;
        }

        public boolean getShowCurrentDirectoryLayoutIfAvailable()
        {
            return mShowCurrentDirectoryLayoutIfAvailable;
        }

        public void setShowCurrentDirectoryLayoutIfAvailable(boolean showCurrentDirectoryLayoutIfAvailable)
        {
            this.mShowCurrentDirectoryLayoutIfAvailable = showCurrentDirectoryLayoutIfAvailable;
        }

        public boolean getShowParentDirectoryLayoutIfAvailable()
        {
            return mShowParentDirectoryLayoutIfAvailable;
        }

        public void setShowParentDirectoryLayoutIfAvailable(boolean showParentDirectoryLayoutIfAvailable)
        {
            this.mShowParentDirectoryLayoutIfAvailable = showParentDirectoryLayoutIfAvailable;
        }

        public boolean getShowSaveFileLayoutIfAvailable()
        {
            return mShowSaveFileLayoutIfAvailable;
        }

        public void setShowSaveFileLayoutIfAvailable(boolean showSaveFileLayoutIfAvailable)
        {
            this.mShowSaveFileLayoutIfAvailable = showSaveFileLayoutIfAvailable;
        }

        public boolean getShowFileFilterLayoutIfAvailable()
        {
            return mShowFileFilterLayoutIfAvailable;
        }

        public void setShowFileFilterLayoutIfAvailable(boolean showFileFilterLayoutIfAvailable)
        {
            this.mShowFileFilterLayoutIfAvailable = showFileFilterLayoutIfAvailable;
        }

        public boolean getShowFilesInNormalView()
        {
            return mShowFilesInNormalView;
        }

        public void setShowFilesInNormalView(boolean showFilesInNormalView)
        {
            this.mShowFilesInNormalView = showFilesInNormalView;
        }

        public boolean getShowFoldersInNormalView()
        {
            return mShowFoldersInNormalView;
        }

        public void setShowFoldersInNormalView(boolean showFoldersInNormalView)
        {
            this.mShowFoldersInNormalView = showFoldersInNormalView;
        }

        public boolean getAutoRefreshDirectorySource()
        {
            return mAutoRefreshDirectorySource;
        }

        public void setAutoRefreshDirectorySource(boolean autoRefreshDirectorySource)
        {
            this.mAutoRefreshDirectorySource = autoRefreshDirectorySource;
        }

        public String getMediaStoreImageExts()
        {
            return mMediaStoreImageExts;
        }

        public void setMediaStoreImageExts(String mediaStoreImageExts)
        {
            this.mMediaStoreImageExts = mediaStoreImageExts;
        }
        // ENCAPSULATED (DONE)
    }

    public class Theme
    {
        // ENCAP BEGIN PUBLIC GET (DONE)
        final int mUnitSp = TypedValue.COMPLEX_UNIT_SP;
        final int mUnitDip = TypedValue.COMPLEX_UNIT_DIP;
        // ENCAP END (DONE)

        Theme() { reset(); }

        void reset()
        {
            mColorBrowserTitle = Color.parseColor("#ffffff");
            mColorActionBar = Color.parseColor("#2233cc");
            mColorDeadSpaceBackground = Color.parseColor("#ffffff");
            mColorTopAccent = Color.parseColor("#000000");
            mColorBottomAccent = Color.parseColor("#000000");
            mColorCurDirBackground = Color.parseColor("#ffffff");
            mColorCurDirLabel = Color.parseColor("#0000cc");
            mColorCurDirText = Color.parseColor("#000000");
            mColorParDirBackground = Color.parseColor("#ffffff");
            mColorParDirText = Color.parseColor("#000000");
            mColorParDirSubText = Color.parseColor("#0000cc");
            mColorListBackground = Color.parseColor("#ffffff");
            mColorListAccent = Color.parseColor("#e0e0e0");
            mColorListTopAccent = Color.parseColor("#000000");
            mColorListBottomAccent = Color.parseColor("#000000");
            mColorListItemText = Color.parseColor("#000000");
            mColorListItemSubText = Color.parseColor("#0000cc");
            mColorGalleryItemText = Color.parseColor("#0000cc");
            mColorSaveFileBoxBackground = Color.parseColor("#ffffff");
            mColorSaveFileBoxText = Color.parseColor("#0000cc");
            mColorSaveFileBoxUnderline = Color.parseColor("#000000");
            mColorSaveFileBoxBottomAccent = Color.parseColor("#000000");
            mColorSaveFileButtonBackground = Color.parseColor("#aa88ff");
            mColorSaveFileButtonText = Color.parseColor("#000000");
            mColorFilterBackground = Color.parseColor("#ffffff");
            mColorFilterText = Color.parseColor("#000000");
            mColorFilterArrow = Color.parseColor("#000000");
            mColorFilterPopupBackground = Color.parseColor("#ffffff");
            mColorFilterPopupText = Color.parseColor("#000000");

            mSizeBrowserTitle = 30;
            mSizeCurDirLabel = 14;
            mSizeCurDirText = 22;
            mSizeParDirText = 19;
            mSizeParDirSubText = 13;
            mSizeListViewItemText = 19;
            mSizeListViewItemSubText = 13;
            mSizeTilesViewItemText = 13;
            mSizeGalleryViewItemText = 11;
            mSizeSaveFileText = 20;
            mSizeSaveFileButtonText = 20;
            mSizeFileFilterText = 16;
            mSizeFileFilterPopupText = 16;

            mFontMode = FontMode.AppDefault;
            //mFontCustomNorm = null;
            //mFontCustomBold = null;
            //mFontCustomItal = null;
            //mFontCustomBdIt = null;
        }

        // ENCAP BEGIN PUBLIC (DONE)
        int mColorBrowserTitle;
        int mColorActionBar;
        int mColorDeadSpaceBackground;
        int mColorTopAccent;
        int mColorBottomAccent;
        int mColorCurDirBackground;
        int mColorCurDirLabel;
        int mColorCurDirText;
        int mColorParDirBackground;
        int mColorParDirText;
        int mColorParDirSubText;
        int mColorListBackground;
        int mColorListAccent;
        int mColorListTopAccent;
        int mColorListBottomAccent;
        int mColorListItemText;
        int mColorListItemSubText;
        int mColorGalleryItemText;
        int mColorSaveFileBoxBackground;
        int mColorSaveFileBoxText;
        int mColorSaveFileBoxUnderline;
        int mColorSaveFileBoxBottomAccent;
        int mColorSaveFileButtonBackground;
        int mColorSaveFileButtonText;
        int mColorFilterBackground;
        int mColorFilterText;
        int mColorFilterArrow;
        int mColorFilterPopupBackground;
        int mColorFilterPopupText;

        float mSizeBrowserTitle;
        float mSizeCurDirLabel;
        float mSizeCurDirText;
        float mSizeParDirText;
        float mSizeParDirSubText;
        float mSizeListViewItemText;
        float mSizeListViewItemSubText;
        float mSizeTilesViewItemText;
        float mSizeGalleryViewItemText;
        float mSizeSaveFileText;
        float mSizeSaveFileButtonText;
        float mSizeFileFilterText;
        float mSizeFileFilterPopupText;

        FontMode mFontMode;
        // ENCAP END (DONE)

        //ENCAP HERE (DONE)
        public int getUnitSp()
        {
            return mUnitSp;
        }

        public int getUnitDip()
        {
            return mUnitDip;
        }

        // ENCAPSULATED (DONE)

        // ENCAP HERE (DONE)
        public int getColorBrowserTitle()
        {
            return mColorBrowserTitle;
        }

        public void setColorBrowserTitle(int colorBrowserTitle)
        {
            this.mColorBrowserTitle = colorBrowserTitle;
        }

        public int getColorActionBar()
        {
            return mColorActionBar;
        }

        public void setColorActionBar(int colorActionBar)
        {
            this.mColorActionBar = colorActionBar;
        }

        public int getColorDeadSpaceBackground()
        {
            return mColorDeadSpaceBackground;
        }

        public void setColorDeadSpaceBackground(int colorDeadSpaceBackground)
        {
            this.mColorDeadSpaceBackground = colorDeadSpaceBackground;
        }

        public int getColorTopAccent()
        {
            return mColorTopAccent;
        }

        public void setColorTopAccent(int colorTopAccent)
        {
            this.mColorTopAccent = colorTopAccent;
        }

        public int getColorBottomAccent()
        {
            return mColorBottomAccent;
        }

        public void setColorBottomAccent(int colorBottomAccent)
        {
            this.mColorBottomAccent = colorBottomAccent;
        }

        public int getColorCurDirBackground()
        {
            return mColorCurDirBackground;
        }

        public void setColorCurDirBackground(int colorCurDirBackground)
        {
            this.mColorCurDirBackground = colorCurDirBackground;
        }

        public int getColorCurDirLabel()
        {
            return mColorCurDirLabel;
        }

        public void setColorCurDirLabel(int colorCurDirLabel)
        {
            this.mColorCurDirLabel = colorCurDirLabel;
        }

        public int getColorCurDirText()
        {
            return mColorCurDirText;
        }

        public void setColorCurDirText(int colorCurDirText)
        {
            this.mColorCurDirText = colorCurDirText;
        }

        public int getColorParDirBackground()
        {
            return mColorParDirBackground;
        }

        public void setColorParDirBackground(int colorParDirBackground)
        {
            this.mColorParDirBackground = colorParDirBackground;
        }

        public int getColorParDirText()
        {
            return mColorParDirText;
        }

        public void setColorParDirText(int colorParDirText)
        {
            this.mColorParDirText = colorParDirText;
        }

        public int getColorParDirSubText()
        {
            return mColorParDirSubText;
        }

        public void setColorParDirSubText(int colorParDirSubText)
        {
            this.mColorParDirSubText = colorParDirSubText;
        }

        public int getColorListBackground()
        {
            return mColorListBackground;
        }

        public void setColorListBackground(int colorListBackground)
        {
            this.mColorListBackground = colorListBackground;
        }

        public int getColorListAccent()
        {
            return mColorListAccent;
        }

        public void setColorListAccent(int colorListAccent)
        {
            this.mColorListAccent = colorListAccent;
        }

        public int getColorListTopAccent()
        {
            return mColorListTopAccent;
        }

        public void setColorListTopAccent(int colorListTopAccent)
        {
            this.mColorListTopAccent = colorListTopAccent;
        }

        public int getColorListBottomAccent()
        {
            return mColorListBottomAccent;
        }

        public void setColorListBottomAccent(int colorListBottomAccent)
        {
            this.mColorListBottomAccent = colorListBottomAccent;
        }

        public int getColorListItemText()
        {
            return mColorListItemText;
        }

        public void setColorListItemText(int colorListItemText)
        {
            this.mColorListItemText = colorListItemText;
        }

        public int getColorListItemSubText()
        {
            return mColorListItemSubText;
        }

        public void setColorListItemSubText(int colorListItemSubText)
        {
            this.mColorListItemSubText = colorListItemSubText;
        }

        public int getColorGalleryItemText()
        {
            return mColorGalleryItemText;
        }

        public void setColorGalleryItemText(int colorGalleryItemText)
        {
            this.mColorGalleryItemText = colorGalleryItemText;
        }

        public int getColorSaveFileBoxBackground()
        {
            return mColorSaveFileBoxBackground;
        }

        public void setColorSaveFileBoxBackground(int colorSaveFileBoxBackground)
        {
            this.mColorSaveFileBoxBackground = colorSaveFileBoxBackground;
        }

        public int getColorSaveFileBoxText()
        {
            return mColorSaveFileBoxText;
        }

        public void setColorSaveFileBoxText(int colorSaveFileBoxText)
        {
            this.mColorSaveFileBoxText = colorSaveFileBoxText;
        }

        public int getColorSaveFileBoxUnderline()
        {
            return mColorSaveFileBoxUnderline;
        }

        public void setColorSaveFileBoxUnderline(int colorSaveFileBoxUnderline)
        {
            this.mColorSaveFileBoxUnderline = colorSaveFileBoxUnderline;
        }

        public int getColorSaveFileBoxBottomAccent()
        {
            return mColorSaveFileBoxBottomAccent;
        }

        public void setColorSaveFileBoxBottomAccent(int colorSaveFileBoxBottomAccent)
        {
            this.mColorSaveFileBoxBottomAccent = colorSaveFileBoxBottomAccent;
        }

        public int getColorSaveFileButtonBackground()
        {
            return mColorSaveFileButtonBackground;
        }

        public void setColorSaveFileButtonBackground(int colorSaveFileButtonBackground)
        {
            this.mColorSaveFileButtonBackground = colorSaveFileButtonBackground;
        }

        public int getColorSaveFileButtonText()
        {
            return mColorSaveFileButtonText;
        }

        public void setColorSaveFileButtonText(int colorSaveFileButtonText)
        {
            this.mColorSaveFileButtonText = colorSaveFileButtonText;
        }

        public int getColorFilterBackground()
        {
            return mColorFilterBackground;
        }

        public void setColorFilterBackground(int colorFilterBackground)
        {
            this.mColorFilterBackground = colorFilterBackground;
        }

        public int getColorFilterText()
        {
            return mColorFilterText;
        }

        public void setColorFilterText(int colorFilterText)
        {
            this.mColorFilterText = colorFilterText;
        }

        public int getColorFilterArrow()
        {
            return mColorFilterArrow;
        }

        public void setColorFilterArrow(int colorFilterArrow)
        {
            this.mColorFilterArrow = colorFilterArrow;
        }

        public int getColorFilterPopupBackground()
        {
            return mColorFilterPopupBackground;
        }

        public void setColorFilterPopupBackground(int colorFilterPopupBackground)
        {
            this.mColorFilterPopupBackground = colorFilterPopupBackground;
        }

        public int getColorFilterPopupText()
        {
            return mColorFilterPopupText;
        }

        public void setColorFilterPopupText(int colorFilterPopupText)
        {
            this.mColorFilterPopupText = colorFilterPopupText;
        }

        public float getSizeBrowserTitle()
        {
            return mSizeBrowserTitle;
        }

        public void setSizeBrowserTitle(float sizeBrowserTitle)
        {
            this.mSizeBrowserTitle = sizeBrowserTitle;
        }

        public float getSizeCurDirLabel()
        {
            return mSizeCurDirLabel;
        }

        public void setSizeCurDirLabel(float sizeCurDirLabel)
        {
            this.mSizeCurDirLabel = sizeCurDirLabel;
        }

        public float getSizeCurDirText()
        {
            return mSizeCurDirText;
        }

        public void setSizeCurDirText(float sizeCurDirText)
        {
            this.mSizeCurDirText = sizeCurDirText;
        }

        public float getSizeParDirText()
        {
            return mSizeParDirText;
        }

        public void setSizeParDirText(float sizeParDirText)
        {
            this.mSizeParDirText = sizeParDirText;
        }

        public float getSizeParDirSubText()
        {
            return mSizeParDirSubText;
        }

        public void setSizeParDirSubText(float sizeParDirSubText)
        {
            this.mSizeParDirSubText = sizeParDirSubText;
        }

        public float getSizeListViewItemText()
        {
            return mSizeListViewItemText;
        }

        public void setSizeListViewItemText(float sizeListViewItemText)
        {
            this.mSizeListViewItemText = sizeListViewItemText;
        }

        public float getSizeListViewItemSubText()
        {
            return mSizeListViewItemSubText;
        }

        public void setSizeListViewItemSubText(float sizeListViewItemSubText)
        {
            this.mSizeListViewItemSubText = sizeListViewItemSubText;
        }

        public float getSizeTilesViewItemText()
        {
            return mSizeTilesViewItemText;
        }

        public void setSizeTilesViewItemText(float sizeTilesViewItemText)
        {
            this.mSizeTilesViewItemText = sizeTilesViewItemText;
        }

        public float getSizeGalleryViewItemText()
        {
            return mSizeGalleryViewItemText;
        }

        public void setSizeGalleryViewItemText(float sizeGalleryViewItemText)
        {
            this.mSizeGalleryViewItemText = sizeGalleryViewItemText;
        }

        public float getSizeSaveFileText()
        {
            return mSizeSaveFileText;
        }

        public void setSizeSaveFileText(float sizeSaveFileText)
        {
            this.mSizeSaveFileText = sizeSaveFileText;
        }

        public float getSizeSaveFileButtonText()
        {
            return mSizeSaveFileButtonText;
        }

        public void setSizeSaveFileButtonText(float sizeSaveFileButtonText)
        {
            this.mSizeSaveFileButtonText = sizeSaveFileButtonText;
        }

        public float getSizeFileFilterText()
        {
            return mSizeFileFilterText;
        }

        public void setSizeFileFilterText(float sizeFileFilterText)
        {
            this.mSizeFileFilterText = sizeFileFilterText;
        }

        public float getSizeFileFilterPopupText()
        {
            return mSizeFileFilterPopupText;
        }

        public void setSizeFileFilterPopupText(float sizeFileFilterPopupText)
        {
            this.mSizeFileFilterPopupText = sizeFileFilterPopupText;
        }

        public FontMode getFontMode()
        {
            return mFontMode;
        }

        public void setFontMode(FontMode fontMode)
        {
            this.mFontMode = fontMode;
        }

        // ENCAPSULATED (DONE)

        private Typeface mFontAppDefaultNorm;
        private Typeface mFontAppDefaultBold;
        private Typeface mFontAppDefaultItal;
        private Typeface mFontAppDefaultBdIt;

        final Typeface mFontSystemNorm = Typeface.defaultFromStyle(Typeface.NORMAL);
        final Typeface mFontSystemBold = Typeface.defaultFromStyle(Typeface.BOLD);
        final Typeface mFontSystemItal = Typeface.defaultFromStyle(Typeface.ITALIC);
        final Typeface mFontSystemBdIt = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC);

        Typeface mFontCustomNorm;
        Typeface mFontCustomBold;
        Typeface mFontCustomItal;
        Typeface mFontCustomBdIt;

        String mFontCustomNormPath;
        String mFontCustomBoldPath;
        String mFontCustomItalPath;
        String mFontCustomBdItPath;

        public Typeface getFontNorm(AssetManager assets)
        {
            if (mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrSystem)
            {
                if (mFontCustomNorm != null) return mFontCustomNorm;
            }
            if (mFontMode == MultiBrowserOptions.FontMode.AppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault)
                return getFontAppDefaultNorm(assets);
            return mFontSystemNorm;
        }

        public Typeface getFontBold(AssetManager assets)
        {
            if (mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrSystem)
            {
                if (mFontCustomBold != null) return mFontCustomBold;
            }
            if (mFontMode == MultiBrowserOptions.FontMode.AppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault)
                return getFontAppDefaultBold(assets);
            return mFontSystemBold;
        }

        public Typeface getFontItal(AssetManager assets)
        {
            if (mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrSystem)
            {
                if (mFontCustomItal != null) return mFontCustomItal;
            }
            if (mFontMode == MultiBrowserOptions.FontMode.AppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault)
                return getFontAppDefaultItal(assets);
            return mFontSystemItal;
        }

        public Typeface getFontBdIt(AssetManager assets)
        {
            if (mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrSystem)
            {
                if (mFontCustomBdIt != null) return mFontCustomBdIt;
            }
            if (mFontMode == MultiBrowserOptions.FontMode.AppDefault || mFontMode == MultiBrowserOptions.FontMode.CustomOrAppDefault)
                return getFontAppDefaultBdIt(assets);
            return mFontSystemBdIt;
        }

        public Typeface getFontSystemNorm()
        {
            return mFontSystemNorm;
        }

        public Typeface getFontSystemBold()
        {
            return mFontSystemBold;
        }

        public Typeface getFontSystemItal()
        {
            return mFontSystemItal;
        }

        public Typeface getFontSystemBdIt()
        {
            return mFontSystemBdIt;
        }

        public Typeface getFontAppDefaultNorm(AssetManager assets)
        {
            if (mFontAppDefaultNorm == null) mFontAppDefaultNorm = Typeface.createFromAsset(assets, "fonts/cambria.ttf");
            return mFontAppDefaultNorm;
        }

        public Typeface getFontAppDefaultBold(AssetManager assets)
        {
            if (mFontAppDefaultBold == null) mFontAppDefaultBold = Typeface.createFromAsset(assets, "fonts/cambriab.ttf");
            return mFontAppDefaultBold;
        }

        public Typeface getFontAppDefaultItal(AssetManager assets)
        {
            if (mFontAppDefaultItal == null) mFontAppDefaultItal = Typeface.createFromAsset(assets, "fonts/cambriai.ttf");
            return mFontAppDefaultItal;
        }

        public Typeface getFontAppDefaultBdIt(AssetManager assets)
        {
            if (mFontAppDefaultBdIt == null) mFontAppDefaultBdIt = Typeface.createFromAsset(assets, "fonts/cambriaz.ttf");
            return mFontAppDefaultBdIt;
        }

        public Typeface getFontCustomNorm()
        {
            return mFontCustomNorm;
        }

        public void setFontCustomNorm(String fontFilePath)
        {
            try
            {
                mFontCustomNorm = Typeface.createFromFile(fontFilePath);
                mFontCustomNormPath = fontFilePath;
            }
            catch (Exception ex)
            {
                mFontCustomNorm = null;
                mFontCustomNormPath = null;
            }
        }

        public Typeface getFontCustomBold()
        {
            return mFontCustomBold;
        }

        public void setFontCustomBold(String fontFilePath)
        {
            try
            {
                mFontCustomBold = Typeface.createFromFile(fontFilePath);
                mFontCustomBoldPath = fontFilePath;
            }
            catch (Exception ex)
            {
                mFontCustomBold = null;
                mFontCustomBoldPath = null;
            }
        }

        public Typeface getFontCustomItal()
        {
            return mFontCustomItal;
        }

        public void setFontCustomItal(String fontFilePath)
        {
            try
            {
                mFontCustomItal = Typeface.createFromFile(fontFilePath);
                mFontCustomItalPath = fontFilePath;
            }
            catch (Exception ex)
            {
                mFontCustomItal = null;
                mFontCustomItalPath = null;
            }
        }

        public Typeface getFontCustomBdIt()
        {
            return mFontCustomBdIt;
        }

        public void setFontCustomBdIt(String fontFilePath)
        {
            try
            {
                mFontCustomBdIt = Typeface.createFromFile(fontFilePath);
                mFontCustomBdItPath = fontFilePath;
            }
            catch (Exception ex)
            {
                mFontCustomBdIt = null;
                mFontCustomBdItPath = null;
            }
        }


    }


}
