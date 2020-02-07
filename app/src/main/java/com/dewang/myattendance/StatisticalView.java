package com.dewang.myattendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import at.grabner.circleprogress.CircleProgressView;

public class StatisticalView extends AppCompatActivity {

    TextView tvAgg,tvProfileName;
    String result = "NoResultYet";
    int id, dwm, dwmt, hmi, hmit, ml, mlt, pds, pdst, agg, attendedLecs, totalLecs;
    String name, image;
    JSONObject jsonObject;
    ImageView ivProfilePic, ivProfilePicNavig;
    JSONArray jsonArray;
    BarChart barChart;
    CircleProgressView pvDWM,pvHMI,pvPDS,pvML;
    Bitmap bitmap;
    int subs[]=new int[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_view);

        barChart= (BarChart) findViewById(R.id.barChartLecs);
        pvDWM= (CircleProgressView) findViewById(R.id.pvDWM);
        pvHMI= (CircleProgressView) findViewById(R.id.pvHMI);
        pvPDS= (CircleProgressView) findViewById(R.id.pvPDS);
        pvML= (CircleProgressView) findViewById(R.id.pvML);

        tvAgg= (TextView) findViewById(R.id.tvAgg);
        tvProfileName= (TextView) findViewById(R.id.tvProfileName);

        ivProfilePic= (ImageView) findViewById(R.id.ivProfilePic);

        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);

        BgTaskStudProfile bgTaskStudProfile = new BgTaskStudProfile();
        bgTaskStudProfile.execute(id);
        try {
            result = bgTaskStudProfile.get();
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Unable to retrieve data", Toast.LENGTH_LONG).show();
            } else {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject jo = jsonArray.getJSONObject(0);
                id = jo.getInt("id");
                name = jo.getString("name");
                dwm = jo.getInt("dwm");
                dwmt = jo.getInt("dwmt");
                hmi = jo.getInt("hmi");
                hmit = jo.getInt("hmit");
                ml = jo.getInt("ml");
                mlt = jo.getInt("mlt");
                pds = jo.getInt("pds");
                pdst = jo.getInt("pdst");
                image = jo.getString("image");
                attendedLecs = dwm + hmi + pds + ml;
                totalLecs = dwmt + hmit + pdst + mlt;
                agg = (int) (((float) attendedLecs / totalLecs) * 100);

                tvAgg.setText(agg);
                tvProfileName.setText(name+"'s Attendance");
                bitmap=getBitmapFromURL(image);
                ivProfilePic.setImageBitmap(bitmap);

                subs[0]=dwm;
                subs[1]=dwmt;
                subs[2]=hmi;
                subs[3]=hmit;
                subs[4]=pds;
                subs[5]=pdst;
                subs[6]=ml;
                subs[7]=mlt;

                int percentDWM=(int)((subs[0]/(float)subs[1])*100);
                pvDWM.setRimWidth(2);
                pvDWM.setBarWidth(20);
                pvDWM.setValueAnimated(percentDWM,2000);

                int percentHMI=(int)((subs[2]/(float)subs[3])*100);
                pvHMI.setRimWidth(2);
                pvHMI.setBarWidth(20);
                pvHMI.setValueAnimated(percentHMI,2000);

                int percentPDS=(int)((subs[4]/(float)subs[5])*100);
                pvPDS.setRimWidth(2);
                pvPDS.setBarWidth(20);
                pvPDS.setValueAnimated(percentPDS,2000);

                int percentML=(int)((subs[6]/(float)subs[7])*100);
                pvML.setRimWidth(2);
                pvML.setBarWidth(20);
                pvML.setValueAnimated(percentML,2000);

                //Y-axis data
                ArrayList<BarEntry> barEntries=new ArrayList<>();
                barEntries.add(new BarEntry(subs[0],0));
                barEntries.add(new BarEntry(subs[1],1));
                barEntries.add(new BarEntry(subs[2],2));
                barEntries.add(new BarEntry(subs[3],3));
                barEntries.add(new BarEntry(subs[4],4));
                barEntries.add(new BarEntry(subs[5],5));
                barEntries.add(new BarEntry(subs[6],6));
                barEntries.add(new BarEntry(subs[7],7));
                BarDataSet barDataSet=new BarDataSet(barEntries,"No. of Lectures");
                barDataSet.setColors(new int[]{R.color.red,R.color.dark_red,R.color.blue,R.color.dark_blue,R.color.green,R.color.dark_green,
                        R.color.orange,R.color.dark_orange},getApplicationContext());

                //X-axis data
                ArrayList<String> subjects=new ArrayList<>();
                subjects.add("DWM");
                subjects.add("");
                subjects.add("HMI");
                subjects.add("");
                subjects.add("PDS");
                subjects.add("");
                subjects.add("ML");
                subjects.add("");

                BarData data=new BarData(subjects,barDataSet);
                barChart.setData(data);

                barChart.setTouchEnabled(true);
                barChart.setDragEnabled(true);
                barChart.animateY(3000);
                barChart.setDescription("");
                barChart.setNoDataText("Unable to Retrieve data");
                Legend legend=barChart.getLegend();
                legend.setEnabled(false);

            }
            }

     catch(InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
class BgTaskStudProfile extends AsyncTask<Integer, Void, String> {
        String json_url, JSON_STRING;

        protected String doInBackground(Integer... params) {
            int id;
            id = params[0];
            try {
                json_url = "http://192.168.43.132/myattendance/get_studprof_data.php";
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id + "", "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
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
    }
