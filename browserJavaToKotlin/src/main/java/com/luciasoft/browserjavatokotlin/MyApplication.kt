package com.luciasoft.browserjavatokotlin

class MyApplication: AppBase()
{
    override fun initOpts(opt: Options, adv: AdvancedOptions, thm: ThemeOptions)
    {
        opt.mFileFilterString =
            " Compatible Image Files ( *.png,*.jpg,*.jpeg ) |*.png,*.jpg,*.jpeg|" +
                " PNG Image Files ( *.png ) |*.png|" +
                " JPG Image Files ( *.jpg,*.jpeg ) |*.jpg,*.jpeg|" +
                " All Files ( *.* ) |*"
    }

}