package com.luciasoft.browser

interface OnSelectItem
{
    fun onSelect(info: SelectedItemInfo)
}

class SelectedItemInfo(val path: String, val action: SelectAction)
{
    enum class SelectAction { ShortClick, LongClick, SaveButton }
}