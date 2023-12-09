package com.luciasoft.browserjavatokotlin;

import android.os.Bundle;
import android.widget.Toast;

import com.luciasoft.browserjavatokotlin.multibrowser.MultiBrowserActivity;
import com.luciasoft.browserjavatokotlin.multibrowser.MultiBrowserOptions;
import com.luciasoft.browserjavatokotlin.multibrowser.OnSelectItem;
import com.luciasoft.browserjavatokotlin.multibrowser.SelectedItemInfo;

public class MainActivity extends MultiBrowserActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MultiBrowserOptions options1 = new MultiBrowserOptions();
        options1.advanced().setDebugMode(true);
        options1.setFileFilter(
                " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
                        " PNG Image Files ( *.png ) |*.png|" +
                        " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
                        " All Files ( *.* ) |*");
        options1.setFileFilterIndex(3);
        options1.setBrowseMode(MultiBrowserOptions.BrowseMode.LoadFilesAndOrFolders);

        MultiBrowserOptions options2 = new MultiBrowserOptions();
        options2.advanced().setDebugMode(false);
        options2.advanced().setAllowLongClickFileForSave(true);
        options2.advanced().setAllowShortClickFileForSave(false);
        options2.setFileFilter(
                " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
                        " PNG Image Files ( *.png ) |*.png|" +
                        " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
                        " All Files ( *.* ) |*");
        options2.setFileFilterIndex(3);
        options2.setBrowseMode(MultiBrowserOptions.BrowseMode.SaveFilesAndOrFolders);
        if (true) options2.setOnSelectFileForSave(new OnSelectItem() {
            @Override
            public void onSelect(SelectedItemInfo info)
            {
                Toast.makeText(MainActivity.this, info.getPath(), Toast.LENGTH_LONG).show();
            }
        });

        MultiBrowserOptions options3 = new MultiBrowserOptions();
        //options3.advanced().setDebugMode(true);

        setOptions(options3, false);

        super.onCreate(savedInstanceState);

        try { options2.saveXml("/sdcard/mboptions.xml"); }
        catch (Exception ex)
        {
            Toast.makeText(this,"" + ex.getMessage(), Toast.LENGTH_LONG).show();
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