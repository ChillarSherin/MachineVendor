package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Codmob on 21-10-16.
 */
public class RecoveryActivity extends Activity {

    EditText text_pass;
    Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recovery_activity);

        btn_confirm= (Button) findViewById(R.id.btn_recovery);
        text_pass= (EditText) findViewById(R.id.pass_recovery);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_pass.getText().toString().equals("chillaradmin")){
                    Intent i=new Intent(getApplicationContext(),AllTablesDisplay.class);
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(),"Wrong Password!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
