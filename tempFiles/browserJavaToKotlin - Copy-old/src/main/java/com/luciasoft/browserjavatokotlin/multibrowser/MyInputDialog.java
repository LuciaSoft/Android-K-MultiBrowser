package com.luciasoft.browser.multibrowser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.luciasoft.browser.R;

class MyInputDialog
{
    private AlertDialog.Builder builder;

    interface DoSomethingWithResult
    {
        void doSomething(String result);
    }

    MyInputDialog(Context context, String title, String subTitle, DoSomethingWithResult doSomething)
    {
        builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);
        builder.setView(dialogLayout);

        EditText inputText = (EditText)dialogLayout.findViewById(R.id.inputText);

        if (title != null) ((TextView)dialogLayout.findViewById(R.id.title)).setText(title);
        if (subTitle != null) ((TextView)dialogLayout.findViewById(R.id.subTitle)).setText(subTitle);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doSomething.doSomething(inputText.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", null);
    }

    void show()
    {
        if (builder != null) builder.show();
    }
}