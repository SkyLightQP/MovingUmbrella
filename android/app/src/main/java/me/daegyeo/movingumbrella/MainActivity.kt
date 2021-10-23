package me.daegyeo.movingumbrella

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import net.daum.mf.map.api.MapView


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_READ_LOCATION = 0x00000001
    private val PERMISSIONS = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_READ_LOCATION);
        registerToolbar()
        registerKakaoMap()
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