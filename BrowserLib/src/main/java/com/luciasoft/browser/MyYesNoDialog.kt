package com.luciasoft.browser

import android.content.Context
import android.content.DialogInterface
import com.luciasoft.browser.MyMessageBox.Companion.show

internal object MyYesNoDialog
{
    fun show(
        context: Context,
        title: String = "",
        message: String = "",
        onYesClick: DialogInterface.OnClickListener? = null
    )
    {
        show(context, title, message, MyMessageBox.ButtonsType.YesNo, onYesClick, null)
    }
}