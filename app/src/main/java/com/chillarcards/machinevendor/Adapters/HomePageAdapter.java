package com.chillarcards.machinevendor.Adapters;


/**
 * Created by Codmob on 05-07-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.AdminSettingsActivity;
import com.chillarcards.machinevendor.AttendanceActivity;
import com.chillarcards.machinevendor.CardActivationActivity;
import com.chillarcards.machinevendor.CardInitializationMenu;
import com.chillarcards.machinevendor.CashOutActivity;
import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.DatabaseActivity;
import com.chillarcards.machinevendor.FeeActivity;
import com.chillarcards.machinevendor.GateEntryActivity;
import com.chillarcards.machinevendor.LibraryActivity;
import com.chillarcards.machinevendor.PaymentActivity;
import com.chillarcards.machinevendor.Payment_type_names;
import com.chillarcards.machinevendor.R;
import com.chillarcards.machinevendor.StoreActivity;
import com.chillarcards.machinevendor.StoreActivityMenu;
import com.chillarcards.machinevendor.TeacherAttendance;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lester Oliver on 1/24/2015.
 */
public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private List<String> date = new ArrayList<>();
    private List<String> desc = new ArrayList<>();
    private List<String> id = new ArrayList<>();
    private List<String> by = new ArrayList<>();
    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;


    public HomePageAdapter(List<String> id, List<String> desc, Activity activity, int rowLayout, Context context) {
        this.desc = desc;
        this.id = id;
        this.activity = activity;
        this.rowLayout = R.layout.list_item;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.button.setText(desc.get(position));
        System.out.println("CHILLAR:eldho " + desc.get(position));

        //listing the ACTIVITIES and Tables

        holder.button.setOnClickListener(v -> {

            if (desc.get(position).equals("STORE") || desc.get(position).equals("CANTEEN") || desc.get(position).equals("SNACKS BAR")) {

                switch (desc.get(position)) {
                    case "STORE":
                        Constants.Category = desc.get(position);
                        System.out.println("CHILLAR: NEw:" + Constants.Category);
                        break;
                    case "CANTEEN":
                        Constants.Category = desc.get(position);
                        System.out.println("CHILLAR: NEw:" + Constants.Category);
                        break;
                    case "SNACKS BAR":
                        Constants.Category = desc.get(position);
                        System.out.println("CHILLAR: NEw:" + Constants.Category);
                        break;
                }

                Intent i = new Intent(mContext, StoreActivity.class);
                Bundle b = new Bundle();
                b.putString("typeId", id.get(position));
                b.putString("typeName", desc.get(position));
                Log.d("abc_id", "onClickPos: " + position + " id: " + id.get(position) + " desc: " + desc.get(position));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);

            } else if (desc.get(position).equals("LIBRARY")) {
                Intent in = new Intent(mContext, LibraryActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            } else if (desc.get(position).equals("ATTENDANCE")) {
                Intent in1 = new Intent(mContext, AttendanceActivity.class);
                in1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in1);

            } else if (desc.get(position).equals("FEE")) {
                Intent in2 = new Intent(mContext, FeeActivity.class);
                in2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in2);

            } else if (desc.get(position).equals("RECHARGE")) {
                Intent in3 = new Intent(mContext, Payment_type_names.class);
                in3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in3);


            } else if (desc.get(position).equals("CARD INITIALISATION")) {
                Constants.adminFlag = 2;
                Intent in4 = new Intent(mContext, CardInitializationMenu.class);
                in4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in4);
            } else if (desc.get(position).equals("PAYMENT")) {
                Intent in5 = new Intent(mContext, PaymentActivity.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("typeId", id.get(position));
                b.putString("typeName", desc.get(position));
                in5.putExtras(b);
                mContext.startActivity(in5);
            } else if (desc.get(position).equals("DATABASE")) {

                Intent in5 = new Intent(mContext, DatabaseActivity.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in5);
            } else if (desc.get(position).equals("CARD ACTIVATION")) {

                Intent in5 = new Intent(mContext, CardActivationActivity.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in5);
            } else if (desc.get(position).equals("ADMIN SETTINGS")) {

                Intent in5 = new Intent(mContext, AdminSettingsActivity.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in5);
            } else if (desc.get(position).equals("BALANCE RETURN")) {

                Intent in5 = new Intent(mContext, CashOutActivity.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in5);
            } else if (desc.get(position).equals("TEACHER ATTENDANCE")) {

                Intent in5 = new Intent(mContext, TeacherAttendance.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in5);
            } else if (desc.get(position).equals("GATE ENTRY")) {

                Intent in5 = new Intent(mContext, GateEntryActivity.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in5);
            } else {

                System.out.println("EventsWallet: Category " + desc.get(position));
                Constants.Category = desc.get(position);
                Intent i = new Intent(mContext, StoreActivityMenu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }


        });

    }


    @Override
    public int getItemCount() {
        return desc.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        final Button button;

        public ViewHolder(View itemView) {
            super(itemView);


            button = (Button) itemView.findViewById(R.id.button_item);

        }
    }

}
