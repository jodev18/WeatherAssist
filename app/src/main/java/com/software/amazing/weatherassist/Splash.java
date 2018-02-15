package com.software.amazing.weatherassist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Splash.this);

        Boolean hasIP = sp.getString("IP_ADDRESS", null) != null;

        if(hasIP){
            delayAnimation();
        }
        else{
            askForIP();
        }
    }

    private void askForIP(){

        AlertDialog.Builder ab = new AlertDialog.Builder(Splash.this);

        ab.setTitle("IP Address");
        ab.setMessage("Please enter the IP address of your device. Cancelling would quit application.");

        final View vv = this.getLayoutInflater().inflate(R.layout.layout_dialog_input,null);

        ab.setView(vv);

        ab.setPositiveButton("Ok", null);

        ab.setNegativeButton("Cancel",null);

        ab.setCancelable(false);

        AlertDialog ad = ab.create();

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                Button saveEntry = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                saveEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView ipField = (TextView)vv.findViewById(R.id.etDialogInputField);

                        String ipAddr = ipField.getText().toString();

                        if(ipAddr.length() >= 11 && ipAddr.contains(".")){
                            SharedPreferences sp = PreferenceManager
                                    .getDefaultSharedPreferences(Splash.this);

                            SharedPreferences.Editor e = sp.edit();

                            e.putString("IP_ADDRESS", ipAddr);
                            e.commit();

                            dialog.dismiss();

                            delayAnimation();
                        }
                        else{
                            Toast.makeText(Splash.this, "Please enter a valid IP address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        ad.show();
    }

    private void delayAnimation(){
        h = new Handler(this.getMainLooper());

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),TemperatureDashboard.class));
                finish();
            }
        },2300);
    }
}
