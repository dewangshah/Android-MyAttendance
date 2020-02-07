package com.dewang.myattendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class HodFeatures extends AppCompatActivity {

    ImageView ivProfilePic,ivProfilePicNavig;
    TextView tvName,tvHODID,tvHODViewStudAttendance,tvHODViewDefaulters,tvHODEditDatabase,tvHODDeptProf,
            tvHODNotif,tvHODViewMarks,tvNavigation,tvIDNavig,tvNameNavig;
    SharedPreferences sp;
    String result = "NoResultYet";
    JSONObject jsonObject;
    JSONArray jsonArray;
    Bitmap bitmap;
    int id;
    String image,dept;
    NavigationView navigationView;
    View headerView;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_features);


        ivProfilePic= (ImageView) findViewById(R.id.ivProfilePic);
        tvName= (TextView) findViewById(R.id.tvName);
        tvHODID= (TextView) findViewById(R.id.tvHODID);
        tvHODViewStudAttendance= (TextView) findViewById(R.id.tvHODViewStudAttendance);
        tvHODViewDefaulters= (TextView) findViewById(R.id.tvHODViewDefaulters);
        tvHODEditDatabase= (TextView) findViewById(R.id.tvHODEditDatabase);
        tvHODDeptProf= (TextView) findViewById(R.id.tvHODDeptProf);
        tvHODNotif= (TextView) findViewById(R.id.tvHODNotif);
        tvHODViewMarks= (TextView) findViewById(R.id.tvHODViewMarks);

        tvNavigation= (TextView) findViewById(R.id.tvNavigation);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        headerView=navigationView.getHeaderView(0);
        tvIDNavig= (TextView) headerView.findViewById(R.id.tvIDNavig);
        tvNameNavig= (TextView) headerView.findViewById(R.id.tvNameNavig);
        ivProfilePicNavig= (ImageView) headerView.findViewById(R.id.ivProfilePicNavig);

        sp=getSharedPreferences("hod",MODE_PRIVATE);
        String name=sp.getString("name","");
        if(name.endsWith("Checked"))
            name=name.replace("Checked","");
        else
        {
            SharedPreferences.Editor editor =sp.edit();
            editor.clear();
            editor.commit();
        }
        //String name=getIntent().getStringExtra("name");
        tvName.setText(name);
        tvName.setTextColor(Color.parseColor("#ffffff"));

        boolean con=isConnectedToServer("http://192.168.43.132/myAttendance/loginHod.php",1500);
        if(!con)
        {
            Toast.makeText(getApplicationContext(),"Server Not Connected",Toast.LENGTH_LONG).show();
        }
        else{

            BackgroundTaskHODProfile backgroundTaskHODProfile = new BackgroundTaskHODProfile(HodFeatures.this);
            backgroundTaskHODProfile.execute(name);
            try {
                result = backgroundTaskHODProfile.get();
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve data", Toast.LENGTH_LONG).show();
                } else {
                    jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject jo = jsonArray.getJSONObject(0);
                    id=jo.getInt("id");
                    image=jo.getString("image");
                    dept=jo.getString("dept");

                    StudentProfile sp=new StudentProfile();
                    bitmap=sp.getBitmapFromURL(image);
                    ivProfilePicNavig.setImageBitmap(bitmap);
                    tvIDNavig.setText("ID:"+id);
                    tvNameNavig.setText(name);
                    ivProfilePic.setImageBitmap(bitmap);
                    tvHODID.setText("ID: "+id);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

                }

        tvHODViewStudAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HodFeatures.this,HodViewAttendance.class);
                startActivity(i);
            }
        });
          tvHODEditDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HodFeatures.this,HodEditDatabase.class);
                startActivity(i);
            }
        });
        tvHODViewDefaulters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HodFeatures.this,DefaulterList.class);
                startActivity(i);
            }
        });
        tvHODNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HodFeatures.this,SendNotifications.class);
                startActivity(i);
            }
        });

        tvNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawer(Gravity.LEFT);
                else
                    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_id:
                        Intent i=new Intent(getApplicationContext(),ScrollingActivity.class);
                        startActivity(i);
                        finish();
                        break;

                    case R.id.notifications_id:
                        Intent in=new Intent(getApplicationContext(),RecieveNotifications.class);
                        startActivity(in);
                        break;

                    case R.id.send_query_id:
                        Toast.makeText(getApplicationContext(),"Kaam Chalu hai",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.about_us_id:
                        Toast.makeText(getApplicationContext(),"Kaam Chalu hai",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.logout_id:
                        sp=getSharedPreferences("hod", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor =sp.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logOut:
                sp=getSharedPreferences("hod", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sp.edit();
                editor.clear();
                editor.commit();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);

            connection.connect();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}