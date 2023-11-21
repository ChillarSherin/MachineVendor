package com.chillarcards.machinevendor.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.ClassesDisplayActivity;
import com.chillarcards.machinevendor.DatabaseHandler;
import com.chillarcards.machinevendor.R;

import java.util.ArrayList;

/**
 * Created by Codmob on 09-07-16.
 */
public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {

    private ArrayList<String> std = new ArrayList<String>();
    private ArrayList<String> div = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> total = new ArrayList<String>();
    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    String attend_typ;
    private AdapterView.OnItemClickListener mItemClickListener;

    DatabaseHandler db;

    String STD,DIV;
    int idPosition;



    public ClassListAdapter(ArrayList<String> std, ArrayList<String> div, String attend_typ, Activity activity, int rowLayout, Context context) {
        this.std = std;
//        this.id = id;
        this.div=div;
//        this.total=tot;
        this.attend_typ=attend_typ;
        this.activity = activity;
        this.rowLayout = R.layout.layout_class_list_item;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        holder.itemid.setText(id.get(position));
        idPosition = position;

        db = DatabaseHandler.getInstance(mContext);

//        STD=std.get(position);
//        DIV=div.get(position);

        System.out.println("Adapter: "+std.get(position) );
        holder.Std.setText(std.get(position));
        holder.Div.setText(div.get(position));
//        holder.total.setText(total.get(position));




//        System.out.println("CHILLAR:id "+id.get(position));
//        System.out.println("CHILLAR:quant "+quant.get(position));
//        System.out.println("CHILLAR:price "+pricc.get(position));






    }



    @Override
    public int getItemCount() {
        return std.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder   implements View.OnClickListener{

//        final TextView itemid;
        final TextView Std;
        final TextView Div;
//        final TextView total;

        RelativeLayout linearbackground;
        Button OkButton,NoButton;
//        Dialog PopupConfrm;



        public ImageView imgViewRemoveIcon;

        public ViewHolder(View itemView) {
            super(itemView);



            Std = (TextView) itemView.findViewById(R.id.std_Tv);
            Div = (TextView) itemView.findViewById(R.id.div_Tv);
//            pric = (TextView) itemView.findViewById(R.id.price);
//            total = (TextView) itemView.findViewById(R.id.total);
            linearbackground=(RelativeLayout)itemView.findViewById(R.id.layoutback);


            imgViewRemoveIcon= (ImageView) itemView.findViewById(R.id.imgremove);


            linearbackground.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.equals(linearbackground)){

                final Dialog dlg = new Dialog(v.getContext());
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dlg.setTitle("Are you sure to cancel the item?");
                dlg.setContentView(R.layout.alert_layout);
                dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dlg.setCancelable(true);
                dlg.setCanceledOnTouchOutside(true);
                dlg.show();

                OkButton= (Button) dlg.findViewById(R.id.btn_ok);
                NoButton= (Button) dlg.findViewById(R.id.btn_no);

//removing the item from the list
                OkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String class_div = std.get(idPosition)+div.get(idPosition);

                        String classDiv=class_div.replace(" ","");
                        System.out.println("Student "+"Class_Div111 "+ classDiv);

                        db.dltAllStudClassbyClsDiv(classDiv);

                        ClassesDisplayActivity.STUD_CLASS.remove(idPosition);
                        ClassesDisplayActivity.STUD_DIV.remove(idPosition);
                        notifyItemRemoved(idPosition);

                        notifyItemRangeChanged(idPosition,ClassesDisplayActivity.STUD_CLASS.size());



                        Intent intent=new Intent(mContext,ClassesDisplayActivity.class);
                        Bundle b=new Bundle();
                        b.putString("id",attend_typ);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtras(b);
                        mContext.startActivity(intent);
                        activity.finish();
                    }
                });

                NoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

//                removeAt(idPosition);

            }
        }

    }





}
