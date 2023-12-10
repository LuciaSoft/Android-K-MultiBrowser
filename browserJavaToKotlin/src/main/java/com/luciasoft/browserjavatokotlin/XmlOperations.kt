package com.luciasoft.browserjavatokotlin

import android.graphics.Color
import com.luciasoft.xml.XmlUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import java.util.Locale
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException

internal class XmlOperations
{
    companion object
    {
    fun getOptions(options: MultiBrowserOptions): Array<Array<Any?>>
    {
        val ad = options.mAdvancedOptions
        val th = options.mThemeOptions
        val opArray = arrayOf<Any?>(
            options.allowAccessToRestrictedFolders,
            "AllowAccessToRestrictedFolders",
            options.alwaysShowDialogForSavingFile,
            "AlwaysShowDialogForSavingFile",
            options.alwaysShowDialogForSavingFolder,
            "AlwaysShowDialogForSavingFolder",
            options.alwaysShowDialogForSavingGalleryItem,
            "AlwaysShowDialogForSavingGalleryItem",
            options.browseMode,
            "BrowseMode",
            options.browserTitle,
            "BrowserTitle",
            options.browserViewType,
            "BrowserViewType",
            options.createDirOnActivityStart,
            "CreateDirOnActivityStart",
            options.currentDir,
            "CurrentDir",
            options.defaultDir,
            "DefaultDir",
            options.defaultSaveFileName,
            "DefaultSaveFileName",
            options.fileFilterIndex,
            "FileFilterIndex",
            options.getFileFilterString(),
            "FileFilterString",
            options.galleryViewColumnCount,
            "GalleryViewColumnCount",
            options.galleryViewSortOrder,
            "GalleryViewSortOrder",
            options.normalViewColumnCount,
            "NormalViewColumnCount",
            options.normalViewSortOrder,
            "NormalViewSortOrder",  //op.mOnSelectFileForLoad, "OnSelectFileForLoad", // SPECIAL
            //op.mOnSelectFileForSave, "OnSelectFileForSave", // SPECIAL
            //op.mOnSelectFolderForLoad, "OnSelectFolderForLoad", // SPECIAL
            //op.mOnSelectFolderForSave, "OnSelectFolderForSave", // SPECIAL
            options.showFileNamesInGalleryView,
            "ShowFileNamesInGalleryView",
            options.showHiddenFiles,
            "ShowHiddenFiles",
            options.showHiddenFolders,
            "ShowHiddenFolders",
            options.showImagesWhileBrowsingGallery,
            "ShowImagesWhileBrowsingGallery",
            options.showImagesWhileBrowsingNormal,
            "ShowImagesWhileBrowsingNormal",
            options.showOverwriteDialogForSavingFileIfExists,
            "ShowOverwriteDialogForSavingFileIfExists"
        )
        val adArray = arrayOf<Any?>(
            ad.allowLongClickFileForLoad, "AllowLongClickFileForLoad",
            ad.allowLongClickFileForSave, "AllowLongClickFileForSave",
            ad.allowLongClickFolderForLoad, "AllowLongClickFolderForLoad",
            ad.allowLongClickFolderForSave, "AllowLongClickFolderForSave",
            ad.allowShortClickFileForLoad, "AllowShortClickFileForLoad",
            ad.allowShortClickFileForSave, "AllowShortClickFileForSave",
            ad.autoRefreshDirectorySource, "AutoRefreshDirectorySource",
            ad.debugMode, "DebugMode",
            ad.longClickSaveFileBehavior, "LongClickSaveFileBehavior",
            ad.mediaStoreImageExts, "MediaStoreImageExts",
            ad.menuEnabled, "MenuEnabled",
            ad.menuOptionColumnCountEnabled, "MenuOptionColumnCountEnabled",
            ad.menuOptionGalleryViewEnabled, "MenuOptionGalleryViewEnabled",
            ad.menuOptionListViewEnabled, "MenuOptionListViewEnabled",
            ad.menuOptionNewFolderEnabled, "MenuOptionNewFolderEnabled",
            ad.menuOptionRefreshDirectoryEnabled, "MenuOptionRefreshDirectoryEnabled",
            ad.menuOptionResetDirectoryEnabled, "MenuOptionResetDirectoryEnabled",
            ad.menuOptionShowHideFileNamesEnabled, "MenuOptionShowHideFileNamesEnabled",
            ad.menuOptionSortOrderEnabled, "MenuOptionSortOrderEnabled",
            ad.menuOptionTilesViewEnabled, "MenuOptionTilesViewEnabled",
            ad.screenRotationMode, "ScreenRotationMode",
            ad.shortClickSaveFileBehavior, "ShortClickSaveFileBehavior",
            ad.showCurrentDirectoryLayoutIfAvailable, "ShowCurrentDirectoryLayoutIfAvailable",
            ad.showFileDatesInListView, "ShowFileDatesInListView",
            ad.showFileFilterLayoutIfAvailable, "ShowFileFilterLayoutIfAvailable",
            ad.showFilesInNormalView, "ShowFilesInNormalView",
            ad.showFileSizesInListView, "ShowFileSizesInListView",
            ad.showFolderCountsInListView, "ShowFolderCountsInListView",
            ad.showFolderDatesInListView, "ShowFolderDatesInListView",
            ad.showFoldersInNormalView, "ShowFoldersInNormalView",
            ad.showParentDirectoryLayoutIfAvailable, "ShowParentDirectoryLayoutIfAvailable",
            ad.showSaveFileLayoutIfAvailable, "ShowSaveFileLayoutIfAvailable"
        )
        val thArray = arrayOf<Any?>(
            th.colorActionBar, "ColorActionBar",
            th.colorBottomAccent, "ColorBottomAccent",
            th.colorBrowserTitle, "ColorBrowserTitle",
            th.colorCurDirBackground, "ColorCurDirBackground",
            th.colorCurDirLabel, "ColorCurDirLabel",
            th.colorCurDirText, "ColorCurDirText",
            th.colorDeadSpaceBackground, "ColorDeadSpaceBackground",
            th.colorFilterArrow, "ColorFilterArrow",
            th.colorFilterBackground, "ColorFilterBackground",
            th.colorFilterPopupBackground, "ColorFilterPopupBackground",
            th.colorFilterPopupText, "ColorFilterPopupText",
            th.colorFilterText, "ColorFilterText",
            th.colorGalleryItemText, "ColorGalleryItemText",
            th.colorListAccent, "ColorListAccent",
            th.colorListBackground, "ColorListBackground",
            th.colorListBottomAccent, "ColorListBottomAccent",
            th.colorListItemSubText, "ColorListItemSubText",
            th.colorListItemText, "ColorListItemText",
            th.colorListTopAccent, "ColorListTopAccent",
            th.colorParDirBackground, "ColorParDirBackground",
            th.colorParDirSubText, "ColorParDirSubText",
            th.colorParDirText, "ColorParDirText",
            th.colorSaveFileBoxBackground, "ColorSaveFileBoxBackground",
            th.colorSaveFileBoxBottomAccent, "ColorSaveFileBoxBottomAccent",
            th.colorSaveFileBoxText, "ColorSaveFileBoxText",
            th.colorSaveFileBoxUnderline, "ColorSaveFileBoxUnderline",
            th.colorSaveFileButtonBackground, "ColorSaveFileButtonBackground",
            th.colorSaveFileButtonText, "ColorSaveFileButtonText",
            th.colorTopAccent, "ColorTopAccent",
            th.mFontCustomBdItPath, "FontCustomBdItPath",
            th.mFontCustomBoldPath, "FontCustomBoldPath",
            th.mFontCustomItalPath, "FontCustomItalPath",
            th.mFontCustomNormPath, "FontCustomNormPath",
            th.fontMode, "FontMode",
            th.sizeBrowserTitle, "SizeBrowserTitle",
            th.sizeCurDirLabel, "SizeCurDirLabel",
            th.sizeCurDirText, "SizeCurDirText",
            th.sizeFileFilterPopupText, "SizeFileFilterPopupText",
            th.sizeFileFilterText, "SizeFileFilterText",
            th.sizeGalleryViewItemText, "SizeGalleryViewItemText",
            th.sizeListViewItemSubText, "SizeListViewItemSubText",
            th.sizeListViewItemText, "SizeListViewItemText",
            th.sizeParDirSubText, "SizeParDirSubText",
            th.sizeParDirText, "SizeParDirText",
            th.sizeSaveFileButtonText, "SizeSaveFileButtonText",
            th.sizeSaveFileText, "SizeSaveFileText",
            th.sizeTilesViewItemText, "SizeTilesViewItemText"
        )
        return arrayOf(opArray, adArray, thArray)
    }

    fun loadOptions(
        options: MultiBrowserOptions,
        doc: Document
    )
    {
        val ad = options.mAdvancedOptions
        val th = options.mThemeOptions
        val root = doc.documentElement
        val normal = root.getElementsByTagName("normal").item(0) as Element
        options.allowAccessToRestrictedFolders =
            parseBooleanOption(
                normal,
                "AllowAccessToRestrictedFolders"
            )
        options.alwaysShowDialogForSavingFile =
            parseBooleanOption(
                normal,
                "AlwaysShowDialogForSavingFile"
            )
        options.alwaysShowDialogForSavingFolder =
            parseBooleanOption(
                normal,
                "AlwaysShowDialogForSavingFolder"
            )
        options.alwaysShowDialogForSavingGalleryItem =
            parseBooleanOption(
                normal,
                "AlwaysShowDialogForSavingGalleryItem"
            )
        options.browseMode =
            MultiBrowserOptions.BrowseMode.valueOf(
                parseIntOption(
                    normal,
                    "BrowseMode"
                )
            )
        options.browserTitle =
            parseStringOption(
                normal,
                "BrowserTitle"
            )?: throw Exception()
        options.browserViewType =
            MultiBrowserOptions.BrowserViewType.valueOf(
                parseIntOption(
                    normal,
                    "BrowserViewType"
                )
            )
        options.createDirOnActivityStart =
            parseBooleanOption(
                normal,
                "CreateDirOnActivityStart"
            )
        options.currentDir =
            parseStringOption(
                normal,
                "CurrentDir"
            )
        options.defaultDir =
            parseStringOption(
                normal,
                "DefaultDir"
            )
        options.defaultSaveFileName =
            parseStringOption(
                normal,
                "DefaultSaveFileName"
            )?: throw Exception()
        options.fileFilterIndex =
            parseIntOption(
                normal,
                "FileFilterIndex"
            )
        options.setFileFilter(
            parseStringOption(
                normal,
                "FileFilterString"
            )?: throw Exception()
        )
        options.galleryViewColumnCount =
            parseIntOption(
                normal,
                "GalleryViewColumnCount"
            )
        options.galleryViewSortOrder =
            MultiBrowserOptions.SortOrder.valueOf(
                parseIntOption(
                    normal,
                    "GalleryViewSortOrder"
                )
            )
        options.normalViewColumnCount =
            parseIntOption(
                normal,
                "NormalViewColumnCount"
            )
        options.normalViewSortOrder =
            MultiBrowserOptions.SortOrder.valueOf(
                parseIntOption(
                    normal,
                    "NormalViewSortOrder"
                )
            )
        options.showFileNamesInGalleryView =
            parseBooleanOption(
                normal,
                "ShowFileNamesInGalleryView"
            )
        options.showHiddenFiles =
            parseBooleanOption(
                normal,
                "ShowHiddenFiles"
            )
        options.showHiddenFolders =
            parseBooleanOption(
                normal,
                "ShowHiddenFolders"
            )
        options.showImagesWhileBrowsingGallery =
            parseBooleanOption(
                normal,
                "ShowImagesWhileBrowsingGallery"
            )
        options.showImagesWhileBrowsingNormal =
            parseBooleanOption(
                normal,
                "ShowImagesWhileBrowsingNormal"
            )
        options.showOverwriteDialogForSavingFileIfExists =
            parseBooleanOption(
                normal,
                "ShowOverwriteDialogForSavingFileIfExists"
            )
        val advanced = root.getElementsByTagName("advanced").item(0) as Element
        ad.allowLongClickFileForLoad =
            parseBooleanOption(
                advanced,
                "AllowLongClickFileForLoad"
            )
        ad.allowLongClickFileForSave =
            parseBooleanOption(
                advanced,
                "AllowLongClickFileForSave"
            )
        ad.allowLongClickFolderForLoad =
            parseBooleanOption(
                advanced,
                "AllowLongClickFolderForLoad"
            )
        ad.allowLongClickFolderForSave =
            parseBooleanOption(
                advanced,
                "AllowLongClickFolderForSave"
            )
        ad.allowShortClickFileForLoad =
            parseBooleanOption(
                advanced,
                "AllowShortClickFileForLoad"
            )
        ad.allowShortClickFileForSave =
            parseBooleanOption(
                advanced,
                "AllowShortClickFileForSave"
            )
        ad.autoRefreshDirectorySource =
            parseBooleanOption(
                advanced,
                "AutoRefreshDirectorySource"
            )
        ad.debugMode =
            parseBooleanOption(
                advanced,
                "DebugMode"
            )
        ad.longClickSaveFileBehavior =
            MultiBrowserOptions.SaveFileBehavior.valueOf(
                parseIntOption(
                    advanced,
                    "LongClickSaveFileBehavior"
                )
            )
        ad.mediaStoreImageExts =
            parseStringOption(
                advanced,
                "MediaStoreImageExts"
            )
        ad.menuEnabled =
            parseBooleanOption(
                advanced,
                "MenuEnabled"
            )
        ad.menuOptionColumnCountEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionColumnCountEnabled"
            )
        ad.menuOptionGalleryViewEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionGalleryViewEnabled"
            )
        ad.menuOptionListViewEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionListViewEnabled"
            )
        ad.menuOptionNewFolderEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionNewFolderEnabled"
            )
        ad.menuOptionRefreshDirectoryEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionRefreshDirectoryEnabled"
            )
        ad.menuOptionResetDirectoryEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionResetDirectoryEnabled"
            )
        ad.menuOptionShowHideFileNamesEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionShowHideFileNamesEnabled"
            )
        ad.menuOptionSortOrderEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionSortOrderEnabled"
            )
        ad.menuOptionTilesViewEnabled =
            parseBooleanOption(
                advanced,
                "MenuOptionTilesViewEnabled"
            )
        ad.screenRotationMode =
            MultiBrowserOptions.ScreenMode.valueOf(
                parseIntOption(
                    advanced,
                    "ScreenRotationMode"
                )
            )
        ad.shortClickSaveFileBehavior =
            MultiBrowserOptions.SaveFileBehavior.valueOf(
                parseIntOption(
                    advanced,
                    "ShortClickSaveFileBehavior"
                )
            )
        ad.showCurrentDirectoryLayoutIfAvailable =
            parseBooleanOption(
                advanced,
                "ShowCurrentDirectoryLayoutIfAvailable"
            )
        ad.showFileDatesInListView =
            parseBooleanOption(
                advanced,
                "ShowFileDatesInListView"
            )
        ad.showFileFilterLayoutIfAvailable =
            parseBooleanOption(
                advanced,
                "ShowFileFilterLayoutIfAvailable"
            )
        ad.showFilesInNormalView =
            parseBooleanOption(
                advanced,
                "ShowFilesInNormalView"
            )
        ad.showFileSizesInListView =
            parseBooleanOption(
                advanced,
                "ShowFileSizesInListView"
            )
        ad.showFolderCountsInListView =
            parseBooleanOption(
                advanced,
                "ShowFolderCountsInListView"
            )
        ad.showFolderDatesInListView =
            parseBooleanOption(
                advanced,
                "ShowFolderDatesInListView"
            )
        ad.showFoldersInNormalView =
            parseBooleanOption(
                advanced,
                "ShowFoldersInNormalView"
            )
        ad.showParentDirectoryLayoutIfAvailable =
            parseBooleanOption(
                advanced,
                "ShowParentDirectoryLayoutIfAvailable"
            )
        ad.showSaveFileLayoutIfAvailable =
            parseBooleanOption(
                advanced,
                "ShowSaveFileLayoutIfAvailable"
            )
        val theme = root.getElementsByTagName("theme").item(0) as Element
        th.colorActionBar = Color.parseColor(
            parseStringOption(
                theme,
                "ColorActionBar"
            )
        )
        th.colorBottomAccent = Color.parseColor(
            parseStringOption(
                theme,
                "ColorBottomAccent"
            )
        )
        th.colorBrowserTitle = Color.parseColor(
            parseStringOption(
                theme,
                "ColorBrowserTitle"
            )
        )
        th.colorCurDirBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorCurDirBackground"
            )
        )
        th.colorCurDirLabel = Color.parseColor(
            parseStringOption(
                theme,
                "ColorCurDirLabel"
            )
        )
        th.colorCurDirText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorCurDirText"
            )
        )
        th.colorDeadSpaceBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorDeadSpaceBackground"
            )
        )
        th.colorFilterArrow = Color.parseColor(
            parseStringOption(
                theme,
                "ColorFilterArrow"
            )
        )
        th.colorFilterBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorFilterBackground"
            )
        )
        th.colorFilterPopupBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorFilterPopupBackground"
            )
        )
        th.colorFilterPopupText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorFilterPopupText"
            )
        )
        th.colorFilterText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorFilterText"
            )
        )
        th.colorGalleryItemText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorGalleryItemText"
            )
        )
        th.colorListAccent = Color.parseColor(
            parseStringOption(
                theme,
                "ColorListAccent"
            )
        )
        th.colorListBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorListBackground"
            )
        )
        th.colorListBottomAccent = Color.parseColor(
            parseStringOption(
                theme,
                "ColorListBottomAccent"
            )
        )
        th.colorListItemSubText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorListItemSubText"
            )
        )
        th.colorListItemText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorListItemText"
            )
        )
        th.colorListTopAccent = Color.parseColor(
            parseStringOption(
                theme,
                "ColorListTopAccent"
            )
        )
        th.colorParDirBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorParDirBackground"
            )
        )
        th.colorParDirSubText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorParDirSubText"
            )
        )
        th.colorParDirText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorParDirText"
            )
        )
        th.colorSaveFileBoxBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorSaveFileBoxBackground"
            )
        )
        th.colorSaveFileBoxBottomAccent = Color.parseColor(
            parseStringOption(
                theme,
                "ColorSaveFileBoxBottomAccent"
            )
        )
        th.colorSaveFileBoxText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorSaveFileBoxText"
            )
        )
        th.colorSaveFileBoxUnderline = Color.parseColor(
            parseStringOption(
                theme,
                "ColorSaveFileBoxUnderline"
            )
        )
        th.colorSaveFileButtonBackground = Color.parseColor(
            parseStringOption(
                theme,
                "ColorSaveFileButtonBackground"
            )
        )
        th.colorSaveFileButtonText = Color.parseColor(
            parseStringOption(
                theme,
                "ColorSaveFileButtonText"
            )
        )
        th.colorTopAccent = Color.parseColor(
            parseStringOption(
                theme,
                "ColorTopAccent"
            )
        )
        th.mFontCustomBdItPath =
            parseStringOption(
                theme,
                "FontCustomBdItPath"
            )
        th.mFontCustomBoldPath =
            parseStringOption(
                theme,
                "FontCustomBoldPath"
            )
        th.mFontCustomItalPath =
            parseStringOption(
                theme,
                "FontCustomItalPath"
            )
        th.mFontCustomNormPath =
            parseStringOption(
                theme,
                "FontCustomNormPath"
            )
        th.fontMode =
            MultiBrowserOptions.FontMode.valueOf(
                parseIntOption(
                    theme,
                    "FontMode"
                )
            )
        th.sizeBrowserTitle =
            parseFloatOption(
                theme,
                "SizeBrowserTitle"
            )
        th.sizeCurDirLabel =
            parseFloatOption(
                theme,
                "SizeCurDirLabel"
            )
        th.sizeCurDirText =
            parseFloatOption(
                theme,
                "SizeCurDirText"
            )
        th.sizeFileFilterPopupText =
            parseFloatOption(
                theme,
                "SizeFileFilterPopupText"
            )
        th.sizeFileFilterText =
            parseFloatOption(
                theme,
                "SizeFileFilterText"
            )
        th.sizeGalleryViewItemText =
            parseFloatOption(
                theme,
                "SizeGalleryViewItemText"
            )
        th.sizeListViewItemSubText =
            parseFloatOption(
                theme,
                "SizeListViewItemSubText"
            )
        th.sizeListViewItemText =
            parseFloatOption(
                theme,
                "SizeListViewItemText"
            )
        th.sizeParDirSubText =
            parseFloatOption(
                theme,
                "SizeParDirSubText"
            )
        th.sizeParDirText =
            parseFloatOption(
                theme,
                "SizeParDirText"
            )
        th.sizeSaveFileButtonText =
            parseFloatOption(
                theme,
                "SizeSaveFileButtonText"
            )
        th.sizeSaveFileText =
            parseFloatOption(
                theme,
                "SizeSaveFileText"
            )
        th.sizeTilesViewItemText =
            parseFloatOption(
                theme,
                "SizeTilesViewItemText"
            )
    }

    @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
    fun loadOptions(options: MultiBrowserOptions, filePath: String)
    {
        val doc = XmlUtils.loadXmlFile(filePath)
        loadOptions(options, doc)
    }

    fun parseStringOption(parentEl: Element, elName: String): String?
    {
        val el = parentEl.getElementsByTagName(elName).item(0) as Element
        var value = el.getAttribute("val")
        if (value.equals("null", ignoreCase = true)) return null
        if (value.startsWith("$")) value = value.substring(1)
        return value
    }

    @Throws(ParserConfigurationException::class, TransformerException::class, IOException::class)
    fun saveOptions(options: MultiBrowserOptions, filePath: String)
    {
        val doc = getOptionsXml(options)
        XmlUtils.saveXml(doc, filePath)
    }

    @Throws(ParserConfigurationException::class)
    fun getOptionsXml(options: MultiBrowserOptions): Document
    {
        val arrays = getOptions(options)
        val opArray = arrays[0]
        val adArray = arrays[1]
        val thArray = arrays[2]
        val doc = XmlUtils.createXmlDocument("options")
        val root = doc.documentElement
        val normal = doc.createElement("normal")
        val advanced = doc.createElement("advanced")
        val theme = doc.createElement("theme")
        root.appendChild(normal)
        root.appendChild(advanced)
        root.appendChild(theme)
        run {
            var i = 0
            while (i < opArray.size)
            {
                val el = getElement(doc, opArray[i + 1], opArray[i])
                normal.appendChild(el)
                i += 2
            }
        }
        run {
            var i = 0
            while (i < adArray.size)
            {
                val el = getElement(doc, adArray[i + 1], adArray[i])
                advanced.appendChild(el)
                i += 2
            }
        }
        var i = 0
        while (i < thArray.size)
        {
            val el = getElement(doc, thArray[i + 1], thArray[i])
            theme.appendChild(el)
            i += 2
        }
        return doc
    }

    fun getElement(doc: Document, name: Any?, value: Any?): Element
    {
        val nameStr = "" + name
        val valStr = getStringValue(nameStr, value)
        val el = doc.createElement(nameStr)
        el.setAttribute("val", valStr)
        return el
    }

    fun getStringValue(name: String, value: Any?): String
    {
        if (value == null) return "null"
        if (value is Enum<*>) return value.toString()
        if (value is String) return "$$value"
        if (value is Boolean) return if (value) "T" else "F"
        return if (name.startsWith("Color")) getColorHexString(
            value as Int
        )
        else "" + value
    }

    fun getColorHexString(color: Int): String
    {
        var hex = Integer.toHexString(color)
        if (hex.length == 8 && hex.startsWith("ff")) hex = hex.substring(2)
        return "#$hex"
    }

    fun parseBooleanOption(parentEl: Element, elName: String?): Boolean
    {
        val el = parentEl.getElementsByTagName(elName).item(0) as Element
        val str = el.getAttribute("val").uppercase(Locale.getDefault())
        return str == "T"
    }

    fun parseIntOption(parentEl: Element, elName: String?): Int
    {
        val el = parentEl.getElementsByTagName(elName).item(0) as Element
        var str = el.getAttribute("val")
        if (str.length >= 2 && str[1] == ':') str = str.substring(0, 1)
        return str.toInt()
    }

    fun parseFloatOption(parentEl: Element, elName: String?): Float
    {
        val el = parentEl.getElementsByTagName(elName).item(0) as Element
        return el.getAttribute("val").toFloat()
    }
    
    }
}