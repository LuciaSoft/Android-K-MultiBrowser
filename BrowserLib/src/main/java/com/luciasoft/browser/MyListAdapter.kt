package com.luciasoft.browser

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luciasoft.browser.Utils.getShortName
import java.io.File

// OPTIMIZED
internal class MyListAdapter(
    private val act: MultiBrowserActivity,
    private val itemList: ArrayList<DirectoryItem>)
    : RecyclerView.Adapter<MyViewHolder>()
{
    private val idMap = (itemList.indices).associateBy { itemList[it] } // .toMap(hashMapOf())

    init
    {
        //for (i in itemList.indices) idMap[itemList[i]] = i // OLD CODE
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long
    {
        val item = itemList[position]
        return idMap[item]!!.toLong()
    }

    override fun getItemCount(): Int
    {
        return itemList.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder
    {
        val view: View =
            if (act.isListView) LayoutInflater.from(viewGroup.context).inflate(
                R.layout.list_item_list_view, viewGroup, false)
            else LayoutInflater.from(viewGroup.context).inflate(
                R.layout.list_item_tiles_view, viewGroup, false)

        view.findViewById<View>(R.id.listItemText).visibility =
            if (act.isGalleryView && !act.OPT.showFileNamesInGalleryView) View.GONE
            else View.VISIBLE

        return MyViewHolder(view, act)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int)
    {
        val listItem = viewHolder.listItem
        val item = itemList[i]
        val isFile = item is FileItem
        val path = item.path
        val image = viewHolder.image
        
        viewHolder.title.text = item.name

        val exists = try { File(path).exists() } catch (ex: Exception) { false }

        if (!exists)
        {
            val infoType: String
            val iconId: Int
            if (isFile)
            {
                infoType = "file"
                iconId = R.mipmap.ic_file_x
            }
            else  // if (item instanceof MultiBrowserDirectoryItem.FolderItem)
            {
                infoType = "folder"
                iconId = R.mipmap.ic_folder_x
            }
            image.scaleType = ImageView.ScaleType.FIT_CENTER
            image.setImageBitmap(BitmapFactory.decodeResource(act.resources, iconId))
            if (act.isListView) viewHolder.info!!.text = "$infoType not found"
            listItem.isClickable = false
            listItem.isLongClickable = false
            listItem.setOnClickListener(null)
            listItem.setOnLongClickListener(null)
            return
        }

        if (isFile)
        {
            var thumb: Bitmap? = null
            if (act.isGalleryView && act.OPT.showImagesWhileBrowsingGallery || !act.isGalleryView && act.OPT.showImagesWhileBrowsingNormal)
            {
                val fileItem = item as FileItem
                val imageId = fileItem.imageId
                if (imageId != null)
                {
                    thumb = try
                    {
                        MediaStore.Images.Thumbnails.getThumbnail(
                            act.contentResolver, imageId.toLong(),
                            MediaStore.Images.Thumbnails.MINI_KIND, null)
                    }
                    catch (ex: Exception)
                    {
                        null
                    }
                    if (thumb != null)
                    {
                        image.scaleType = ImageView.ScaleType.CENTER_CROP
                        image.setImageBitmap(thumb)
                    }
                }
            }
            if (thumb == null)
            {
                image.scaleType = ImageView.ScaleType.FIT_CENTER
                image.setImageBitmap(BitmapFactory.decodeResource(act.resources, R.mipmap.ic_file))
            }
        }
        else // item is FolderItem
        {
            image.scaleType = ImageView.ScaleType.FIT_CENTER
            image.setImageBitmap(BitmapFactory.decodeResource(act.resources, R.mipmap.ic_folder))
        }

        val hidden = try { File(path).isHidden } catch (ex: Exception) { false }
        if (hidden) image.imageAlpha = 127 else image.imageAlpha = 255

        if (act.isListView) viewHolder.info!!.text = item.info

        val loadFilesFolders = act.OPT.browseMode == BrowseMode.LoadFilesAndOrFolders
        val saveFilesFolders = act.OPT.browseMode == BrowseMode.SaveFilesAndOrFolders
        val loadFolders = act.OPT.browseMode == BrowseMode.LoadFolders
        val saveFolders = act.OPT.browseMode == BrowseMode.SaveFolders

        val load = isFile && loadFilesFolders || !isFile && (loadFolders || loadFilesFolders)
        val save = isFile && saveFilesFolders || !isFile && (saveFolders || saveFilesFolders)

        val saveBoxVisible = act.findViewById<View>(R.id.saveFileLayout).visibility != View.GONE

        val sendToSaveBoxShortClick = save && isFile && saveBoxVisible &&
            act.ADV.allowShortClickFileForSave &&
            act.ADV.shortClickSaveFileBehavior != SaveFileBehavior.SaveFile
        val sendToSaveBoxLongClick = save && isFile && saveBoxVisible &&
            act.ADV.allowLongClickFileForSave &&
            act.ADV.longClickSaveFileBehavior != SaveFileBehavior.SaveFile

        val shortClickable = !isFile || sendToSaveBoxShortClick ||
            load && act.ADV.allowShortClickFileForLoad ||
            save && act.ADV.allowShortClickFileForSave

        val longClickable = sendToSaveBoxLongClick ||
            isFile && (load && act.ADV.allowLongClickFileForLoad || save && act.ADV.allowLongClickFileForSave) ||
            !isFile && (load && act.ADV.allowLongClickFolderForLoad || save && act.ADV.allowLongClickFolderForSave)

        listItem.isClickable = shortClickable

        if (!shortClickable)
        {
            listItem.setOnClickListener(null)
        }
        else
        {
            listItem.setOnClickListener(View.OnClickListener {
                if (!isFile)
                {
                    act.refreshView(path, false, false)
                }
                else // item is FileItem
                {
                    if (load)
                    {
                        act.onSelect(true, true, false, false, path)
                    }
                    else
                    {
                        if (sendToSaveBoxShortClick) act.setEditTextSaveFileName(getShortName(path))
                        val saveFile = !saveBoxVisible ||
                            act.ADV.shortClickSaveFileBehavior != SaveFileBehavior.SendNameToSaveBoxOrSaveFile
                        if (saveFile) act.onSelect(true, false, false, false, path)
                    }
                }
            })
        }

        listItem.isLongClickable = longClickable

        if (!longClickable)
        {
            listItem.setOnLongClickListener(null)
        }
        else
        {
            listItem.setOnLongClickListener(View.OnLongClickListener {
                if (!isFile)
                {
                    if (load) act.onSelect(false, true, true, false, path)
                    else act.onSelect(false, false, true, false, path)
                }
                else // item is FileItem
                {
                    if (load)
                    {
                        act.onSelect(true, true, true, false, path)
                    }
                    else
                    {
                        if (sendToSaveBoxLongClick) act.setEditTextSaveFileName(getShortName(path))
                        val saveFile = !saveBoxVisible ||
                            act.ADV.longClickSaveFileBehavior != SaveFileBehavior.SendNameToSaveBoxOrSaveFile
                        if (saveFile) act.onSelect(true, false, true, false, path)
                    }
                }
                return@OnLongClickListener true
            })
        }
    }
}

// OPTIMIZED
internal class MyViewHolder(view: View, act: MultiBrowserActivity)
    : RecyclerView.ViewHolder(view)
{
    val listItem = view as LinearLayout
    val title: TextView = view.findViewById(R.id.listItemText)
    val image: ImageView = view.findViewById(R.id.listItemIcon)
    var info: TextView? = null

    init
    {
        listItem.setBackgroundColor(act.THM.colorListBackground)
        title.typeface = act.THM.getFontBdIt(act.assets)

        if (act.isGalleryView)
        {
            title.setTextColor(act.THM.colorGalleryItemText)
            title.setTextSize(act.THM.unitSp, act.THM.sizeGalleryViewItemText)
        }
        else
        {
            title.setTextColor(act.THM.colorListItemText)

            if (act.isListView)
            {
                title.setTextSize(act.THM.unitSp, act.THM.sizeListViewItemText)
                info = view.findViewById(R.id.listItemSubText)
                info!!.typeface = act.THM.getFontNorm(act.assets)
                info!!.setTextColor(act.THM.colorListItemSubText)
                info!!.setTextSize(act.THM.unitSp, act.THM.sizeListViewItemSubText)
                view.findViewById<View>(R.id.listItemAccent).setBackgroundColor(act.THM.colorListAccent)
            }
            else
            {
                title.setTextSize(act.THM.unitSp, act.THM.sizeTilesViewItemText)
            }
        }
    }
}