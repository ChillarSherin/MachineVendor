package com.chillarcards.machinevendor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.AttendenceNFC;
import com.chillarcards.machinevendor.ClassesDisplayActivity;
import com.chillarcards.machinevendor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 12-07-2016.
 */

//ATTENDENCE ADAPTER
public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    String paymntnameString;
    private List<String> id = new ArrayList<>();
    private List<String> typname = new ArrayList<>();


    public AttendenceAdapter(List<String> id, List<String> typname, Activity activity, int rowLayout, Context context) {
        this.id = id;
        this.typname = typname;
        this.activity = activity;
        this.rowLayout = R.layout.attendencelitsitem;
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

        holder.attendancetypnameBtn.setText(typname.get(position));
        holder.attndtypeidBtn.setText(id.get(position));

        paymntnameString = holder.attndtypeidBtn.getText().toString();

        //sending attendence type naem using type id
        holder.attendancetypnameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id.get(position).equals("1")) {


                    Intent mIntent = new Intent(mContext, ClassesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id", String.valueOf(id.get(position)));
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtras(b);
                    mContext.startActivity(mIntent);
                } else {

                    Intent mIntent = new Intent(mContext, AttendenceNFC.class);
                    Bundle b = new Bundle();
                    b.putString("id", String.valueOf(id.get(position)));
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtras(b);
                    mContext.startActivity(mIntent);

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return typname.size();
    }


    //define textviewes and recyclyerview
    public static class ViewHolder extends RecyclerView.ViewHolder {


        final TextView attndtypeidBtn, attendancetypnameBtn;
        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);


            attndtypeidBtn = (TextView) itemView.findViewById(R.id.buttonattndId);
            attendancetypnameBtn = (TextView) itemView.findViewById(R.id.buttonTypname);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.list2);

        }
    }

}






