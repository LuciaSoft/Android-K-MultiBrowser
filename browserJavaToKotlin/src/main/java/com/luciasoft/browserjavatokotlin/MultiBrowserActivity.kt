package com.luciasoft.browserjavatokotlin

import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.luciasoft.browserjavatokotlin.ListUtils.getDirectoryItemsFromFileSystem
import com.luciasoft.browserjavatokotlin.ListUtils.getImageInfos
import com.luciasoft.browserjavatokotlin.MyMessageBox.Companion.show
import com.luciasoft.browserjavatokotlin.MyYesNoDialog.show
import com.luciasoft.browserjavatokotlin.OptionsMenu.onMenuOpened
import com.luciasoft.browserjavatokotlin.OptionsMenu.onOptionsItemSelected
import com.luciasoft.browserjavatokotlin.Utils.arrayContains
import com.luciasoft.browserjavatokotlin.Utils2.directoryIsReadable
import com.luciasoft.browserjavatokotlin.Utils.getFileExtensionLowerCaseWithDot
import com.luciasoft.browserjavatokotlin.Utils.getParentDir
import com.luciasoft.browserjavatokotlin.Utils.toastLong
import com.luciasoft.collections.DirectoryItem
import java.io.File

open class MultiBrowserActivity
    : AppCompatActivity()
{
    //private var tmpOptions: MultiBrowserOptions? = null

    lateinit var DAT: DataHolder
    lateinit var OPT: Options
    lateinit var ADV: AdvancedOptions
    lateinit var THM: ThemeOptions

    /*val options: MultiBrowserOptions?
        get()
        {
            if (DAT == null && tmpOptions == null) return null
            return if (DAT == null) tmpOptions else DAT.mOptions
        }*/

    /*fun setOptions(options: MultiBrowserOptions?, squelchException: Boolean): Boolean
    {
        if (options == null)
        {
            if (squelchException) return false
            throw IllegalArgumentException("The options cannot be null.")
        }
        if (DAT == null)
        {
            tmpOptions = options
        }
        else
        {
            DAT.mOptions = options
            tmpOptions = null
        }
        return true
    }*/

    /*public MultiBrowserActivity()
    {
        super();

        if (MultiBrowserOptions.mDefaultScreenOrientation == null)
            MultiBrowserOptions.mDefaultScreenOrientation = getRequestedOrientation();

        oh = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(OptionsHolder.class);

        configureScreenRotation();
    }

    public MultiBrowserActivity(int contentLayoutId)
    {
        super(contentLayoutId);

        if (MultiBrowserOptions.mDefaultScreenOrientation == null)
            MultiBrowserOptions.mDefaultScreenOrientation = getRequestedOrientation();

        oh = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(OptionsHolder.class);

        configureScreenRotation();
    }*/

    lateinit var mRecyclerView: MyRecyclerView
    lateinit var mEditTextSaveFileName: EditText

    fun setEditTextSaveFileName(name: String)
    {
        mEditTextSaveFileName.setText(name)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        DAT = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[DataHolder::class.java]
        OPT = DAT.mOptions!!
        ADV = OPT.mAdvancedOptions
        THM = OPT.mThemeOptions

        /*if (tmpOptions != null)
        {
            DAT.mOptions = tmpOptions
            tmpOptions = null
        }*/
        configureScreenRotation()
        try
        {
            with(supportActionBar!!)
            {
                val tv = TextView(applicationContext)
                tv.typeface = THM.getFontBold(assets)
                tv.text = OPT.browserTitle
                tv.setTextColor(THM.colorBrowserTitle)
                tv.setTextSize(THM.unitSp, THM.sizeBrowserTitle)
                this.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
                this.customView = tv
                this.setBackgroundDrawable(ColorDrawable(THM.colorActionBar))
            }
        }
        catch (ex: Exception)
        {

        }

        if (OPT.currentDir != null)
        {
            try
            {
                val dir = File(OPT.currentDir!!)
                if (!dir.exists() && OPT.createDirOnActivityStart) try
                {
                    dir.mkdirs()
                }
                catch (ex2: Exception)
                {
                }
                OPT.currentDir = dir.canonicalPath
            }
            catch (ex: Exception)
            {
            }
            if (OPT.defaultDir == null) OPT.defaultDir = OPT.currentDir
        }
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.setHasFixedSize(true)
        setupParentDirLayout()
        setupSaveFileLayout()
        setupFileFilterLayout()
        setupSwipeRefreshLayout()
        refreshLayoutManager()
        findViewById<View>(R.id.deadSpaceBackground).setBackgroundColor(THM.colorDeadSpaceBackground)
        findViewById<View>(R.id.topAccent).setBackgroundColor(THM.colorTopAccent)
        (findViewById<View>(R.id.curDirLabel) as TextView).setTypeface(
            THM.getFontBold(
                assets
            )
        )
        (findViewById<View>(R.id.curDirLabel) as TextView).setTextColor(THM.colorCurDirLabel)
        (findViewById<View>(R.id.curDirLabel) as TextView).setBackgroundColor(THM.colorCurDirBackground)
        (findViewById<View>(R.id.curDirLabel) as TextView).setTextSize(
            THM.unitSp,
            THM.sizeCurDirLabel
        )
        (findViewById<View>(R.id.curDirText) as TextView).setTypeface(
            THM.getFontBold(
                assets
            )
        )
        (findViewById<View>(R.id.curDirText) as TextView).setTextColor(THM.colorCurDirText)
        (findViewById<View>(R.id.curDirText) as TextView).setBackgroundColor(THM.colorCurDirBackground)
        (findViewById<View>(R.id.curDirText) as TextView).setTextSize(
            THM.unitSp,
            THM.sizeCurDirText
        )
        findViewById<View>(R.id.topAccent2).setBackgroundColor(THM.colorListTopAccent)
        findViewById<View>(R.id.saveFileLayout).setBackgroundColor(THM.colorSaveFileBoxBackground)
        findViewById<View>(R.id.bottomAccent).setBackgroundColor(THM.colorListBottomAccent)
        (findViewById<View>(R.id.saveFileEditText) as EditText).setTypeface(
            THM.getFontBold(
                assets
            )
        )
        (findViewById<View>(R.id.saveFileEditText) as EditText).setTextColor(THM.colorSaveFileBoxText)
        (findViewById<View>(R.id.saveFileEditText) as EditText).background.setColorFilter(
            THM.colorSaveFileBoxUnderline,
            PorterDuff.Mode.SRC_ATOP
        )
        (findViewById<View>(R.id.saveFileEditText) as EditText).setTextSize(
            THM.unitSp,
            THM.sizeSaveFileText
        )
        (findViewById<View>(R.id.saveFileButton) as Button).setTypeface(
            THM.getFontBold(
                assets
            )
        )
        (findViewById<View>(R.id.saveFileButton) as Button).setTextColor(THM.colorSaveFileButtonText)
        (findViewById<View>(R.id.saveFileButton) as Button).setTextSize(
            THM.unitSp,
            THM.sizeSaveFileButtonText
        )
        ViewCompat.setBackgroundTintList(
            ((findViewById<View>(R.id.saveFileButton) as Button)),
            ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled)),
                intArrayOf(THM.colorSaveFileButtonBackground)
            )
        )
        findViewById<View>(R.id.fileFilterLayout).setBackgroundColor(THM.colorFilterBackground)
        findViewById<View>(R.id.bottomAccent2).setBackgroundColor(THM.colorSaveFileBoxBottomAccent)
        (findViewById<View>(R.id.fileFilterSpinner) as Spinner).background.setColorFilter(
            THM.colorFilterArrow,
            PorterDuff.Mode.SRC_ATOP
        )
        findViewById<View>(R.id.bottomAccent3).setBackgroundColor(THM.colorBottomAccent)
        findViewById<View>(R.id.parDirLayout).setBackgroundColor(THM.colorParDirBackground)
        (findViewById<View>(R.id.parDirText) as TextView).setTypeface(
            THM.getFontBold(
                assets
            )
        )
        (findViewById<View>(R.id.parDirText) as TextView).setTextColor(THM.colorParDirText)
        (findViewById<View>(R.id.parDirText) as TextView).setTextSize(
            THM.unitSp,
            THM.sizeParDirText
        )
        (findViewById<View>(R.id.parDirSubText) as TextView).setTypeface(
            THM.getFontBold(
                assets
            )
        )
        (findViewById<View>(R.id.parDirSubText) as TextView).setTextColor(THM.colorParDirSubText)
        (findViewById<View>(R.id.parDirSubText) as TextView).setTextSize(
            THM.unitSp,
            THM.sizeParDirSubText
        )
        findViewById<View>(R.id.parDirLayoutAccent).setBackgroundColor(THM.colorListAccent)
        refreshView(true, false)
    }

    private fun configureScreenRotation()
    {
        if (DAT.mDefaultScreenOrientation == null) DAT.mDefaultScreenOrientation =
            requestedOrientation

        if (ADV.screenRotationMode === Options.ScreenMode.AllowPortraitUprightOnly)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        else if (ADV.screenRotationMode === Options.ScreenMode.AllowPortraitUprightAndLandscape)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
        else if (ADV.screenRotationMode === Options.ScreenMode.AllowLandscapeOnly)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
        else if (ADV.screenRotationMode === Options.ScreenMode.AllowAll)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        }
        else if (ADV.screenRotationMode === Options.ScreenMode.SystemDefault)
        {
            if (DAT.mDefaultScreenOrientation != null) requestedOrientation =
                DAT.mDefaultScreenOrientation!!
        }
    }

    private fun setupParentDirLayout()
    {
        (findViewById<View>(R.id.parDirLayout) as LinearLayout).setOnClickListener(
            View.OnClickListener {
                val parentDir = if (OPT.currentDir == null) null else getParentDir(OPT.currentDir!!)
                if (!parentDir.isNullOrEmpty()) refreshView(parentDir, false, false)
            })
        (findViewById<View>(R.id.parentDirIcon) as ImageView).setImageResource(R.mipmap.ic_folder_up)
        (findViewById<View>(R.id.parDirSubText) as TextView).text = "(Go Up)"
    }

    private fun setupSaveFileLayout()
    {
        mEditTextSaveFileName = findViewById<View>(R.id.saveFileEditText) as EditText
        if (OPT.defaultSaveFileName != "") mEditTextSaveFileName.setText(OPT.defaultSaveFileName)
        val btnSaveFile = findViewById<View>(R.id.saveFileButton) as Button
        btnSaveFile.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(view: View)
            {
                val filename: String = mEditTextSaveFileName.text.toString().trim { it <= ' ' }
                mEditTextSaveFileName.setText(filename)
                if (filename.isEmpty()) return
                val filename2 = checkFileNameAndExt(filename)
                if (filename2 == null)
                {
                    toastLong(this@MultiBrowserActivity, "Invalid file name or extension.")
                }
                else
                {
                    var dir = OPT.currentDir!!
                    if (!dir.endsWith("/")) dir += "/"
                    val fullpath = dir + filename
                    onSelect(true, false, false, true, fullpath)
                }
            }
        })
    }

    private fun setupFileFilterLayout()
    {
        val spinnerFileFilters = findViewById<Spinner>(R.id.fileFilterSpinner)
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            this,
            R.layout.file_filter_popup_item,
            OPT.mFileFilterDescrips
        )
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
            {
                val view = super.getView(position, convertView, parent)
                (view as TextView).typeface = THM.getFontNorm(assets)
                view.setTextColor(THM.colorFilterText)
                view.setTextSize(THM.unitSp, THM.sizeFileFilterText)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View
            {
                val view = super.getView(position, convertView, parent)
                val text = view.findViewById<TextView>(R.id.fileFilterPopupItem)
                text.setTypeface(THM.getFontNorm(assets))
                text.setTextColor(THM.colorFilterPopupText)
                text.setBackgroundColor(THM.colorFilterPopupBackground)
                text.setTextSize(THM.unitSp, THM.sizeFileFilterPopupText)
                return view
            }
        }
        spinnerFileFilters.adapter = adapter
        spinnerFileFilters.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            )
            {
                if (OPT.fileFilterIndex == position) return
                if (mEditTextSaveFileName.visibility != View.GONE)
                {
                    var filename: String =
                        mEditTextSaveFileName.text.toString().trim { it <= ' ' }
                    if (!filename.isEmpty())
                    {
                        filename = changeFileExt(filename, OPT.fileFilterIndex, position)
                    }
                    mEditTextSaveFileName.setText(filename)
                }
                OPT.fileFilterIndex = position
                refreshView(false, false)
            }

            override fun onNothingSelected(parent: AdapterView<*>?)
            {
            }
        }
        spinnerFileFilters.setSelection(OPT.fileFilterIndex)
    }

    private fun setupSwipeRefreshLayout()
    {
        val swipeRefreshLayout = findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(object : OnRefreshListener
        {
            override fun onRefresh()
            {
                swipeRefreshLayout.isRefreshing = true
                refreshView(true, false)
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    fun refreshView(forceSourceReload: Boolean, refreshLayout: Boolean)
    {
        refreshView(OPT.currentDir, forceSourceReload, refreshLayout)
    }

    fun refreshView(dir: String?, forceSourceReload: Boolean, refreshLayout: Boolean)
    {
        val firstLoad = DAT.mFirstLoad
        DAT.mFirstLoad = false
        val items = getDirectoryItems(dir, forceSourceReload)
        val galleryView = OPT.browserViewType === Options.BrowserViewType.Gallery
        var showLayouts = true
        if (items == null)
        {
            if (galleryView || dir == null)
            {
                mRecyclerView.text = "error reading items"
                showLayouts = false
            }
            else
            {
                if (firstLoad)
                {
                    mRecyclerView.text = "cannot read directory:\n$dir"
                    showLayouts = false
                }
                else
                {
                    toastLong(this, "The directory \"$dir\" is unreadable.")
                }
            }
        }
        else
        {
            if (!galleryView) OPT.currentDir = dir
            if (refreshLayout) refreshLayoutManager()
            if (items.size == 0) mRecyclerView.text = "no items"
            else mRecyclerView.clearText()
            mRecyclerView.adapter = MyListAdapter(this, items)
        }
        configureLayouts(showLayouts)
    }

    private fun getDirectoryItems(
        dir: String?,
        forceSourceReload: Boolean
    ): ArrayList<DirectoryItem>?
    {
        if (dir == null) return null
        val galleryView = OPT.browserViewType === Options.BrowserViewType.Gallery
        val readable = galleryView || directoryIsReadable(this, dir)
        if (!readable) return null
        if (galleryView || OPT.showImagesWhileBrowsingNormal)
        {
            val reload = (forceSourceReload ||
                ADV.autoRefreshDirectorySource || (DAT.mMediaStoreImageInfoList == null))
            if (reload) DAT.mMediaStoreImageInfoList = getImageInfos(this)
        }
        val items: ArrayList<DirectoryItem>?
        if (galleryView)
        {
            items = DAT.mMediaStoreImageInfoList
        }
        else
        {
            val reload = forceSourceReload || ADV.autoRefreshDirectorySource || (
                DAT.mFileSystemDirectoryItems == null) || (
                OPT.currentDir == null) || !dir.equals(OPT.currentDir, ignoreCase = true)
            if (reload) DAT.mFileSystemDirectoryItems = getDirectoryItemsFromFileSystem(
                this,
                dir,
                OPT.mFileFilters[OPT.fileFilterIndex]
            )
            items = DAT.mFileSystemDirectoryItems
        }
        return items
    }

    private fun configureLayouts(showLayouts: Boolean)
    {
        val curDirLayout = findViewById<LinearLayout>(R.id.curDirLayout)
        val parDirLayout = findViewById<LinearLayout>(R.id.parDirLayout)
        val saveFileLayout = findViewById<LinearLayout>(R.id.saveFileLayout)
        val fileFilterLayout = findViewById<LinearLayout>(R.id.fileFilterLayout)
        var showCurrentDirLayout = false
        var showParentDirLayout = false
        var showSaveFileLayout = false
        var showFileFilterLayout = false
        val galleryView = OPT.browserViewType === Options.BrowserViewType.Gallery
        if (!galleryView && showLayouts)
        {
            val isRoot = (OPT.currentDir == "/")
            showParentDirLayout = ADV.showParentDirectoryLayoutIfAvailable && !isRoot
            showCurrentDirLayout = ADV.showCurrentDirectoryLayoutIfAvailable
            showSaveFileLayout = ADV.showSaveFileLayoutIfAvailable &&
                OPT.browseMode === Options.BrowseMode.SaveFilesAndOrFolders
            showFileFilterLayout = ADV.showFileFilterLayoutIfAvailable &&
                (OPT.browseMode === Options.BrowseMode.LoadFilesAndOrFolders ||
                    OPT.browseMode === Options.BrowseMode.SaveFilesAndOrFolders)
        }
        if (showCurrentDirLayout)
        {
            curDirLayout.visibility = View.VISIBLE
            (findViewById<View>(R.id.curDirText) as TextView).text = OPT.currentDir
        }
        else
        {
            curDirLayout.visibility = View.GONE
        }
        if (showParentDirLayout)
        {
            parDirLayout.visibility = View.VISIBLE
            (findViewById<View>(R.id.parDirText) as TextView).text = getParentDir(OPT.currentDir!!)
        }
        else
        {
            parDirLayout.visibility = View.GONE
        }
        if (showSaveFileLayout)
        {
            saveFileLayout.visibility = View.VISIBLE
        }
        else
        {
            saveFileLayout.visibility = View.GONE
        }
        if (showFileFilterLayout)
        {
            fileFilterLayout.visibility = View.VISIBLE
        }
        else
        {
            fileFilterLayout.visibility = View.GONE
        }
    }

    private fun refreshLayoutManager()
    {
        val manager: RecyclerView.LayoutManager
        if (OPT.browserViewType === Options.BrowserViewType.List)
        {
            manager = LinearLayoutManager(applicationContext)
        }
        else
        {
            val columnCount =
                if (OPT.browserViewType === Options.BrowserViewType.Tiles) OPT.normalViewColumnCount else OPT.galleryViewColumnCount
            manager = GridLayoutManager(applicationContext, columnCount)
        }
        mRecyclerView.layoutManager = manager
    }

    fun onSelect(
        file: Boolean,
        load: Boolean,
        longClick: Boolean,
        saveButton: Boolean,
        path: String
    )
    {
        onSelect(false, file, load, longClick, saveButton, path)
    }

    private fun onSelect(
        skipDialog: Boolean,
        file: Boolean,
        load: Boolean,
        longClick: Boolean,
        saveButton: Boolean,
        path: String
    )
    {
        if (!skipDialog && !load)
        {
            var showDialog = false
            if (OPT.browserViewType === Options.BrowserViewType.Gallery)
            {
                if (OPT.alwaysShowDialogForSavingGalleryItem) showDialog = true
            }
            else if (file)
            {
                if (OPT.alwaysShowDialogForSavingFile || OPT.showOverwriteDialogForSavingFileIfExists) showDialog =
                    true
            }
            else
            {
                if (OPT.alwaysShowDialogForSavingFolder) showDialog = true
            }
            if (showDialog)
            {
                val exists: Boolean
                try
                {
                    exists = File(path).exists()
                }
                catch (ex: Exception)
                {
                    show(
                        this,
                        "Error",
                        "An unexpected error occurred.",
                        MyMessageBox.ButtonsType.Ok,
                        null,
                        null
                    )
                    return
                }
                val title =
                    (if (exists) "Overwrite " else "Save ") + (if (file) "File?" else "Folder?")
                val str1 = if (exists) "overwrite" else "save"
                val str2 = if (file) "file" else "folder"
                val message = "Do you want to $str1 the $str2: $path?"
                show(this, title, message, object : DialogInterface.OnClickListener
                {
                    override fun onClick(dialog: DialogInterface, which: Int)
                    {
                        onSelect(true, file, load, longClick, saveButton, path)
                    }
                })
                return
            }
        }

        val action =
            if (saveButton) SelectAction.SaveButton
            else if (longClick) SelectAction.LongClick
            else SelectAction.ShortClick

        onSelectItem(path, action, file, load)
    }

    open fun onSelectItem(
        path: String,
        action: SelectAction,
        file: Boolean,
        load: Boolean)
    {
        if (ADV.debugMode)
        {
            var title: String = ""
            title += if (load) "LOAD " else "SAVE "
            title += if (file) "FILE " else "FOLDER "
            title += if (action == SelectAction.SaveButton) "(SAVE BUTTON)" else (if (action == SelectAction.LongClick) "(LONG CLICK)" else "(SHORT CLICK)")
            val message = "PATH=[$path]"
            show(this, title, message, MyMessageBox.ButtonsType.Ok, null, null)
        }
    }

    private fun changeFileExt(
        filename: String,
        oldFileFilterIndex: Int,
        newFileFilterIndex: Int
    ): String
    {
        var filename = filename
        val newExts = OPT.mFileFilters[newFileFilterIndex]
        if ((newExts[0] == "*")) return filename
        val ext = getFileExtensionLowerCaseWithDot(filename)
        if (ext.isEmpty()) return filename + newExts[0]
        if (arrayContains(newExts, ext)) return filename
        val oldExts = OPT.mFileFilters[oldFileFilterIndex]
        if (oldExts.get(0) != "*" && arrayContains(oldExts, ext))
        {
            filename = filename.substring(0, filename.length - ext.length)
        }
        filename += newExts[0]
        return filename
    }

    private fun checkFileNameAndExt(filename: String): String?
    {
        //if (!OPTIONS().mAllowHiddenFiles && filename.startsWith(".")) return null;
        val ext = getFileExtensionLowerCaseWithDot(filename)
        val filters = OPT.mFileFilters[OPT.fileFilterIndex]
        if ((filters[0] == "*"))
        {
            //if (ext.isEmpty() && !OPTIONS().mAllowUndottedFileExts) return null;
            return filename
        }
        return if (ext.isNotEmpty() && arrayContains(
                filters,
                ext
            )) filename
        else filename + filters[0]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        if (ADV.menuEnabled)
        {
            menuInflater.inflate(R.menu.menu, menu)
            applyFontToMenu(menu)
        }
        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean
    {
        onMenuOpened(this, menu)
        return super.onMenuOpened(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return if (onOptionsItemSelected(this, item)) true else super.onOptionsItemSelected(item)
    }

    private fun applyFontToMenu(m: Menu)
    {
        for (i in 0 until m.size())
        {
            applyFontToMenuItem(m.getItem(i))
        }
    }

    private fun applyFontToMenuItem(mi: MenuItem)
    {
        if (mi.hasSubMenu())
        {
            for (i in 0 until mi.subMenu!!.size())
            {
                applyFontToMenuItem(mi.subMenu!!.getItem(i))
            }
        }
        val font = THM.getFontNorm(assets)
        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(
            CustomTypefaceSpan("Font", font), 0,
            mNewTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        mi.title = mNewTitle
    }
}