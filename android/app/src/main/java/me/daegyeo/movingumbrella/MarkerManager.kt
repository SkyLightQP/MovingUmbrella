package me.daegyeo.movingumbrella

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class MarkerManager(val naverMap: NaverMap) {
    companion object {
        val markerList = mutableListOf<Marker>()
    }

    fun add(markerPosition: LatLng) {
        val marker = Marker().apply {
            position = markerPosition
            captionText = "최근 우산 위치"
            icon = MarkerIcons.YELLOW
            map = naverMap
        }

        markerList.add(marker)
    }

    fun clear() {
        markerList.forEach {
            it.map = null
        }
        markerList.clear()
    }
}