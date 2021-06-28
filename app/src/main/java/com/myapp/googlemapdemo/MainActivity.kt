package com.myapp.googlemapdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity() {

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var client: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(this)

        putMarker()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(this,Array(1) {Manifest.permission.ACCESS_FINE_LOCATION},44)
        }
    }

    private fun putMarker() {
        supportMapFragment.getMapAsync{
           it.setOnMapLongClickListener{s->
               it.addMarker(MarkerOptions().position(s))
           }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val task = client.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                val location = it
                supportMapFragment.getMapAsync { s ->
                    val latLng = LatLng(location.latitude,location.longitude)
                    val options = MarkerOptions().position(latLng).title("I am there")
                    s.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f))
                    s.addMarker(options)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 44) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                getCurrentLocation()
            }
        }
    }
}