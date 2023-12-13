package com.luciasoft.browser

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

internal class MyMessageBox private constructor(context: Context) : AlertDialog(context)
{
    internal enum class ButtonsType
    {
        Ok, OkCancel, YesNo
    }

    companion object
    {
        @JvmStatic
        fun show(
            context: Context,
            title: String = "",
            message: String = "",
            buttons: ButtonsType = ButtonsType.Ok,
            onOkYesClick: DialogInterface.OnClickListener? = null,
            onCancelNoClick: DialogInterface.OnClickListener? = null
        )
        {
            val mb = MyMessageBox(context)
            val twoButtons = buttons == ButtonsType.OkCancel || buttons == ButtonsType.YesNo
            mb.setTitle(title)
            mb.setMessage(message)
            mb.setCancelable(twoButtons)
            val button1Text = if (buttons == ButtonsType.YesNo) "Yes" else "OK"
            val button2Text = if (buttons == ButtonsType.YesNo) "No" else "Cancel"
            mb.setButton(-1, button1Text, onOkYesClick)
            if (twoButtons) mb.setButton(-2, button2Text, onCancelNoClick)
            mb.show()
        }
    }
}