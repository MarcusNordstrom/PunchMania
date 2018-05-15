package punchmania.punchmania;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class LocationTester extends Activity {
    public String ProviderName;
    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
                lm.removeUpdates(this);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    LocationProvider provider = lm.getProvider("gps");

    @SuppressLint("MissingPermission")
    public void getLocation() {
        getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates("gps", 6000, 1, locationListener);
    }
}
