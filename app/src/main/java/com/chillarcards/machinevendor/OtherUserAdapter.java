package com.chillarcards.machinevendor;

/**
 * Created by Codmob on 15-05-18.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;


//import com.chillarcards.eventswallet.R;


public class OtherUserAdapter extends RecyclerView.Adapter<OtherUserAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    String machineCode, userId;
    DatabaseHandler db;
    private ArrayList<String> idList = new ArrayList<String>();
    private ArrayList<String> typeList = new ArrayList<String>();
    private int FINGERID_1 = 1;
    private int FINGERID_2 = 2;
    private int MATCH_FINGER_1 = 3;
    private int MATCH_FINGER_2 = 4;


    public OtherUserAdapter(String machineCode, String userId, ArrayList<String> idList, ArrayList<String> typeList, Activity activity, int rowLayout, Context context) {
        this.idList = idList;
        this.typeList = typeList;
        this.userId = userId;
        this.machineCode = machineCode;
        this.typeList = typeList;
        this.typeList = typeList;
        this.activity = activity;
        this.rowLayout = R.layout.other_user_types;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        db = DatabaseHandler.getInstance(mContext);

        holder.userType.setText(typeList.get(position));


    }


    @Override
    public int getItemCount() {
        return idList.size();
    }

    private void GetDetailsPHP(final Context context, final ProgressBar progressBar, String phNum, String id) {

        String tag_string_req = "getdetails";

        final String[] cardSerial = new String[1];

        String URL = Constants.APP_URL + "r_user_info.php?machineCode=" + machineCode + "&user_id=" + userId + "&userTypeID=" + id + "&userPhone=" + phNum;
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URL Blocked " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String studentName = jsonObject.getString("name");
                    final String studentID = jsonObject.getString("userID");
                    int isInitialised = jsonObject.getInt("isInitialised");
                    cardSerial[0] = jsonObject.getString("cardSerial");

                    final Dialog dlg = new Dialog(context);

                    dlg.setContentView(R.layout.popup_student_details);
                    dlg.setTitle("Student Details");
                    dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button OkButton = (Button) dlg.findViewById(R.id.btnok);
                    Button NoButton = (Button) dlg.findViewById(R.id.btnno);
                    TextView name = (TextView) dlg.findViewById(R.id.name);
                    TextView std = (TextView) dlg.findViewById(R.id.std);
                    name.setText(studentName);
                    try {

                        dlg.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    OkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.cancel();

                            progressBar.setVisibility(View.VISIBLE);

                            if (cardSerial[0] != null) {

                                if (!cardSerial[0].equals("")) {

                                    initiatePHP(cardSerial[0], progressBar);

                                } else {


                                }
                            } else {


                            }
                            /*ConfirmPHP(studentID);*/


                        }
                    });


                    NoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dlg.cancel();

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("status");
                        String code = jsonObject1.getString("code");
                        if (code.equals("error")) ;
                        {
                            String message = jsonObject1.getString("message");
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(mContext, "Network not available", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void initiatePHP(final String cardserial, final ProgressBar progressBar) {

        String tag_string_req = "initiatePHP";

        String URL = Constants.APP_URL + "c_initiate_other_user_card_transfer.php?machineCode=" + machineCode + "&user_id=" + userId + "&cardSerial=" + cardserial;

        System.out.println("URL  " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT initiatePHP ", response.toString());
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());


                try {


                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("status");

                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");

                    if (code.equals("success")) {

                        if (message.equals("Successfully initiated card transfer")) {


                            if (!cardserial.equals("")) {

                                ArrayList<Blocked_Cards_Info> blocked_cards_infos = new ArrayList<Blocked_Cards_Info>();

                                blocked_cards_infos.clear();

                                Blocked_Cards_Info blockcards = new Blocked_Cards_Info(0, cardserial);

                                blocked_cards_infos.add(blockcards);
                                int status = db.addBlockCardInfo(blocked_cards_infos);

                                if (status == 0) {

                                    Toast.makeText(mContext, "Successfully initiated card transfer", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(mContext, "Failed to write to db", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(mContext, "ERROR- Userserial not Attached!", Toast.LENGTH_SHORT).show();
                            }

                        } else if (message.equals("Waiting for updating all the machines")) {

                            Toast.makeText(mContext, "Waiting for updating all the machines to be server updated.", Toast.LENGTH_SHORT).show();


                            JSONArray pendingMachines = jsonObject.getJSONArray("pending_machines");

                            String machines = "";

                            for (int i = 0; i < pendingMachines.length(); ++i) {

                                JSONObject jsonObject2 = pendingMachines.getJSONObject(i);
                                String MachineCode = jsonObject2.getString("MachineCode");
                                String machineSerial = jsonObject2.getString("machineSerial");

//                                Toast.makeText(MachineBlockTransfer.this, MachineCode+" - "+machineSerial, Toast.LENGTH_SHORT).show();

                                machines = machines + MachineCode + " - " + machineSerial + "\n";

                            }

                            alertbox1(machines);

                        } else if (message.equals("Ready for card transfer")) {

                            ConfirmPHP(cardserial, progressBar);

                        }

                    } else {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("status");
                        String code = jsonObject1.getString("code");
                        if (code.equals("error")) ;
                        {
                            String message = jsonObject1.getString("message");
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(mContext, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();

            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    private void ConfirmPHP(String cardserial, final ProgressBar progressBar) {


        String tag_string_req = "confirmphp";


        String URL = Constants.APP_URL + "c_block_card_transfer.php?machineCode=" + machineCode + "&user_id=" + userId + "&cardSerial=" + cardserial;
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URL Blocked " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String amt = jsonObject.getString("amount");
                    String new_card = jsonObject.getString("new_cardSerial");

                    Intent i = new Intent(mContext, MachineBlkTrnsferNFC.class);
                    Bundle b = new Bundle();
                    b.putString("amount", amt);
                    b.putString("cardsl", new_card);
                    b.putString("stdCode", "");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtras(b);
                    mContext.startActivity(i);


                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("status");
                        String code = jsonObject1.getString("code");
                        if (code.equals("error")) ;
                        {
                            String message = jsonObject1.getString("message");

                            if (message.equals("Please update all transactions")) {
                                Toast.makeText(mContext, "No success transactions found for this student. Please update the database and then try.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(mContext, "Some error occurred . please try again", Toast.LENGTH_SHORT).show();

            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    public void alertbox1(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                activity);
        alert.setTitle("Machines to be updated : ");

        alert.setCancelable(true);
        alert.setMessage(msg);

        DialogInterface.OnClickListener listener;
        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        dialog.dismiss();


                    }
                });

        alert.show();
    }

    public void alertbox2(String msg, Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                context);
        alert.setTitle("ALERT!");

        alert.setCancelable(true);
        alert.setMessage(msg);

        DialogInterface.OnClickListener listener;
        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        dialog.dismiss();


                    }
                });
        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        dialog.dismiss();


                    }
                });

        alert.show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView userType;
        public ImageView imgViewRemoveIcon;
        LinearLayout linearbackground;
        Button OkButton;
        EditText phNoTxt;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);


            userType = (TextView) itemView.findViewById(R.id.userTypeTxt);

            linearbackground = (LinearLayout) itemView.findViewById(R.id.layoutback);


            imgViewRemoveIcon = (ImageView) itemView.findViewById(R.id.imgremove);


            linearbackground.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.equals(linearbackground)) {

                final Context context = v.getContext();
                final Dialog dlg = new Dialog(context);
//                dlg.requestWindowFeature(Window.);
                dlg.setTitle("Enter Phone Number");
                dlg.setContentView(R.layout.phone_number_enter);
                dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dlg.setCancelable(true);
                dlg.setCanceledOnTouchOutside(true);
                dlg.show();

                OkButton = (Button) dlg.findViewById(R.id.btn_Submit);
                phNoTxt = (EditText) dlg.findViewById(R.id.et_number);
                progressBar = (ProgressBar) dlg.findViewById(R.id.progbar);

                //removing the item from the list
                OkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dlg.dismiss();
                        String phNum = phNoTxt.getText().toString();
                        System.out.println("BiomEtric: " + phNum);

                        String id = idList.get(getAdapterPosition());

                        progressBar.setVisibility(View.VISIBLE);

                        GetDetailsPHP(context, progressBar, phNum, id);

                    }
                });


            }
        }

    }

}
