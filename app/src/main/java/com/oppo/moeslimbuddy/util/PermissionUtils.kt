package com.oppo.moeslimbuddy.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object PermissionUtils {
    fun checkPermission(
        fragmentActivity: FragmentActivity, permission: String,
        resultLauncher: ActivityResultLauncher<String>,
        prepareAskPermissionHandler: (() -> Unit)?, grantCallback: () -> Unit
    ) {
        if (userRunTimePermission()) {
            when {
                ContextCompat.checkSelfPermission(
                    fragmentActivity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    grantCallback()
                }

                fragmentActivity.shouldShowRequestPermissionRationale(
                    permission
                ) -> {
                    if (prepareAskPermissionHandler == null) {
                        askPermission(resultLauncher, permission)
                    } else {
                        prepareAskPermissionHandler()
                    }
                }

                else -> {
                    if (prepareAskPermissionHandler == null) {
                        askPermission(resultLauncher, permission)
                    } else {
                        prepareAskPermissionHandler()
                    }
                }
            }
        } else {
            grantCallback()
        }
    }

    fun checkPermissions(
        fragmentActivity: FragmentActivity, permissions: Array<String>,
        resultLauncher: ActivityResultLauncher<Array<String>>,
        prepareAskPermissionHandler: (() -> Unit)?, grantCallback: () -> Unit
    ) {
        if (userRunTimePermission()) {
            var isAllowed = true

            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        fragmentActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    isAllowed = false
                    break
                }
            }

            if (isAllowed) grantCallback() else {
                if (prepareAskPermissionHandler == null) {
                    askPermissions(resultLauncher, permissions)
                } else {
                    prepareAskPermissionHandler()
                }
            }

        } else {
            grantCallback()
        }
    }

    private fun isPermissionAllowed(context: Context, permission: String): Boolean {
        if (userRunTimePermission()) {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    fun isPermissionLocationAllowed(context: Context) = isPermissionAllowed(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private fun askPermission(resultLauncher: ActivityResultLauncher<String>, permission: String) {
        resultLauncher.launch(permission)
    }

    private fun askPermissions(
        resultLauncher: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>
    ) {
        resultLauncher.launch(permissions)
    }

    fun askLocationPermission(resultLauncher: ActivityResultLauncher<String>) {
        resultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun askStoragePermission(resultLauncher: ActivityResultLauncher<String>) {
        resultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


    fun userRunTimePermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun Activity.goToAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}