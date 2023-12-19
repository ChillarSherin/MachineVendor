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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.DatabaseActivity;
import com.chillarcards.machinevendor.R;
import com.chillarcards.machinevendor.StoreActivity;
import com.chillarcards.machinevendor.StoreActivityMenu;

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
        if (desc.get(position).equals("STORE")){
            holder.buttonIcon.setImageResource(R.drawable.ic_shop);
          //  holder.button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_shop, 0, 0, 0);
        } else if (desc.get(position).equals("CANTEEN")) {
            holder.buttonIcon.setImageResource(R.drawable.ic_canteen);
        } else if (desc.get(position).equals("SNACKS BAR")) {
            holder.buttonIcon.setImageResource(R.drawable.ic_snacks);
        }else{
            holder.buttonIcon.setImageResource(R.drawable.ic_no);
            // holder.button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_no, 0, 0, 0);
        }
        //listing the ACTIVITIES and Tables

        holder.cardView.setOnClickListener(v -> {

            if (desc.get(position).equals("STORE") || desc.get(position).equals("CANTEEN") || desc.get(position).equals("SNACKS BAR")) {

                switch (desc.get(position)) {
                    case "STORE":
                    case "CANTEEN":
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

            }else if (desc.get(position).equals("DATABASE")) {

                Intent in5 = new Intent(mContext, DatabaseActivity.class);
                in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in5);
            }
            else {

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


        final TextView button;
        final ImageView buttonIcon;
        final CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            buttonIcon = itemView.findViewById(R.id.button_item_icon);
            button = itemView.findViewById(R.id.button_item);
            cardView = itemView.findViewById(R.id.order_frm);

        }
    }

}
