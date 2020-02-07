package com.dewang.myattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class StudentLogin extends AppCompatActivity {

    EditText etStudRegNo,etStudId;
    Button btnStudLogin;
    CheckBox cbremember;
    String login_name,login_pass;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        btnStudLogin= (Button) findViewById(R.id.btnStudlogin);
        etStudRegNo= (EditText) findViewById(R.id.etStudRegNo);
        etStudId= (EditText) findViewById(R.id.etStudId);
        cbremember= (CheckBox) findViewById(R.id.remember);
        //tvStudent= (TextView) findViewById(R.id.tvStudent);
        sp = getSharedPreferences("student", MODE_PRIVATE);

        if (sp.getBoolean("ne", false) == false) {
            btnStudLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_name = etStudId.getText().toString();
                    login_pass = etStudRegNo.getText().toString();
                    String result = "NoResultYet";
                    String name = "";
                    boolean connected=isConnectedToServer("http://192.168.43.132/myattendance/loginStud.php",1500);
                    if(connected==true) {
                        BackgroundTaskStud backgroundTaskStud = new BackgroundTaskStud(StudentLogin.this);
                        backgroundTaskStud.execute(login_name, login_pass);
                        try {
                            result = backgroundTaskStud.get();
                            name = result.substring(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences.Editor et = sp.edit();
                        et.commit();
                        if (result.contains("true")) {
                            if(cbremember.isChecked())
                            {
                                et.putString("stud_id", login_name.toUpperCase()+"Checked");
                                et.putBoolean("ne", true);
                                et.commit();
                            }
                            else
                            {
                                et.putString("stud_id", login_name.toUpperCase());
                                et.putBoolean("ne", true);
                                et.commit();
                            }
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), StudentProfile.class);
                            //i.putExtra("loginID", login_name.toUpperCase());
                            startActivity(i);
                            finish();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Unable to Connect with server", Toast.LENGTH_LONG).show();
                }
            });

        }
        else
        {
            Intent i = new Intent(getApplicationContext(),StudentProfile.class);
            startActivity(i);
            finish();
        }
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
}