package com.luciasoft.browser.multibrowser;

import android.app.AlertDialog;
import android.content.Context;

class MyMessageBox extends AlertDialog
{
    enum ButtonsType { Ok, OkCancel, YesNo };

    private MyMessageBox(Context context)
    {
        super(context);
    }

    static void show(Context context, String title, String message, ButtonsType buttons, OnClickListener onOkYesClick, OnClickListener onCancelNoClick)
    {
        if (title == null) title = "";
        if (message == null) message = "";

        MyMessageBox mb = new MyMessageBox(context);

        if (buttons == null) buttons = ButtonsType.Ok;

        boolean twoButtons = buttons == ButtonsType.OkCancel || buttons == ButtonsType.YesNo;

        mb.setTitle(title);
        mb.setMessage(message);
        mb.setCancelable(twoButtons);

        String button1Text = buttons == ButtonsType.YesNo ? "Yes" : "OK";
        String button2Text = buttons == ButtonsType.YesNo ? "No" : "Cancel";

        mb.setButton(-1, button1Text, onOkYesClick);
        if (twoButtons) mb.setButton(-2, button2Text, onCancelNoClick);

        mb.show();
    }
}
