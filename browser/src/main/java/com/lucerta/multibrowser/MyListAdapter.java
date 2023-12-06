package com.lucerta.multibrowser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luciasoft.browser.UtilsKt;
import com.luciasoft.collections.DirectoryItem;
import com.luciasoft.collections.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.luciasoft.browser.R;

class MyListAdapter extends RecyclerView.Adapter<MyViewHolder>
{
    MyListAdapter(MultiBrowserActivity act, ArrayList<DirectoryItem> itemList)
    {
        this.act = act;
        this.itemList = itemList;

        for (int i = 0; i < itemList.size(); i++) mIdMap.put(itemList.get(i), i);

        setHasStableIds(true);
    }

    private HashMap<DirectoryItem, Integer> mIdMap = new HashMap<>();
    private MultiBrowserActivity act;
    private ArrayList<DirectoryItem> itemList;

    @Override
    public long getItemId(int position)
    {
        DirectoryItem item = itemList.get(position);
        return mIdMap.get(item);
    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view;

        if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.List)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_item_list_view, viewGroup, false);
        }
        else
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_item_tiles_view, viewGroup, false);

            if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery && !act.OPT().mShowFileNamesInGalleryView)
                view.findViewById(R.id.listItemText).setVisibility(View.GONE);
            else
                view.findViewById(R.id.listItemText).setVisibility(View.VISIBLE);
        }

        return new MyViewHolder(view, act);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i)
    {
        LinearLayout listItem = viewHolder.listItem;

        DirectoryItem item = itemList.get(i);

        String path = item.getPath();
        String name = item.getName();

        viewHolder.title.setText(name);

        ImageView image = viewHolder.image;

        boolean exists;
        {
            try { exists = new File(path).exists(); }
            catch (Exception ex) { exists = false; }
        }

        if (!exists)
        {
            String infoType;
            int iconId;

            if (item instanceof FileItem)
            {
                infoType = "file";
                iconId = R.mipmap.ic_file_x;
            }
            else // if (item instanceof MultiBrowserDirectoryItem.FolderItem)
            {
                infoType = "folder";
                iconId = R.mipmap.ic_folder_x;
            }

            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setImageBitmap(BitmapFactory.decodeResource(act.getResources(), iconId));

            if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.List)
            {
                String str = infoType + " not found";
                viewHolder.info.setText(str);
            }

            listItem.setClickable(false);
            listItem.setLongClickable(false);
            listItem.setOnClickListener(null);
            listItem.setOnLongClickListener(null);

            return;
        }

        boolean galleryView = act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery;

        boolean isFile = item instanceof FileItem;

        if (isFile)
        {
            Bitmap thumb = null;

            if ((galleryView && act.OPT().mShowImagesWhileBrowsingGallery) ||
                (!galleryView && act.OPT().mShowImagesWhileBrowsingNormal))
            {
                FileItem fileItem = (FileItem)item;

                Integer imageId = fileItem.getImageId();

                if (imageId != null)
                {
                    try
                    {
                        thumb = MediaStore.Images.Thumbnails.getThumbnail(
                            act.getContentResolver(), imageId,
                            MediaStore.Images.Thumbnails.MINI_KIND, null);
                    }
                    catch (Exception ex) { thumb = null; }

                    if (thumb != null)
                    {
                        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        image.setImageBitmap(thumb);
                    }
                }
            }

            if (thumb == null)
            {
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image.setImageBitmap(BitmapFactory.decodeResource(act.getResources(), R.mipmap.ic_file));
            }
        }
        else // item instanceof MultiBrowserDirectoryItem.FolderItem
        {
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setImageBitmap(BitmapFactory.decodeResource(act.getResources(), R.mipmap.ic_folder));
        }

        boolean hidden;
        {
            try { hidden = new File(path).isHidden(); }
            catch (Exception ex) { hidden = false; }
        }
        if (hidden) image.setImageAlpha(127);
        else image.setImageAlpha(255);

        if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.List)
            viewHolder.info.setText(item.getInfo());

        boolean loadFilesFolders = act.OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.LoadFilesAndOrFolders;
        boolean saveFilesFolders = act.OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.SaveFilesAndOrFolders;
        boolean loadFolders = act.OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.LoadFolders;
        boolean saveFolders = act.OPT().mBrowseMode == MultiBrowserOptions.BrowseMode.SaveFolders;

        boolean load =
            (isFile && loadFilesFolders) ||
            (!isFile && (loadFolders || loadFilesFolders));
        boolean save =
            (isFile && saveFilesFolders) ||
            (!isFile && (saveFolders || saveFilesFolders));

        boolean saveBoxVisible =
            act.findViewById(R.id.saveFileLayout).getVisibility() != View.GONE;

        boolean sendToSaveBoxShortClick =
            save && isFile && saveBoxVisible && act.ADV().mAllowShortClickFileForSave &&
            (act.ADV().mDebugMode || act.OPT().mOnSelectFileForSave != null) &&
            act.ADV().mShortClickSaveFileBehavior != MultiBrowserOptions.SaveFileBehavior.SaveFile;
        boolean sendToSaveBoxLongClick =
            save && isFile && saveBoxVisible && act.ADV().mAllowLongClickFileForSave &&
            (act.ADV().mDebugMode || act.OPT().mOnSelectFileForSave != null) &&
            act.ADV().mLongClickSaveFileBehavior != MultiBrowserOptions.SaveFileBehavior.SaveFile;

        boolean shortClickable =
            act.ADV().mDebugMode || !isFile || sendToSaveBoxShortClick ||
            (load && act.ADV().mAllowShortClickFileForLoad && act.OPT().mOnSelectFileForLoad != null) ||
            (save && act.ADV().mAllowShortClickFileForSave && act.OPT().mOnSelectFileForSave != null);
        boolean longClickable =
            act.ADV().mDebugMode || sendToSaveBoxLongClick ||
            (isFile &&
                ((load && act.ADV().mAllowLongClickFileForLoad && act.OPT().mOnSelectFileForLoad != null) ||
                (save && act.ADV().mAllowLongClickFileForSave && act.OPT().mOnSelectFileForSave != null))) ||
            (!isFile &&
                ((load && act.ADV().mAllowLongClickFolderForLoad && act.OPT().mOnSelectFolderForLoad != null) ||
                (save && act.ADV().mAllowLongClickFolderForSave && act.OPT().mOnSelectFolderForSave != null)));

        listItem.setClickable(shortClickable);
        if (!shortClickable) listItem.setOnClickListener(null);
        else
        {
            listItem.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!isFile)
                    {
                        act.refreshView(path, false, false);
                        return;
                    }

                    // item istanceof FileItem

                    if (load)
                    {
                        act.onSelect(true, true, false, false, path);
                    }
                    else
                    {
                        boolean saveFile =
                            !saveBoxVisible || act.ADV().mShortClickSaveFileBehavior != MultiBrowserOptions.SaveFileBehavior.SendNameToSaveBoxOrSaveFile;

                        if (sendToSaveBoxShortClick)
                            act.setEditTextSaveFileName(UtilsKt.getShortName(path));
                        if (saveFile) act.onSelect(true, false, false, false, path);
                    }
                }
            });
        }

        listItem.setLongClickable(longClickable);
        if (!longClickable) listItem.setOnLongClickListener(null);
        else
        {
            listItem.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if (!isFile)
                    {
                        if (load) act.onSelect(false, true, true, false, path);
                        else act.onSelect(false, false, true, false, path);

                        return true;
                    }

                    // item istanceof FileItem

                    if (load)
                    {
                        act.onSelect(true, true, true, false, path);
                    }
                    else
                    {
                        boolean saveFile =
                            !saveBoxVisible || act.ADV().mLongClickSaveFileBehavior != MultiBrowserOptions.SaveFileBehavior.SendNameToSaveBoxOrSaveFile;

                        if (sendToSaveBoxLongClick)
                            act.setEditTextSaveFileName(UtilsKt.getShortName(path));
                        if (saveFile) act.onSelect(true, false, true, false, path);

                    }

                    return true;
                }
            });
        }
    }
}

class MyViewHolder extends RecyclerView.ViewHolder
{
    LinearLayout listItem;
    TextView title;
    ImageView image;
    TextView info;

    MyViewHolder(View view, MultiBrowserActivity act)
    {
        super(view);

        listItem = (LinearLayout)view;
        listItem.setBackgroundColor(act.THM().mColorListBackground);
        title = view.findViewById(R.id.listItemText);
        title.setTypeface(act.THM().getFontBdIt(act.getAssets()));
        image = view.findViewById(R.id.listItemIcon);
        if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.Gallery)
        {
            title.setTextColor(act.THM().mColorGalleryItemText);
            title.setTextSize(act.THM().mUnitSp, act.THM().mSizeGalleryViewItemText);
        }
        else
        {
            title.setTextColor(act.THM().mColorListItemText);
            if (act.OPT().mBrowserViewType == MultiBrowserOptions.BrowserViewType.List)
            {
                title.setTextSize(act.THM().mUnitSp, act.THM().mSizeListViewItemText);
                info = view.findViewById(R.id.listItemSubText);
                info.setTypeface(act.THM().getFontNorm(act.getAssets()));
                info.setTextColor(act.THM().mColorListItemSubText);
                info.setTextSize(act.THM().mUnitSp, act.THM().mSizeListViewItemSubText);
                View accent = view.findViewById(R.id.listItemAccent);
                accent.setBackgroundColor(act.THM().mColorListAccent);
            }
            else
            {
                title.setTextSize(act.THM().mUnitSp, act.THM().mSizeTilesViewItemText);
            }
        }
    }
}