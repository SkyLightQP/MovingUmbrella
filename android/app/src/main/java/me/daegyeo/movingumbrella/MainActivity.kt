package me.daegyeo.movingumbrella

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
    private val TAG = "MovingUmbrella"
    private val ACCESS_LOCATION_CODE = 1000
    private val BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

    private lateinit var locationSource: FusedLocationSource
    private lateinit var mMapView: MapView
    private lateinit var bluetoothAdapter: BluetoothAdapter

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

        mMapView = findViewById(R.id.map)
        locationSource = FusedLocationSource(this, ACCESS_LOCATION_CODE)
        mMapView.getMapAsync(this)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!fineLocation.isGrant() || !coarseLocation.isGrant()) {
            Toast.makeText(this, "위치 추적을 위해 우산 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
            Permission.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                ACCESS_LOCATION_CODE
            )
        }

        findViewById<ImageButton>(R.id.bluetooth).setOnClickListener {
            if (!bluetoothAdapter.isEnabled) return@setOnClickListener

            val pairedDevices = bluetoothAdapter.bondedDevices
            if (pairedDevices.size > 0) {
                AlertDialog.Builder(this).apply {
                    setTitle("우산 연결하기")
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
            val socket = device[0].createRfcommSocketToServiceRecord(BLUETOOTH_UUID)
            socket.connect()
        } catch (ex: IOException) {
            Log.e(TAG, ex.stackTraceToString())
            Toast.makeText(this, "${deviceName}에 연결을 할 수 없습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tracking() {
        mapData.naverMap?.locationTrackingMode = LocationTrackingMode.Follow
    }

    override fun onMapReady(naverMap: NaverMap) {
        mapData.naverMap = naverMap
        naverMap.locationSource = locationSource
        if (fineLocation.isGrant() && coarseLocation.isGrant()) tracking()

        naverMap.addOnLocationChangeListener {
            mapData.lastLocation = Pair(it.latitude, it.longitude)
        }
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
