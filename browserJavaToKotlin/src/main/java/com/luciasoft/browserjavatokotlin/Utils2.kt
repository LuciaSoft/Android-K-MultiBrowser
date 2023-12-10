package com.luciasoft.browserjavatokotlin

import java.io.File

internal object Utils2
{
    fun directoryIsReadable(act: MultiBrowserActivity, directory: String?): Boolean
    {
        val dir: File
        dir = try
        {
            File(directory)
        }
        catch (ex: Exception)
        {
            return false
        }
        val exists: Boolean
        exists = try
        {
            File(directory).exists()
        }
        catch (ex: Exception)
        {
            return false
        }
        if (!exists) return false
        if (!act.OPT.allowAccessToRestrictedFolders)
        {
            val canRead: Boolean
            canRead = try
            {
                dir.canRead()
            }
            catch (ex: Exception)
            {
                return false
            }
            return canRead
        }
        return true
    }
}