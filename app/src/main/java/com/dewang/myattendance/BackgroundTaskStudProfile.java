package com.dewang.myattendance;

import android.content.Context;
import android.os.AsyncTask;

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

/**
 * Created by Dewang on 3/16/2017.
 */

public class BackgroundTaskStudProfile extends AsyncTask<String,Void,String> {

    Context ctx;
    BackgroundTaskStudProfile(Context ctx)
    {
        this.ctx=ctx;
    }
    String json_url,JSON_STRING;

    @Override
    protected String doInBackground(String... params) {
        String stud_id="";
        stud_id=params[0];
        try {
            json_url="http://192.168.43.132/myattendance/get_studprofile_data.php";
            URL url=new URL(json_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data= URLEncoder.encode("studid","UTF-8")+"="+ URLEncoder.encode(stud_id,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            StringBuilder stringBuilder = new StringBuilder();
            while((JSON_STRING=bufferedReader.readLine())!=null)
            {
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
