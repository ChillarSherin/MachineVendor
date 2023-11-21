package com.chillarcards.machinevendor.DisplayAdapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.DatabaseHandler;
import com.chillarcards.machinevendor.ModelClass.Create_Leave;
import com.chillarcards.machinevendor.ModelClass.Reason;
import com.chillarcards.machinevendor.R;

/**
 * Created by test on 03-08-2016.
 */
public class ParentsMeetingAdapter extends  RecyclerView.Adapter<ParentsMeetingAdapter.ViewHolder> {

    private List<String> stdid = new ArrayList<>();
    private List<String> stdname = new ArrayList<>();
    private List<String> stddiv = new ArrayList<>();
    //    private List<String> stdimg = new ArrayList<>();
    private List<String> r_type = new ArrayList<>();
    private List<String> r_typeid = new ArrayList<>();

    String parentId,studId;
    public ArrayAdapter<String> adapter,adapter1;

    String username,password,m_code,cardserial;
    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos,userid;
    String parameters,r_id,spinner_text,reason;

    DatabaseHandler db;

    public ParentsMeetingAdapter(List<String> stdid, List<String> stdname, List<String> stddiv,String parentId, String user_name, String pass_word, String machineCode, String cardserial, int userid, Activity activity, int rowLayout, Context context) {
        this.stdid = stdid;
        this.stdname = stdname;
        this.stddiv = stddiv;
        this.parentId = parentId;
        this.username = user_name;
        this.password = pass_word;
        this.m_code = machineCode;
        this.cardserial = cardserial;
        this.userid = userid;


        this.activity = activity;
        this.rowLayout = R.layout.item_student_details;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }


    // setting user details

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.stdid.setText(stdid.get(position));
        holder.stdname.setText(stdname.get(position));
        holder.stddiv.setText(stddiv.get(position));
//        String img=stdimg.get(position);
//        System.out.println("ijaz  : image    "+img);

//        Picasso.with(mContext).load(img).into(holder.stud_img);

//        Uri imageUri = Uri.parse(stdimg.get(position));
//        holder.stud_img.setImageURI(imageUri);

        studId=stdid.get(position);





        System.out.println("CHILLAR:eldho " + stdid.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                holder.progbar.setVisibility(View.VISIBLE);
                download(v);

                holder.progbar.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public int getItemCount() {
        return stdid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView stdid,stdname,stddiv;
        final RelativeLayout layout;
        //        final ImageView stud_img;
        final ProgressBar progbar;
        public ViewHolder(View itemView) {
            super(itemView);

            stdid = (TextView) itemView.findViewById(R.id.stdntid);
            stdname = (TextView) itemView.findViewById(R.id.stdName);
            stddiv = (TextView) itemView.findViewById(R.id.stddiv);
            layout=(RelativeLayout)itemView.findViewById(R.id.layt_student);
//            stud_img=(ImageView) itemView.findViewById(R.id.stud_img);
            progbar=(ProgressBar) itemView.findViewById(R.id.prog);



        }
    }
    private void download(final View v) {
//        String  tag_string_req = "download student list";
//
//
//        String url=Constants.APP_URL+"r_reason_type.php?machineUserName="+username+"&machineUserPassword="+password+"&schoolMachineCode="+m_code;
//
//        System.out.println("Url Call_1 " + url);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                url, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String response) {
////				progressBar.setVisibility(View.GONE);
//                System.out.println("Url Call_1 " + response);
//
////                db.deleteAllStudentlist();
//                parseJSONPAymentType(response,v);
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("ErrorVolleyTrans " + error.getMessage());
//                Toast.makeText(mContext, "some error occurred"+error, Toast.LENGTH_LONG).show();
//
//            }
//        });
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//// Adding request to request queue
//
//        App.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
//    private void parseJSONPAymentType(String json, View v) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//            r_type.clear();
//            r_typeid.clear();
//
//            for (int i = 0; i < jsar.length(); i++) {
//
//                String s = jsar.getString(i);
//                JSONObject jsobj;
//                jsobj = new JSONObject(s);
//
//                String id=jsobj.getString("id");
//                String reason_type_name=jsobj.getString("reason_type_name");
//
////                String password=jsobj.getString("password");
//
//
//
////                System.out.println("CODMOB: schoolId "+paymentId+paymenttype);
//
//r_type.add(reason_type_name);
//r_typeid.add(id);
//
//
//
//            }
////          progressBar.setVisibility(View.GONE);
////            Toast.makeText(mContext, "Download Completed", Toast.LENGTH_SHORT).show();
//            call_fn(v);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }


        r_type.clear();
        r_typeid.clear();

        db = DatabaseHandler.getInstance(mContext);

        List <Reason> test10=db.getAllReason();

        for (Reason studentList1:test10){
            System.out.println("ParentSystem: Reason ReasonId: "+ studentList1.getReasonId()+" reasontype: "+studentList1.getReasonType());


            r_type.add(studentList1.getReasonType());
            r_typeid.add(studentList1.getReasonId());
        }

        call_fn(v);



    }
    private void call_fn(View v) {
        final Dialog dlg = new Dialog(v.getContext());
//                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setTitle("LEAVE REASON");
        dlg.setContentView(R.layout.popup_parents_meeting);
        dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        dlg.show();



//        final Spinner spinner_type= (Spinner) dlg.findViewById(spinner);
        final EditText et_reason= (EditText) dlg.findViewById(R.id.et_reason);
        final  Button submit= (Button)dlg.findViewById(R.id.btnok);
        final  Button cancel= (Button)dlg.findViewById(R.id.btncancel);


        r_id=db.getReasonId("Parents Meeting");

        System.out.println("Chillrrr: "+r_id);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reason=et_reason.getText().toString();

                reason();
                dlg.dismiss();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dlg.dismiss();
            }
        });










    }

    private void reason() {

        if (reason.length()==0)
        {

            Create_Leave create_leave=new Create_Leave(studId,parentId,r_id,"",getcurrentdatetime(),"");
            long add=db.addCreateLeave(create_leave);
            if(add!=-1) {

                List<Create_Leave> test1 = db.getAllCreateLeave();

                for (Create_Leave studentList1 : test1) {
                    System.out.println("ParentSystem: CreateLeave StdUserId: " + studentList1.getStud_userId() + " ParUserId: " + studentList1.getParent_userId()
                            + " reasonId " + studentList1.getReasonTypId() + " Reason " + studentList1.getReasonComment() + " TimeStamp " + studentList1.getTime_stamp() + " ServerTimeStamp " + studentList1.getServertimestamp());
                }

                Toast.makeText(mContext, "Success!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(mContext, "Error Writing to Database", Toast.LENGTH_SHORT).show();
            }

        }
        else{

            Create_Leave create_leave=new Create_Leave(studId,parentId,r_id,reason,getcurrentdatetime(),"");
            long add=db.addCreateLeave(create_leave);
            if(add!=-1) {

                List<Create_Leave> test1 = db.getAllCreateLeave();

                for (Create_Leave studentList1 : test1) {
                    System.out.println("ParentSystem: CreateLeave StdUserId: " + studentList1.getStud_userId() + " ParUserId: " + studentList1.getParent_userId()
                            + " reasonId " + studentList1.getReasonTypId() + " Reason " + studentList1.getReasonComment() + " TimeStamp " + studentList1.getTime_stamp() + " ServerTimeStamp " + studentList1.getServertimestamp());
                }
                Toast.makeText(mContext, "Success!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(mContext, "Error Writing to Database", Toast.LENGTH_SHORT).show();
            }


        }



//           String  tag_string_req = "send_otp";
//        String param;
//        param="machineCode="+m_code+"&user_id="+userid+"&cardSerial="+cardserial+"&reasonTypeID="+r_id+"&dateTime="+getcurrentdatetime();
//        if (reason.length()==0)
//        {
//
//        }
//        else
//        {
//            param=param+"&reason="+reason;
//        }
//            String url =Constants.APP_URL + "c_student_reason.php?"+param;
//        System.out.println("ParaSentOtp " + url);
//            StringRequest strReq = new StringRequest(Request.Method.GET,
//                    url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    String res = response;
//                    System.out.println("ResSentOtp " + res);
//                    try {
//                        JSONObject resUser = new JSONObject(response);
//                        System.out.println("ijaz  : resUser " + resUser);
//                        String ArrayStatus=resUser.getString("status");
//                        JSONObject ObStatus=new JSONObject(ArrayStatus);
//                        String Status=ObStatus.getString("code");
//                        System.out.println("StatusAvail " + Status);
//
//
//
//                        if(Status.equals("success")){
//                            Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
//
//
//                        }else if(Status.equals("error")){
//                            Toast.makeText(mContext, "Error , Try Again!!!", Toast.LENGTH_SHORT).show();
//
//                        }
//
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("PaymntException  "+e.toString());
//                        // Toast.makeText(getApplicationContext(), "some error occurred."+e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    System.out.println("ErrorVolleyTrans "+error.getMessage());
////                Toast.makeText(getApplicationContext(), "some error occurred"+error, Toast.LENGTH_SHORT).show();
//                }
//            });
//            strReq.setRetryPolicy(new DefaultRetryPolicy(
//                    10000,
//                    3,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//// Adding request to request queue
//            App.getInstance().addToRequestQueue(strReq, tag_string_req);





    }
    public String getcurrentdatetime() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }
}