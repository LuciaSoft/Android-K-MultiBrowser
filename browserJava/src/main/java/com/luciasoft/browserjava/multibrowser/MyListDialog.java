package com.luciasoft.browserjava.multibrowser;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

class MyListDialog
{
    private int choice = -1;

    private void setChoice(int choice)
    {
        this.choice = choice;
    }

    int getChoice()
    {
        return choice;
    }

    void show(Context context, String title, final String[] options, int defaultChoice, DialogInterface.OnClickListener onOkClick)
    {
        if (title == null) title = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        builder.setSingleChoiceItems(options, defaultChoice, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                setChoice(which);
            }
        });

        builder.setPositiveButton("OK", onOkClick);

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}