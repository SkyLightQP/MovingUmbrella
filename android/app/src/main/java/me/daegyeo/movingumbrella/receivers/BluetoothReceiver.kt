package me.daegyeo.movingumbrella.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import me.daegyeo.movingumbrella.MainActivity


class BluetoothReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val device: BluetoothDevice? = intent!!.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        val name = device!!.name
        Toast.makeText(context, "${device!!.name} 연결이 끊겼습니다.", Toast.LENGTH_LONG).show()
        if (name == "Umbrella") {
            Toast.makeText(context, "${MainActivity.lastLocation.first}", Toast.LENGTH_LONG).show()

            Marker().apply {
                position = LatLng(MainActivity.lastLocation.first, MainActivity.lastLocation.second)
                map = MainActivity.mNaverMap
            }
        }
    }
}
