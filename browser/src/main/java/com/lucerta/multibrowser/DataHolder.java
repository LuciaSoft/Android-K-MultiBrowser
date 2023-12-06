package com.lucerta.multibrowser;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.luciasoft.collections.DirectoryItem;
import com.luciasoft.collections.MediaStoreImageInfoTree;

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
