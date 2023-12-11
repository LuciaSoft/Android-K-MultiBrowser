package com.luciasoft.browserjavatokotlin

import android.os.Bundle

class MainActivity
    : MultiBrowserActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        //val opt1 = Options()
        //val adv1 = AdvancedOptions()
        
        //adv1.debugMode = true
        //opt1.mFileFilterString =
        //    " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
        //        " PNG Image Files ( *.png ) |*.png|" +
        //        " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
        //        " All Files ( *.* ) |*"
        //opt1.startFileFilterIndex = 3
        //opt1.browseMode = Options.BrowseMode.LoadFilesAndOrFolders
        //
        //val opt2 = Options()
        //val adv2 = AdvancedOptions()
        //
        //adv2.debugMode = false
        //adv2.allowLongClickFileForSave = true
        //adv2.allowShortClickFileForSave = false
        //opt2.mFileFilterString
        //    " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
        //        " PNG Image Files ( *.png ) |*.png|" +
        //        " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
        //        " All Files ( *.* ) |*"
        //
        //opt2.startFileFilterIndex = 3
        //opt2.browseMode = Options.BrowseMode.SaveFilesAndOrFolders
        /*if (true) options2.onSelectFileForSave = object : OnSelectItem
        {
            override fun onSelect(info: SelectedItemInfo)
            {
                Toast.makeText(this@MainActivity, info.path, Toast.LENGTH_LONG).show()
            }
        }*/
        
        //val options3 = Options()
        //options3.advanced().setDebugMode(true);

        //setOptions(options3, false);
        
        super.onCreate(savedInstanceState)
        
        /*try
        {
            options2.saveXml("/sdcard/mboptions.xml")
        }
        catch (ex: Exception)
        {
            Toast.makeText(this, "" + ex.message, Toast.LENGTH_LONG).show()
        }*/

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