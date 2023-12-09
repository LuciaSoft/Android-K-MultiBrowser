package com.luciasoft.browserjavatokotlin

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

internal class MyListDialog
{
    var choice = -1
        private set

    fun show(
        context: Context?,
        title: String?,
        options: Array<String?>?,
        defaultChoice: Int,
        onOkClick: DialogInterface.OnClickListener?
    )
    {
        var title = title
        if (title == null) title = ""
        val builder = AlertDialog.Builder(
            context!!
        )
        builder.setTitle(title)
        builder.setSingleChoiceItems(
            options,
            defaultChoice,
            object : DialogInterface.OnClickListener
            {
                override fun onClick(dialog: DialogInterface, which: Int)
                {
                    choice = which
                }
            })
        builder.setPositiveButton("OK", onOkClick)
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }
}