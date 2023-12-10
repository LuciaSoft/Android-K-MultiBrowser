package com.luciasoft.browserjavatokotlin

import android.content.Context
import android.content.DialogInterface
import com.luciasoft.browserjavatokotlin.MyMessageBox.Companion.show

internal class MyYesNoDialog
{
    companion object
    {
        fun show(
            context: Context,
            title: String = "",
            message: String = "",
            onYesClick: DialogInterface.OnClickListener? = null)
        {
            show(context, title, message, MyMessageBox.ButtonsType.YesNo, onYesClick, null)
        }
    }
}