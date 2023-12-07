package com.luciasoft.browser.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucerta.multibrowser.MultiBrowserOptions
import com.luciasoft.browser.DataHolder
import com.luciasoft.browser.MyRecyclerView
import com.luciasoft.browser.R

class MainActivity : AppCompatActivity()
{
    private var data: DataHolder? = null
    private var tmpOptions: MultiBrowserOptions? = null

    private var mRecyclerView: MyRecyclerView? = null;
    private var mEditTextSaveFileName: EditText? = null;

    val DAT = { data }
    val OPT = { data?.mOptions }
    val ADV = { data?.mOptions?.advanced() }
    val THM = { data?.mOptions?.theme() }

    fun getOptions(): MultiBrowserOptions?
    {
        if (data == null && tmpOptions == null) return null;
        if (data == null) return tmpOptions;
        return data!!.mOptions;
    }

    fun setOptions(options: MultiBrowserOptions?, squelchException: Boolean): Boolean
    {
        if (options == null)
        {
            if (squelchException) return false;
            throw IllegalArgumentException("The options cannot be null.");
        }

        if (data == null)
        {
            tmpOptions = options;
        }
        else
        {
            data!!.mOptions = options
            tmpOptions = null;
        }

        return true;
    }

    fun setEditTextSaveFileName(name: String)
    {
        mEditTextSaveFileName!!.setText(name);
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        data = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
            application
        ))[DataHolder::class.java]

        if (data!!.mDefaultScreenOrientation == null) data!!.mDefaultScreenOrientation =
            requestedOrientation;

        if (tmpOptions != null)
        {
            data!!.mOptions = tmpOptions;
            tmpOptions = null;
        }
    }
}