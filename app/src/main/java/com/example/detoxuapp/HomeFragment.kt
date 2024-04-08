package com.example.detoxuapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result


class HomeFragment : Fragment() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var locationManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize location manager
        locationManager = requireContext().getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager

        // Check if the location permissions are granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions
            requestLocationPermissions()
        } else {
            // Permissions already granted, get last known location
            getLastKnownLocation()
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permissions granted, get last known location
                getLastKnownLocation()
            } else {
                // Location permissions denied, handle it gracefully
                // You may show a message to the user explaining why you need location access
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLastKnownLocation() {
        try {
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {
                // Last known location is available
                val latitude = lastLocation.latitude
                val longitude = lastLocation.longitude
                // Do something with latitude and longitude

                makeApiCallToFetchCoordinates(latitude,longitude)
                Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
            } else {
                // Last known location is not available, request location updates
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }, null)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun makeApiCallToFetchCoordinates(latitude: Double, longitude: Double) {
        val url = " https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}"
        val params = listOf("appid" to "0b82f769c646dcb30d40560e5ca0152e","lat" to latitude.toString(),"lon" to longitude.toString())
        Fuel.get(url, params)
            .header("Authorization", "API Key 0b82f769c646dcb30d40560e5ca0152e")
            .response { result ->
                when (result) {
                    is Result.Success -> {
                        val responseRes = result.get()
                        activity?.runOnUiThread {
                            Toast.makeText(activity, "Request Sent", Toast.LENGTH_LONG).show()
                        }
                        // Handle successful response here
                    }

                    is Result.Failure -> {
                        val error = result.getException()
                        activity?.runOnUiThread {
                            Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                        }
                        // Handle error
                    }
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
