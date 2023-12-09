package com.luciasoft.browserjavatokotlin

import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Environment
import android.util.TypedValue
import java.io.IOException
import java.util.Arrays
import java.util.Locale
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException

class MultiBrowserOptions
{
    val ALL_FILES_FILTER = " All Files ( * ) |*"
    @Throws(IOException::class, TransformerException::class, ParserConfigurationException::class)
    fun saveXml(filePath: String)
    {
        XmlOperations.saveOptions(this, filePath)
    }

    enum class FontMode(val value: Int)
    {
        System(1), AppDefault(2), CustomOrSystem(3), CustomOrAppDefault(4);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(fontMode: Int): FontMode?
            {
                return when (fontMode)
                {
                    1 -> System
                    2 -> AppDefault
                    3 -> CustomOrSystem
                    4 -> CustomOrAppDefault
                    else -> null
                }
            }
        }
    }

    enum class ScreenMode(val value: Int)
    {
        NotSpecified(1), SystemDefault(2), AllowPortraitUprightAndLandscape(3), AllowPortraitUprightOnly(
        4
    ),
        AllowLandscapeOnly(5), AllowAll(6);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(screenMode: Int): ScreenMode?
            {
                return when (screenMode)
                {
                    1 -> NotSpecified
                    2 -> SystemDefault
                    3 -> AllowPortraitUprightAndLandscape
                    4 -> AllowPortraitUprightOnly
                    5 -> AllowLandscapeOnly
                    6 -> AllowAll
                    else -> null
                }
            }
        }
    }

    enum class BrowseMode(val value: Int)
    {
        LoadFilesAndOrFolders(1), SaveFilesAndOrFolders(2), LoadFolders(3), SaveFolders(4);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(browseMode: Int): BrowseMode?
            {
                return when (browseMode)
                {
                    1 -> LoadFilesAndOrFolders
                    2 -> SaveFilesAndOrFolders
                    3 -> LoadFolders
                    4 -> SaveFolders
                    else -> null
                }
            }
        }
    }

    enum class BrowserViewType(val value: Int)
    {
        List(1), Tiles(2), Gallery(3);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(browserViewType: Int): BrowserViewType?
            {
                return when (browserViewType)
                {
                    1 -> List
                    2 -> Tiles
                    3 -> Gallery
                    else -> null
                }
            }
        }
    }

    enum class SortOrder(val value: Int)
    {
        PathAscending(1), PathDescending(2), DateAscending(3), DateDescending(4), SizeAscending(5), SizeDescending(
        6
    );

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(sortOrder: Int): SortOrder?
            {
                return when (sortOrder)
                {
                    1 -> PathAscending
                    2 -> PathDescending
                    3 -> DateAscending
                    4 -> DateDescending
                    5 -> SizeAscending
                    6 -> SizeDescending
                    else -> null
                }
            }
        }
    }

    enum class SaveFileBehavior(val value: Int)
    {
        SaveFile(1), SendNameToSaveBoxOrSaveFile(2), SendNameToSaveBoxAndSaveFile(3);

        override fun toString(): String
        {
            return "$value:$name"
        }

        companion object
        {
            @JvmStatic
            fun valueOf(saveFileBehavior: Int): SaveFileBehavior?
            {
                return when (saveFileBehavior)
                {
                    1 -> SaveFile
                    2 -> SendNameToSaveBoxOrSaveFile
                    3 -> SendNameToSaveBoxAndSaveFile
                    else -> null
                }
            }
        }
    }

    @JvmField
    var mAdvancedOptions: Advanced
    @JvmField
    var mThemeOptions: Theme
    fun advanced(): Advanced
    {
        return mAdvancedOptions
    }

    fun theme(): Theme
    {
        return mThemeOptions
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
        onSelectFileForLoad = null
        onSelectFileForSave = null
        onSelectFolderForLoad = null
        onSelectFolderForSave = null
        mAdvancedOptions.reset()
        mThemeOptions.reset()
    }

    // ENCAP HERE (DONE)
    // ENCAP BEGIN PUBLIC (DONE)
    var browserTitle: String? = null
    var browseMode: BrowseMode? = null
    var browserViewType: BrowserViewType? = null
    lateinit var normalViewSortOrder: SortOrder
    lateinit var galleryViewSortOrder: SortOrder
    var normalViewColumnCount = 0
    var galleryViewColumnCount = 0
    var currentDir: String? = null
    var defaultDir: String? = null
    var defaultSaveFileName: String? = null
    var createDirOnActivityStart = false
    var fileFilterIndex = 0
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
    var onSelectFileForLoad: OnSelectItem? = null
    var onSelectFileForSave: OnSelectItem? = null
    var onSelectFolderForLoad: OnSelectItem? = null
    var onSelectFolderForSave: OnSelectItem? = null

    // ENCAP END (DONE)
    //@JvmField
    lateinit var mFileFilters: Array<Array<String>>
    //@JvmField
    lateinit var mFileFilterDescrips: Array<String>

    init
    {
        setFileFilter(ALL_FILES_FILTER)
        mAdvancedOptions = Advanced()
        mThemeOptions = Theme()
        reset()
    }

    // ENCAPSULATED (DONE)
    fun setFileFilter(filterString: String)
    {
        val array =
            filterString.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(array.size % 2 == 0) { "The filter string must be divisible by 2." }
        val filters = Array(array.size / 2) { ""}
        val descriptions = Array(array.size / 2) { "" }
        var i = 0
        while (i < array.size)
        {
            descriptions[i / 2] = array[i]
            filters[i / 2] = array[i + 1]
            i += 2
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
            val exts =
                filters[i]!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
        for (i in newDescrips.indices) newDescrips[i] =
            " " + newDescrips[i]!!.trim { it <= ' ' } + " "
        mFileFilters = filterArray
        mFileFilterDescrips = newDescrips
    }

    val fileFilterString: String
        get()
        {
            var result = ""
            for (i in mFileFilters.indices)
            {
                val filters = mFileFilters[i]
                val descrip = mFileFilterDescrips[i]!!.trim { it <= ' ' }
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

    inner class Advanced internal constructor()
    {
        fun reset()
        {
            debugMode = false
            screenRotationMode = ScreenMode.NotSpecified
            shortClickSaveFileBehavior = SaveFileBehavior.SendNameToSaveBoxOrSaveFile
            longClickSaveFileBehavior = SaveFileBehavior.SendNameToSaveBoxAndSaveFile
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

        // ENCAPSULATED (DONE)
        // ENCAP BEGIN PUBLIC (DONE)
        var debugMode = false
        var screenRotationMode: ScreenMode? = null
        var shortClickSaveFileBehavior: SaveFileBehavior? = null
        var longClickSaveFileBehavior: SaveFileBehavior? = null
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

        // ENCAP END (DONE)
        // ENCAP HERE (DONE)
        // ENCAP BEGIN PUBLIC (DONE) NEW OPTIONS!!!
        var showFileDatesInListView = false
        var showFileSizesInListView = false
        var showFolderDatesInListView = false
        var showFolderCountsInListView = false

        // ENCAP END (DONE)
        var autoRefreshDirectorySource = false

        // ENCAPSULATED (DONE)
        var mediaStoreImageExts: String? = null

        init
        {
            reset()
        }
    }

    inner class Theme internal constructor()
    {
        // ENCAP END (DONE)
        //ENCAP HERE (DONE)
        // ENCAP BEGIN PUBLIC GET (DONE)
        val unitSp = TypedValue.COMPLEX_UNIT_SP
        val unitDip = TypedValue.COMPLEX_UNIT_DIP
        fun reset()
        {
            colorBrowserTitle = Color.parseColor("#ffffff")
            colorActionBar = Color.parseColor("#2233cc")
            colorDeadSpaceBackground = Color.parseColor("#ffffff")
            colorTopAccent = Color.parseColor("#000000")
            colorBottomAccent = Color.parseColor("#000000")
            colorCurDirBackground = Color.parseColor("#ffffff")
            colorCurDirLabel = Color.parseColor("#0000cc")
            colorCurDirText = Color.parseColor("#000000")
            colorParDirBackground = Color.parseColor("#ffffff")
            colorParDirText = Color.parseColor("#000000")
            colorParDirSubText = Color.parseColor("#0000cc")
            colorListBackground = Color.parseColor("#ffffff")
            colorListAccent = Color.parseColor("#e0e0e0")
            colorListTopAccent = Color.parseColor("#000000")
            colorListBottomAccent = Color.parseColor("#000000")
            colorListItemText = Color.parseColor("#000000")
            colorListItemSubText = Color.parseColor("#0000cc")
            colorGalleryItemText = Color.parseColor("#0000cc")
            colorSaveFileBoxBackground = Color.parseColor("#ffffff")
            colorSaveFileBoxText = Color.parseColor("#0000cc")
            colorSaveFileBoxUnderline = Color.parseColor("#000000")
            colorSaveFileBoxBottomAccent = Color.parseColor("#000000")
            colorSaveFileButtonBackground = Color.parseColor("#aa88ff")
            colorSaveFileButtonText = Color.parseColor("#000000")
            colorFilterBackground = Color.parseColor("#ffffff")
            colorFilterText = Color.parseColor("#000000")
            colorFilterArrow = Color.parseColor("#000000")
            colorFilterPopupBackground = Color.parseColor("#ffffff")
            colorFilterPopupText = Color.parseColor("#000000")
            sizeBrowserTitle = 30f
            sizeCurDirLabel = 14f
            sizeCurDirText = 22f
            sizeParDirText = 19f
            sizeParDirSubText = 13f
            sizeListViewItemText = 19f
            sizeListViewItemSubText = 13f
            sizeTilesViewItemText = 13f
            sizeGalleryViewItemText = 11f
            sizeSaveFileText = 20f
            sizeSaveFileButtonText = 20f
            sizeFileFilterText = 16f
            sizeFileFilterPopupText = 16f
            fontMode = FontMode.AppDefault
            //mFontCustomNorm = null;
            //mFontCustomBold = null;
            //mFontCustomItal = null;
            //mFontCustomBdIt = null;
        }

        // ENCAPSULATED (DONE)
        // ENCAP HERE (DONE)
        // ENCAP BEGIN PUBLIC (DONE)
        var colorBrowserTitle = 0
        var colorActionBar = 0
        var colorDeadSpaceBackground = 0
        var colorTopAccent = 0
        var colorBottomAccent = 0
        var colorCurDirBackground = 0
        var colorCurDirLabel = 0
        var colorCurDirText = 0
        var colorParDirBackground = 0
        var colorParDirText = 0
        var colorParDirSubText = 0
        var colorListBackground = 0
        var colorListAccent = 0
        var colorListTopAccent = 0
        var colorListBottomAccent = 0
        var colorListItemText = 0
        var colorListItemSubText = 0
        var colorGalleryItemText = 0
        var colorSaveFileBoxBackground = 0
        var colorSaveFileBoxText = 0
        var colorSaveFileBoxUnderline = 0
        var colorSaveFileBoxBottomAccent = 0
        var colorSaveFileButtonBackground = 0
        var colorSaveFileButtonText = 0
        var colorFilterBackground = 0
        var colorFilterText = 0
        var colorFilterArrow = 0
        var colorFilterPopupBackground = 0
        var colorFilterPopupText = 0
        var sizeBrowserTitle = 0f
        var sizeCurDirLabel = 0f
        var sizeCurDirText = 0f
        var sizeParDirText = 0f
        var sizeParDirSubText = 0f
        var sizeListViewItemText = 0f
        var sizeListViewItemSubText = 0f
        var sizeTilesViewItemText = 0f
        var sizeGalleryViewItemText = 0f
        var sizeSaveFileText = 0f
        var sizeSaveFileButtonText = 0f
        var sizeFileFilterText = 0f
        var sizeFileFilterPopupText = 0f
        var fontMode: FontMode? = null

        // ENCAPSULATED (DONE)
        private var mFontAppDefaultNorm: Typeface? = null
        private var mFontAppDefaultBold: Typeface? = null
        private var mFontAppDefaultItal: Typeface? = null
        private var mFontAppDefaultBdIt: Typeface? = null
        val fontSystemNorm = Typeface.defaultFromStyle(Typeface.NORMAL)
        val fontSystemBold = Typeface.defaultFromStyle(Typeface.BOLD)
        val fontSystemItal = Typeface.defaultFromStyle(Typeface.ITALIC)
        val fontSystemBdIt = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
        var fontCustomNorm: Typeface? = null
        var fontCustomBold: Typeface? = null
        var fontCustomItal: Typeface? = null
        var fontCustomBdIt: Typeface? = null
        @JvmField
        var mFontCustomNormPath: String? = null
        @JvmField
        var mFontCustomBoldPath: String? = null
        @JvmField
        var mFontCustomItalPath: String? = null
        @JvmField
        var mFontCustomBdItPath: String? = null

        // ENCAP END (DONE)
        init
        {
            reset()
        }

        fun getFontNorm(assets: AssetManager?): Typeface?
        {
            if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
            {
                if (fontCustomNorm != null) return fontCustomNorm
            }
            return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultNorm(
                assets
            )
            else fontSystemNorm
        }

        fun getFontBold(assets: AssetManager?): Typeface?
        {
            if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
            {
                if (fontCustomBold != null) return fontCustomBold
            }
            return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultBold(
                assets
            )
            else fontSystemBold
        }

        fun getFontItal(assets: AssetManager?): Typeface?
        {
            if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
            {
                if (fontCustomItal != null) return fontCustomItal
            }
            return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultItal(
                assets
            )
            else fontSystemItal
        }

        fun getFontBdIt(assets: AssetManager?): Typeface?
        {
            if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
            {
                if (fontCustomBdIt != null) return fontCustomBdIt
            }
            return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultBdIt(
                assets
            )
            else fontSystemBdIt
        }

        fun getFontAppDefaultNorm(assets: AssetManager?): Typeface?
        {
            if (mFontAppDefaultNorm == null) mFontAppDefaultNorm =
                Typeface.createFromAsset(assets, "fonts/cambria.ttf")
            return mFontAppDefaultNorm
        }

        fun getFontAppDefaultBold(assets: AssetManager?): Typeface?
        {
            if (mFontAppDefaultBold == null) mFontAppDefaultBold =
                Typeface.createFromAsset(assets, "fonts/cambriab.ttf")
            return mFontAppDefaultBold
        }

        fun getFontAppDefaultItal(assets: AssetManager?): Typeface?
        {
            if (mFontAppDefaultItal == null) mFontAppDefaultItal =
                Typeface.createFromAsset(assets, "fonts/cambriai.ttf")
            return mFontAppDefaultItal
        }

        fun getFontAppDefaultBdIt(assets: AssetManager?): Typeface?
        {
            if (mFontAppDefaultBdIt == null) mFontAppDefaultBdIt =
                Typeface.createFromAsset(assets, "fonts/cambriaz.ttf")
            return mFontAppDefaultBdIt
        }

        fun setFontCustomNorm(fontFilePath: String?)
        {
            try
            {
                fontCustomNorm = Typeface.createFromFile(fontFilePath)
                mFontCustomNormPath = fontFilePath
            }
            catch (ex: Exception)
            {
                fontCustomNorm = null
                mFontCustomNormPath = null
            }
        }

        fun setFontCustomBold(fontFilePath: String?)
        {
            try
            {
                fontCustomBold = Typeface.createFromFile(fontFilePath)
                mFontCustomBoldPath = fontFilePath
            }
            catch (ex: Exception)
            {
                fontCustomBold = null
                mFontCustomBoldPath = null
            }
        }

        fun setFontCustomItal(fontFilePath: String?)
        {
            try
            {
                fontCustomItal = Typeface.createFromFile(fontFilePath)
                mFontCustomItalPath = fontFilePath
            }
            catch (ex: Exception)
            {
                fontCustomItal = null
                mFontCustomItalPath = null
            }
        }

        fun setFontCustomBdIt(fontFilePath: String?)
        {
            try
            {
                fontCustomBdIt = Typeface.createFromFile(fontFilePath)
                mFontCustomBdItPath = fontFilePath
            }
            catch (ex: Exception)
            {
                fontCustomBdIt = null
                mFontCustomBdItPath = null
            }
        }
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