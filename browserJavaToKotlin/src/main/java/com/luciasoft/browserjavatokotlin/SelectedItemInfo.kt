package com.luciasoft.browserjavatokotlin

class SelectedItemInfo internal constructor(@JvmField var path: String, var action: SelectAction)
{
    enum class SelectAction
    {
        ShortClick, LongClick, SaveButton
    }

}