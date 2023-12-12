package com.luciasoft.browserjavatokotlin

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
import com.luciasoft.browserjavatokotlin.Utils.getFileExtensionLowerCaseWithDot
import com.luciasoft.browserjavatokotlin.Utils.getParentDir
import com.luciasoft.browserjavatokotlin.Utils.toastLong
import com.luciasoft.browserjavatokotlin.Utils2.directoryIsReadable
import java.io.File

open class MultiBrowserActivity: AppCompatActivity()
{
    lateinit var DAT: Data
    val OPT get() = DAT.OPT
    val ADV get() = DAT.ADV
    val THM get() = DAT.THM

    val isListView get() = OPT.browserViewType == Options.BrowserViewType.List
    val isTilesView get() = OPT.browserViewType == Options.BrowserViewType.Tiles
    val isGalleryView get() = OPT.browserViewType == Options.BrowserViewType.Gallery

    var sortOrder: Options.SortOrder
        get() { return if (isGalleryView) OPT.galleryViewSortOrder else OPT.normalViewSortOrder }
        set(value) { if (isGalleryView) OPT.galleryViewSortOrder = value else OPT.normalViewSortOrder = value }

    private lateinit var fileFilterArray: Array<Array<String>>
    private lateinit var fileFilterDescripArray: Array<String>
    private lateinit var recyclerView: MyRecyclerView
    private lateinit var editTextSaveFileName: EditText

    fun setEditTextSaveFileName(name: String)
    {
        editTextSaveFileName.setText(name)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        
        if (!Permissions.checkExternalStoragePermission(this)) Permissions.requestExternalStoragePermission(this)

        val app = application as AppBase
        DAT = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(app))[Data::class.java]
        
        val pair = FileFilters.getFileFilterArrays(OPT.fileFilterString)
        fileFilterArray = pair.first
        fileFilterDescripArray = pair.second

        configureScreenRotation()

        try
        {
            with (supportActionBar!!)
            {
                this.customView = with (TextView(applicationContext))
                {
                    this.typeface = THM.getFontBold(assets)
                    this.text = OPT.browserTitle
                    this.setTextColor(THM.colorBrowserTitle)
                    this.setTextSize(THM.unitSp, THM.sizeBrowserTitle)
                    this
                }
                this.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
                this.setBackgroundDrawable(ColorDrawable(THM.colorActionBar))
            }
        }
        catch (ex: Exception)
        {

        }

        if (DAT.currentDir != null)
        {
            val dir = File(DAT.currentDir!!)
            try { if (!dir.exists() && OPT.createDirOnActivityStart) dir.mkdirs() } catch (_: Exception) { }
            try { DAT.currentDir = dir.canonicalPath } catch (_: Exception) { }
            if (OPT.defaultDir == null) OPT.defaultDir = DAT.currentDir
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)

        setupParentDirLayout()
        setupSaveFileLayout()
        setupFileFilterLayout()
        setupSwipeRefreshLayout()
        refreshLayoutManager()
        setupViews()

        refreshView(true, false)
    }

    private fun configureScreenRotation()
    {
        if (DAT.defaultScreenOrientation == null) DAT.defaultScreenOrientation = requestedOrientation
        fun set(orientation: Int) { requestedOrientation = orientation }
        when (ADV.screenRotationMode)
        {
            Options.ScreenMode.AllowPortraitUprightOnly -> set(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            Options.ScreenMode.AllowPortraitUprightAndLandscape -> set(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            Options.ScreenMode.AllowLandscapeOnly -> set(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            Options.ScreenMode.AllowAll -> set(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
            Options.ScreenMode.SystemDefault -> set(DAT.defaultScreenOrientation!!)
            else -> { }
        }
    }

    private fun setupViews()
    {
        findViewById<View>(R.id.deadSpaceBackground).setBackgroundColor(THM.colorDeadSpaceBackground)
        findViewById<View>(R.id.topAccent).setBackgroundColor(THM.colorTopAccent)
        with (findViewById<TextView>(R.id.curDirLabel))
        {
            this.typeface = THM.getFontBold(assets)
            this.setTextColor(THM.colorCurDirLabel)
            this.setBackgroundColor(THM.colorCurDirBackground)
            this.setTextSize(THM.unitSp, THM.sizeCurDirLabel)
        }
        with (findViewById<TextView>(R.id.curDirText))
        {
            this.typeface = THM.getFontBold(assets)
            this.setTextColor(THM.colorCurDirText)
            this.setBackgroundColor(THM.colorCurDirBackground)
            this.setTextSize(THM.unitSp, THM.sizeCurDirText)
        }
        findViewById<View>(R.id.topAccent2).setBackgroundColor(THM.colorListTopAccent)
        findViewById<View>(R.id.saveFileLayout).setBackgroundColor(THM.colorSaveFileBoxBackground)
        findViewById<View>(R.id.bottomAccent).setBackgroundColor(THM.colorListBottomAccent)
        with (findViewById<EditText>(R.id.saveFileEditText))
        {
            this.typeface = THM.getFontBold(assets)
            this.setTextColor(THM.colorSaveFileBoxText)
            this.background.setColorFilter(THM.colorSaveFileBoxUnderline, PorterDuff.Mode.SRC_ATOP)
            this.setTextSize(THM.unitSp, THM.sizeSaveFileText)
        }
        with (findViewById<Button>(R.id.saveFileButton))
        {
            this.typeface = THM.getFontBold(assets)
            this.setTextColor(THM.colorSaveFileButtonText)
            this.setTextSize(THM.unitSp, THM.sizeSaveFileButtonText)
            ViewCompat.setBackgroundTintList(this, ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled)),
                intArrayOf(THM.colorSaveFileButtonBackground)))
        }
        findViewById<View>(R.id.fileFilterLayout).setBackgroundColor(THM.colorFilterBackground)
        findViewById<View>(R.id.bottomAccent2).setBackgroundColor(THM.colorSaveFileBoxBottomAccent)
        findViewById<Spinner>(R.id.fileFilterSpinner).background.setColorFilter(THM.colorFilterArrow, PorterDuff.Mode.SRC_ATOP)
        findViewById<View>(R.id.bottomAccent3).setBackgroundColor(THM.colorBottomAccent)
        findViewById<View>(R.id.parDirLayout).setBackgroundColor(THM.colorParDirBackground)
        with (findViewById<TextView>(R.id.parDirText))
        {
            this.typeface = THM.getFontBold(assets)
            this.setTextColor(THM.colorParDirText)
            this.setTextSize(THM.unitSp, THM.sizeParDirText)
        }
        with (findViewById<TextView>(R.id.parDirSubText))
        {
            this.typeface = THM.getFontBold(assets)
            this.setTextColor(THM.colorParDirSubText)
            this.setTextSize(THM.unitSp, THM.sizeParDirSubText)
        }
        findViewById<View>(R.id.parDirLayoutAccent).setBackgroundColor(THM.colorListAccent)
    }

    private fun setupParentDirLayout()
    {
        (findViewById<View>(R.id.parDirLayout) as LinearLayout).setOnClickListener(
            View.OnClickListener {
                val parentDir = if (DAT.currentDir == null) null else getParentDir(DAT.currentDir!!)
                if (!parentDir.isNullOrEmpty()) refreshView(parentDir, false, false)
            })
        (findViewById<View>(R.id.parentDirIcon) as ImageView).setImageResource(R.mipmap.ic_folder_up)
        (findViewById<View>(R.id.parDirSubText) as TextView).text = "(Go Up)"
    }

    private fun setupSaveFileLayout()
    {
        editTextSaveFileName = findViewById<View>(R.id.saveFileEditText) as EditText
        if (OPT.defaultSaveFileName != "") editTextSaveFileName.setText(OPT.defaultSaveFileName)
        val btnSaveFile = findViewById<View>(R.id.saveFileButton) as Button
        btnSaveFile.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(view: View)
            {
                val filename: String = editTextSaveFileName.text.toString().trim { it <= ' ' }
                editTextSaveFileName.setText(filename)
                if (filename.isEmpty()) return
                val filename2 = checkFileNameAndExt(filename)
                if (filename2 == null)
                {
                    toastLong(this@MultiBrowserActivity, "Invalid file name or extension.")
                }
                else
                {
                    var dir = DAT.currentDir!!
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
            fileFilterDescripArray
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

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
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
                if (DAT.fileFilterIndex == position) return
                if (editTextSaveFileName.visibility != View.GONE)
                {
                    var filename: String =
                        editTextSaveFileName.text.toString().trim { it <= ' ' }
                    if (!filename.isEmpty())
                    {
                        filename = changeFileExt(filename, DAT.fileFilterIndex!!, position)
                    }
                    editTextSaveFileName.setText(filename)
                }
                DAT.fileFilterIndex = position
                refreshView(false, false)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
        spinnerFileFilters.setSelection(DAT.fileFilterIndex!!)
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
        refreshView(DAT.currentDir, forceSourceReload, refreshLayout)
    }

    fun refreshView(dir: String?, forceSourceReload: Boolean, refreshLayout: Boolean)
    {
        val firstLoad = DAT.firstLoad
        DAT.firstLoad = false
        val items = getDirectoryItems(dir, forceSourceReload)
        var showLayouts = true
        if (items == null)
        {
            if (isGalleryView || dir == null)
            {
                recyclerView.text = "error reading items"
                showLayouts = false
            }
            else
            {
                if (firstLoad)
                {
                    recyclerView.text = "cannot read directory:\n$dir"
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
            if (!isGalleryView) DAT.currentDir = dir
            if (refreshLayout) refreshLayoutManager()
            if (items.size == 0) recyclerView.text = "no items"
            else recyclerView.clearText()
            recyclerView.adapter = MyListAdapter(this, items)
        }
        configureLayouts(showLayouts)
    }

    private fun getDirectoryItems(
        dir: String?,
        forceSourceReload: Boolean
    ): ArrayList<DirectoryItem>?
    {
        if (dir == null) return null
        val readable = isGalleryView || directoryIsReadable(this, dir)
        if (!readable) return null
        if (isGalleryView || OPT.showImagesWhileBrowsingNormal)
        {
            val reload = (forceSourceReload ||
                ADV.autoRefreshDirectorySource || (DAT.mediaStoreImageInfoList == null))
            if (reload) DAT.mediaStoreImageInfoList = getImageInfos(this)
        }
        val items: ArrayList<DirectoryItem>?
        if (isGalleryView)
        {
            items = DAT.mediaStoreImageInfoList
        }
        else
        {
            val reload = forceSourceReload || ADV.autoRefreshDirectorySource || (
                DAT.fileSystemDirectoryItems == null) || (
                DAT.currentDir == null) || !dir.equals(DAT.currentDir, ignoreCase = true)
            if (reload) DAT.fileSystemDirectoryItems = getDirectoryItemsFromFileSystem(
                this,
                dir,
                fileFilterArray[DAT.fileFilterIndex!!]
            )
            items = DAT.fileSystemDirectoryItems
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
        if (!isGalleryView && showLayouts)
        {
            val isRoot = (DAT.currentDir == "/")
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
            (findViewById<View>(R.id.curDirText) as TextView).text = DAT.currentDir
        }
        else
        {
            curDirLayout.visibility = View.GONE
        }
        if (showParentDirLayout)
        {
            parDirLayout.visibility = View.VISIBLE
            (findViewById<View>(R.id.parDirText) as TextView).text = getParentDir(DAT.currentDir!!)
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
        if (isListView)
        {
            manager = LinearLayoutManager(applicationContext)
        }
        else
        {
            val columnCount =
                if (isTilesView) OPT.normalViewColumnCount else OPT.galleryViewColumnCount
            manager = GridLayoutManager(applicationContext, columnCount)
        }
        recyclerView.layoutManager = manager
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
            if (isGalleryView)
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
        val newExts = fileFilterArray[newFileFilterIndex]
        if ((newExts[0] == "*")) return filename
        val ext = getFileExtensionLowerCaseWithDot(filename)
        if (ext.isEmpty()) return filename + newExts[0]
        if (arrayContains(newExts, ext)) return filename
        val oldExts = fileFilterArray[oldFileFilterIndex]
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
        val filters = fileFilterArray[DAT.fileFilterIndex!!]
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