package me.daegyeo.movingumbrella

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import me.daegyeo.movingumbrella.runtimePermission.Permission
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val ACCESS_LOCATION_CODE = 1000
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mNaverMap: NaverMap
    private lateinit var mMapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerToolbar()
        mMapView = findViewById(R.id.map)
        locationSource = FusedLocationSource(this, ACCESS_LOCATION_CODE)
        mMapView.getMapAsync(this)

        val fineLocation =
            Permission(this, Manifest.permission.ACCESS_FINE_LOCATION).checkPermission()
        val coarseLocation =
            Permission(this, Manifest.permission.ACCESS_COARSE_LOCATION).checkPermission()
        if (fineLocation && coarseLocation) {
            tracking()
        } else {
            Toast.makeText(this, "위치 추적을 위해 위치 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
            Permission.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                ACCESS_LOCATION_CODE
            )
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        mNaverMap = naverMap
        naverMap.locationSource = locationSource
    }

    private fun registerToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.new_toolbar)
        setSupportActionBar(toolbar)
    }

    private fun tracking() {
        mNaverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_LOCATION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tracking()
                }
            }
        }
    }
}
