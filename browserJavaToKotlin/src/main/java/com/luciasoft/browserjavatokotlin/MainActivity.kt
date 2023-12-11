package com.luciasoft.browserjavatokotlin

class MainActivity
    : MultiBrowserActivity()
{
    override fun initialize()
    {
        val opt1 = APP.OPT
        opt1.mFileFilterString =
            " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
                " PNG Image Files ( *.png ) |*.png|" +
                " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
                " All Files ( *.* ) |*"
    }
}