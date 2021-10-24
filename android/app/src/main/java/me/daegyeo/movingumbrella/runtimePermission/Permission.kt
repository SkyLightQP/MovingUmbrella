package me.daegyeo.movingumbrella.runtimePermission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permission(val activity: Activity, val permission: String, val REQUEST_RESULT_CODE: Int) {
    fun checkPermission(): Boolean {
        val isGrant = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        if (!isGrant) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_RESULT_CODE)
        }
        return isGrant
    }
}