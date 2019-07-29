package com.alexjreyes.autobot_client;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.google.android.gms.common.api.Response;

public class ControlsFragment extends Fragment implements SensorEventListener {
    SensorManager gyroSensorManager, accelerometerSensorManager;
    Sensor gyroSensor, accelerometerSensor;
    int sensorType;
    ImageView rightArrow,leftArrow,upArrow,downArrow;
    String URL = "http://autobot.alexjreyes.com/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gyroSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = gyroSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroSensorManager.registerListener(this, gyroSensor, gyroSensorManager.SENSOR_DELAY_NORMAL);

        accelerometerSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = accelerometerSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerSensorManager.registerListener(this, accelerometerSensor, accelerometerSensorManager.SENSOR_DELAY_NORMAL);;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_controls, container, false);

        rightArrow = view.findViewById(R.id.rightClick);
        leftArrow = view.findViewById(R.id.leftClick);
        downArrow = view.findViewById(R.id.reverseClick);
        upArrow = view.findViewById(R.id.forwardClick);

        rightArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRequest( "right");
                    }
                }
        );
        leftArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRequest( "left");
                    }
                }
        );
        upArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRequest( "forward");
                    }
                }
        );
        downArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRequest( "right");
                    }
                }
        );
        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        sensorType = sensorEvent.sensor.getType();
//        switch (sensorType) {
////            case Sensor.TYPE_GYROSCOPE:
////                if(sensorEvent.values[2] > 0.5f) { //left
////                    getRequest("gyro", "left");
////
////                } else if(sensorEvent.values[2] < -0.5f) { // right
////                    getRequest("gyro", "right");
////                }
////                break;
//            case Sensor.TYPE_ACCELEROMETER:
//                float x = sensorEvent.values[0];
//                float y = sensorEvent.values[1];
//                float z = sensorEvent.values[2];
//                xCoordinate.setText("X Coordinate: "+ x);
//                yCoordinate.setText("Y Coordinate: " + y);
//                zCoordinate.setText("Z Coordinate: " + z);
//                if(y>0) { //reverse
//                    getRequest("accel", "reverse");
//                } else { //forward
//                    getRequest("accel", "forward");
//                }
//                break;
//            default:
//                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//                break;
//        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //    public void getRequest(final String type, String direction) {
//        if(type == "gyro"){
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + direction, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                gyroText.setText("Gyro Tilt: " + response);
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        gyroText.setText("Gyro Error: " + error);
//                    }
//                });
//
//        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
//        rQueue.add(stringRequest);
//    }
//        else if(type == "accel"){
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + direction, new Response.Listener<String>() {
//
//                @Override
//                public void onResponse(String response) {
//                    accelText.setText("Accel Tilt: " + response);
//                }
//            },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            accelText.setText("Accel Error: " + error);
//                        }
//                    });
//            RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
//            rQueue.add(stringRequest);
//        }
//    }
    public void getRequest(String direction) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + direction, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(),"Movement: " + response,Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Movement: " + error,Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        rQueue.add(stringRequest);


    }
}
