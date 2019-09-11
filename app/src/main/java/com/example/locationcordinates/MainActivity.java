package com.example.locationcordinates;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btstart,btstop;
    TextView cordinates;
    BroadcastReceiver receiver;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if( receiver == null)
        {
            receiver= new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    cordinates.append("\n"+"Longitude is"+intent.getExtras().get("Longitude").toString()+"  "+"Latitude is "+intent.getExtras().get("Latitude").toString());

                }
            };

        }
        registerReceiver(receiver,new IntentFilter("Location_Update"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btstart=(Button)findViewById(R.id.btn_start);
        btstop=(Button)findViewById(R.id.btn_stop);
        cordinates=(TextView)findViewById(R.id.cordi);
        if(!runtime_service())
        {
            enable_Button();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                enable_Button();
            }
            else {
                runtime_service();
            }
        }
    }

    private void enable_Button()
    {
        btstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),GPS_SERVICE.class);
                startService(intent);
            }
        });
        btstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),GPS_SERVICE.class);
                stopService(intent);
            }
        });
    }

    private boolean runtime_service()
    {
        if(Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            Log.d("permission if","trueeeee");
            return (true);

        }
        else
        {
            Log.d("permission else","Falseeeee");
            return (false);

        }
    }
}
