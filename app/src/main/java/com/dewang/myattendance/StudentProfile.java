package com.dewang.myattendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import static android.R.attr.data;
import static com.dewang.myattendance.R.id.toolbar;
import static com.dewang.myattendance.R.id.tvName;

public class StudentProfile extends AppCompatActivity {

    TextView tvStudViewTT,tvProfileName,tvStudViewDetails,tvStudViewMarks,tvStudClass,tvStudID,tvStudAgg,
            tvAttendedLecs,tvTotalLecs,tvNavigation,tvIDNavig,tvNameNavig;
    SharedPreferences sp;
    DrawerLayout drawerLayout;
    int id,dwm,dwmt,hmi,hmit,ml,mlt,pds,pdst,agg,attendedLecs,totalLecs;
    String name,image;
    JSONObject jsonObject;
    ImageView ivProfilePic,ivProfilePicNavig;
    JSONArray jsonArray;
    String result = "NoResultYet";
    NavigationView navigationView;
    View headerView;
    Bitmap bitmap;
    int subs[]=new int[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        tvStudViewTT = (TextView) findViewById(R.id.tvStudViewTT);
        tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        tvStudViewDetails = (TextView) findViewById(R.id.tvStudViewdetails);
        tvStudViewMarks = (TextView) findViewById(R.id.tvStudViewMarks);
        tvStudClass = (TextView) findViewById(R.id.tvStudClass);
        tvStudID = (TextView) findViewById(R.id.tvStudID);
        tvStudAgg= (TextView) findViewById(R.id.tvStudAgg);
        tvAttendedLecs= (TextView) findViewById(R.id.tvAttendedLecs);
        tvTotalLecs= (TextView) findViewById(R.id.tvTotalLecs);
        ivProfilePic= (ImageView) findViewById(R.id.ivProfilePic);

        tvNavigation= (TextView) findViewById(R.id.tvNavigation);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        headerView=navigationView.getHeaderView(0);
        tvIDNavig= (TextView) headerView.findViewById(R.id.tvIDNavig);
        tvNameNavig= (TextView) headerView.findViewById(R.id.tvNameNavig);
        ivProfilePicNavig= (ImageView) headerView.findViewById(R.id.ivProfilePicNavig);

        sp = getSharedPreferences("student", MODE_PRIVATE);
        String StudID = sp.getString("stud_id", "");
        if(StudID.endsWith("Checked"))
            StudID=StudID.replace("Checked","");
        else
        {
            SharedPreferences.Editor editor =sp.edit();
            editor.clear();
            editor.commit();
        }
        tvStudID.setText(StudID);

        boolean connected = isConnectedToServer("http://192.168.43.132/myattendance/loginStud.php", 1500);
        if (connected == true) {
            BackgroundTaskStudProfile backgroundTaskStudProfile = new BackgroundTaskStudProfile(StudentProfile.this);
            backgroundTaskStudProfile.execute(StudID);
            try {
                result = backgroundTaskStudProfile.get();
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve data", Toast.LENGTH_LONG).show();
                } else {
                    jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject jo = jsonArray.getJSONObject(0);
                    id=jo.getInt("id");
                    name = jo.getString("name");
                    dwm=jo.getInt("dwm");
                    dwmt=jo.getInt("dwmt");
                    hmi=jo.getInt("hmi");
                    hmit=jo.getInt("hmit");
                    ml=jo.getInt("ml");
                    mlt=jo.getInt("mlt");
                    pds=jo.getInt("pds");
                    pdst=jo.getInt("pdst");
                    image=jo.getString("image");
                    attendedLecs=dwm+hmi+pds+ml;
                    totalLecs=dwmt+hmit+pdst+mlt;
                    agg=(int)(((float)attendedLecs/totalLecs)*100);
                    //Attendance attendance=new Attendance(id,name,dwm,dwmt,hmi,hmit,ml,mlt,pds,pdst,stud_id,image,agg);


                    tvStudAgg.setText(agg+"%");
                    if(agg<50)
                        Toast.makeText(getApplicationContext(),"You are in Defaulters list!",Toast.LENGTH_LONG).show();
                    tvProfileName.setText(name);
                    tvStudClass.setText("BE 3 "+id);
                    tvAttendedLecs.setText(attendedLecs+"");
                    tvTotalLecs.setText(totalLecs+"");
                    bitmap=getBitmapFromURL(image);
                    ivProfilePic.setImageBitmap(bitmap);
                    tvIDNavig.setText("ID:"+id);
                    ivProfilePicNavig.setImageBitmap(bitmap);
                    tvNameNavig.setText(name);

                    subs[0]=dwm;
                    subs[1]=dwmt;
                    subs[2]=hmi;
                    subs[3]=hmit;
                    subs[4]=pds;
                    subs[5]=pdst;
                    subs[6]=ml;
                    subs[7]=mlt;
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

        tvStudViewTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "www.shahandanchor.com/2014/computerengineering/time-table/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http:" + url));
                startActivity(i);
            }
        });

        tvStudViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StatisticalViewStud.class);
                i.putExtra("lecs",subs);
                startActivity(i);
            }
        });

        tvStudViewMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Marks", Toast.LENGTH_LONG).show();
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
                        sp=getSharedPreferences("student", Context.MODE_PRIVATE);
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
            // Handle your exceptions
            return false;
        }
    }

    public Bitmap getBitmapFromURL(String src)
    {
        try {
            URL url=new URL(src);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap= BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
