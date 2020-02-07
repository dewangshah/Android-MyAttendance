package com.dewang.myattendance;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

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


public class BackgroundTaskProf extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    Context ctx;
    BackgroundTaskProf(Context ctx)
    {
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information");
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url="http://192.168.43.132/myattendance/loginProf.php";
        String login_name=params[0];
        String login_pass=params[1];
        try {
            URL url=new URL(login_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data= URLEncoder.encode("login_name","UTF-8")+"="+ URLEncoder.encode(login_name,"UTF-8")+"&"+
                    URLEncoder.encode("login_pass","UTF-8")+"="+ URLEncoder.encode(login_pass,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String response="";
            String line="";
            while((line=bufferedReader.readLine())!=null)
            {
                response+=line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;
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
        if(!result.contains("true"))
        {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }
}
