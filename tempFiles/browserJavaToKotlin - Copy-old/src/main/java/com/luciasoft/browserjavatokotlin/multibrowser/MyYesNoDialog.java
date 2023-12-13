package com.luciasoft.browser.multibrowser;

import android.content.Context;
import android.content.DialogInterface;

class MyYesNoDialog
{
	static void show(Context context, String title, String message, DialogInterface.OnClickListener onYesClick)
	{
		MyMessageBox.show(context, title, message, MyMessageBox.ButtonsType.YesNo, onYesClick, null);
	}
}
