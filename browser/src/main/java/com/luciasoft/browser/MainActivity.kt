package com.luciasoft.browser

import android.content.pm.ActivityInfo
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
    lateinit var mEditTextSaveFileName: EditText
    lateinit var mRecyclerView: MyRecyclerView

    val DAT = Data
    val OPT = Options
    val ADV = AdvancedOptions
    val THM = ThemeOptions

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        configureScreenRotation()

        val actionBar: ActionBar? = try { supportActionBar } catch (ex: Exception) { null }
        if (actionBar != null)
        {
            val tv = TextView(getApplicationContext());
            tv.setTypeface(THM.getFontBold(getAssets()));
            tv.setText(OPT.browserTitle);
            tv.setTextColor(THM.colorBrowserTitle);
            tv.setTextSize(THM.unitSp, THM.sizeBrowserTitle);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(tv);
            actionBar.setBackgroundDrawable(ColorDrawable(THM.colorActionBar));
        }
    }

    private fun configureScreenRotation()
    {
        if (ADV.screenRotationMode == Options.ScreenMode.AllowPortraitUprightOnly)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        else if (ADV.screenRotationMode == Options.ScreenMode.AllowPortraitUprightAndLandscape)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
        else if (ADV.screenRotationMode == Options.ScreenMode.AllowLandscapeOnly)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
        else if (ADV.screenRotationMode == Options.ScreenMode.AllowAll)
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        }
        else if (ADV.screenRotationMode == Options.ScreenMode.SystemDefault)
        {
            if (DAT!!.mDefaultScreenOrientation != null) requestedOrientation =
                DAT!!.mDefaultScreenOrientation!!
        }
    }
}