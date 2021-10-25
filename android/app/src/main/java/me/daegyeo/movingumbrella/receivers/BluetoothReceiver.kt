package me.daegyeo.movingumbrella.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.naver.maps.geometry.LatLng
import me.daegyeo.movingumbrella.Constants
import me.daegyeo.movingumbrella.MainActivity
import me.daegyeo.movingumbrella.MarkerManager


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
        val marker = MarkerManager(mapData.naverMap!!)

        if (name == DEVICE_NAME) {
            marker.clear()

            marker.add(
                LatLng(
                    mapData.lastLocation.first,
                    mapData.lastLocation.second
                )
            )

            shardPreferences.edit {
                putString(
                    Constants.LOCATION_KEY,
                    "${mapData.lastLocation.first}:${mapData.lastLocation.second}"
                )
            }
        }
    }
}
