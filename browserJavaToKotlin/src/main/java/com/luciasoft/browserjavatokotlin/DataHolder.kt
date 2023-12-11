package com.luciasoft.browserjavatokotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.MediaStoreImageInfoTree

class DataHolder(var app: Application)
    : AndroidViewModel(app)
{
    var OPT: Options? = null
    var ADV: AdvancedOptions? = null
    var THM: ThemeOptions? = null
    var mDefaultScreenOrientation: Int? = null
    var mFileSystemDirectoryItems: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoList: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoTree: MediaStoreImageInfoTree? = null
    var mFirstLoad = true

    init
    {
        OPT = Options()
        ADV = AdvancedOptions()
        THM = ThemeOptions()
    }

    override fun onCleared()
    {
        super.onCleared()
        OPT = null
        ADV = null
        THM = null
        mDefaultScreenOrientation = null
        mFileSystemDirectoryItems = null
        mMediaStoreImageInfoList = null
        mMediaStoreImageInfoTree = null
        mFirstLoad = true
    }
}