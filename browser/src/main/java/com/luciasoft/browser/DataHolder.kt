package com.luciasoft.browser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lucerta.multibrowser.MultiBrowserOptions
import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.MediaStoreImageInfoTree

class DataHolder(app: Application) : AndroidViewModel(app)
{
    var mOptions: MultiBrowserOptions? = MultiBrowserOptions()
    var mDefaultScreenOrientation: Int? = null
    var mFileSystemDirectoryItems: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoList: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoTree: MediaStoreImageInfoTree? = null
    var mFirstLoad = true

    override fun onCleared()
    {
        super.onCleared()

        mOptions = null
        mDefaultScreenOrientation = null
        mFileSystemDirectoryItems = null
        mMediaStoreImageInfoList = null
        mMediaStoreImageInfoTree = null
        mFirstLoad = true
    }
}