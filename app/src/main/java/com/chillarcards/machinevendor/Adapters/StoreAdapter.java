package com.chillarcards.machinevendor.Adapters;


/**
 * Created by Codmob on 07-07-16.
 */

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.R;
import com.chillarcards.machinevendor.StoreActivtySubMenu;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lester Oliver on 1/24/2015.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {


    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;

    private List<String> desc = new ArrayList<>();
    private List<Integer> descId = new ArrayList<>();

    private String typeId,typeName;



    public StoreAdapter(String typeId, String typeName, List<String> category, List<Integer> categoryID, Activity activity, int list_store_category_item, Context applicationContext) {

        this.desc = category;
        this.descId = categoryID;
        this.typeId = typeId;
        this.typeName = typeName;

        this.activity = activity;
        this.rowLayout = R.layout.list_store_category_item;
        this.mContext = applicationContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.desc.setText(desc.get(position));

        System.out.println("CHILLAR:desc" + desc.get(position));
        System.out.println("CHILLAR:descId" + descId.get(position));
        System.out.println("CHILLAR:size" + getItemCount());


        holder.storeLinearlayout.setOnClickListener(v -> {

            SharedPreferences.Editor editor1 = mContext.getSharedPreferences("Chillar", MODE_PRIVATE).edit();
            editor1.putString("ITEMID", String.valueOf(descId.get(position)));
            editor1.putString("ITEM", String.valueOf(desc.get(position)));
            editor1.commit();

            Intent i = new Intent(mContext.getApplicationContext(), StoreActivtySubMenu.class);
            Bundle b = new Bundle();
            b.putString("ITEM", desc.get(position));
            b.putInt("ITEMID", descId.get(position));
            b.putString("typeId", typeId);
            b.putString("typeName", typeName);
            i.putExtras(b);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);


        });

    }


    @Override
    public int getItemCount() {
        return desc.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView desc;
        LinearLayout storeLinearlayout;

        public ViewHolder(View itemView) {
            super(itemView);

            desc = (TextView) itemView.findViewById(R.id.text1);
            storeLinearlayout = (LinearLayout) itemView.findViewById(R.id.layoutliststore);
        }
    }

}
