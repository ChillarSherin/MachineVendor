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
 * Created by test on 03-08-2016.
 */
public class PaymentTypeAdapter extends RecyclerView.Adapter<PaymentTypeAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> ID = new ArrayList<>();
    private List<String> Paytypename = new ArrayList<>();

    public PaymentTypeAdapter(List<String> ID, List<String> Paytypename, Activity activity, int rowLayout, Context context) {
        this.ID = ID;
        this.Paytypename = Paytypename;

        this.activity = activity;
        this.rowLayout = R.layout.table_paymenttype;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //setting payment type name and id
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.ID.setText(ID.get(position));
        holder.PAYTYPENAME.setText(Paytypename.get(position));


        System.out.println("CHILLAR:eldho " + ID.get(position));
    }


    @Override
    public int getItemCount() {
        return ID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ID, PAYTYPENAME;

        public ViewHolder(View itemView) {
            super(itemView);

            ID = (TextView) itemView.findViewById(R.id.att_id);
            PAYTYPENAME = (TextView) itemView.findViewById(R.id.transidd2);


        }
    }


}