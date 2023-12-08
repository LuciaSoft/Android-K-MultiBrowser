package com.luciasoft.browser

class AdvancedOptions
{
    fun reset()
    {
        debugMode = false
        screenRotationMode = Options.ScreenMode.NotSpecified
        shortClickSaveFileBehavior = Options.SaveFileBehavior.SendNameToSaveBoxOrSaveFile
        longClickSaveFileBehavior = Options.SaveFileBehavior.SendNameToSaveBoxAndSaveFile
        allowShortClickFileForLoad = true
        allowShortClickFileForSave = true
        allowLongClickFileForLoad = false
        allowLongClickFileForSave = false
        allowLongClickFolderForLoad = true
        allowLongClickFolderForSave = true
        menuEnabled = true
        menuOptionListViewEnabled = true
        menuOptionTilesViewEnabled = true
        menuOptionGalleryViewEnabled = true
        menuOptionColumnCountEnabled = true
        menuOptionSortOrderEnabled = true
        menuOptionResetDirectoryEnabled = true
        menuOptionRefreshDirectoryEnabled = false
        menuOptionShowHideFileNamesEnabled = true
        menuOptionNewFolderEnabled = true
        showCurrentDirectoryLayoutIfAvailable = true
        showParentDirectoryLayoutIfAvailable = true
        showSaveFileLayoutIfAvailable = true
        showFileFilterLayoutIfAvailable = true
        showFilesInNormalView = true
        showFoldersInNormalView = true
        showFileDatesInListView = true
        showFileSizesInListView = true
        showFolderDatesInListView = false
        showFolderCountsInListView = true
        autoRefreshDirectorySource = true
        mediaStoreImageExts = "*"
    }

    var debugMode = false
    var screenRotationMode: Options.ScreenMode? = null
    var shortClickSaveFileBehavior: Options.SaveFileBehavior? = null
    var longClickSaveFileBehavior: Options.SaveFileBehavior? = null
    var allowShortClickFileForLoad = false
    var allowShortClickFileForSave = false
    var allowLongClickFileForLoad = false
    var allowLongClickFileForSave = false
    var allowLongClickFolderForLoad = false
    var allowLongClickFolderForSave = false
    var menuEnabled = false
    var menuOptionListViewEnabled = false
    var menuOptionTilesViewEnabled = false
    var menuOptionGalleryViewEnabled = false
    var menuOptionColumnCountEnabled = false
    var menuOptionSortOrderEnabled = false
    var menuOptionResetDirectoryEnabled = false
    var menuOptionRefreshDirectoryEnabled = false
    var menuOptionShowHideFileNamesEnabled = false
    var menuOptionNewFolderEnabled = false
    var showCurrentDirectoryLayoutIfAvailable = false
    var showParentDirectoryLayoutIfAvailable = false
    var showSaveFileLayoutIfAvailable = false
    var showFileFilterLayoutIfAvailable = false
    var showFilesInNormalView = false
    var showFoldersInNormalView = false

    var showFileDatesInListView = false
    var showFileSizesInListView = false
    var showFolderDatesInListView = false
    var showFolderCountsInListView = false

    var autoRefreshDirectorySource = false

    var mediaStoreImageExts: String? = null

    init
    {
        reset()
    }
}