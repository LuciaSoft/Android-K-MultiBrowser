package com.luciasoft.browserjavatokotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.MediaStoreImageInfoTree

class Data(app: Application): AndroidViewModel(app)
{
    var OPT: Options? = Options()
    var ADV: AdvancedOptions? = AdvancedOptions()
    var THM: ThemeOptions? = ThemeOptions()

    var currentDir: String? = null
    var fileFilterIndex = 0

    init
    {
        val ab = app as AppBase
        ab.initOpts(OPT!!, ADV!!, THM!!)
        currentDir = OPT!!.startDir
        fileFilterIndex = OPT!!.startFileFilterIndex
    }

    var mDefaultScreenOrientation: Int? = null
    var mFileSystemDirectoryItems: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoList: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoTree: MediaStoreImageInfoTree? = null
    var mFirstLoad = true

    override fun onCleared()
    {
        super.onCleared()
        OPT = null
        ADV = null
        THM = null
        currentDir = null
        fileFilterIndex = 0
        mDefaultScreenOrientation = null
        mFileSystemDirectoryItems = null
        mMediaStoreImageInfoList = null
        mMediaStoreImageInfoTree = null
        mFirstLoad = true
    }
}