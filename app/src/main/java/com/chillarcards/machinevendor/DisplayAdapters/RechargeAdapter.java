package com.chillarcards.machinevendor.DisplayAdapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.R;

/**
 * Created by test on 02-08-2016.
 */
public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> reachid = new ArrayList<>();
    private List<String> tranID = new ArrayList<>();
    private List<String> reachAmount = new ArrayList<>();
    private List<String> reachTime = new ArrayList<>();
    private List<String> paytypeId = new ArrayList<>();
    private List<String> servrTS = new ArrayList<>();

    public RechargeAdapter(List<String> reachid, List<String> tranID, List<String> reachAmount, List<String> reachTime,
                           List<String> paytypeId, List<String> servrTS, Activity activity, int rowLayout, Context context) {
        this.reachid = reachid;
        this.tranID = tranID;
        this.reachAmount = reachAmount;
        this.reachTime = reachTime;
        this.paytypeId = paytypeId;
        this.servrTS = servrTS;

        this.activity = activity;
        this.rowLayout = R.layout.table_item_recharge;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }


    //setting all recharge type details
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.REACHID.setText(reachid.get(position));
        holder.TRANSID.setText(tranID.get(position));
        holder.REACHAMOUNT.setText(reachAmount.get(position));
        holder.REACHTIME.setText(reachTime.get(position));
        holder.PAYTYPID.setText(paytypeId.get(position));
        holder.SERVERTS.setText(servrTS.get(position));

        System.out.println("CHILLAR:eldho " + reachid.get(position));
    }


    @Override
    public int getItemCount() {
        return reachid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView REACHID, TRANSID, REACHAMOUNT, REACHTIME, PAYTYPID, SERVERTS;

        public ViewHolder(View itemView) {
            super(itemView);

            REACHID = (TextView) itemView.findViewById(R.id.rechrgid);
            TRANSID = (TextView) itemView.findViewById(R.id.transidd);
            REACHAMOUNT = (TextView) itemView.findViewById(R.id.rechAmount);
            REACHTIME = (TextView) itemView.findViewById(R.id.reachtime);
            PAYTYPID = (TextView) itemView.findViewById(R.id.paytypid);
            SERVERTS = (TextView) itemView.findViewById(R.id.Servertimestamp);


        }
    }


}
