package com.luciasoft.browserjavatokotlin.multibrowser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.format.Time;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class Utils
{
    static void copyFileFromAssets(AssetManager assets, String inputFilePath, String outputFilePath) throws IOException
    {
        InputStream is = assets.open(inputFilePath);
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        byte[] buffer = new byte[10240];
        while (true)
        {
            int count = is.read(buffer, 0, 10240);
            if (count == -1) break;
            fos.write(buffer, 0, count);
        }
        is.close();
        fos.flush();
        fos.close();
    }

    static String[] getValidExts(String[] exts)
    {
        if (exts == null) return new String[] { "*" };

        ArrayList<String> list = new ArrayList<>();

        for (String ext : exts)
        {
            if (ext == null) continue;
            ext = ext.trim().toLowerCase();
            if (ext.isEmpty()) continue;
            if (ext.equals("*")) return new String[] { "*" };
            if (!ext.startsWith(".")) ext = "." + ext;
            list.add(ext);
        }

        if (list.size() == 0) return new String[] { "*" };

        return list.toArray(new String[0]);
    }

    static String[] getValidExts(String exts)
    {
        if (exts == null) return new String[] { "*" };

        String[] extArray = exts.split(",");

        return getValidExts(extArray);
    }

    static String getDateString(long dateMs)
    {
        Time t = new Time();
        t.set(dateMs);
        return t.format("%m/%d/%Y");
    }

    static String getFileExtensionLowerCaseWithDot(String fileNameOrPath)
    {
        int pos1 = fileNameOrPath.lastIndexOf('.');
        if (pos1 == -1) return "";
        int pos2 = fileNameOrPath.lastIndexOf('/', pos1);
        if (pos2 > pos1) return "";

        return fileNameOrPath.substring(pos1).toLowerCase();
    }

    static String getParentDir(String path)
    {
        if (path.equals("/")) return "";

        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

        int pos = path.lastIndexOf('/');

        if (pos == -1) return "";

        if (pos == 0) return "/";

        return path.substring(0, pos);
    }

    static String getShortName(String path)
    {
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

        int pos = path.lastIndexOf('/');

        if (pos == -1) return path;

        return path.substring(pos + 1);
    }

    static String trimStart(String str, String trim)
    {
        while(str.startsWith(trim))
        {
            str = str.substring(trim.length());
        }

        return str;
    }

    static String trimEnd(String str, String trim)
    {
        while(str.endsWith(trim))
        {
            str = str.substring(0, str.length() - trim.length());
        }

        return str;
    }

    static void toastShort(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static void toastLong(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static <T> boolean arrayContains(T[] array, T item)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (array[i].equals(item)) return true;
        }

        return false;
    }

    static int charCount(String str, char ch)
    {
        int count = 0;

        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == ch) count++;
        }

        return count;
    }

    @SuppressLint("DefaultLocale")
    static String getFileSizeString(long fileSize)
    {
        float kb = fileSize / 1024f;
        float mb = kb >= 1024 ? kb / 1024 : 0;
        float gb = mb >= 1024 ? mb / 1024 : 0;

        if (gb >= 1) return String.format("%.2f", gb) + " Gb";
        if (mb >= 1) return String.format("%.2f", mb) + " Mb";
        if (kb >= 1) return String.format("%.2f", kb) + " Kb";

        if (fileSize == 1) return "1 byte";
        return "" + fileSize + " bytes";
    }

    static boolean directoryIsReadable(MultiBrowserActivity act, String directory)
    {
        File dir;
        try { dir = new File(directory); }
        catch (Exception ex) { return false; }

        boolean exists;
        try { exists = new File(directory).exists(); }
        catch (Exception ex) { return false; }
        if (!exists) return false;

        if (!act.OPT().mAllowAccessToRestrictedFolders)
        {
            boolean canRead;
            try { canRead = dir.canRead(); }
            catch (Exception ex) { return false; }

            return canRead;
        }

        return true;
    }

    static boolean filePassesFilter(String[] exts, String fileNameOrPath)
    {
        if (exts == null || exts.length == 0) return true;

        exts = getValidExts(exts);

        String fileExt = getFileExtensionLowerCaseWithDot(fileNameOrPath);

        for (String ext : exts)
        {
            if (ext.equals("*") || ext.equals(fileExt)) return true;
        }

        return false;
    }

}
