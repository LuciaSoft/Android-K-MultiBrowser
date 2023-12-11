package com.luciasoft.browserjavatokotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.luciasoft.collections.DirectoryItem
import com.luciasoft.collections.MediaStoreImageInfoTree

class Data(app: Application): AndroidViewModel(app)
{
    var OPT: Options = Options()
    var ADV: AdvancedOptions = AdvancedOptions()
    var THM: ThemeOptions = ThemeOptions()

    var currentDir: String? = null
    var fileFilterIndex = 0

    init
    {
        val ab = app as AppBase
        ab.initOpts(OPT, ADV, THM)
        currentDir = OPT.startDir
        fileFilterIndex = OPT.startFileFilterIndex
    }

    var defaultScreenOrientation: Int? = null
    var fileSystemDirectoryItems: ArrayList<DirectoryItem>? = null
    var mediaStoreImageInfoList: ArrayList<DirectoryItem>? = null
    var mediaStoreImageInfoTree: MediaStoreImageInfoTree? = null
    var firstLoad = true
}