package com.lucerta.multibrowser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;

import com.luciasoft.browser.UtilsKt;
import com.luciasoft.browser.OnSelectItem;
import com.luciasoft.browser.*;
import com.luciasoft.collections.DirectoryItem;

public class MultiBrowserActivity extends AppCompatActivity
{
    private DataHolder data;
    private Options tmpOptions;

    DataHolder DAT() { return data; }
    public Options OPT() { return data.getMOptions(); }
    Options.Advanced ADV() { return data.getMOptions().advanced(); }
    public Options.Theme THM() { return data.getMOptions().theme(); }

    public Options getOptions()
    {
        if (data == null && tmpOptions == null) return null;
        if (data == null) return tmpOptions;
        return data.getMOptions();
    }

    public boolean setOptions(Options options, boolean squelchException)
    {
        if (options == null)
        {
            if (squelchException) return false;
            throw new IllegalArgumentException("The options cannot be null.");
        }

        if (data == null)
        {
            tmpOptions = options;
        }
        else
        {
            data.setMOptions(options);
            tmpOptions = null;
        }

        return true;
    }

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

    private MyRecyclerView mRecyclerView;
    private EditText mEditTextSaveFileName;

    void setEditTextSaveFileName(String name)
    {
        mEditTextSaveFileName.setText(name);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        data = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(DataHolder.class);

        if (data.getMDefaultScreenOrientation() == null) data.setMDefaultScreenOrientation(getRequestedOrientation());

        if (tmpOptions != null)
        {
            data.setMOptions(tmpOptions);
            tmpOptions = null;
        }

        configureScreenRotation();

        ActionBar actionBar;
        try { actionBar = getSupportActionBar(); }
        catch (Exception ex) { actionBar = null; }
        if (actionBar != null)
        {
            TextView tv = new TextView(getApplicationContext());
            tv.setTypeface(THM().getFontBold(getAssets()));
            tv.setText(OPT().browserTitle);
            tv.setTextColor(THM().colorBrowserTitle);
            tv.setTextSize(THM().unitSp, THM().sizeBrowserTitle);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(tv);
            actionBar.setBackgroundDrawable(new ColorDrawable(THM().colorActionBar));
        }

        if (OPT().currentDir != null)
        {
            try
            {
                File dir = new File(OPT().currentDir);
                if (!dir.exists() && OPT().createDirOnActivityStart)
                    try { dir.mkdirs(); } catch (Exception ex2) { }
                OPT().currentDir = dir.getCanonicalPath();
            }
            catch (Exception ex) { }
            if (OPT().defaultDir == null) OPT().defaultDir = OPT().currentDir;
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        setupParentDirLayout();
        setupSaveFileLayout();
        setupFileFilterLayout();
        setupSwipeRefreshLayout();
        refreshLayoutManager();

        findViewById(R.id.deadSpaceBackground).setBackgroundColor(THM().colorDeadSpaceBackground);
        findViewById(R.id.topAccent).setBackgroundColor(THM().colorTopAccent);
        ((TextView)findViewById(R.id.curDirLabel)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.curDirLabel)).setTextColor(THM().colorCurDirLabel);
        ((TextView)findViewById(R.id.curDirLabel)).setBackgroundColor(THM().colorCurDirBackground);
        ((TextView)findViewById(R.id.curDirLabel)).setTextSize(THM().unitSp, THM().sizeCurDirLabel);
        ((TextView)findViewById(R.id.curDirText)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.curDirText)).setTextColor(THM().colorCurDirText);
        ((TextView)findViewById(R.id.curDirText)).setBackgroundColor(THM().colorCurDirBackground);
        ((TextView)findViewById(R.id.curDirText)).setTextSize(THM().unitSp, THM().sizeCurDirText);
        findViewById(R.id.topAccent2).setBackgroundColor(THM().colorListTopAccent);
        findViewById(R.id.saveFileLayout).setBackgroundColor(THM().colorSaveFileBoxBackground);
        findViewById(R.id.bottomAccent).setBackgroundColor(THM().colorListBottomAccent);
        ((EditText)findViewById(R.id.saveFileEditText)).setTypeface(THM().getFontBold(getAssets()));
        ((EditText)findViewById(R.id.saveFileEditText)).setTextColor(THM().colorSaveFileBoxText);
        ((EditText)findViewById(R.id.saveFileEditText)).getBackground().setColorFilter(THM().colorSaveFileBoxUnderline, PorterDuff.Mode.SRC_ATOP);
        ((EditText)findViewById(R.id.saveFileEditText)).setTextSize(THM().unitSp, THM().sizeSaveFileText);
        ((Button)findViewById(R.id.saveFileButton)).setTypeface(THM().getFontBold(getAssets()));
        ((Button)findViewById(R.id.saveFileButton)).setTextColor(THM().colorSaveFileButtonText);
        ((Button)findViewById(R.id.saveFileButton)).setTextSize(THM().unitSp, THM().sizeSaveFileButtonText);
        ViewCompat.setBackgroundTintList(((Button)findViewById(R.id.saveFileButton)),
                new ColorStateList(new int[][]{ new int[] { android.R.attr.state_enabled} }, new int[]{ THM().colorSaveFileButtonBackground}));
        findViewById(R.id.fileFilterLayout).setBackgroundColor(THM().colorFilterBackground);
        findViewById(R.id.bottomAccent2).setBackgroundColor(THM().colorSaveFileBoxBottomAccent);
        ((Spinner)findViewById(R.id.fileFilterSpinner)).getBackground().setColorFilter(THM().colorFilterArrow, PorterDuff.Mode.SRC_ATOP);
        findViewById(R.id.bottomAccent3).setBackgroundColor(THM().colorBottomAccent);
        findViewById(R.id.parDirLayout).setBackgroundColor(THM().colorParDirBackground);
        ((TextView)findViewById(R.id.parDirText)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.parDirText)).setTextColor(THM().colorParDirText);
        ((TextView)findViewById(R.id.parDirText)).setTextSize(THM().unitSp, THM().sizeParDirText);
        ((TextView)findViewById(R.id.parDirSubText)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.parDirSubText)).setTextColor(THM().colorParDirSubText);
        ((TextView)findViewById(R.id.parDirSubText)).setTextSize(THM().unitSp, THM().sizeParDirSubText);
        findViewById(R.id.parDirLayoutAccent).setBackgroundColor(THM().colorListAccent);

        refreshView(true, false);
    }

    private void configureScreenRotation()
    {
        if (ADV().screenRotationMode == Options.ScreenMode.AllowPortraitUprightOnly)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if (ADV().screenRotationMode == Options.ScreenMode.AllowPortraitUprightAndLandscape)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        else if (ADV().screenRotationMode == Options.ScreenMode.AllowLandscapeOnly)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else if (ADV().screenRotationMode == Options.ScreenMode.AllowAll)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
        else if (ADV().screenRotationMode == Options.ScreenMode.SystemDefault)
        {
            if (data.getMDefaultScreenOrientation() != null) setRequestedOrientation(data.getMDefaultScreenOrientation());
        }
    }

    private void setupParentDirLayout()
    {
        ((LinearLayout)findViewById(R.id.parDirLayout)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String parentDir = UtilsKt.getParentDir(OPT().currentDir);

                if (!parentDir.isEmpty()) refreshView(parentDir, false, false);
            }
        });

        ((ImageView)findViewById(R.id.parentDirIcon)).setImageResource(R.mipmap.ic_folder_up);
        ((TextView)findViewById(R.id.parDirSubText)).setText("(Go Up)");
    }

    private void setupSaveFileLayout()
    {
        mEditTextSaveFileName = (EditText)findViewById(R.id.saveFileEditText);
        if (OPT().defaultSaveFileName != null) mEditTextSaveFileName.setText(OPT().defaultSaveFileName);

        Button btnSaveFile = (Button)findViewById(R.id.saveFileButton) ;
        btnSaveFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String filename = mEditTextSaveFileName.getText().toString().trim();

                mEditTextSaveFileName.setText(filename);

                if (filename.isEmpty()) return;

                filename = checkFileNameAndExt(filename);

                if (filename == null)
                {
                    UtilsKt.toastLong(MultiBrowserActivity.this, "Invalid file name or extension.");
                }
                else
                {
                    String dir = OPT().currentDir;
                    if (!dir.endsWith("/")) dir += "/";
                    String fullpath = dir + filename;
                    onSelect(true, false, false, true, fullpath);
                }
            }
        });
    }

    private void setupFileFilterLayout()
    {
        Spinner spinnerFileFilters = findViewById(R.id.fileFilterSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.file_filter_popup_item, OPT().mFileFilterDescrips)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                ((TextView)view).setTypeface(THM().getFontNorm(getAssets()));
                ((TextView)view).setTextColor(THM().colorFilterText);
                ((TextView)view).setTextSize(THM().unitSp, THM().sizeFileFilterText);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.fileFilterPopupItem);
                text.setTypeface(THM().getFontNorm(getAssets()));
                text.setTextColor(THM().colorFilterPopupText);
                text.setBackgroundColor(THM().colorFilterPopupBackground);
                text.setTextSize(THM().unitSp, THM().sizeFileFilterPopupText);
                return view;
            }
        };
        
        spinnerFileFilters.setAdapter(adapter);
        
        spinnerFileFilters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id)
            {
                if (OPT().fileFilterIndex == position) return;
                
                if (mEditTextSaveFileName.getVisibility() != View.GONE)
                {
                    String filename = mEditTextSaveFileName.getText().toString().trim();
                    
                    if (!filename.isEmpty())
                    {
                        filename = changeFileExt(filename, OPT().fileFilterIndex, position);
                    }

                    mEditTextSaveFileName.setText(filename);
                }
                    
                OPT().fileFilterIndex = position;
                    
                refreshView(false, false);
            }

            @Override
            public void onNothingSelected(AdapterView parent) { }
        });

        spinnerFileFilters.setSelection(OPT().fileFilterIndex);
    }

    private void setupSwipeRefreshLayout()
    {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayout.setRefreshing(true);
                refreshView(true, false);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void resetDir()
    {
        OPT().currentDir = OPT().defaultDir;

        refreshView(true, false);
    }
    public void refreshView(boolean forceSourceReload, boolean refreshLayout)
    {
        refreshView(OPT().currentDir, forceSourceReload, refreshLayout);
    }

    void refreshView(String dir, boolean forceSourceReload, boolean refreshLayout)
    {
        boolean firstLoad = data.getMFirstLoad();
        data.setMFirstLoad(false);

        ArrayList<DirectoryItem> items = getDirectoryItems(dir, forceSourceReload);

        boolean galleryView = OPT().browserViewType == Options.BrowserViewType.Gallery;

        boolean showLayouts = true;

        if (items == null)
        {
            if (galleryView || dir == null)
            {
                String text = "error reading items";
                mRecyclerView.setText(this, text);
                showLayouts = false;
            }
            else
            {
                if (firstLoad)
                {
                    String text = "cannot read directory:\n" + dir;
                    mRecyclerView.setText(this, text);
                    showLayouts = false;
                }
                else
                {
                    UtilsKt.toastLong(this, "The directory \"" + dir + "\" is unreadable.");
                }
            }
        }
        else
        {
            if (!galleryView) OPT().currentDir = dir;

            if (refreshLayout) refreshLayoutManager();

            if (items.size() == 0) mRecyclerView.setText(this, "no items");
            else mRecyclerView.clearText();

            mRecyclerView.setAdapter(new MyListAdapter(this, items));
        }

        configureLayouts(showLayouts);
    }

    @Nullable
    private ArrayList<DirectoryItem> getDirectoryItems(String dir, boolean forceSourceReload)
    {
        if (dir == null) return null;

        boolean galleryView = OPT().browserViewType == Options.BrowserViewType.Gallery;

        boolean readable = galleryView || Utils2Kt.directoryIsReadable(this, dir);
        if (!readable) return null;

        if (galleryView || OPT().showImagesWhileBrowsingNormal)
        {
            boolean reload = forceSourceReload ||
                    ADV().autoRefreshDirectorySource || data.getMMediaStoreImageInfoList() == null;

            if (reload) data.setMMediaStoreImageInfoList(ListUtils.getImageInfos(this));
        }

        ArrayList<DirectoryItem> items;
        if (galleryView)
        {
            items = data.getMMediaStoreImageInfoList();
        }
        else
        {
            boolean reload = forceSourceReload || ADV().autoRefreshDirectorySource ||
                    data.getMFileSystemDirectoryItems() == null ||
                    OPT().currentDir == null || !dir.equalsIgnoreCase(OPT().currentDir);

            if (reload) data.setMFileSystemDirectoryItems(
                ListUtils.getDirectoryItemsFromFileSystem(this, dir, OPT().mFileFilters[OPT().fileFilterIndex]));

            items = data.getMFileSystemDirectoryItems();
        }

        return items;
    }

    private void configureLayouts(boolean showLayouts)
    {
        LinearLayout curDirLayout = findViewById(R.id.curDirLayout);
        LinearLayout parDirLayout = findViewById(R.id.parDirLayout);
        LinearLayout saveFileLayout = findViewById(R.id.saveFileLayout);
        LinearLayout fileFilterLayout = findViewById(R.id.fileFilterLayout);

        boolean showCurrentDirLayout = false;
        boolean showParentDirLayout = false;
        boolean showSaveFileLayout = false;
        boolean showFileFilterLayout = false;

        boolean galleryView = OPT().browserViewType == Options.BrowserViewType.Gallery;

        if (!galleryView && showLayouts)
        {
            boolean isRoot = OPT().currentDir.equals("/");
            showParentDirLayout = ADV().showParentDirectoryLayoutIfAvailable && !isRoot;
            showCurrentDirLayout = ADV().showCurrentDirectoryLayoutIfAvailable;
            showSaveFileLayout = ADV().showSaveFileLayoutIfAvailable &&
                OPT().browseMode == Options.BrowseMode.SaveFilesAndOrFolders;
            showFileFilterLayout = ADV().showFileFilterLayoutIfAvailable &&
                (OPT().browseMode == Options.BrowseMode.LoadFilesAndOrFolders ||
                OPT().browseMode == Options.BrowseMode.SaveFilesAndOrFolders);
        }

        if (showCurrentDirLayout)
        {
            curDirLayout.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.curDirText)).setText(OPT().currentDir);
        }
        else
        {
            curDirLayout.setVisibility(View.GONE);
        }

        if (showParentDirLayout)
        {
            parDirLayout.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.parDirText)).setText(UtilsKt.getParentDir(OPT().currentDir));
        }
        else
        {
            parDirLayout.setVisibility(View.GONE);
        }

        if (showSaveFileLayout)
        {
            saveFileLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            saveFileLayout.setVisibility(View.GONE);
        }

        if (showFileFilterLayout)
        {
            fileFilterLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            fileFilterLayout.setVisibility(View.GONE);
        }
    }

    private void refreshLayoutManager()
    {
        RecyclerView.LayoutManager manager;
        if (OPT().browserViewType == Options.BrowserViewType.List)
        {
            manager = new LinearLayoutManager(getApplicationContext());
        }
        else
        {
            int columnCount = OPT().browserViewType == Options.BrowserViewType.Tiles ?
                OPT().normalViewColumnCount : OPT().galleryViewColumnCount;

            manager = new GridLayoutManager(getApplicationContext(), columnCount);

        }
        mRecyclerView.setLayoutManager(manager);
    }

    void onSelect(boolean file, boolean load, boolean longClick, boolean saveButton, String path)
    {
        onSelect(false, file, load, longClick, saveButton, path);
    }

    private void onSelect(boolean skipDialog, boolean file, boolean load, boolean longClick, boolean saveButton, String path)
    {
        if (!skipDialog && !load)
        {
            boolean showDialog = false;

            if (OPT().browserViewType == Options.BrowserViewType.Gallery)
            {
                if (OPT().alwaysShowDialogForSavingGalleryItem) showDialog = true;
            }
            else if (file)
            {
                if (OPT().alwaysShowDialogForSavingFile || OPT().showOverwriteDialogForSavingFileIfExists)
                    showDialog = true;
            }
            else
            {
                if (OPT().alwaysShowDialogForSavingFolder) showDialog = true;
            }

            if (showDialog)
            {
                boolean exists;
                try { exists = new File(path).exists(); }
                catch (Exception ex)
                {
                    MyMessageBox.Companion.show(this, "Error", "An unexpected error occurred.", MyMessageBox.ButtonsType.Ok, null, null);
                    return;
                }

                String title = (exists ? "Overwrite " : "Save ") + (file ? "File?" : "Folder?");
                String str1 = exists ? "overwrite" : "save";
                String str2 = file ? "file" : "folder";
                String message = "Do you want to " + str1 + " the " + str2 + ": " + path + "?";

                MyYesNoDialog.Companion.show(this, title, message, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        onSelect(true, file, load, longClick, saveButton, path);
                    }
                });

                return;
            }
        }

        OnSelectItem onSelectItemObject = getOnSelectItemObject(file, load);

        if (onSelectItemObject != null)
        {
            SelectedItemInfo.SelectAction action;
            if (saveButton) action = SelectedItemInfo.SelectAction.SaveButton;
            else if (longClick) action = SelectedItemInfo.SelectAction.LongClick;
            else action = SelectedItemInfo.SelectAction.ShortClick;

            onSelectItemObject.onSelect(new SelectedItemInfo(path, action));
        }
        else
        {
            if (ADV().debugMode)
            {
                String title = "";
                title += load ? "LOAD " : "SAVE ";
                title += file ? "FILE " : "FOLDER ";
                title += saveButton ? "(SAVE BUTTON)" : (longClick ? "(LONG CLICK)" : "(SHORT CLICK)");
                String message = "PATH=[" + path + "]";
                MyMessageBox.Companion.show(this, title, message, MyMessageBox.ButtonsType.Ok, null, null);
            }
        }
    }

    private OnSelectItem getOnSelectItemObject(boolean file, boolean load)
    {
        if (file)
        {
            if (load) return OPT().onSelectFileForLoad;
            else return OPT().onSelectFileForSave;
        }
        else
        {
            if (load) return OPT().onSelectFolderForLoad;
            else return OPT().onSelectFolderForSave;
        }
    }

    private String changeFileExt(String filename, int oldFileFilterIndex, int newFileFilterIndex)
    {
        String[] newExts = OPT().mFileFilters[newFileFilterIndex];

        if (newExts[0].equals("*")) return filename;

        String ext = UtilsKt.getFileExtensionLowerCaseWithDot(filename);

        if (ext.isEmpty()) return filename + newExts[0];

        if (UtilsKt.arrayContains(newExts, ext)) return filename;

        String[] oldExts = OPT().mFileFilters[oldFileFilterIndex];

        if (!oldExts[0].equals("*") && UtilsKt.arrayContains(oldExts, ext))
        {
            filename = filename.substring(0, filename.length() - ext.length());
        }

        filename += newExts[0];

        return filename;
    }

    private String checkFileNameAndExt(String filename)
    {
        //if (!OPTIONS().mAllowHiddenFiles && filename.startsWith(".")) return null;

        String ext = UtilsKt.getFileExtensionLowerCaseWithDot(filename);

        String[] filters = OPT().mFileFilters[OPT().fileFilterIndex];

        if (filters[0].equals("*"))
        {
            //if (ext.isEmpty() && !OPTIONS().mAllowUndottedFileExts) return null;

            return filename;
        }

        if (!ext.isEmpty() && UtilsKt.arrayContains(filters, ext)) return filename;

        return filename + filters[0];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (ADV().menuEnabled)
        {
            getMenuInflater().inflate(R.menu.menu, menu);
            applyFontToMenu(menu, this);
        }

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        OptionsMenu.onMenuOpened(this, menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (OptionsMenu.onOptionsItemSelected(this, item)) return true;
        return super.onOptionsItemSelected(item);
    }

    public void applyFontToMenu(Menu m, Context mContext)
    {
        for(int i = 0; i < m.size(); i++)
        {
            applyFontToMenuItem(m.getItem(i),mContext);
        }
    }

    public void applyFontToMenuItem(MenuItem mi, Context mContext)
    {
        if (mi.hasSubMenu())
        {
            for (int i = 0; i < mi.getSubMenu().size(); i++)
            {
                applyFontToMenuItem(mi.getSubMenu().getItem(i), mContext);
            }
        }

        Typeface font = THM().getFontNorm(getAssets());
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(
            new CustomTypefaceSpan("Font", font), 0,
            mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
}