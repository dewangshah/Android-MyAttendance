package com.dewang.myattendance;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import static android.view.View.generateViewId;

public class MainActivity extends AppCompatActivity {

    Animation a;
    ImageView iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv1.startAnimation(a);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(), ScrollingActivity.class);
                startActivity(i);
                finish();
            }
        }).start();


    }
}
