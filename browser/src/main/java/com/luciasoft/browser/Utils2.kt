package com.luciasoft.browser

import com.luciasoft.browser.MultiBrowserActivity
import java.io.File

fun directoryIsReadable(act: MultiBrowserActivity, directory: String): Boolean
{
    var dir: File? = null;
    try { dir = File(directory); }
    catch (ex: Exception) { return false; }

    var exists = false;
    try { exists = File(directory).exists(); }
    catch (ex: Exception) { return false; }
    if (!exists) return false;

    if (!act.OPT.allowAccessToRestrictedFolders)
    {
        var canRead = false;
        try { canRead = dir.canRead(); }
        catch (ex: Exception) { return false; }

        return canRead;
    }

    return true;
}