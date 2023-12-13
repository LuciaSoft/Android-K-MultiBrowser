package com.luciasoft.browser

import java.io.File

internal object Utils2
{
    fun directoryIsReadable(act: MultiBrowserActivity, directory: String?): Boolean
    {
        val dir = try
        {
            File(directory)
        }
        catch (ex: Exception)
        {
            return false
        }
        val exists = try
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
            val canRead = try
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