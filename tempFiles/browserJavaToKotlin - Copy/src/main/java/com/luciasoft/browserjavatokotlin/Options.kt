package com.luciasoft.browserjavatokotlin

import android.os.Environment

class Options
{
    private val ALL_FILES_FILTER = " All Files ( * ) |*"

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