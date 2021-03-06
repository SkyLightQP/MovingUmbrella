package me.daegyeo.movingumbrella

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import me.daegyeo.movingumbrella.data.MapData
import me.daegyeo.movingumbrella.runtimePermission.Permission
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mMapView: MapView
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var shardPreferences: SharedPreferences

    companion object {
        var mapData: MapData = MapData(null, Pair(0.0, 0.0))
    }

    private val fineLocation = Permission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    private val coarseLocation = Permission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.new_toolbar)
        setSupportActionBar(toolbar)

        shardPreferences = getSharedPreferences(Constants.PACKAGE, MODE_PRIVATE)
        mMapView = findViewById(R.id.map)
        locationSource = FusedLocationSource(this, Constants.ACCESS_LOCATION_CODE)
        mMapView.getMapAsync(this)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!fineLocation.isGrant() || !coarseLocation.isGrant()) {
            Toast.makeText(this, "?????? ????????? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_LONG).show()
            Permission.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constants.ACCESS_LOCATION_CODE
            )
        }

        findViewById<ImageButton>(R.id.bluetooth).setOnClickListener {
            if (!bluetoothAdapter.isEnabled) return@setOnClickListener

            val pairedDevices = bluetoothAdapter.bondedDevices
            if (pairedDevices.size > 0) {
                AlertDialog.Builder(this).apply {
                    setTitle("?????? ????????????")
                    val deviceNames = pairedDevices.map {
                        it.name
                    }
                    setItems(deviceNames.toTypedArray()) { _, which ->
                        connectBluetoothDevice(deviceNames[which])
                    }
                    create().show()
                }
            }
        }
    }

    private fun connectBluetoothDevice(deviceName: String) {
        try {
            val device = bluetoothAdapter.bondedDevices.filter { it.name == deviceName }
            val socket = device[0].createRfcommSocketToServiceRecord(Constants.BLUETOOTH_UUID)
            socket.connect()
        } catch (ex: IOException) {
            Log.e(Constants.TAG, ex.stackTraceToString())
            Toast.makeText(this, "${deviceName}??? ????????? ??? ??? ????????????.\n?????? ??????????????????.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startTracking() {
        mapData.naverMap?.locationTrackingMode = LocationTrackingMode.Follow
    }

    override fun onMapReady(naverMap: NaverMap) {
        mapData.naverMap = naverMap
        naverMap.locationSource = locationSource
        if (fineLocation.isGrant() && coarseLocation.isGrant()) startTracking()

        naverMap.addOnLocationChangeListener {
            val marker = MarkerManager(naverMap)
            mapData.lastLocation = Pair(it.latitude, it.longitude)

            val saveLocation = shardPreferences.getString(Constants.LOCATION_KEY, null)
            if (saveLocation != null) {
                val token = saveLocation.split(':')
                val lat = token[0].toDouble()
                val lon = token[1].toDouble()
                marker.add(LatLng(lat, lon))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.ACCESS_LOCATION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTracking()
                }
            }
        }
    }
}
