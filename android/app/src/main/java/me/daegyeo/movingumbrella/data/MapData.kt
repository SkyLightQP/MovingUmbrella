package me.daegyeo.movingumbrella.data

import com.naver.maps.map.NaverMap

data class MapData(var naverMap: NaverMap?, var lastLocation: Pair<Double, Double>)
