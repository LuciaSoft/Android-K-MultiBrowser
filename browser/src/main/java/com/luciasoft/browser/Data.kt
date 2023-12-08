package com.luciasoft.browser

import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.MediaStoreImageInfoTree

class Data
{
    var mDefaultScreenOrientation: Int? = null
    var mFileSystemDirectoryItems: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoList: ArrayList<DirectoryItem>? = null
    var mMediaStoreImageInfoTree: MediaStoreImageInfoTree? = null
    var mFirstLoad = true
}