package com.chillarcards.machinevendor.Adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.R;

import java.util.ArrayList;

/**
 * Created by Codmob on 09-07-16.
 */
public class PreOrderListAdapter extends RecyclerView.Adapter<PreOrderListAdapter.ViewHolder> {

    private ArrayList<String> pricc = new ArrayList<String>();
    private ArrayList<String> quant = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> total = new ArrayList<String>();
    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private AdapterView.OnItemClickListener mItemClickListener;



    public PreOrderListAdapter(ArrayList<String> id, ArrayList<String> quanti,ArrayList<String> pricc,ArrayList<String> tot, Activity activity,int rowLayout, Context context) {
        this.quant = quanti;
        this.id = id;
        this.pricc=pricc;
        this.total=tot;
        this.activity = activity;
        this.rowLayout = R.layout.item_preorder_list;
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

        holder.itemid.setText(id.get(position));
        holder.pric.setText(pricc.get(position));
        holder.qty.setText(quant.get(position));
        holder.total.setText(total.get(position));



//        System.out.println("CHILLAR:id "+id.get(position));
//        System.out.println("CHILLAR:quant "+quant.get(position));
//        System.out.println("CHILLAR:price "+pricc.get(position));





    }



    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView itemid;
        final TextView qty;
        final TextView pric;
        final TextView total;
        LinearLayout linearbackground;
        Button OkButton,NoButton;
        Dialog PopupConfrm;



        public ImageView imgViewRemoveIcon;

        public ViewHolder(View itemView) {
            super(itemView);



            itemid = (TextView) itemView.findViewById(R.id.itemid);
            qty = (TextView) itemView.findViewById(R.id.quantity);
            pric = (TextView) itemView.findViewById(R.id.price);
            total = (TextView) itemView.findViewById(R.id.total);
            linearbackground=(LinearLayout)itemView.findViewById(R.id.layoutback);







        }


    }


    public void setOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }




}
