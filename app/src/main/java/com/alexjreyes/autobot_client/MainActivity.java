package com.alexjreyes.autobot_client;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
    public static FrameLayout mapFragmentContainer;
    public static MapView mMapView;
    public static GoogleMap googleMap;
    public static MarkerOptions markerOptions;
    public static Marker marker;
    public static Button toggleViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragmentContainer = findViewById(R.id.mapFragmentContainer);
        mapFragmentContainer.setVisibility(mapFragmentContainer.GONE);

        // TODO: Replace webview with fragment for toggling
        webView = findViewById(R.id.webView);
        webView.loadUrl("http://autobot.alexjreyes.com/stream");
        webView.setVisibility(webView.VISIBLE);

        toggleViewBtn = findViewById(R.id.toggleViewBtn);
        toggleViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.getVisibility() == View.VISIBLE){
                    webView.setVisibility(View.GONE);
                    mapFragmentContainer.setVisibility(View.VISIBLE);
                } else {
                    webView.setVisibility(View.VISIBLE);
                    mapFragmentContainer.setVisibility(View.GONE);
                }
            }
        });

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
