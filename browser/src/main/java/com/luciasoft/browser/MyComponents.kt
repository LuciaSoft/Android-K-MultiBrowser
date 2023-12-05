package com.luciasoft.browser

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView

class MyInputDialog(context: Context, title: String = "", subTitle: String = "", doSomething: DoSomethingWithResult)
{
    interface DoSomethingWithResult
    {
        fun doSomething(result: String)
    }

    private val builder = AlertDialog.Builder(context)

    init
    {
        @SuppressLint("InflateParams")
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.input_dialog, null)
        builder.setView(dialogLayout)

        dialogLayout.findViewById<TextView>(R.id.title).text = title
        dialogLayout.findViewById<TextView>(R.id.subTitle).text = subTitle

        val inputText = dialogLayout.findViewById<EditText>(R.id.inputText)

        builder.setPositiveButton("OK")
        { dialog, which -> doSomething.doSomething(inputText.text.toString()) }

        builder.setNegativeButton("Cancel", null)
    }

    fun show() { builder.show() }
}

class MyListDialog
{
    var choice = -1
    private set

    fun show(context: Context, title: String = "", options: Array<String>, defaultChoice: Int, onOkClick: OnClickListener? = null)
    {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

        builder.setSingleChoiceItems(options, defaultChoice)
        { dialog, which -> choice = which }

        builder.setPositiveButton("OK", onOkClick)
        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }
}

class MyMessageBox(context: Context) : AlertDialog(context)
{
    enum class ButtonsType { Ok, OkCancel, YesNo }

    companion object
    {
        fun show(context: Context, title: String = "", message: String = "", buttons: ButtonsType = ButtonsType.Ok, onOkYesClick: OnClickListener? = null, onCancelNoClick: OnClickListener? = null)
        {
            val mb = MyMessageBox(context)

            val twoButtons = buttons == ButtonsType.OkCancel || buttons == ButtonsType.YesNo

            mb.setTitle(title)
            mb.setMessage(message)
            mb.setCancelable(twoButtons)

            val button1Text = if (buttons == ButtonsType.YesNo) "Yes" else "Ok"
            val button2Text = if (buttons == ButtonsType.YesNo) "No" else "Cancel"

            mb.setButton(-1, button1Text, onOkYesClick)
            if (twoButtons) mb.setButton(-2, button2Text, onCancelNoClick)

            mb.show()
        }
    }
}

class MyYesNoDialog
{
    companion object
    {
        fun show(context: Context, title: String = "", message: String = "", onYesClick: OnClickListener? = null)
        {
            MyMessageBox.show(context, title, message, MyMessageBox.ButtonsType.YesNo, onYesClick, null)
        }
    }
}