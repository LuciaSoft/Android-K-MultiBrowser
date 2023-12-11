package com.luciasoft.browserjavatokotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.MediaStoreImageInfoTree

class DataHolder(var app: Application)
    : AndroidViewModel(app)
{
    @JvmField
    var OPT: Options? = null
    @JvmField
    var ADV: AdvancedOptions? = null
    @JvmField
    var THM: ThemeOptions? = null
    @JvmField
    var mDefaultScreenOrientation: Int? = null
    @JvmField
    var mFileSystemDirectoryItems: ArrayList<DirectoryItem>? = null
    @JvmField
    var mMediaStoreImageInfoList: ArrayList<DirectoryItem>? = null
    @JvmField
    var mMediaStoreImageInfoTree: MediaStoreImageInfoTree? = null
    @JvmField
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