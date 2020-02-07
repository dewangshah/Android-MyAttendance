package com.dewang.myattendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class ProfessorFeatures extends AppCompatActivity {

    TextView tvName,tvProfViewStudAttendance,tvProfViewDefaulters,tvSubStatistics,tvProfUploadMarks,tvProfSub,
            tvProfID,tvAoS,tvNavigation,tvIDNavig,tvNameNavig,tvProfSubject,tvLecsConducted,tvLecsAssigned;
    ImageView ivProfilePic,ivProfilePicNavig;
    DrawerLayout drawerLayout;
    SharedPreferences sp;
    String result = "NoResultYet";
    JSONObject jsonObject;
    JSONArray jsonArray;
    Bitmap bitmap;
    int id=0;
    String subject,image,AoS;
    NavigationView navigationView;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_features);

        tvName= (TextView) findViewById(R.id.tvName);
        tvProfViewStudAttendance= (TextView) findViewById(R.id.tvProfViewStudAttendance);
        tvProfViewDefaulters= (TextView) findViewById(R.id.tvProfViewDefaulters);
        tvSubStatistics= (TextView) findViewById(R.id.tvSubStatistics);
        tvProfUploadMarks= (TextView) findViewById(R.id.tvProfUploadMarks);
        tvProfID= (TextView) findViewById(R.id.tvProfID);
        tvProfSub= (TextView) findViewById(R.id.tvProfSub);
        tvAoS= (TextView) findViewById(R.id.tvAoS);
        ivProfilePic= (ImageView) findViewById(R.id.ivProfilePic);
        tvProfSubject= (TextView) findViewById(R.id.tvProfSubject);
        tvLecsConducted= (TextView) findViewById(R.id.tvLecsConducted);
        tvLecsAssigned= (TextView) findViewById(R.id.tvLecsAssigned);

        tvNavigation= (TextView) findViewById(R.id.tvNavigation);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        headerView=navigationView.getHeaderView(0);
        tvIDNavig= (TextView) headerView.findViewById(R.id.tvIDNavig);
        tvNameNavig= (TextView) headerView.findViewById(R.id.tvNameNavig);
        ivProfilePicNavig= (ImageView) headerView.findViewById(R.id.ivProfilePicNavig);

        sp=getSharedPreferences("prof",MODE_PRIVATE);
        String name=sp.getString("name","");
        if(name.endsWith("Checked"))
            name=name.replace("Checked","");
        else
        {
            SharedPreferences.Editor editor =sp.edit();
            editor.clear();
            editor.commit();
        }
        tvName.setText(name);
        tvName.setTextColor(Color.parseColor("#ffffff"));

        boolean connected = isConnectedToServer("http://192.168.43.132/myattendance/loginProf.php", 1500);

        if (connected == true) {
            BackgroundTaskProfProfile backgroundTaskProfProfile = new BackgroundTaskProfProfile(ProfessorFeatures.this);
            backgroundTaskProfProfile.execute(name);
            try {
                result = backgroundTaskProfProfile.get();
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve data", Toast.LENGTH_LONG).show();
                } else {
                    jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject jo = jsonArray.getJSONObject(0);
                    id=jo.getInt("id");
                    image=jo.getString("image");
                    subject=jo.getString("subject");
                    AoS=jo.getString("AoS");

                    StudentProfile studentProfile=new StudentProfile();
                    bitmap=studentProfile.getBitmapFromURL(image);
                    ivProfilePic.setImageBitmap(bitmap);
                    ivProfilePicNavig.setImageBitmap(bitmap);
                    tvNameNavig.setText(name);
                    tvProfID.setText("ID: "+id);
                    tvIDNavig.setText("ID:"+id);
                    tvAoS.setText(AoS);
                    tvProfSub.setText(subject);

                    BackgroundTaskProfSub backgroundTaskProfSub = new BackgroundTaskProfSub();
                    backgroundTaskProfSub.execute((subject+"t").toLowerCase());
                    result=backgroundTaskProfSub.get();
                    if(result!=null)
                    tvLecsConducted.setText(result);
                    tvProfSubject.setText(subject);
                    tvLecsAssigned.setText("30");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else
            Toast.makeText(getApplicationContext(),"Unable to Connect to Server",Toast.LENGTH_LONG).show();

        tvProfViewStudAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ProfessorFeatures.this,ProfViewAttendance.class);
                startActivity(i);
            }
        });

        tvProfUploadMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ProfessorFeatures.this,ProfUploadMarks.class);
                startActivity(i);
            }
        });

        tvProfViewDefaulters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ProfessorFeatures.this,DefaulterList.class);
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
                        sp=getSharedPreferences("prof", Context.MODE_PRIVATE);
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


    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content),"Server Not Connected",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return false;
        }
    }
}