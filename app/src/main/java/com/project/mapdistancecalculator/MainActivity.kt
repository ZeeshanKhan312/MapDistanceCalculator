package com.project.mapdistancecalculator

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map:GoogleMap
    private val PERMISSION_CODE:Int=1
    lateinit var currLocation :Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var search:SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search=findViewById(R.id.search_bar)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment //as keyword to typecast
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                map.clear()
                var location:String=search.query.toString()
                lateinit var addressList:List<Address>
                if(location!=null){
                    var geocoder=Geocoder(this@MainActivity)
                    try{
                       addressList= geocoder.getFromLocationName(location,10) as List<Address>
                    }catch (e:IOException){
                        e.printStackTrace()
                    }
                    lateinit var latLang:LatLng
                    val distance= FloatArray(10)
                    for(i in addressList.indices){
                        latLang=LatLng(addressList[i].latitude,addressList[i].longitude)
                        Location.distanceBetween(currLocation.latitude,currLocation.longitude,latLang.latitude,latLang.longitude,distance)
                        map.addMarker(MarkerOptions().position(latLang).title(location).snippet("Distance = ${distance[i]/1000}km"))
                    }
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang,10f))

                    Toast.makeText(this@MainActivity, "The distance between them ${distance[0]/1000}km ", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        }
        )
    }

    private fun getLastLocation(){
        //to check whether location access is provided or not
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_CODE)
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location->
            if(location!=null){
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true //for blue dots current location
                currLocation = location
                var you=LatLng(currLocation.latitude,currLocation.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(you,12f))  //for zooming into location
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        map.uiSettings.isZoomControlsEnabled=true
        var office=LatLng( 28.6082231,77.4345915)
        map.addMarker(MarkerOptions().position(office).title("Office")) //for adding a marker on the map
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(office,10f))
    }

    //to check whether the permission is granted after PERMISSION BOX
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==PERMISSION_CODE){
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
            else
                Toast.makeText(this, "Location Access is required!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id:Int=item.itemId
        if(id==R.id.normalMap){
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        else if(id==R.id.satelliteMap){
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
        else if(id==R.id.terrainMap){
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
        else if(id==R.id.hybridMap){
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
        return super.onOptionsItemSelected(item)
    }
}