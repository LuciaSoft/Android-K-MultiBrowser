package com.luciasoft.browserjavatokotlin

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
            title: String?,
            message: String?,
            buttons: ButtonsType?,
            onOkYesClick: DialogInterface.OnClickListener?,
            onCancelNoClick: DialogInterface.OnClickListener?
        )
        {
            var title = title
            var message = message
            var buttons = buttons
            if (title == null) title = ""
            if (message == null) message = ""
            val mb = MyMessageBox(context)
            if (buttons == null) buttons = ButtonsType.Ok
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