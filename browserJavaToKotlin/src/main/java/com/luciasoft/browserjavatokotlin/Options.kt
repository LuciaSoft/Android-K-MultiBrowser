package com.luciasoft.browserjavatokotlin

import android.os.Environment
import java.util.Arrays
import java.util.Locale

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
        setFileFilter(ALL_FILES_FILTER)
        browserTitle = "Multi Browser"
        browseMode = BrowseMode.SaveFilesAndOrFolders
        browserViewType = BrowserViewType.List
        normalViewSortOrder = SortOrder.PathAscending
        galleryViewSortOrder = SortOrder.DateDescending
        normalViewColumnCount = 4
        galleryViewColumnCount = 3
        currentDir = extStoragePath
        defaultDir = null
        defaultSaveFileName = ""
        createDirOnActivityStart = false
        fileFilterIndex = 0
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
    }

    var currentDir: String? = null // MOVE TO DATA HOLDER
    var fileFilterIndex = 0 // MOVE TO DATA HOLDER
    lateinit var mFileFilters: Array<Array<String>> // MOVE TO DATA HOLDER
    lateinit var mFileFilterDescrips: Array<String> // MOVE TO DATA HOLDER

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

    init
    {
        reset()
    }
    
    var mFileFilterString: String
        get() = getFileFilterString()
        set(value) { setFileFilter(value) }

    fun setFileFilter(filterString: String)
    {
        val array = filterString.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(array.size % 2 == 0) { "The filter string must be divisible by 2." }
        val filters = Array(array.size / 2) { "" }
        val descriptions = Array(array.size / 2) { "" }
        for (i in array.indices step(2))
        {
            descriptions[i / 2] = array[i]
            filters[i / 2] = array[i + 1]
        }
        setFileFilter(filters, descriptions)
    }

    @Throws(IllegalArgumentException::class)
    fun setFileFilter(filters: Array<String>, descriptions: Array<String>)
    {
        require(filters.size == descriptions.size) { "The filters and the descriptions must have the same length." }
        val filterArray: Array<Array<String>> = Array(filters.size) { emptyArray() }
        for (i in filters.indices)
        {
            val list = ArrayList<String>()
            val exts = filters[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (j in exts.indices)
            {
                var ext = exts[j].trim { it <= ' ' }.lowercase(Locale.getDefault())
                if (ext == "*" || ext == "*.*")
                {
                    list.clear()
                    list.add("*")
                    break
                }
                while (ext.startsWith("*")) ext = ext.substring(1)
                if (ext.isEmpty()) continue
                if (!ext.startsWith(".")) ext = ".$ext"
                list.add(ext)
            }
            filterArray[i] = list.toTypedArray()
        }
        val newDescrips = Arrays.copyOf(descriptions, descriptions.size)
        for (i in newDescrips.indices) newDescrips[i] = " " + newDescrips[i].trim { it <= ' ' } + " "
        mFileFilters = filterArray
        mFileFilterDescrips = newDescrips
    }

    fun getFileFilterString(): String
    {
        var result = ""
        for (i in mFileFilters.indices)
        {
            val filters = mFileFilters[i]
            val descrip = mFileFilterDescrips[i].trim { it <= ' ' }
            if (i > 0) result += "|"
            result += " $descrip |"
            for (j in filters.indices)
            {
                var filter = filters[j].trim { it <= ' ' }
                if (!filter.startsWith("*"))
                {
                    filter = if (!filter.startsWith(".")) "*.$filter" else "*$filter"
                }
                if (j > 0) result += ","
                result += filter
            }
        }
        return result
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