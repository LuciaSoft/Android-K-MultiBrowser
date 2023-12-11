package com.luciasoft.browserjavatokotlin

import android.os.Environment

class Options
{
    private val ALL_FILES_FILTER = " All Files ( * ) |*"

    enum class FontMode(private val value: Int)
    {
        System(1), AppDefault(2), CustomOrSystem(3), CustomOrAppDefault(4);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(fontMode: Int): FontMode
            {
                return when (fontMode)
                {
                    1 -> System
                    2 -> AppDefault
                    3 -> CustomOrSystem
                    4 -> CustomOrAppDefault
                    else -> throw Exception()
                }
            }
        }
    }

    enum class ScreenMode(private val value: Int)
    {
        NotSpecified(1), SystemDefault(2), AllowPortraitUprightAndLandscape(3),
        AllowPortraitUprightOnly(4), AllowLandscapeOnly(5), AllowAll(6);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(screenMode: Int): ScreenMode
            {
                return when (screenMode)
                {
                    1 -> NotSpecified
                    2 -> SystemDefault
                    3 -> AllowPortraitUprightAndLandscape
                    4 -> AllowPortraitUprightOnly
                    5 -> AllowLandscapeOnly
                    6 -> AllowAll
                    else -> throw Exception()
                }
            }
        }
    }

    enum class BrowseMode(private val value: Int)
    {
        LoadFilesAndOrFolders(1), SaveFilesAndOrFolders(2),
        LoadFolders(3), SaveFolders(4);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(browseMode: Int): BrowseMode
            {
                return when (browseMode)
                {
                    1 -> LoadFilesAndOrFolders
                    2 -> SaveFilesAndOrFolders
                    3 -> LoadFolders
                    4 -> SaveFolders
                    else -> throw Exception()
                }
            }
        }
    }

    enum class BrowserViewType(private val value: Int)
    {
        List(1), Tiles(2), Gallery(3);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(browserViewType: Int): BrowserViewType
            {
                return when (browserViewType)
                {
                    1 -> List
                    2 -> Tiles
                    3 -> Gallery
                    else -> throw Exception()
                }
            }
        }
    }

    enum class SortOrder(private val value: Int)
    {
        PathAscending(1), PathDescending(2), DateAscending(3),
        DateDescending(4), SizeAscending(5), SizeDescending(6);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(sortOrder: Int): SortOrder
            {
                return when (sortOrder)
                {
                    1 -> PathAscending
                    2 -> PathDescending
                    3 -> DateAscending
                    4 -> DateDescending
                    5 -> SizeAscending
                    6 -> SizeDescending
                    else -> throw Exception()
                }
            }
        }
    }

    enum class SaveFileBehavior(private val value: Int)
    {
        SaveFile(1), SendNameToSaveBoxOrSaveFile(2), SendNameToSaveBoxAndSaveFile(3);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(saveFileBehavior: Int): SaveFileBehavior
            {
                return when (saveFileBehavior)
                {
                    1 -> SaveFile
                    2 -> SendNameToSaveBoxOrSaveFile
                    3 -> SendNameToSaveBoxAndSaveFile
                    else -> throw Exception()
                }
            }
        }
    }

    fun reset()
    {
        fileFilterString = ALL_FILES_FILTER
        browserTitle = "Multi Browser"
        browseMode = BrowseMode.SaveFilesAndOrFolders
        browserViewType = BrowserViewType.List
        normalViewSortOrder = SortOrder.PathAscending
        galleryViewSortOrder = SortOrder.DateDescending
        normalViewColumnCount = 4
        galleryViewColumnCount = 3
        defaultDir = null
        defaultSaveFileName = ""
        createDirOnActivityStart = false
        alwaysShowDialogForSavingFile = true
        alwaysShowDialogForSavingFolder = true
        alwaysShowDialogForSavingGalleryItem = true
        showOverwriteDialogForSavingFileIfExists = true
        showHiddenFiles = true
        showHiddenFolders = true
        showFileNamesInGalleryView = false
        showImagesWhileBrowsingNormal = true
        showImagesWhileBrowsingGallery = true
        allowAccessToRestrictedFolders = false
        startFileFilterIndex = 0
        startDir = extStoragePath
        fileFilterString = ALL_FILES_FILTER
    }

    lateinit var browserTitle: String
    lateinit var browseMode: BrowseMode
    lateinit var browserViewType: BrowserViewType
    lateinit var normalViewSortOrder: SortOrder
    lateinit var galleryViewSortOrder: SortOrder
    var normalViewColumnCount = 0
    var galleryViewColumnCount = 0
    var defaultDir: String? = null
    lateinit var defaultSaveFileName: String
    var createDirOnActivityStart = false
    var alwaysShowDialogForSavingFile = false
    var alwaysShowDialogForSavingFolder = false
    var alwaysShowDialogForSavingGalleryItem = false
    var showOverwriteDialogForSavingFileIfExists = false
    var showHiddenFiles = false
    var showHiddenFolders = false
    var showFileNamesInGalleryView = false
    var showImagesWhileBrowsingNormal = false
    var showImagesWhileBrowsingGallery = false
    var allowAccessToRestrictedFolders = false
    var startFileFilterIndex = 0
    var startDir: String? = null
    lateinit var fileFilterString: String

    init
    {
        reset()
    }

    companion object
    {
        val extStoragePath: String?
            get() = try
            {
                Environment.getExternalStorageDirectory().canonicalPath
            }
            catch (ex: Exception)
            {
                null
            }
    }
}