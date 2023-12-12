package com.luciasoft.browserjava.multibrowser;

import com.luciasoft.browserjava.collections.BinarySearchTree;

import java.util.Comparator;

class DirectoryItem
{
    DirectoryItem(String path)
    {
        this.path = path;
    }

    DirectoryItem(String path, String info)
    {
        this.path = path;
        this.info = info;
    }
    
    DirectoryItem(String path, Long date, String info)
    {
        this.path = path;
        this.date = date;
        this.info = info;
    }

    private String path = null;
    private String name = null;
    private String info = null;
    private Long date = null;

    String getPath()
    {
        return path;
    }

    String getName()
    {
        if (name == null)
        {
            name = Utils.getShortName(path);
        }

        return name;
    }

    String getInfo()
    {
        return info;
    }

    void setInfo(String info)
    {
        this.info = info;
    }
    
    Long getDate()
    {
        return date;
    }
    
    void setDate(Long date)
    {
        this.date = date;
    }
}

class FileItem extends DirectoryItem
{
    FileItem(String path)
    {
        super(path);
    }

    FileItem(String path, Integer imageId)
    {
        super(path);

        this.imageId = imageId;
    }

    FileItem(String path, String info)
    {
        super(path, info);
    }

    FileItem(String path, Long date, Long size, String info, Integer imageId)
    {
        super(path, date, info);
        
        this.size = size;
        this.imageId = imageId;
    }

    Long size = null;
    Integer imageId = null;
    
    Long getSize()
    {
        return size;
    }
    
    void setSize(Long size)
    {
        this.size = size;
    }

    Integer getImageId()
    {
        return imageId;
    }

    void setImageId(int imageId)
    {
        this.imageId = imageId;
    }
}

class FolderItem extends DirectoryItem
{
    FolderItem(String path)
    {
        super(path);
    }

    FolderItem(String path, String info)
    {
        super(path, info);
    }

    FolderItem(String path, Long date, String info)
    {
        super(path, date, info);
    }
}

class MediaStoreImageInfoTree extends BinarySearchTree<FileItem>
{
    MediaStoreImageInfoTree()
    {
        super(FileItemPathComparator.getComparator());
    }

    FileItem getFileItem(String path)
    {
        return super.getData(new FileItem(path));
    }

    Integer getImageId(String path)
    {
        FileItem fileItem = getFileItem(path);

        if (fileItem == null) return null;

        return fileItem.getImageId();
    }
}

class FileItemPathComparator implements Comparator<FileItem>
{
    @Override
    public int compare(FileItem fileItem1, FileItem fileItem2)
    {
        return fileItem1.getPath().compareToIgnoreCase(fileItem2.getPath());
    }

    private static FileItemPathComparator comparator;

    public static FileItemPathComparator getComparator()
    {
        if (comparator == null) comparator = new FileItemPathComparator();

        return comparator;
    }
}
