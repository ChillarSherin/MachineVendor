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
public class OnlineTransAdapter extends RecyclerView.Adapter<OnlineTransAdapter.ViewHolder> {


    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> tranID = new ArrayList<>();
    private List<String> onlineTransID = new ArrayList<>();
    private List<String> servrTS = new ArrayList<>();

    public OnlineTransAdapter(List<String> tranID, List<String> onlineTransID, List<String> servrTS, Activity activity, int rowLayout, Context context) {
        this.tranID = tranID;
        this.onlineTransID = onlineTransID;
        this.servrTS = servrTS;

        this.activity = activity;
        this.rowLayout = rowLayout;
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


        holder.TRANSID.setText(tranID.get(position));
        holder.ONLINTRANS.setText(onlineTransID.get(position));
        holder.SERVERTS.setText(servrTS.get(position));

        System.out.println("CHILLAR:eldho " + tranID.get(position));
    }


    @Override
    public int getItemCount() {
        return tranID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView TRANSID, ONLINTRANS, SERVERTS;

        public ViewHolder(View itemView) {
            super(itemView);

            TRANSID = (TextView) itemView.findViewById(R.id.transIDD);
            ONLINTRANS = (TextView) itemView.findViewById(R.id.onlinetransidd);
            SERVERTS = (TextView) itemView.findViewById(R.id.Servertimestamp);


        }
    }


}
