package com.alexjreyes.autobot_client;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONObject;

public class MainActivity extends FragmentActivity {
    private WebView webView;

    // Map Fragment
    public static MapView mMapView;
    public static GoogleMap googleMap;
    public static MarkerOptions markerOptions;
    public static Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Replace webview with fragment for toggling
        webView = findViewById(R.id.webView);
        webView.loadUrl("http://autobot.alexjreyes.com/stream");
        webView.setVisibility(webView.VISIBLE);

        showControls();
        showMap();
        setupPusher();
   }

    private void setupPusher() {
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        Pusher pusher = new Pusher(BuildConfig.pusherAPIKey, options);

        Channel channel = pusher.subscribe("autobot");

        channel.bind("update-gps", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(data);

                            double lat = (double) obj.get("latitude");
                            double lng = (double) obj.get("longitude");
                            LatLng location = new LatLng(lat, lng);

                            if (marker != null) { marker.remove(); }
                            marker = googleMap.addMarker(markerOptions.position(location));

                            CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        } catch (Exception ex) {
                            Log.e("Pusher error", ex.getMessage());
                        }

                    }
                });
            }
        });

        pusher.connect();
    }

    public void showMap() {
        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mapFragmentContainer, mapFragment)
                .commit();
    }

    public void showControls() {
        ControlsFragment controlFragment = new ControlsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.controlFragmentContainer, controlFragment)
                .commit();
    }
}
