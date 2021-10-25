package me.daegyeo.movingumbrella.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import me.daegyeo.movingumbrella.Constants
import me.daegyeo.movingumbrella.MainActivity


class BluetoothReceiver : BroadcastReceiver() {
    val mapData = MainActivity.mapData
    val DEVICE_NAME = "HC-06"

    override fun onReceive(context: Context?, intent: Intent?) {
        val device: BluetoothDevice? = intent!!.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        val name = device!!.name
        val shardPreferences = context!!.getSharedPreferences(
            Constants.PACKAGE,
            AppCompatActivity.MODE_PRIVATE
        )

        if (name == DEVICE_NAME) {
            // TODO: 모든 마커를 지운다.
                
            Marker().apply {
                position = LatLng(
                    mapData.lastLocation.first,
                    mapData.lastLocation.second
                )
                captionText = "최근 우산 위치"
                icon = MarkerIcons.YELLOW
                map = mapData.naverMap
            }

            shardPreferences.edit {
                putString(
                    Constants.LOCATION_KEY,
                    "${mapData.lastLocation.first}:${mapData.lastLocation.second}"
                )
            }
        }
    }
}
