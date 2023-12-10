package com.luciasoft.browserjavatokotlin

import android.os.Bundle
import android.widget.Toast

class MainActivity
    : MultiBrowserActivity
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        val options1 = MultiBrowserOptions()
        options1.mAdvancedOptions.debugMode = true
        options1.setFileFilter(
            " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
                " PNG Image Files ( *.png ) |*.png|" +
                " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
                " All Files ( *.* ) |*"
        )
        options1.fileFilterIndex = 3
        options1.browseMode = MultiBrowserOptions.BrowseMode.LoadFilesAndOrFolders
        val options2 = MultiBrowserOptions()
        options2.mAdvancedOptions.debugMode = false
        options2.mAdvancedOptions.allowLongClickFileForSave = true
        options2.mAdvancedOptions.allowShortClickFileForSave = false
        options2.setFileFilter(
            " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
                " PNG Image Files ( *.png ) |*.png|" +
                " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
                " All Files ( *.* ) |*"
        )
        options2.fileFilterIndex = 3
        options2.browseMode = MultiBrowserOptions.BrowseMode.SaveFilesAndOrFolders
        if (true) options2.onSelectFileForSave = object : OnSelectItem
        {
            override fun onSelect(info: SelectedItemInfo)
            {
                Toast.makeText(this@MainActivity, info.path, Toast.LENGTH_LONG).show()
            }
        }
        val options3 = MultiBrowserOptions()
        //options3.advanced().setDebugMode(true);

        //setOptions(options3, false);
        super.onCreate(savedInstanceState)
        try
        {
            options2.saveXml("/sdcard/mboptions.xml")
        }
        catch (ex: Exception)
        {
            Toast.makeText(this, "" + ex.message, Toast.LENGTH_LONG).show()
        }

        /*try
        {
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/cambria.ttf");
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }
}