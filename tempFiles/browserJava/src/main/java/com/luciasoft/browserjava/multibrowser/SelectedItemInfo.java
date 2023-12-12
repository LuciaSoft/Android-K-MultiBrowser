package com.luciasoft.browserjava.multibrowser;

public class SelectedItemInfo
{
    public enum SelectAction { ShortClick, LongClick, SaveButton };
    String path;
    SelectAction action;

    SelectedItemInfo(String path, SelectAction action)
    {
        this.path = path;
        this.action = action;
    }

    public String getPath()
    {
        return path;
    }

    public SelectAction getAction()
    {
        return action;
    }
}
