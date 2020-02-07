package com.dewang.myattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class ProfLogin extends AppCompatActivity {

    Button btnProfLogin;
    EditText etProfPassword,etProfLogin;
    CheckBox cbremember;
    TextView tvProf;
    String login_name,login_pass;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_login);

        btnProfLogin = (Button) findViewById(R.id.btnProfLogin);
        etProfPassword = (EditText) findViewById(R.id.etProfPassword);
        etProfLogin = (EditText) findViewById(R.id.etProfUserName);
        sp = getSharedPreferences("prof", MODE_PRIVATE);
        cbremember= (CheckBox) findViewById(R.id.remember);

        //Typeface customFont=Typeface.createFromAsset(getAssets(),"fonts/alexbrush_regular.ttf");
        //tvProf.setTypeface(customFont);

        if (sp.getBoolean("ne", false) == false) {
            btnProfLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_name = etProfLogin.getText().toString();
                    login_pass = etProfPassword.getText().toString();
                    String result = "NoResultYet";
                    String name = "";
                    boolean connected = isConnectedToServer("http://192.168.43.132/myattendance/loginProf.php", 1500);
                    if (connected == true) {
                        BackgroundTaskProf backgroundTaskProf = new BackgroundTaskProf(ProfLogin.this);
                        backgroundTaskProf.execute(login_name, login_pass);
                        try {
                            result = backgroundTaskProf.get();
                            name = result.substring(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences.Editor et = sp.edit();

                        if (result.contains("true")) {
                            if(cbremember.isChecked())
                            {
                                et.putString("name", name+"Checked");
                                et.putBoolean("ne", true);
                                et.commit();
                            }
                            else
                            {
                                et.putString("name", name);
                                et.putBoolean("ne", true);
                                et.commit();
                            }
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ProfessorFeatures.class);
                            i.putExtra("name", name);
                            startActivity(i);
                            finish();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "Unable to Connect with server", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Intent i = new Intent(getApplicationContext(),ProfessorFeatures.class);
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