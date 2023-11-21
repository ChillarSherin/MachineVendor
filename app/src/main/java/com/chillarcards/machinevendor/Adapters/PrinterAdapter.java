package com.chillarcards.machinevendor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 20-07-2016.
 */

public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    String paymntnameString;
    private List<String> item = new ArrayList<>();
    private List<String> qtity = new ArrayList<>();
    private List<String> peramount = new ArrayList<>();


    public PrinterAdapter(List<String> item, List<String> qtity, List<String> peramount, Activity activity, int rowLayout, Context context) {
        this.item = item;
        this.qtity = qtity;
        this.peramount = peramount;
        this.activity = activity;
        this.rowLayout = R.layout.printeradapter;
        this.mContext = context;


//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.itemtxview.setText(item.get(position));
        holder.quantityTextView.setText(qtity.get(position));
        holder.perTextView.setText(peramount.get(position));


    }


    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView itemtxview, quantityTextView, perTextView;

        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);


            itemtxview = (TextView) itemView.findViewById(R.id.itemtxtv);
            quantityTextView = (TextView) itemView.findViewById(R.id.qtytxtv);
            perTextView = (TextView) itemView.findViewById(R.id.peramnttxtv);

            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.list2);

        }
    }

}

