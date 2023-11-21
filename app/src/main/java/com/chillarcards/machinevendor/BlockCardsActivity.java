package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Device_Info;


/**
 * Created by Codmob on 25-09-17.
 */

public class BlockCardsActivity extends Activity {

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    Button button;
    EditText editText;
    TextView textView1,textView2,textView3;

    String schoolname,schoolid,cardnumbers;
    DatabaseHandler db ;

    
    int ret_poweron;
    int ret_fieldon;

    int tardtc;
    int tama_on;
    ProgressBar progressBar;

    Dialog dlg;
    String studentCode="";



    String User_name,Pass_word;
    int userid;
    int useridinteger;
    String schooid,schoomachcode,machineserial;

    @Override
    protected void onCreate(Bundle savedInstanceState)  throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_block_cards);

        db = DatabaseHandler.getInstance(getApplicationContext());

        dlg=new Dialog(this);
        drawerArrow();
        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        button= (Button) findViewById(R.id.writebtn);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid= editor.getInt("userid",0);
        schooid = editor.getString("schoolId", "");
        schoomachcode= editor.getString("schlMachCode", "");
        machineserial= editor.getString("machineserial", "");

        editText= (EditText) findViewById(R.id.cardserial);
        textView1= (TextView) findViewById(R.id.schoolid);
        textView2= (TextView) findViewById(R.id.cardnumber);
        textView3=(TextView)findViewById(R.id.schoolname);

        List<Device_Info> device_infos = db.getAlldevInfo();
//
        for (Device_Info us : device_infos) {
            String test = "machineid: " + us.getMachine_id() + " serialno : " + us.getSerial_no() + " school : " + us.getSchool_id() +
                    " mainurl : " + us.getMain_server_url() + " mainupload : " + us.getMain_upload_path();
            System.out.println("Device Info: " + test);

            schoolname=us.getSchoolname();
            schoolid=us.getSchool_id();
            cardnumbers=us.getSerial_no();

        }

        textView1.setText(schoolid);
        textView2.setText(cardnumbers);
        textView3.setText(schoolname);

//INITIALIZING NEW CARD

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String serialno= String.valueOf(editText.getText());

                if(serialno.length()<6||serialno.length()>6){

                    Toast.makeText(getApplicationContext(),"6 digits only!",Toast.LENGTH_SHORT).show();

                }else{
                    final String blk_cardSl=cardnumbers+serialno;

                    dlg.setContentView(R.layout.popup_confirm);
                    dlg.setTitle("Block This Card?");
                    dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button OkButton = (Button) dlg.findViewById(R.id.btnok);
                    Button NoButton = (Button) dlg.findViewById(R.id.btnno);
                    TextView name= (TextView) dlg.findViewById(R.id.name);


                    name.setText(blk_cardSl);


                    try{

                        dlg.show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    OkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.cancel();

                            ArrayList<Blocked_Cards_Info> blocked_cards_infos =new ArrayList<Blocked_Cards_Info>();

                            blocked_cards_infos.clear();

                            Blocked_Cards_Info blockcards = new Blocked_Cards_Info(0, blk_cardSl);

                            blocked_cards_infos.add(blockcards);
                            int status=db.addBlockCardInfo(blocked_cards_infos);
                            if(status==0) {

                                Toast.makeText(getApplicationContext(), "Card Blocked.", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getApplicationContext(),"Failed to block card.",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                    NoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dlg.cancel();


                        }
                    });







                }




            }
        });


    }



    public void drawerArrow() {
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.white));
        imageView.setImageDrawable(drawerArrowDrawable);

        offset = 0;
        flipped = false;
        drawerArrowDrawable.setFlip(flipped);
        drawerArrowDrawable.setParameter(offset);

        offset = 1;
        flipped = true;
        drawerArrowDrawable.setFlip(flipped);
        drawerArrowDrawable.setParameter(offset);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();

            }
        });
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}
