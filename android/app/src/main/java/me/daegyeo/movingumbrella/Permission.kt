package me.daegyeo.movingumbrella

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class Permission(val activity: Activity, val permissions: Array<String>) {
    fun checkPermission() {
        val denyPermissions = permissions.filter {
            ActivityCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (denyPermissions.isNotEmpty()) {
            Toast.makeText(activity, "인터넷, 위치, 블루투스 권한을 설정해주세요.", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(activity, permissions, 0)
        }
    }
}