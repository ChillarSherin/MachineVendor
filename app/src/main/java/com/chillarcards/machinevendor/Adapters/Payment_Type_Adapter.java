package com.chillarcards.machinevendor.Adapters;

/**
 * Created by Codmob on 11-07-16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.AmountEnterActivity;
import com.chillarcards.machinevendor.BlockTransferActivity;
import com.chillarcards.machinevendor.BlockedcardsTransfer;
import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.MachineBlockTransfer;
import com.chillarcards.machinevendor.OnlineRechargeActivity;
import com.chillarcards.machinevendor.OnlineRechargeNewActivity;
import com.chillarcards.machinevendor.R;

/**
 * Created by test on 07-07-2016.
 */

public class Payment_Type_Adapter extends RecyclerView.Adapter<Payment_Type_Adapter.ViewHolder> {

    private List<String> idpay = new ArrayList<>();
    private List<String> namepay = new ArrayList<>();

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    String paymntnameString;


    public Payment_Type_Adapter(List<String> id, List<String> namepay, Activity activity,int rowLayout, Context context) {
        this.idpay = id;
        this.namepay = namepay;
        this.activity = activity;
        this.rowLayout = R.layout.paymenttype_listitem;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.paytypetextv.setText(namepay.get(position));
        holder.payidtxtv.setText(idpay.get(position));
        paymntnameString=holder.paytypetextv.getText().toString();


        holder.paytypetextv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(namepay.get(position).equals("CASH")){

                    String paymenttypename=namepay.get(position);


                    System.out.println("CHILLLLL: PAYID1"+Integer.parseInt(idpay.get(position)));

                    Intent mIntent = new Intent(mContext, AmountEnterActivity.class);
                    Bundle c2=new Bundle();
                    c2.putInt("paymenttypeId", Integer.parseInt(idpay.get(position)));
                    c2.putString("paytypename",namepay.get(position));
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtras(c2);
                    mContext.startActivity(mIntent);

                }else if(namepay.get(position).equals("CARD TRANSFER")){

                    Intent mIntent1 = new Intent(mContext, MachineBlockTransfer.class);
                    mIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(mIntent1);

                }else if(namepay.get(position).equals("ONLINE")){

                    Intent mIntent1 = new Intent(mContext, OnlineRechargeNewActivity.class);
                    Bundle c2=new Bundle();
                    c2.putInt("paymenttypeId", Integer.parseInt(idpay.get(position)));
                    c2.putString("paytypename",namepay.get(position));
                    mIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent1.putExtras(c2);
                    mContext.startActivity(mIntent1);
                }



            }
        });


    }




    @Override
    public int getItemCount() {
        return namepay.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView paytypetextv,payidtxtv;
        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            paytypetextv = (TextView) itemView.findViewById(R.id.pay_typetxtv);
            payidtxtv = (TextView) itemView.findViewById(R.id.payidtxtv);
            mRecyclerView = (RecyclerView)itemView.findViewById(R.id.list2);

        }
    }

}
