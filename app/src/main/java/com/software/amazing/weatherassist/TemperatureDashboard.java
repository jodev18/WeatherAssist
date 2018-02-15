package com.software.amazing.weatherassist;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.software.amazing.weatherassist.core.GlobalVars;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TemperatureDashboard extends AppCompatActivity {

    private Handler h;

    @BindView(R.id.tvBrightness) TextView tvBright;
    @BindView(R.id.tvHumidity) TextView tvHumd;
    @BindView(R.id.tvTemperature) TextView tvTemp;
    @BindView(R.id.tvUV) TextView tvUV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_temperature_dashboard);
        setTitle("Dashboard");

        ButterKnife.bind(this);

        h = new Handler(this.getMainLooper());

        initPrereqs();
    }

    private void initPrereqs(){

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLight();
            }
        },1000);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getHumidity();
            }
        },6000);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTemp();
            }
        },11000);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUV();
            }
        },16000);
    }

    private void getTemp(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    String url = GlobalVars.DATA_SERVER_ADDRESS + GlobalVars.DATA_URI_TEMPERATURE;

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();

                    if(response.body() != null){
                        try{

                            final String temp = response.body().string();

                            h.post(new Runnable() {
                                @Override
                                public void run() {
//                                    vTemp.setText(temp + "° F");
                                    tvTemp.setText(temp + "° C");
                                    Toast.makeText(TemperatureDashboard.this,
                                            "Temperature is " + temp + " degrees Celsius.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (NullPointerException nulLEx){

                            Log.e("TEMP_FETCH","NullPointerException encountered during temperature fetch");

                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TemperatureDashboard.this, "Error connecting to sensor!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else{
                        Log.e("FETCH_DATA","No data captured.");
                        Toast.makeText(TemperatureDashboard.this, "Error connecting to sensor!", Toast.LENGTH_SHORT).show();
                    }



                }
                catch(IOException ioex){
                    Log.e("TEMP_FETCH","IOException encountered during temperature fetch: " + ioex.getMessage());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TemperatureDashboard.this, "Error connecting to sensor!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    private void getHumidity(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    String url = GlobalVars.DATA_SERVER_ADDRESS + GlobalVars.DATA_URI_HUMIDITY;

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();

                    final String hum = response.body().string();

                    final String hum_disp = "Humidity is " + hum + "%.";



                    h.post(new Runnable() {
                        @Override
                        public void run() {
//                            vHumidity.setText(hum + "%");
                            tvHumd.setText(hum_disp);
                            Toast.makeText(TemperatureDashboard.this, hum_disp, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch(IOException ioex){
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TemperatureDashboard.this, "Error connecting to sensor!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * Retrieves light data and displays it
     * to the textview.
     */
    private void getLight(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    String url = GlobalVars.DATA_SERVER_ADDRESS + GlobalVars.DATA_URI_LIGHT;

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();

                    String light = response.body().string();

                    Integer bright = Integer.parseInt(light);

                    String message = "";

                    if(bright > 0 && bright < 10){
                        message = "It is dark outside.";
                    }
                    else if(bright > 10 && bright < 50){
                        message =  "It is quite dim.";
                    }
                    else if(bright > 50 && bright < 100){
                        message = "There's a glow outside.";
                    }
                    else if(bright > 100 && bright < 200){
                        message = "There's a bright glow outside.";
                    }
                    else if(bright > 200 && bright < 300){
                        message = "There's quite a bright light outside.";
                    }
                    else if(bright > 300) {
                        message = "It is bright outside.";
                    }



                    //finalize
                    final String theMessage = message;

                    h.post(new Runnable() {
                        @Override
                        public void run() {
//                            vBrightness.setText(theMessage);
                            tvBright.setText(theMessage);
                            Toast.makeText(TemperatureDashboard.this, theMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch(IOException ioex){
                    h.post(new Runnable() {
                        @Override
                        public void run() {
//                            vBrightness.setText("Error connecting to sensor!");
                            Toast.makeText(TemperatureDashboard.this,
                                    "Error connecting to sensor!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void getUV(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    String url = GlobalVars.DATA_SERVER_ADDRESS + GlobalVars.DATA_URI_UV;

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();

                    final String uv = response.body().string();

                    final String uv_disp = "UV Index: " + uv;

                    h.post(new Runnable() {
                        @Override
                        public void run() {
//                            vHumidity.setText(hum + "%");
                            tvUV.setText(uv_disp);
                            Toast.makeText(TemperatureDashboard.this, uv_disp, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch(IOException ioex){
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TemperatureDashboard.this, "Error connecting to sensor!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
