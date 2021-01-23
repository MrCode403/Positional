package app.simple.positional.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import app.simple.positional.util.NullSafety.isNull

class LocationService : Service(), LocationListener {
    private var locationManager: LocationManager? = null
    private var handler = Handler(Looper.getMainLooper())
    private var delay: Long = 1000

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationManager = baseContext.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        requestLastKnownLocation()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        locationManager?.removeUpdates(this)
        handler.removeCallbacks(locationUpdater)
        super.onDestroy()
    }

    override fun onLocationChanged(location: Location) {
        Intent().also { intent ->
            intent.action = "location"
            intent.putExtra("location", location)
            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)
        }
    }

    // only for API < 29
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Intent().also { intent ->
            intent.action = "provider"
            intent.putExtra("location_provider", provider)
            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)
        }
    }

    override fun onProviderEnabled(provider: String) {
        requestLastKnownLocation()
        Intent().also { intent ->
            intent.action = "provider"
            intent.putExtra("location_provider", provider)
            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)
        }
    }

    override fun onProviderDisabled(provider: String) {
        handler.removeCallbacks(locationUpdater)
        Intent().also { intent ->
            intent.action = "provider"
            intent.putExtra("location_provider", provider)
            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)
        }
    }

    private val locationUpdater: Runnable = object : Runnable {
        override fun run() {
            requestLocation()
            handler.postDelayed(this, delay)
        }
    }

    private fun requestLastKnownLocation() {
        if (checkPermission()) return

        var location: Location? = null

        when {
            locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> {
                location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            locationManager!!.isProviderEnabled(LocationManager.PASSIVE_PROVIDER) -> {
                location = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            }
        }

        if (location.isNull()) {
            handler.post(locationUpdater)
        } else {
            onLocationChanged(location!!)
            handler.post(locationUpdater)
        }
    }

    private fun requestLocation() {
        if (checkPermission()) return

        when {
            locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, delay, 0f, this)
            }
            locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> {
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, delay, 0f, this)
            }
            else -> {
                locationManager?.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, delay, 0f, this)
            }
        }
    }

    private fun checkPermission(): Boolean {
        return !(ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }
}
