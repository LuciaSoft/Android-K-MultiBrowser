package com.luciasoft.browserjavatokotlin

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

internal class MyListDialog
{
    var choice = -1
        private set

    fun show(
        context: Context,
        title: String = "",
        options: Array<String>,
        defaultChoice: Int,
        onOkClick: DialogInterface.OnClickListener? = null
    )
    {
        with (AlertDialog.Builder(context))
        {
            this.setTitle(title)
            this.setSingleChoiceItems(
                options,
                defaultChoice,
                object : DialogInterface.OnClickListener
                {
                    override fun onClick(dialog: DialogInterface, which: Int)
                    {
                        choice = which
                    }
                })
            this.setPositiveButton("OK", onOkClick)
            this.setNegativeButton("Cancel", null)
            this.create().show()
        }
    }
}