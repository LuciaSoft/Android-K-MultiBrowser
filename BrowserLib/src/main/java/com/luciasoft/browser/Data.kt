package com.luciasoft.browser

import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class Data(app: Application): AndroidViewModel(app)
{
    val OPT: Options = Options()
    val ADV: AdvancedOptions = AdvancedOptions()
    val THM: ThemeOptions = ThemeOptions()

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
    var mediaStoreImageInfoMap: HashMap<String, Int> = HashMap()
    var firstLoad = true

    override fun onCleared()
    {
        super.onCleared()
        mediaStoreImageInfoMap.clear()
    }
}