package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;



import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;

/**
 * Created by Codmob on 27-09-17.
 */

public class BlockCardsDownloadActivity extends Activity {

    ProgressBar progressBar;
    String User_name,Pass_word,userID,schooid,schoomachcode,machineserial,machineID,machineSL;
    int useridinteger;
    String versionName="";

    int statusCount=0;
    int totalCount=0;

    DatabaseHandler db ;
    ArrayList<Blocked_Cards_Info> blkCardsArray=new ArrayList<Blocked_Cards_Info>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blk_cards_dwnld);

        db = DatabaseHandler.getInstance(getApplicationContext());

        intialiseUIElements();
        sharedPrefs();

        BlockedCards1();
    }



    private void sharedPrefs() {

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userID= String.valueOf(editor.getInt("userid",0));
        schooid = editor.getString("schoolId", "");
        schoomachcode= editor.getString("schlMachCode", "");
        machineserial= editor.getString("machineserial", "");
        machineID=editor.getString("machineid","");
        machineSL=editor.getString("machineSL","");
        versionName = editor.getString("appVersion", "");

        useridinteger= Integer.parseInt(userID);

        System.out.println("CODMOB: sharedPrefs(): "+schooid+schoomachcode+machineserial);

    }

    private void intialiseUIElements() {

        progressBar= (ProgressBar) findViewById(R.id.progbar);

    }

    private void BlockedCards1() {

        try {

            String tag_string_req = "list_announce";

            String lastUpdated = db.getLastBlockCardsupdate();

            String url="";

            if(lastUpdated.equals("")){

                url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20");

            }else{

                url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20")+"&blockFromDate="+lastUpdated.replace(" ","%20");

            }

            System.out.println("UrlNotice " + url);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    System.out.println("CODMOB:Addmessages " + response);

                    parseJSONBlockedcards1(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private void parseJSONBlockedcards1(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);


//            SQLiteDatabase dh=db.getWritableDatabase();

            try {

//                dh.beginTransaction();

                totalCount=jsar.length();
                blkCardsArray.clear();

                progressBar.setVisibility(View.VISIBLE);

                for (int i = 0; i < jsar.length(); i++) {

                    String s = jsar.getString(i);
                    JSONObject jsobj;
                    jsobj = new JSONObject(s);


                    int blockedcardsId = jsobj.getInt("blocked_cards_id");
                    String cardSerial = jsobj.getString("card_serial");


                    System.out.println("CODMOB: serial " + blockedcardsId + cardSerial);


                    try {


                        Blocked_Cards_Info blockcards = new Blocked_Cards_Info(blockedcardsId, cardSerial);



                        blkCardsArray.add(blockcards);


                    } catch (Exception e) // no guruji
                    {

                        e.printStackTrace();

                    }
                }

                int status= db.addBlockCardInfo(blkCardsArray);

                if(status==0){

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Download Finished.", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Blocked Cards not fully updated.", Toast.LENGTH_SHORT).show();
                    finish();
                }

//                dh.setTransactionSuccessful();

            }finally {


            }





        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
