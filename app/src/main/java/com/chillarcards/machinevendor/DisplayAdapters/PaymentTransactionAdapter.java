package com.chillarcards.machinevendor.DisplayAdapters;

/**
 * Created by Codmob on 02-08-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.LibraryActivity;
import com.chillarcards.machinevendor.Printer.PaymentBillPrinter;
import com.chillarcards.machinevendor.R;


/**
 * Created by Lester Oliver on 1/24/2015.
 */
public class PaymentTransactionAdapter extends RecyclerView.Adapter<PaymentTransactionAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> transid = new ArrayList<>();
    private List<String> id = new ArrayList<>();
    private List<String> amount = new ArrayList<>();
    private List<String> serverts = new ArrayList<>();


    public PaymentTransactionAdapter(List<String> id, List<String> transid, List<String> amount,
                                     List<String> serverts, Activity activity, int rowLayout, Context context) {
        this.transid = transid;
        this.id = id;
        this.amount = amount;
        this.serverts = serverts;
        this.activity = activity;
        this.rowLayout = R.layout.table_item_payment;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //setting payment transaction details

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.transid.setText(transid.get(position));
        holder.id.setText(id.get(position));
        holder.amount.setText(amount.get(position));
        holder.serverts.setText(serverts.get(position));


        System.out.println("CHILLAR:eldho " + transid.get(position));

        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(mContext, PaymentBillPrinter.class);
                Bundle b = new Bundle();
                b.putString("trans_id", transid.get(position));
                in.putExtras(b);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });


    }


    @Override
    public int getItemCount() {
        return transid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView transid, id, amount, serverts;
        final Button print;


        public ViewHolder(View itemView) {
            super(itemView);

            transid = (TextView) itemView.findViewById(R.id.transid);
            id = (TextView) itemView.findViewById(R.id.id);
            amount = (TextView) itemView.findViewById(R.id.amount);
            serverts = (TextView) itemView.findViewById(R.id.serverts);
            print = (Button) itemView.findViewById(R.id.print);


        }
    }

}