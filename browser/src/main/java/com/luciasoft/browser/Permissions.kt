package com.luciasoft.browser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lucerta.multibrowser.MultiBrowserActivity

object Permissions
{
    const val PERMISSION_EXTERNAL_STORAGE = 1

    fun checkExternalStoragePermission(activity: AppCompatActivity): Boolean
    {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            //Android is 11 (R) or above
            Environment.isExternalStorageManager()
        }
        else
        {
            //Below android 11
            val write = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestExternalStoragePermission(activity: AppCompatActivity)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        {
            requestManageExternalStoragePermissionLegacy(activity)
        }
        else
        {
            requestManageExternalStoragePermissionNew(activity)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun requestManageExternalStoragePermissionNew(activity: AppCompatActivity)
    {
        try
        {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            getStorageActivityResultLauncher(activity).launch(intent)
        }
        catch (e: Exception)
        {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            getStorageActivityResultLauncher(activity).launch(intent)
        }
    }

    private fun requestManageExternalStoragePermissionLegacy(activity: AppCompatActivity)
    {
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            //else
            run {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_EXTERNAL_STORAGE
                )
            }
        }
        else
        {
            // Permission has already been granted
            try
            {
                carryOutAction(activity, PERMISSION_EXTERNAL_STORAGE)
            }
            catch (ex: Exception)
            {
            }
        }
    }

    private fun carryOutAction(activity: AppCompatActivity, permission: Int): Boolean
    {
        when (permission)
        {
            PERMISSION_EXTERNAL_STORAGE ->
            {
                val mba = activity as MultiBrowserActivity
                mba.resetDir()
                Toast.makeText(activity, "External Storage Access Granted", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
        }
        return true
    }

    private var storageActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    private fun getStorageActivityResultLauncher(activity: AppCompatActivity): ActivityResultLauncher<Intent>
    {
        if (storageActivityResultLauncher == null) storageActivityResultLauncher = activity.registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?>
            {
                override fun onActivityResult(result: ActivityResult?)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    {
                        //Android is 11 (R) or above
                        if (Environment.isExternalStorageManager())
                        {
                            //Manage External Storage Permissions Granted
                        }
                        else
                        {
                            Toast.makeText(
                                activity,
                                "Storage Permissions Denied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else
                    {
                        //Below android 11
                    }
                }
            })

        return storageActivityResultLauncher!!
    }
}