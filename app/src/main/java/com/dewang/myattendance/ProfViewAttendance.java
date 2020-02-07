package com.dewang.myattendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.concurrent.ExecutionException;

public class ProfViewAttendance extends AppCompatActivity {
    JSONObject jsonObject;
    String parse_json;
    JSONArray jsonArray;
    AttendanceAdapter attendanceAdapter;
    ListView lView;
    int stud_id[]=new int[50];
    int id,dwm,dwmt,hmi,hmit,ml,mlt,pds,pdst,attendedLecs,totalLecs,agg;
    String image,studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_view_attendance);
        bgTask1 backgrnd=new bgTask1();
        backgrnd.execute("http://192.168.43.132/myattendance/json_get_data.php");
        lView= (ListView) findViewById(R.id.lView);
        attendanceAdapter=new AttendanceAdapter(this,R.layout.row_layout);
        lView.setAdapter(attendanceAdapter);
        try {
            parse_json= backgrnd.get();

            if(parse_json==null)
            {
                Toast.makeText(getApplicationContext(),"Unable to retrieve data",Toast.LENGTH_LONG).show();
            }

            else
            {
                jsonObject=new JSONObject(parse_json);
                jsonArray=jsonObject.getJSONArray("server_response");
                int count=0;
                int id;
                String name;

                while (count<50){
                    JSONObject jo=jsonArray.getJSONObject(count);
                    id=jo.getInt("id");
                    studentID=jo.getString("stud_id");
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
                    stud_id[count]=id;
                    Attendance attendance=new Attendance(id,name,dwm,dwmt,hmi,hmit,ml,mlt,pds,pdst,studentID,image,agg);
                    attendanceAdapter.add(attendance);
                    count++;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(ProfViewAttendance.this,StatisticalView.class);
                i.putExtra("id",stud_id[position]);
                startActivity(i);
            }
        });
    }


    }

    class bgTask1 extends AsyncTask<String, Void, String> {

        String JSON_STRING;
        String json_url;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            JSON_STRING=result;

        }
    }

