package app.simple.positional.services

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import app.simple.positional.location.LocalLocationProvider
import app.simple.positional.location.callbacks.LocationProviderListener

class LocationService : Service(), LocationProviderListener {
    private var locationProvider: LocalLocationProvider? = null
    private lateinit var intent: Intent
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        this.intent = Intent("location_update")
        locationProvider = LocalLocationProvider()
        locationProvider!!.init(baseContext, this)
        locationProvider!!.delay = 1000
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        locationProvider!!.removeLocationCallbacks()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        locationProvider!!.removeLocationCallbacks()
        super.onDestroy()
    }

    override fun onLocationChanged(location: Location) {
        Intent().also { intent ->
            intent.action = "location_update"
            intent.putExtra("location", location)
            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)
        }
        //intent.putExtra("latitude", location.getLatitude());
        //intent.putExtra("longitude", location.getLongitude());
        //intent.putExtra("bearing", beari)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}