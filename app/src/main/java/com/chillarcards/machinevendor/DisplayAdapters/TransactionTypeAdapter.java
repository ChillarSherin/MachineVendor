package com.chillarcards.machinevendor.DisplayAdapters;

/**
 * Created by test on 03-08-2016.
 */

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


public class TransactionTypeAdapter extends RecyclerView.Adapter<TransactionTypeAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> TrantypeTid = new ArrayList<>();
    private List<String> Transtypename = new ArrayList<>();

    public TransactionTypeAdapter(List<String> TrantypeTid, List<String> Transtypename, Activity activity, int rowLayout, Context context) {
        this.TrantypeTid = TrantypeTid;
        this.Transtypename = Transtypename;

        this.activity = activity;
        this.rowLayout = R.layout.table_transtype;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.TRANSTYPEID.setText(TrantypeTid.get(position));
        holder.TRANSTYPENAME.setText(Transtypename.get(position));


        System.out.println("CHILLAR:eldho " + TrantypeTid.get(position));
    }


    @Override
    public int getItemCount() {
        return TrantypeTid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView TRANSTYPEID, TRANSTYPENAME;

        public ViewHolder(View itemView) {
            super(itemView);

            TRANSTYPEID = (TextView) itemView.findViewById(R.id.trans_typid);
            TRANSTYPENAME = (TextView) itemView.findViewById(R.id.trans_typename);


        }
    }


}