package com.chillarcards.machinevendor.DisplayAdapters;

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

import com.chillarcards.machinevendor.Printer.PaymentBillPrinter;
import com.chillarcards.machinevendor.Printer.SaleItemPrinter;
import com.chillarcards.machinevendor.R;

/**
 * Created by test on 02-08-2016.
 */
public class ItemsaleAdapter extends RecyclerView.Adapter<ItemsaleAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> Saletrid = new ArrayList<>();
    private List<String> tranID = new ArrayList<>();
    private List<String> Billno = new ArrayList<>();
    private List<String> Totalamount = new ArrayList<>();
    private List<String> servrTS = new ArrayList<>();

    public ItemsaleAdapter(List<String> Saletrid, List<String> tranID, List<String> Billno, List<String> Totalamount, List<String> Serverts,
                           Activity activity, int rowLayout, Context context) {
        this.Saletrid = Saletrid;
        this.tranID = tranID;
        this.Billno = Billno;
        this.Totalamount = Totalamount;
        this.servrTS = Serverts;

        this.activity = activity;
        this.rowLayout = R.layout.table_item_itemsale;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);

    }

    //setting item sale details
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.SALESTARNSID.setText(Saletrid.get(position));
        holder.TRANSID.setText(tranID.get(position));
        holder.BILLNO.setText(Billno.get(position));
        holder.TOTAMOUNT.setText(Totalamount.get(position));
        holder.ServerTS.setText(servrTS.get(position));


        System.out.println("CHILLAR:eldho " + Saletrid.get(position));

        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, SaleItemPrinter.class);
                Bundle b = new Bundle();
                b.putString("trans_id", tranID.get(position));
                in.putExtras(b);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);
            }
        });

    }


    @Override
    public int getItemCount() {
        return Saletrid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView SALESTARNSID, TRANSID, BILLNO, TOTAMOUNT, ServerTS;

        final Button print;

        public ViewHolder(View itemView) {
            super(itemView);

            SALESTARNSID = (TextView) itemView.findViewById(R.id.saletransID);
            TRANSID = (TextView) itemView.findViewById(R.id.transid);
            BILLNO = (TextView) itemView.findViewById(R.id.billNo);
            TOTAMOUNT = (TextView) itemView.findViewById(R.id.TotalAMount);
            ServerTS = (TextView) itemView.findViewById(R.id.Servertimestamp);

            print = (Button) itemView.findViewById(R.id.print);


        }
    }


}
