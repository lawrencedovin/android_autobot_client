package com.alexjreyes.autobot_client;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
//import com.google.android.gms.common.api.Response;

public class ControlsFragment extends Fragment implements SensorEventListener {
    private SensorManager accelerometerSensorManager;
    private Sensor accelerometerSensor;
    private int sensorType;
    private ImageView rightArrow,leftArrow,upArrow,downArrow;
    private TextView accelText;
    private String URL = "http://autobot.alexjreyes.com/";
    private long lastExecution = new Date().getTime();
    private Button autoDriveBtn;
    private Boolean sensorDeactivated;
    private LabeledSwitch toggleSensorsSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accelerometerSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = accelerometerSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerSensorManager.unregisterListener(this);
        sensorDeactivated = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_controls, container, false);

        rightArrow = view.findViewById(R.id.rightClick);
        leftArrow = view.findViewById(R.id.leftClick);
        downArrow = view.findViewById(R.id.reverseClick);
        upArrow = view.findViewById(R.id.forwardClick);

        accelText = view.findViewById(R.id.accel_sensor);
        autoDriveBtn = view.findViewById(R.id.autoDriveBtn);

        toggleSensorsSwitch = view.findViewById(R.id.toggleSensorsSwitch);

        if(accelerometerSensor == null) {
            Toast.makeText(getActivity(), "Device has no Accelerometer", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

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

        autoDriveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autoDrive();
                    }
                }
        );

        toggleSensorsSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    onSensorsActivated();
                } else {
                    onSensorsDeactivated();
                }
            }
        });
        return view;
    }

    public void onSensorsActivated() {
        accelerometerSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(getActivity(),"Sensors Activated",Toast.LENGTH_SHORT).show();
    }
    public void onSensorsDeactivated() {
        accelerometerSensorManager.unregisterListener(this);
        Toast.makeText(getActivity(),"Sensors Deactivated",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorType = sensorEvent.sensor.getType();
        if(new Date().getTime() - lastExecution < 750){
            return ;
        }

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];

                if(x<0.0f && x>-2.0f && y>3.0f) { //reverse
                    getSensorRequest("accel", "reverse");
                    lastExecution = new Date().getTime();
                } else if(x<0.0f && x>-2.0f && y>-3.0f && y<-1.5f) { //forward
                    getSensorRequest("accel", "forward");
                    lastExecution = new Date().getTime();
                } else if(y<0.0f && y>-2.0f && x>3.0f){
                    getSensorRequest("accel", "left");
                    lastExecution = new Date().getTime();
                } else if(y<0.0f && y>-2.0f && x<-3.0f && x<-1.5f){
                    getSensorRequest("accel", "right");
                    lastExecution = new Date().getTime();
                } else if(y>-1.0 && y<1.0 && x>-1.0 && x<1.0){
                    lastExecution = new Date().getTime();
                }
                break;
            default:
                break;
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void getSensorRequest(final String type, String direction) {
        if(type == "accel"){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + direction, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
//                    accelText.setText("Movement Response: " + response);
                    Toast.makeText(getActivity(),"Movement Response: " + response,Toast.LENGTH_SHORT).show();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            accelText.setText("Movement Error: " + error);
                            Toast.makeText(getActivity(),"Movement Error: " + error,Toast.LENGTH_SHORT).show();
                        }
                    });
            RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            rQueue.add(stringRequest);
        }
    }

    public void getRequest(String direction) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + direction, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                accelText.setText("Movement Response: " + response);
                Toast.makeText(getActivity(),"Movement Response: " + response,Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        accelText.setText("Movement Error: " + error);
                        Toast.makeText(getActivity(),"Movement Error: " + error,Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        rQueue.add(stringRequest);

    }

    public void autoDrive() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "/activate", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //accelText.setText("Autodrive Activated: " + response);
                Toast.makeText(getActivity(),"Autodrive Activated: " + response,Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        accelText.setText("Autodrive Error: " + error);
                        Toast.makeText(getActivity(),"Autodrive Error: " + error,Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        rQueue.add(stringRequest);

    }
}
