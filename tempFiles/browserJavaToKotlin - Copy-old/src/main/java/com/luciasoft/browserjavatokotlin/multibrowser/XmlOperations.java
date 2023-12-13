package com.luciasoft.browser.multibrowser;

import android.graphics.Color;

import com.luciasoft.browser.xml.XmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

class XmlOperations
{
    static Object[][] getOptions(MultiBrowserOptions options)
    {
        MultiBrowserOptions op = options;
        MultiBrowserOptions.Advanced ad = options.mAdvancedOptions;
        MultiBrowserOptions.Theme th = options.mThemeOptions;

        Object[] opArray = new Object[]{
            op.mAllowAccessToRestrictedFolders, "AllowAccessToRestrictedFolders",
            op.mAlwaysShowDialogForSavingFile, "AlwaysShowDialogForSavingFile",
            op.mAlwaysShowDialogForSavingFolder, "AlwaysShowDialogForSavingFolder",
            op.mAlwaysShowDialogForSavingGalleryItem, "AlwaysShowDialogForSavingGalleryItem",
            op.mBrowseMode, "BrowseMode",
            op.mBrowserTitle, "BrowserTitle",
            op.mBrowserViewType, "BrowserViewType",
            op.mCreateDirOnActivityStart, "CreateDirOnActivityStart",
            op.mCurrentDir, "CurrentDir",
            op.mDefaultDir, "DefaultDir",
            op.mDefaultSaveFileName, "DefaultSaveFileName",
            op.mFileFilterIndex, "FileFilterIndex",
            op.getFileFilterString(), "FileFilterString",
            op.mGalleryViewColumnCount, "GalleryViewColumnCount",
            op.mGalleryViewSortOrder, "GalleryViewSortOrder",
            op.mNormalViewColumnCount, "NormalViewColumnCount",
            op.mNormalViewSortOrder, "NormalViewSortOrder",
            //op.mOnSelectFileForLoad, "OnSelectFileForLoad", // SPECIAL
            //op.mOnSelectFileForSave, "OnSelectFileForSave", // SPECIAL
            //op.mOnSelectFolderForLoad, "OnSelectFolderForLoad", // SPECIAL
            //op.mOnSelectFolderForSave, "OnSelectFolderForSave", // SPECIAL
            op.mShowFileNamesInGalleryView, "ShowFileNamesInGalleryView",
            op.mShowHiddenFiles, "ShowHiddenFiles",
            op.mShowHiddenFolders, "ShowHiddenFolders",
            op.mShowImagesWhileBrowsingGallery, "ShowImagesWhileBrowsingGallery",
            op.mShowImagesWhileBrowsingNormal, "ShowImagesWhileBrowsingNormal",
            op.mShowOverwriteDialogForSavingFileIfExists, "ShowOverwriteDialogForSavingFileIfExists"
        };

        Object[] adArray = new Object[]{
            ad.mAllowLongClickFileForLoad, "AllowLongClickFileForLoad",
            ad.mAllowLongClickFileForSave, "AllowLongClickFileForSave",
            ad.mAllowLongClickFolderForLoad, "AllowLongClickFolderForLoad",
            ad.mAllowLongClickFolderForSave, "AllowLongClickFolderForSave",
            ad.mAllowShortClickFileForLoad, "AllowShortClickFileForLoad",
            ad.mAllowShortClickFileForSave, "AllowShortClickFileForSave",
            ad.mAutoRefreshDirectorySource, "AutoRefreshDirectorySource",
            ad.mDebugMode, "DebugMode",
            ad.mLongClickSaveFileBehavior, "LongClickSaveFileBehavior",
            ad.mMediaStoreImageExts, "MediaStoreImageExts",
            ad.mMenuEnabled, "MenuEnabled",
            ad.mMenuOptionColumnCountEnabled, "MenuOptionColumnCountEnabled",
            ad.mMenuOptionGalleryViewEnabled, "MenuOptionGalleryViewEnabled",
            ad.mMenuOptionListViewEnabled, "MenuOptionListViewEnabled",
            ad.mMenuOptionNewFolderEnabled, "MenuOptionNewFolderEnabled",
            ad.mMenuOptionRefreshDirectoryEnabled, "MenuOptionRefreshDirectoryEnabled",
            ad.mMenuOptionResetDirectoryEnabled, "MenuOptionResetDirectoryEnabled",
            ad.mMenuOptionShowHideFileNamesEnabled, "MenuOptionShowHideFileNamesEnabled",
            ad.mMenuOptionSortOrderEnabled, "MenuOptionSortOrderEnabled",
            ad.mMenuOptionTilesViewEnabled, "MenuOptionTilesViewEnabled",
            ad.mScreenRotationMode, "ScreenRotationMode",
            ad.mShortClickSaveFileBehavior, "ShortClickSaveFileBehavior",
            ad.mShowCurrentDirectoryLayoutIfAvailable, "ShowCurrentDirectoryLayoutIfAvailable",
            ad.mShowFileDatesInListView, "ShowFileDatesInListView",
            ad.mShowFileFilterLayoutIfAvailable, "ShowFileFilterLayoutIfAvailable",
            ad.mShowFilesInNormalView, "ShowFilesInNormalView",
            ad.mShowFileSizesInListView, "ShowFileSizesInListView",
            ad.mShowFolderCountsInListView, "ShowFolderCountsInListView",
            ad.mShowFolderDatesInListView, "ShowFolderDatesInListView",
            ad.mShowFoldersInNormalView, "ShowFoldersInNormalView",
            ad.mShowParentDirectoryLayoutIfAvailable, "ShowParentDirectoryLayoutIfAvailable",
            ad.mShowSaveFileLayoutIfAvailable, "ShowSaveFileLayoutIfAvailable"
        };

        Object[] thArray = new Object[]{
            th.mColorActionBar, "ColorActionBar",
            th.mColorBottomAccent, "ColorBottomAccent",
            th.mColorBrowserTitle, "ColorBrowserTitle",
            th.mColorCurDirBackground, "ColorCurDirBackground",
            th.mColorCurDirLabel, "ColorCurDirLabel",
            th.mColorCurDirText, "ColorCurDirText",
            th.mColorDeadSpaceBackground, "ColorDeadSpaceBackground",
            th.mColorFilterArrow, "ColorFilterArrow",
            th.mColorFilterBackground, "ColorFilterBackground",
            th.mColorFilterPopupBackground, "ColorFilterPopupBackground",
            th.mColorFilterPopupText, "ColorFilterPopupText",
            th.mColorFilterText, "ColorFilterText",
            th.mColorGalleryItemText, "ColorGalleryItemText",
            th.mColorListAccent, "ColorListAccent",
            th.mColorListBackground, "ColorListBackground",
            th.mColorListBottomAccent, "ColorListBottomAccent",
            th.mColorListItemSubText, "ColorListItemSubText",
            th.mColorListItemText, "ColorListItemText",
            th.mColorListTopAccent, "ColorListTopAccent",
            th.mColorParDirBackground, "ColorParDirBackground",
            th.mColorParDirSubText, "ColorParDirSubText",
            th.mColorParDirText, "ColorParDirText",
            th.mColorSaveFileBoxBackground, "ColorSaveFileBoxBackground",
            th.mColorSaveFileBoxBottomAccent, "ColorSaveFileBoxBottomAccent",
            th.mColorSaveFileBoxText, "ColorSaveFileBoxText",
            th.mColorSaveFileBoxUnderline, "ColorSaveFileBoxUnderline",
            th.mColorSaveFileButtonBackground, "ColorSaveFileButtonBackground",
            th.mColorSaveFileButtonText, "ColorSaveFileButtonText",
            th.mColorTopAccent, "ColorTopAccent",
            th.mFontCustomBdItPath, "FontCustomBdItPath",
            th.mFontCustomBoldPath, "FontCustomBoldPath",
            th.mFontCustomItalPath, "FontCustomItalPath",
            th.mFontCustomNormPath, "FontCustomNormPath",
            th.mFontMode, "FontMode",
            th.mSizeBrowserTitle, "SizeBrowserTitle",
            th.mSizeCurDirLabel, "SizeCurDirLabel",
            th.mSizeCurDirText, "SizeCurDirText",
            th.mSizeFileFilterPopupText, "SizeFileFilterPopupText",
            th.mSizeFileFilterText, "SizeFileFilterText",
            th.mSizeGalleryViewItemText, "SizeGalleryViewItemText",
            th.mSizeListViewItemSubText, "SizeListViewItemSubText",
            th.mSizeListViewItemText, "SizeListViewItemText",
            th.mSizeParDirSubText, "SizeParDirSubText",
            th.mSizeParDirText, "SizeParDirText",
            th.mSizeSaveFileButtonText, "SizeSaveFileButtonText",
            th.mSizeSaveFileText, "SizeSaveFileText",
            th.mSizeTilesViewItemText, "SizeTilesViewItemText"
        };

        return new Object[][]{opArray, adArray, thArray};
    }

    static void loadOptions(MultiBrowserOptions options, Document doc)
    {
        MultiBrowserOptions op = options;
        MultiBrowserOptions.Advanced ad = options.mAdvancedOptions;
        MultiBrowserOptions.Theme th = options.mThemeOptions;

        Element root = doc.getDocumentElement();
        Element normal = (Element)root.getElementsByTagName("normal").item(0);
        op.mAllowAccessToRestrictedFolders = parseBooleanOption(normal, "AllowAccessToRestrictedFolders");
        op.mAlwaysShowDialogForSavingFile = parseBooleanOption(normal, "AlwaysShowDialogForSavingFile");
        op.mAlwaysShowDialogForSavingFolder = parseBooleanOption(normal, "AlwaysShowDialogForSavingFolder");
        op.mAlwaysShowDialogForSavingGalleryItem = parseBooleanOption(normal, "AlwaysShowDialogForSavingGalleryItem");
        op.mBrowseMode = MultiBrowserOptions.BrowseMode.valueOf(parseIntOption(normal, "BrowseMode"));
        op.mBrowserTitle = parseStringOption(normal, "BrowserTitle");
        op.mBrowserViewType = MultiBrowserOptions.BrowserViewType.valueOf(parseIntOption(normal, "BrowserViewType"));
        op.mCreateDirOnActivityStart = parseBooleanOption(normal, "CreateDirOnActivityStart");
        op.mCurrentDir = parseStringOption(normal, "CurrentDir");
        op.mDefaultDir = parseStringOption(normal, "DefaultDir");
        op.mDefaultSaveFileName = parseStringOption(normal, "DefaultSaveFileName");
        op.mFileFilterIndex = parseIntOption(normal, "FileFilterIndex");
        op.setFileFilter(parseStringOption(normal, "FileFilterString"));
        op.mGalleryViewColumnCount = parseIntOption(normal, "GalleryViewColumnCount");
        op.mGalleryViewSortOrder = MultiBrowserOptions.SortOrder.valueOf(parseIntOption(normal, "GalleryViewSortOrder"));
        op.mNormalViewColumnCount = parseIntOption(normal, "NormalViewColumnCount");
        op.mNormalViewSortOrder = MultiBrowserOptions.SortOrder.valueOf(parseIntOption(normal, "NormalViewSortOrder"));
        op.mShowFileNamesInGalleryView = parseBooleanOption(normal, "ShowFileNamesInGalleryView");
        op.mShowHiddenFiles = parseBooleanOption(normal, "ShowHiddenFiles");
        op.mShowHiddenFolders = parseBooleanOption(normal, "ShowHiddenFolders");
        op.mShowImagesWhileBrowsingGallery = parseBooleanOption(normal, "ShowImagesWhileBrowsingGallery");
        op.mShowImagesWhileBrowsingNormal = parseBooleanOption(normal, "ShowImagesWhileBrowsingNormal");
        op.mShowOverwriteDialogForSavingFileIfExists = parseBooleanOption(normal, "ShowOverwriteDialogForSavingFileIfExists");

        Element advanced = (Element)root.getElementsByTagName("advanced").item(0);
        ad.mAllowLongClickFileForLoad = parseBooleanOption(advanced, "AllowLongClickFileForLoad");
        ad.mAllowLongClickFileForSave = parseBooleanOption(advanced, "AllowLongClickFileForSave");
        ad.mAllowLongClickFolderForLoad = parseBooleanOption(advanced, "AllowLongClickFolderForLoad");
        ad.mAllowLongClickFolderForSave = parseBooleanOption(advanced, "AllowLongClickFolderForSave");
        ad.mAllowShortClickFileForLoad = parseBooleanOption(advanced, "AllowShortClickFileForLoad");
        ad.mAllowShortClickFileForSave = parseBooleanOption(advanced, "AllowShortClickFileForSave");
        ad.mAutoRefreshDirectorySource = parseBooleanOption(advanced, "AutoRefreshDirectorySource");
        ad.mDebugMode = parseBooleanOption(advanced, "DebugMode");
        ad.mLongClickSaveFileBehavior = MultiBrowserOptions.SaveFileBehavior.valueOf(parseIntOption(advanced, "LongClickSaveFileBehavior"));
        ad.mMediaStoreImageExts = parseStringOption(advanced, "MediaStoreImageExts");
        ad.mMenuEnabled = parseBooleanOption(advanced, "MenuEnabled");
        ad.mMenuOptionColumnCountEnabled = parseBooleanOption(advanced, "MenuOptionColumnCountEnabled");
        ad.mMenuOptionGalleryViewEnabled = parseBooleanOption(advanced, "MenuOptionGalleryViewEnabled");
        ad.mMenuOptionListViewEnabled = parseBooleanOption(advanced, "MenuOptionListViewEnabled");
        ad.mMenuOptionNewFolderEnabled = parseBooleanOption(advanced, "MenuOptionNewFolderEnabled");
        ad.mMenuOptionRefreshDirectoryEnabled = parseBooleanOption(advanced, "MenuOptionRefreshDirectoryEnabled");
        ad.mMenuOptionResetDirectoryEnabled = parseBooleanOption(advanced, "MenuOptionResetDirectoryEnabled");
        ad.mMenuOptionShowHideFileNamesEnabled = parseBooleanOption(advanced, "MenuOptionShowHideFileNamesEnabled");
        ad.mMenuOptionSortOrderEnabled = parseBooleanOption(advanced, "MenuOptionSortOrderEnabled");
        ad.mMenuOptionTilesViewEnabled = parseBooleanOption(advanced, "MenuOptionTilesViewEnabled");
        ad.mScreenRotationMode = MultiBrowserOptions.ScreenMode.valueOf(parseIntOption(advanced, "ScreenRotationMode"));
        ad.mShortClickSaveFileBehavior = MultiBrowserOptions.SaveFileBehavior.valueOf(parseIntOption(advanced, "ShortClickSaveFileBehavior"));
        ad.mShowCurrentDirectoryLayoutIfAvailable = parseBooleanOption(advanced, "ShowCurrentDirectoryLayoutIfAvailable");
        ad.mShowFileDatesInListView = parseBooleanOption(advanced, "ShowFileDatesInListView");
        ad.mShowFileFilterLayoutIfAvailable = parseBooleanOption(advanced, "ShowFileFilterLayoutIfAvailable");
        ad.mShowFilesInNormalView = parseBooleanOption(advanced, "ShowFilesInNormalView");
        ad.mShowFileSizesInListView = parseBooleanOption(advanced, "ShowFileSizesInListView");
        ad.mShowFolderCountsInListView = parseBooleanOption(advanced, "ShowFolderCountsInListView");
        ad.mShowFolderDatesInListView = parseBooleanOption(advanced, "ShowFolderDatesInListView");
        ad.mShowFoldersInNormalView = parseBooleanOption(advanced, "ShowFoldersInNormalView");
        ad.mShowParentDirectoryLayoutIfAvailable = parseBooleanOption(advanced, "ShowParentDirectoryLayoutIfAvailable");
        ad.mShowSaveFileLayoutIfAvailable = parseBooleanOption(advanced, "ShowSaveFileLayoutIfAvailable");

        Element theme = (Element)root.getElementsByTagName("theme").item(0);
        th.mColorActionBar = Color.parseColor(parseStringOption(theme, "ColorActionBar"));
        th.mColorBottomAccent = Color.parseColor(parseStringOption(theme, "ColorBottomAccent"));
        th.mColorBrowserTitle = Color.parseColor(parseStringOption(theme, "ColorBrowserTitle"));
        th.mColorCurDirBackground = Color.parseColor(parseStringOption(theme, "ColorCurDirBackground"));
        th.mColorCurDirLabel = Color.parseColor(parseStringOption(theme, "ColorCurDirLabel"));
        th.mColorCurDirText = Color.parseColor(parseStringOption(theme, "ColorCurDirText"));
        th.mColorDeadSpaceBackground = Color.parseColor(parseStringOption(theme, "ColorDeadSpaceBackground"));
        th.mColorFilterArrow = Color.parseColor(parseStringOption(theme, "ColorFilterArrow"));
        th.mColorFilterBackground = Color.parseColor(parseStringOption(theme, "ColorFilterBackground"));
        th.mColorFilterPopupBackground = Color.parseColor(parseStringOption(theme, "ColorFilterPopupBackground"));
        th.mColorFilterPopupText = Color.parseColor(parseStringOption(theme, "ColorFilterPopupText"));
        th.mColorFilterText = Color.parseColor(parseStringOption(theme, "ColorFilterText"));
        th.mColorGalleryItemText = Color.parseColor(parseStringOption(theme, "ColorGalleryItemText"));
        th.mColorListAccent = Color.parseColor(parseStringOption(theme, "ColorListAccent"));
        th.mColorListBackground = Color.parseColor(parseStringOption(theme, "ColorListBackground"));
        th.mColorListBottomAccent = Color.parseColor(parseStringOption(theme, "ColorListBottomAccent"));
        th.mColorListItemSubText = Color.parseColor(parseStringOption(theme, "ColorListItemSubText"));
        th.mColorListItemText = Color.parseColor(parseStringOption(theme, "ColorListItemText"));
        th.mColorListTopAccent = Color.parseColor(parseStringOption(theme, "ColorListTopAccent"));
        th.mColorParDirBackground = Color.parseColor(parseStringOption(theme, "ColorParDirBackground"));
        th.mColorParDirSubText = Color.parseColor(parseStringOption(theme,  "ColorParDirSubText"));
        th.mColorParDirText = Color.parseColor(parseStringOption(theme, "ColorParDirText"));
        th.mColorSaveFileBoxBackground = Color.parseColor(parseStringOption(theme, "ColorSaveFileBoxBackground"));
        th.mColorSaveFileBoxBottomAccent = Color.parseColor(parseStringOption(theme, "ColorSaveFileBoxBottomAccent"));
        th.mColorSaveFileBoxText = Color.parseColor(parseStringOption(theme, "ColorSaveFileBoxText"));
        th.mColorSaveFileBoxUnderline = Color.parseColor(parseStringOption(theme, "ColorSaveFileBoxUnderline"));
        th.mColorSaveFileButtonBackground = Color.parseColor(parseStringOption(theme, "ColorSaveFileButtonBackground"));
        th.mColorSaveFileButtonText = Color.parseColor(parseStringOption(theme, "ColorSaveFileButtonText"));
        th.mColorTopAccent = Color.parseColor(parseStringOption(theme, "ColorTopAccent"));
        th.mFontCustomBdItPath = parseStringOption(theme, "FontCustomBdItPath");
        th.mFontCustomBoldPath = parseStringOption(theme, "FontCustomBoldPath");
        th.mFontCustomItalPath = parseStringOption(theme, "FontCustomItalPath");
        th.mFontCustomNormPath = parseStringOption(theme, "FontCustomNormPath");
        th.mFontMode = MultiBrowserOptions.FontMode.valueOf(parseIntOption(theme, "FontMode"));
        th.mSizeBrowserTitle = parseFloatOption(theme, "SizeBrowserTitle");
        th.mSizeCurDirLabel = parseFloatOption(theme, "SizeCurDirLabel");
        th.mSizeCurDirText = parseFloatOption(theme, "SizeCurDirText");
        th.mSizeFileFilterPopupText = parseFloatOption(theme, "SizeFileFilterPopupText");
        th.mSizeFileFilterText = parseFloatOption(theme, "SizeFileFilterText");
        th.mSizeGalleryViewItemText = parseFloatOption(theme, "SizeGalleryViewItemText");
        th.mSizeListViewItemSubText = parseFloatOption(theme, "SizeListViewItemSubText");
        th.mSizeListViewItemText = parseFloatOption(theme, "SizeListViewItemText");
        th.mSizeParDirSubText = parseFloatOption(theme, "SizeParDirSubText");
        th.mSizeParDirText = parseFloatOption(theme, "SizeParDirText");
        th.mSizeSaveFileButtonText = parseFloatOption(theme, "SizeSaveFileButtonText");
        th.mSizeSaveFileText = parseFloatOption(theme, "SizeSaveFileText");
        th.mSizeTilesViewItemText = parseFloatOption(theme, "SizeTilesViewItemText");
    }

    static void loadOptions(MultiBrowserOptions options, String filePath) throws IOException, SAXException, ParserConfigurationException
    {
        Document doc = XmlUtils.loadXmlFile(filePath);
        loadOptions(options, doc);
    }

    static String parseStringOption(Element parentEl, String elName)
    {
        Element el = (Element)parentEl.getElementsByTagName(elName).item(0);
        String value = el.getAttribute("val");
        if (value.equalsIgnoreCase("null")) return null;
        if (value.startsWith("$")) value = value.substring(1);
        return value;
    }

    static void saveOptions(MultiBrowserOptions options, String filePath) throws ParserConfigurationException, TransformerException, IOException
    {
        Document doc = getOptionsXml(options);
        XmlUtils.saveXml(doc, filePath);
    }

    static Document getOptionsXml(MultiBrowserOptions options) throws ParserConfigurationException
    {
        Object[][] arrays = getOptions(options);
        Object[] opArray = arrays[0];
        Object[] adArray = arrays[1];
        Object[] thArray = arrays[2];

        Document doc = XmlUtils.createXmlDocument("options");
        Element root = doc.getDocumentElement();
        Element normal = doc.createElement("normal");
        Element advanced = doc.createElement("advanced");
        Element theme = doc.createElement("theme");
        root.appendChild(normal);
        root.appendChild(advanced);
        root.appendChild(theme);
        for (int i = 0; i < opArray.length; i += 2)
        {
            Element el = getElement(doc, opArray[i+1], opArray[i]);
            normal.appendChild(el);
        }
        for (int i = 0; i < adArray.length; i += 2)
        {
            Element el = getElement(doc, adArray[i+1], adArray[i]);
            advanced.appendChild(el);
        }
        for (int i = 0; i < thArray.length; i += 2)
        {
            Element el = getElement(doc, thArray[i+1], thArray[i]);
            theme.appendChild(el);
        }
        
        return doc;
    }

    static Element getElement(Document doc, Object name, Object value)
    {
        String nameStr = "" + name;
        String valStr = getStringValue(nameStr, value);
        Element el = doc.createElement(nameStr);
        el.setAttribute("val", valStr);
        return el;
    }
    
    static String getStringValue(String name, Object value)
    {
        if (value == null) return "null";
        if (value instanceof Enum) return value.toString();
        if (value instanceof String) return "$" + value;
        if (value instanceof Boolean) return (boolean)value ? "T" : "F";
        if (name.startsWith("Color")) return getColorHexString((int)value);
        return "" + value;
    }

    static String getColorHexString(int color)
    {
        String hex = Integer.toHexString(color);
        if (hex.length() == 8 && hex.startsWith("ff")) hex = hex.substring(2);
        return "#" + hex;
    }

    static boolean parseBooleanOption(Element parentEl, String elName)
    {
        Element el = (Element)parentEl.getElementsByTagName(elName).item(0);
        String str = el.getAttribute("val").toUpperCase();
        return str.equals("T");
    }

    static int parseIntOption(Element parentEl, String elName)
    {
        Element el = (Element)parentEl.getElementsByTagName(elName).item(0);
        String str = el.getAttribute("val");
        if (str.length() >= 2 && str.charAt(1) == ':') str = str.substring(0, 1);
        return Integer.parseInt(str);
    }

    static float parseFloatOption(Element parentEl, String elName)
    {
        Element el = (Element)parentEl.getElementsByTagName(elName).item(0);
        return Float.parseFloat(el.getAttribute("val"));
    }

}
