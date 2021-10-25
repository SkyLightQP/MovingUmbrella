package me.daegyeo.movingumbrella.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import me.daegyeo.movingumbrella.MainActivity


class BluetoothReceiver : BroadcastReceiver() {
    val mapData = MainActivity.mapData

    override fun onReceive(context: Context?, intent: Intent?) {
        val device: BluetoothDevice? = intent!!.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        val name = device!!.name

        if (name == "Umbrella") {
            Marker().apply {
                position = LatLng(
                    mapData.lastLocation.first,
                    mapData.lastLocation.second
                )
                map = mapData.naverMap
            }
        }
    }
}
