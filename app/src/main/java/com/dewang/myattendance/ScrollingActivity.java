package com.dewang.myattendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class ScrollingActivity extends AppCompatActivity {

    Button btnHod,btnProf,btnStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        btnHod= (Button) findViewById(R.id.btnHOD);
        btnProf= (Button) findViewById(R.id.btnProf);
        btnStudent= (Button) findViewById(R.id.btnStudent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean isConnected=true;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!isConnected) {
            Snackbar mSnackbar=Snackbar.make(findViewById(android.R.id.content), "No Internet Connectivity",
                    Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkConnection();
                }

            });
            ViewGroup group = (ViewGroup) mSnackbar.getView();
            group.setBackgroundColor(ContextCompat.getColor(ScrollingActivity.this, R.color.materialRed));
            mSnackbar.setActionTextColor(Color.WHITE);
            mSnackbar.show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check=checkConnectionOnButtonPressed();
                String email="mail@shahandanchor.com";
                if(check) {
                     Intent i=new Intent(Intent.ACTION_SENDTO);
                     i.setData(Uri.parse("mailto:"+email));
                    startActivity(i);
                }
                else
                    checkConnection();
            }
        });
      btnHod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=checkConnectionOnButtonPressed();
                if(check) {
                    Intent i = new Intent(getApplicationContext(), HodLogin.class);
                    startActivity(i);
                }
                else
                    checkConnection();
            }
        });

        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=checkConnectionOnButtonPressed();
                if(check) {
                    Intent i = new Intent(getApplicationContext(), ProfLogin.class);
                    startActivity(i);
                }
                else
                    checkConnection();
            }
        });

       btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=checkConnectionOnButtonPressed();
                if(check) {
                    Intent i = new Intent(getApplicationContext(), StudentLogin.class);
                    startActivity(i);
                }
                else
                    checkConnection();
            }
        });
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit this application?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert=builder.create();
        alert.setTitle("Exit");
        alert.show();
    }
    public void checkConnection()
    {
        boolean isConnected=true;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!isConnected) {
            Snackbar mSnackbar=Snackbar.make(findViewById(android.R.id.content), "No Internet Connectivity",
                    Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkConnection();
                }

            });
            ViewGroup group = (ViewGroup) mSnackbar.getView();
            group.setBackgroundColor(ContextCompat.getColor(ScrollingActivity.this, R.color.materialRed));
            mSnackbar.setActionTextColor(Color.WHITE);
            //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)group.getLayoutParams();
            //params.gravity = Gravity.TOP;
            //group.setLayoutParams(params);
            mSnackbar.show();
        }
        else
        {
            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "                     Connection Established", Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snack.getView();
            group.setBackgroundColor(ContextCompat.getColor(ScrollingActivity.this, R.color.materialGreen));
            snack.show();
        }
    }

    public boolean checkConnectionOnButtonPressed(){
        boolean isConnected=true;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
