package com.luciasoft.browserjavatokotlin

import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.MediaStoreImageInfoTree

class Data(app: MultiBrowser)
{
    var currentDir: String? = app.OPT.startDir
    var fileFilterIndex = app.OPT.startFileFilterIndex

    var mDefaultScreenOrientation: Int? = null
    var mFileSystemDirectoryItems: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoList: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoTree: MediaStoreImageInfoTree? = null
    var mFirstLoad = true
}