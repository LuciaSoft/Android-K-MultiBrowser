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
    private MultiBrowserOptions tmpOptions;

    DataHolder DAT() { return data; }
    public MultiBrowserOptions OPT() { return data.mOptions; }
    MultiBrowserOptions.Advanced ADV() { return data.mOptions.advanced(); }
    MultiBrowserOptions.Theme THM() { return data.mOptions.theme(); }

    public MultiBrowserOptions getOptions()
    {
        if (data == null && tmpOptions == null) return null;
        if (data == null) return tmpOptions;
        return data.mOptions;
    }

    public boolean setOptions(MultiBrowserOptions options, boolean squelchException)
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
            data.mOptions = options;
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

        if (data.mDefaultScreenOrientation == null) data.mDefaultScreenOrientation = getRequestedOrientation();

        if (tmpOptions != null)
        {
            data.mOptions = tmpOptions;
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
            tv.setText(OPT().mBrowserTitle);
            tv.setTextColor(THM().mColorBrowserTitle);
            tv.setTextSize(THM().mUnitSp, THM().mSizeBrowserTitle);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(tv);
            actionBar.setBackgroundDrawable(new ColorDrawable(THM().mColorActionBar));
        }

        if (OPT().mCurrentDir != null)
        {
            try
            {
                File dir = new File(OPT().mCurrentDir);
                if (!dir.exists() && OPT().mCreateDirOnActivityStart)
                    try { dir.mkdirs(); } catch (Exception ex2) { }
                OPT().mCurrentDir = dir.getCanonicalPath();
            }
            catch (Exception ex) { }
            if (OPT().mDefaultDir == null) OPT().mDefaultDir = OPT().mCurrentDir;
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        setupParentDirLayout();
        setupSaveFileLayout();
        setupFileFilterLayout();
        setupSwipeRefreshLayout();
        refreshLayoutManager();

        findViewById(R.id.deadSpaceBackground).setBackgroundColor(THM().mColorDeadSpaceBackground);
        findViewById(R.id.topAccent).setBackgroundColor(THM().mColorTopAccent);
        ((TextView)findViewById(R.id.curDirLabel)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.curDirLabel)).setTextColor(THM().mColorCurDirLabel);
        ((TextView)findViewById(R.id.curDirLabel)).setBackgroundColor(THM().mColorCurDirBackground);
        ((TextView)findViewById(R.id.curDirLabel)).setTextSize(THM().mUnitSp, THM().mSizeCurDirLabel);
        ((TextView)findViewById(R.id.curDirText)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.curDirText)).setTextColor(THM().mColorCurDirText);
        ((TextView)findViewById(R.id.curDirText)).setBackgroundColor(THM().mColorCurDirBackground);
        ((TextView)findViewById(R.id.curDirText)).setTextSize(THM().mUnitSp, THM().mSizeCurDirText);
        findViewById(R.id.topAccent2).setBackgroundColor(THM().mColorListTopAccent);
        findViewById(R.id.saveFileLayout).setBackgroundColor(THM().mColorSaveFileBoxBackground);
        findViewById(R.id.bottomAccent).setBackgroundColor(THM().mColorListBottomAccent);
        ((EditText)findViewById(R.id.saveFileEditText)).setTypeface(THM().getFontBold(getAssets()));
        ((EditText)findViewById(R.id.saveFileEditText)).setTextColor(THM().mColorSaveFileBoxText);
        ((EditText)findViewById(R.id.saveFileEditText)).getBackground().setColorFilter(THM().mColorSaveFileBoxUnderline, PorterDuff.Mode.SRC_ATOP);
        ((EditText)findViewById(R.id.saveFileEditText)).setTextSize(THM().mUnitSp, THM().mSizeSaveFileText);
        ((Button)findViewById(R.id.saveFileButton)).setTypeface(THM().getFontBold(getAssets()));
        ((Button)findViewById(R.id.saveFileButton)).setTextColor(THM().mColorSaveFileButtonText);
        ((Button)findViewById(R.id.saveFileButton)).setTextSize(THM().mUnitSp, THM().mSizeSaveFileButtonText);
        ViewCompat.setBackgroundTintList(((Button)findViewById(R.id.saveFileButton)),
                new ColorStateList(new int[][]{ new int[] { android.R.attr.state_enabled} }, new int[]{ THM().mColorSaveFileButtonBackground }));
        findViewById(R.id.fileFilterLayout).setBackgroundColor(THM().mColorFilterBackground);
        findViewById(R.id.bottomAccent2).setBackgroundColor(THM().mColorSaveFileBoxBottomAccent);
        ((Spinner)findViewById(R.id.fileFilterSpinner)).getBackground().setColorFilter(THM().mColorFilterArrow, PorterDuff.Mode.SRC_ATOP);
        findViewById(R.id.bottomAccent3).setBackgroundColor(THM().mColorBottomAccent);
        findViewById(R.id.parDirLayout).setBackgroundColor(THM().mColorParDirBackground);
        ((TextView)findViewById(R.id.parDirText)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.parDirText)).setTextColor(THM().mColorParDirText);
        ((TextView)findViewById(R.id.parDirText)).setTextSize(THM().mUnitSp, THM().mSizeParDirText);
        ((TextView)findViewById(R.id.parDirSubText)).setTypeface(THM().getFontBold(getAssets()));
        ((TextView)findViewById(R.id.parDirSubText)).setTextColor(THM().mColorParDirSubText);
        ((TextView)findViewById(R.id.parDirSubText)).setTextSize(THM().mUnitSp, THM().mSizeParDirSubText);
        findViewById(R.id.parDirLayoutAccent).setBackgroundColor(THM().mColorListAccent);

        refreshView(true, false);
    }

    private void configureScreenRotation()
    {
        if (ADV().mScreenRotationMode == MultiBrowserOptions.ScreenMode.AllowPortraitUprightOnly)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if (ADV().mScreenRotationMode == MultiBrowserOptions.ScreenMode.AllowPortraitUprightAndLandscape)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        else if (ADV().mScreenRotationMode == MultiBrowserOptions.ScreenMode.AllowLandscapeOnly)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else if (ADV().mScreenRotationMode == MultiBrowserOptions.ScreenMode.AllowAll)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
        else if (ADV().mScreenRotationMode == MultiBrowserOptions.ScreenMode.SystemDefault)
        {
            if (data.mDefaultScreenOrientation != null) setRequestedOrientation(data.mDefaultScreenOrientation);
        }
    }

    private void setupParentDirLayout()
    {
        ((LinearLayout)findViewById(R.id.parDirLayout)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String parentDir = UtilsKt.getParentDir(OPT().mCurrentDir);

                if (!parentDir.isEmpty()) refreshView(parentDir, false, false);
            }
        });

        ((ImageView)findViewById(R.id.parentDirIcon)).setImageResource(R.mipmap.ic_folder_up);
        ((TextView)findViewById(R.id.parDirSubText)).setText("(Go Up)");
    }

    private void setupSaveFileLayout()
    {
        mEditTextSaveFileName = (EditText)findViewById(R.id.saveFileEditText);
        if (OPT().mDefaultSaveFileName != null) mEditTextSaveFileName.setText(OPT().mDefaultSaveFileName);

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
                    String dir = OPT().mCurrentDir;
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
                ((TextView)view).setTextColor(THM().mColorFilterText);
                ((TextView)view).setTextSize(THM().mUnitSp, THM().mSizeFileFilterText);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.fileFilterPopupItem);
                text.setTypeface(THM().getFontNorm(getAssets()));
                text.setTextColor(THM().mColorFilterPopupText);
                text.setBackgroundColor(THM().mColorFilterPopupBackground);
                text.setTextSize(THM().mUnitSp, THM().mSizeFileFilterPopupText);
                return view;
            }
        };
        
        spinnerFileFilters.setAdapter(adapter);
        
        spinnerFileFilters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id)
            {
                if (OPT().mFileFilterIndex == position) return;
                
                if (mEditTextSaveFileName.getVisibility() != View.GONE)
                {
                    String filename = mEditTextSaveFileName.getText().toString().trim();
                    
                    if (!filename.isEmpty())
                    {
                        filename = changeFileExt(filename, OPT().mFileFilterIndex, position);
                    }

                    mEditTextSaveFileName.setText(filename);
                }
                    
                OPT().mFileFilterIndex = position;
                    
                refreshView(false, false);
            }

            @Override
            public void onNothingSelected(AdapterView parent) { }
        });

        spinnerFileFilters.setSelection(OPT().mFileFilterIndex);
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

    public void refreshView(boolean forceSourceReload, boolean refreshLayout)
    {
        refreshView(OPT().mCurrentDir, forceSourceReload, refreshLayout);
    }

    void refreshView(String dir, boolean forceSourceReload, boolean refreshLayout)
    {
        boolean firstLoad = data.mFirstLoad;
        data.mFirstLoad = false;

        ArrayList<DirectoryItem> items = getDirectoryItems(dir, forceSourceReload);

        boolean galleryView = OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery;

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
            if (!galleryView) OPT().mCurrentDir = dir;

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

        boolean galleryView = OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery;

        boolean readable = galleryView || Utils2Kt.directoryIsReadable(this, dir);
        if (!readable) return null;

        if (galleryView || OPT().mShowImagesWhileBrowsingNormal)
        {
            boolean reload = forceSourceReload ||
                    ADV().mAutoRefreshDirectorySource || data.mMediaStoreImageInfoList == null;

            if (reload) data.mMediaStoreImageInfoList = ListUtils.getImageInfos(this);
        }

        ArrayList<DirectoryItem> items;
        if (galleryView)
        {
            items = data.mMediaStoreImageInfoList;
        }
        else
        {
            boolean reload = forceSourceReload || ADV().mAutoRefreshDirectorySource ||
                    data.mFileSystemDirectoryItems == null ||
                    OPT().mCurrentDir == null || !dir.equalsIgnoreCase(OPT().mCurrentDir);

            if (reload) data.mFileSystemDirectoryItems =
                    ListUtils.getDirectoryItemsFromFileSystem(this, dir, OPT().mFileFilters[OPT().mFileFilterIndex]);

            items = data.mFileSystemDirectoryItems;
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

        boolean galleryView = OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery;

        if (!galleryView && showLayouts)
        {
            boolean isRoot = OPT().mCurrentDir.equals("/");
            showParentDirLayout = ADV().mShowParentDirectoryLayoutIfAvailable && !isRoot;
            showCurrentDirLayout = ADV().mShowCurrentDirectoryLayoutIfAvailable;
            showSaveFileLayout = ADV().mShowSaveFileLayoutIfAvailable &&
                OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.SaveFilesAndOrFolders;
            showFileFilterLayout = ADV().mShowFileFilterLayoutIfAvailable &&
                (OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.LoadFilesAndOrFolders ||
                OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.SaveFilesAndOrFolders);
        }

        if (showCurrentDirLayout)
        {
            curDirLayout.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.curDirText)).setText(OPT().mCurrentDir);
        }
        else
        {
            curDirLayout.setVisibility(View.GONE);
        }

        if (showParentDirLayout)
        {
            parDirLayout.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.parDirText)).setText(UtilsKt.getParentDir(OPT().mCurrentDir));
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
        if (OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.List)
        {
            manager = new LinearLayoutManager(getApplicationContext());
        }
        else
        {
            int columnCount = OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Tiles ?
                OPT().mNormalViewColumnCount : OPT().mGalleryViewColumnCount;

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

            if (OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery)
            {
                if (OPT().mAlwaysShowDialogForSavingGalleryItem) showDialog = true;
            }
            else if (file)
            {
                if (OPT().mAlwaysShowDialogForSavingFile || OPT().mShowOverwriteDialogForSavingFileIfExists)
                    showDialog = true;
            }
            else
            {
                if (OPT().mAlwaysShowDialogForSavingFolder) showDialog = true;
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
            if (ADV().mDebugMode)
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
            if (load) return OPT().mOnSelectFileForLoad;
            else return OPT().mOnSelectFileForSave;
        }
        else
        {
            if (load) return OPT().mOnSelectFolderForLoad;
            else return OPT().mOnSelectFolderForSave;
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

        String[] filters = OPT().mFileFilters[OPT().mFileFilterIndex];

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
        if (ADV().mMenuEnabled)
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