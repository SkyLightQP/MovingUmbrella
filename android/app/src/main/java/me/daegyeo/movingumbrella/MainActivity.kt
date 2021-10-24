package me.daegyeo.movingumbrella

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import net.daum.mf.map.api.MapView


class MainActivity : AppCompatActivity() {
    private val PERMISSION_LIST = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH
    )
    private val permission = Permission(this, PERMISSION_LIST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerToolbar()
        registerKakaoMap()

        permission.checkPermission()
    }

    private fun registerToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.new_toolbar)
        setSupportActionBar(toolbar)
    }

    private fun registerKakaoMap() {
        val mapView = MapView(this)
        val mapViewContainer = findViewById<View>(R.id.map) as ViewGroup
        mapViewContainer.addView(mapView)
    }
}