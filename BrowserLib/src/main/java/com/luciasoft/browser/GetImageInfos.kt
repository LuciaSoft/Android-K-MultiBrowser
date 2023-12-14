package com.luciasoft.browser

import android.database.Cursor
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.luciasoft.utils.randomize
import java.io.File

// OPTIMIZED
internal object GetImageInfos
{
    fun getImageInfos(act: MultiBrowserActivity): ArrayList<DirectoryItem>? // OPTIMIZED
    {
        val sortOrderString: String = when (act.sortOrder)
        {
            SortOrder.PathAscending -> MediaStore.Images.Media.DATA
            SortOrder.PathDescending -> MediaStore.Images.Media.DATA + " DESC"
            SortOrder.DateAscending -> MediaStore.Images.Media.DATE_MODIFIED
            SortOrder.DateDescending -> MediaStore.Images.Media.DATE_MODIFIED + " DESC"
            SortOrder.SizeAscending -> MediaStore.Images.Media.SIZE
            SortOrder.SizeDescending -> MediaStore.Images.Media.SIZE + " DESC"
            else -> MediaStore.Images.Media.DATA
        }

        //Cursor cursor = context.managedQuery( // DEPRICATED
        //MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cols, null,
        //null, sortOrder); // GET DATA IN CURSOR IN DESC ORDER
        val cursor: Cursor =
            try
            {
                with(CursorLoader(act))
                {
                    val cols = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
                    this.uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    this.projection = cols
                    this.selection = null
                    this.sortOrder = sortOrderString
                    this.loadInBackground()
                }
            }
            catch (ex: Exception) { null } ?: return null

        val list = ArrayList<DirectoryItem>()
        val exts = Utils.getValidExts(act.ADV.mediaStoreImageExts)

        act.DAT.mediaStoreImageInfoMap.clear()

        while (cursor.moveToNext())
        {
            var imagePath: String
            val imageFile = try
            {
                val imagePathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                imagePath = cursor.getString(imagePathCol)
                File(imagePath)
            }
            catch (ex: Exception) { continue }

            val ipath = try { imageFile.canonicalPath }
            catch (ex2: Exception) { try { imageFile.absolutePath } catch (ex3: Exception) { null } }
            if (ipath != null) imagePath = ipath

            if (!Utils.filePassesFilter(exts, imagePath)) continue

            try
            {
                val imageIdCol = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val imageId = cursor.getInt(imageIdCol)
                list.add(FileItem(imagePath, null, null, null, imageId))

                act.DAT.mediaStoreImageInfoMap[imagePath] = imageId
            }
            catch (_: Exception) { }
        }

        return list
    }
}