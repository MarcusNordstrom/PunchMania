package punchmania.punchmania;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class Location extends Activity {
    public String ProviderName;

    public void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        LocationProvider provider = lm.getProvider("gps");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /* TODO: We should consider calling ActivityCompat#requestPermissions
             here to request the missing permissions, and then overriding
             public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
             to handle the case where the user grants the permission.
             See the documentation for ActivityCompat#requestPermissions for more details.
            */
            return;
        }
        lm.requestLocationUpdates("gps", 6000, 1, locationListener);
    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {

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
}
