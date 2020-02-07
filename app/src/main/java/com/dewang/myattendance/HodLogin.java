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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class HodLogin extends AppCompatActivity {

    Button btnHodLogin;
    EditText etHodPassword,etHodLogin;
    CheckBox cbremember;
    String login_name,login_pass;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_login);

        btnHodLogin = (Button) findViewById(R.id.btnHodLogin);
        etHodPassword = (EditText) findViewById(R.id.etHodPassword);
        etHodLogin = (EditText) findViewById(R.id.ethodUserName);
        cbremember= (CheckBox) findViewById(R.id.remember);
        sp = getSharedPreferences("hod", MODE_PRIVATE);

        //Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/alexbrush_regular.ttf");
        //tvhod.setTypeface(customFont);
        if (sp.getBoolean("ne", false) == false) {
            btnHodLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_name = etHodLogin.getText().toString();
                    login_pass = etHodPassword.getText().toString();
                    String result = "NoResultYet";
                    String name = "";
                    boolean connected=isConnectedToServer("http://192.168.43.132/myattendance/loginHod.php",1500);
                    if(connected==true) {
                        BackgroundTaskHod backgroundTask = new BackgroundTaskHod(HodLogin.this);
                        backgroundTask.execute(login_name, login_pass);
                        try {
                            result = backgroundTask.get();
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
                            Intent i = new Intent(getApplicationContext(), HodFeatures.class);
                            i.putExtra("name", name);
                            startActivity(i);
                            finish();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Unable to Connect with Server", Toast.LENGTH_LONG).show();
                }
            });

        }
        else
        {
            Intent i = new Intent(getApplicationContext(), HodFeatures.class);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {

        }
        return true;
    }
}
