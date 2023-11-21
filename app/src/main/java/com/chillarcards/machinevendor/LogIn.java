package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by test on 19-07-2016.
 */

public class LogIn extends Activity {

    EditText urname, passwrd, machineid, machSerial;
    TextView loginHeading;
    Button login;
    String userString, passString, machID, machSl;
    DatabaseHandler db;
    String registerFlag, machFlag;
    LinearLayout recovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);

        db = DatabaseHandler.getInstance(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences("Chillar", MODE_PRIVATE);
        registerFlag = prefs.getString("initialised", "");
        machFlag = prefs.getString("machineflag", "");

        urname = findViewById(R.id.usernameedt);
        passwrd = findViewById(R.id.passwrdedt);
        machineid = findViewById(R.id.machineid);
        machSerial = findViewById(R.id.machineSerial);
        loginHeading = findViewById(R.id.login_head);
        login =findViewById(R.id.loginbtn);
        recovery = findViewById(R.id.recovery);

        recovery.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RecoveryActivity.class);
            startActivity(i);
        });

        if (Constants.machineID.length() > 0 || machFlag.equals("true")) {
            machineid.setVisibility(View.GONE);
            machSerial.setVisibility(View.GONE);
            loginHeading.setText("Sign In");
            login.setText("Continue");
        }

        login.setOnClickListener(v -> {

            userString = urname.getText().toString().trim();
            passString = passwrd.getText().toString().trim();

            if (machFlag.equals("true")) {
                Checkdb2();
            }
            else {
                machID = machineid.getText().toString().trim();
                machSl = machSerial.getText().toString().trim();

                if (userString.equals("") || passString.equals("") || machID.equals("") || machSl.equals("")) {

                    Toast.makeText(LogIn.this, "Please fill all the fields...", Toast.LENGTH_SHORT).show();

                } else {
                    Checkdb();
                }
            }
        });


    }

    private void Checkdb2() {


        String storedPassword = db.getSinlgeEntry(userString);

        // Check if the Stored password matches with Password entered by user
        if (passString.equals(storedPassword)) {

            Toast.makeText(LogIn.this, "Login Successfully", Toast.LENGTH_LONG).show();
            int userId = Integer.parseInt(db.getUIDbyName(userString));

            SharedPreferences.Editor editor = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
            editor.putString("username", userString);
            editor.putString("password", passString);
            editor.putInt("userid", userId);
            editor.putString("initialised", "true");
            editor.remove("logout");
            editor.commit();

            Intent ine = new Intent(getApplicationContext(), SecondActivity.class);
            ine.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ine);
            finish();

            System.out.println("LoginActivity: " + userId);

        }
        else if (passString.equals("exception")) {
            Toast.makeText(LogIn.this, "Some Error. Please Try Again.", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("Login Failed!!");
            Toast.makeText(LogIn.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
        }


    }

    private void Checkdb() {

        SharedPreferences.Editor editor = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
        editor.putString("machineid", machineid.getText().toString());
        editor.putString("machineSL", machSerial.getText().toString());
        editor.commit();


        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("usernamebundle", urname.getText().toString());
        bundle.putString("passwordbundle", passwrd.getText().toString());
        bundle.putString("machinID", machineid.getText().toString());
        bundle.putString("machinSL", machSerial.getText().toString());
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtras(bundle);
        startActivity(in);


    }

    @Override
    public void onBackPressed() {
        return;
    }
}
