package com.luciasoft.browser

import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue

class ThemeOptions
{
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
        //fontCustomNorm = null;
        //fontCustomBold = null;
        //fontCustomItal = null;
        //fontCustomBdIt = null;
    }

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
    lateinit var fontMode: FontMode

    private var fontAppDefaultNorm: Typeface? = null
    private var fontAppDefaultBold: Typeface? = null
    private var fontAppDefaultItal: Typeface? = null
    private var fontAppDefaultBdIt: Typeface? = null
    private val fontSystemNorm: Typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    private val fontSystemBold: Typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    private val fontSystemItal: Typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
    private val fontSystemBdIt: Typeface = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
    private var fontCustomNorm: Typeface? = null
    private var fontCustomBold: Typeface? = null
    private var fontCustomItal: Typeface? = null
    private var fontCustomBdIt: Typeface? = null
    var fontCustomNormPath: String? = null
    var fontCustomBoldPath: String? = null
    var fontCustomItalPath: String? = null
    var fontCustomBdItPath: String? = null

    init
    {
        reset()
    }

    fun getFontNorm(assets: AssetManager): Typeface
    {
        if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
        {
            if (fontCustomNorm != null) return fontCustomNorm!!
        }
        return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultNorm(assets)
        else fontSystemNorm
    }

    fun getFontBold(assets: AssetManager): Typeface
    {
        if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
        {
            if (fontCustomBold != null) return fontCustomBold!!
        }
        return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultBold(assets)
        else fontSystemBold
    }

    fun getFontItal(assets: AssetManager): Typeface
    {
        if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
        {
            if (fontCustomItal != null) return fontCustomItal!!
        }
        return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultItal(assets)
        else fontSystemItal
    }

    fun getFontBdIt(assets: AssetManager): Typeface
    {
        if (fontMode == FontMode.CustomOrAppDefault || fontMode == FontMode.CustomOrSystem)
        {
            if (fontCustomBdIt != null) return fontCustomBdIt!!
        }
        return if (fontMode == FontMode.AppDefault || fontMode == FontMode.CustomOrAppDefault) getFontAppDefaultBdIt(assets)
        else fontSystemBdIt
    }

    fun getFontAppDefaultNorm(assets: AssetManager): Typeface
    {
        if (fontAppDefaultNorm == null) fontAppDefaultNorm =
            Typeface.createFromAsset(assets, "fonts/cambria.ttf")
        return fontAppDefaultNorm!!
    }

    fun getFontAppDefaultBold(assets: AssetManager): Typeface
    {
        if (fontAppDefaultBold == null) fontAppDefaultBold =
            Typeface.createFromAsset(assets, "fonts/cambriab.ttf")
        return fontAppDefaultBold!!
    }

    fun getFontAppDefaultItal(assets: AssetManager): Typeface
    {
        if (fontAppDefaultItal == null) fontAppDefaultItal =
            Typeface.createFromAsset(assets, "fonts/cambriai.ttf")
        return fontAppDefaultItal!!
    }

    fun getFontAppDefaultBdIt(assets: AssetManager): Typeface
    {
        if (fontAppDefaultBdIt == null) fontAppDefaultBdIt =
            Typeface.createFromAsset(assets, "fonts/cambriaz.ttf")
        return fontAppDefaultBdIt!!
    }

    fun setFontCustomNorm(fontFilePath: String)
    {
        try
        {
            fontCustomNorm = Typeface.createFromFile(fontFilePath)
            fontCustomNormPath = fontFilePath
        }
        catch (ex: Exception)
        {
            fontCustomNorm = null
            fontCustomNormPath = null
        }
    }

    fun setFontCustomBold(fontFilePath: String)
    {
        try
        {
            fontCustomBold = Typeface.createFromFile(fontFilePath)
            fontCustomBoldPath = fontFilePath
        }
        catch (ex: Exception)
        {
            fontCustomBold = null
            fontCustomBoldPath = null
        }
    }

    fun setFontCustomItal(fontFilePath: String)
    {
        try
        {
            fontCustomItal = Typeface.createFromFile(fontFilePath)
            fontCustomItalPath = fontFilePath
        }
        catch (ex: Exception)
        {
            fontCustomItal = null
            fontCustomItalPath = null
        }
    }

    fun setFontCustomBdIt(fontFilePath: String)
    {
        try
        {
            fontCustomBdIt = Typeface.createFromFile(fontFilePath)
            fontCustomBdItPath = fontFilePath
        }
        catch (ex: Exception)
        {
            fontCustomBdIt = null
            fontCustomBdItPath = null
        }
    }
}
