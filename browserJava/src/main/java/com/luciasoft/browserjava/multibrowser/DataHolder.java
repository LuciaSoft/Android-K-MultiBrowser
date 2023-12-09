package com.luciasoft.browserjava.multibrowser;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

public class DataHolder extends AndroidViewModel
{
    public DataHolder(Application app)
    {
        super(app);
        this.app = app;

        mOptions = new MultiBrowserOptions();
    }

    Application app;
    MultiBrowserOptions mOptions = null;
    Integer mDefaultScreenOrientation = null;
    ArrayList<DirectoryItem> mFileSystemDirectoryItems = null;
    ArrayList<DirectoryItem> mMediaStoreImageInfoList = null;
    MediaStoreImageInfoTree mMediaStoreImageInfoTree = null;
    boolean mFirstLoad = true;



    @Override
    protected void onCleared()
    {
        super.onCleared();

        mOptions = null;
        mDefaultScreenOrientation = null;
        mFileSystemDirectoryItems = null;
        mMediaStoreImageInfoList = null;
        mMediaStoreImageInfoTree = null;
        mFirstLoad = true;
    }

}
