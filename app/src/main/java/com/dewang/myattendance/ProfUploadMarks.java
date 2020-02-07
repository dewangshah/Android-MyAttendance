package com.dewang.myattendance;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.dewang.myattendance.R.drawable.attendance;

public class ProfUploadMarks extends AppCompatActivity {

    Button btnUpload;
    Spinner spnid,spnclass;
    EditText etPDSMarks,etHMIMarks,etMLMarks,etDWMMarks;
    TextView tvStudName;
    String parse_json;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String roll_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_upload_marks);

        btnUpload= (Button) findViewById(R.id.btnUpload);
        etPDSMarks= (EditText) findViewById(R.id.etPDSMarks);
        etDWMMarks= (EditText) findViewById(R.id.etDWMMarks);
        etHMIMarks= (EditText) findViewById(R.id.etHMIMarks);
        etMLMarks= (EditText) findViewById(R.id.etMLMarks);
        tvStudName= (TextView) findViewById(R.id.tvStudName);
        spnclass= (Spinner) findViewById(R.id.spnclass);
        spnid= (Spinner) findViewById(R.id.spnid);


        final String idNamePair[][]=new String[50][50];
        final ArrayList alId=new ArrayList();
        //alId.add("Select Roll No.");

        ArrayList<String> alClass=new ArrayList<String>();
        //alClass.add("Select Class");
        alClass.add("BE-3");
        alClass.add("BE-4");
        alClass.add("BE-D");

        ArrayAdapter<String> classAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,alClass);
        spnclass.setAdapter(classAdapter);
        spnclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String studClass=parent.getItemAtPosition(position).toString().toLowerCase();
                    BgTaskRollNo bgTaskRollNo = new BgTaskRollNo();
                    bgTaskRollNo.execute(roll_url,studClass);
                    try {
                        parse_json=bgTaskRollNo.get();
                        Toast.makeText(getApplicationContext(),parse_json,Toast.LENGTH_LONG).show();

                        if(parse_json==null);
                           // Toast.makeText(getApplicationContext(),"Unable to retrieve Roll Numbers",Toast.LENGTH_LONG).show();
                        else
                        {
                            jsonObject=new JSONObject(parse_json);
                            jsonArray=jsonObject.getJSONArray("server_response");
                            int count=0;

                            while (count<50){
                                JSONObject jo=jsonArray.getJSONObject(count);
                                if((jo.getInt("id")+"")==null)
                                    break;
                                alId.add(jo.getInt("id"));
                                idNamePair[count][0]=(jo.getInt("id"))+"";
                                idNamePair[count][1]=jo.getString("name");
                                int id1=Integer.parseInt(idNamePair[count][0]);
                                //Attendance attendance=new Attendance(id1,idNamePair[count][1]);
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
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> idAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,alId);
        spnid.setAdapter(idAdapter);
        spnid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String studId=parent.getItemAtPosition(position).toString();
                if(studId!=null && studId!=(0+"")) {
                    Toast.makeText(getApplicationContext(),studId,Toast.LENGTH_LONG).show();
                    int value=0;
                    for(int i=0;i<50;i++)
                    {
                        if(idNamePair[i][0]==null)
                            break;
                        if(idNamePair[i][0].equals(studId))
                        {
                            value=i;
                            break;
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"Roll No:"+idNamePair[1][0]+"Name:"+idNamePair[1][1],Toast.LENGTH_LONG).show();
                    //tvStudName.setText(idNamePair[value][1]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

class BgTaskRollNo extends AsyncTask<String,Void,String>{
    String JSON_STRING;
    String roll_url,json;

    @Override
    protected void onPreExecute() {
        roll_url="http://192.168.43.132/myattendance/json_roll.php";
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url=new URL(roll_url);
            String studClass=params[0];
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            //httpURLConnection.connect();
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data= URLEncoder.encode("studClass","UTF-8")+"="+ URLEncoder.encode(studClass,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
               json+=stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return json.trim();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
