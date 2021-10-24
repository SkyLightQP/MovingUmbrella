package me.daegyeo.movingumbrella

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import net.daum.mf.map.api.MapView


class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerToolbar()
        mapView = findViewById(R.id.map)
    }

    private fun registerToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.new_toolbar)
        setSupportActionBar(toolbar)
    }
}