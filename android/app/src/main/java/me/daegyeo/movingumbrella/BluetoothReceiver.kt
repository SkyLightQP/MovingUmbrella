package me.daegyeo.movingumbrella

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class BluetoothReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val device: BluetoothDevice? = intent!!.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        Toast.makeText(context, "${device!!.name} 연결이 끊겼습니다.", Toast.LENGTH_LONG).show()
    }
}