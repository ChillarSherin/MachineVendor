package com.chillarcards.machinevendor.Adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.chillarcards.machinevendor.CheckoutActivity;
import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.R;

/**
 * Created by Codmob on 09-07-16.
 */
public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    private String typeId,typeName;
    private ArrayList<String> pricc = new ArrayList<String>();
    private ArrayList<String> quant = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> total = new ArrayList<String>();


    public CheckoutAdapter(String typeId, String typeName,ArrayList<String> id, ArrayList<String> quanti, ArrayList<String> pricc, ArrayList<String> tot, Activity activity, int rowLayout, Context context) {
        this.quant = quanti;
        this.id = id;
        this.pricc = pricc;
        this.typeId = typeId;
        this.typeName = typeName;
        this.total = tot;
        this.activity = activity;
        this.rowLayout = R.layout.checkout_list_item;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.itemid.setText(id.get(position));
        holder.pric.setText(pricc.get(position));
        holder.qty.setText(quant.get(position));
        holder.total.setText(total.get(position));


    }


    @Override
    public int getItemCount() {
        return id.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView itemid;
        final TextView qty;
        final TextView pric;
        final TextView total;
        ImageView imgViewRemoveIcon;
        LinearLayout linearbackground;
        Button OkButton, NoButton;


        public ViewHolder(View itemView) {
            super(itemView);
            itemid = (TextView) itemView.findViewById(R.id.itemid);
            qty = (TextView) itemView.findViewById(R.id.quantity);
            pric = (TextView) itemView.findViewById(R.id.price);
            total = (TextView) itemView.findViewById(R.id.total);
            linearbackground = (LinearLayout) itemView.findViewById(R.id.layoutback);
            imgViewRemoveIcon = (ImageView) itemView.findViewById(R.id.imgremove);
            linearbackground.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (v.equals(linearbackground)) {

                final Dialog dlg = new Dialog(v.getContext());
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dlg.setContentView(R.layout.alert_layout);
                dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dlg.setCancelable(true);
                dlg.setCanceledOnTouchOutside(true);
                dlg.show();

                OkButton = (Button) dlg.findViewById(R.id.btn_ok);
                NoButton = (Button) dlg.findViewById(R.id.btn_no);

                OkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Constants.sales_items.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());

                        notifyItemRangeChanged(getAdapterPosition(), Constants.sales_items.size());

                        Intent intent = new Intent(mContext, CheckoutActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b=new Bundle();
                        b.putString("typeId",typeId);
                        b.putString("typeName",typeName);
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

            }
        }
    }


}
