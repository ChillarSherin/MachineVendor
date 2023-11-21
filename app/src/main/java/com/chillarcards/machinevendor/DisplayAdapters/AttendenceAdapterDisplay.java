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
public class AttendenceAdapterDisplay extends RecyclerView.Adapter<AttendenceAdapterDisplay.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> attndencedata = new ArrayList<>();
    private List<String> transactionID = new ArrayList<>();
    private List<String> INout = new ArrayList<>();
    private List<String> AttndenceType = new ArrayList<>();
    private List<String> Srvts = new ArrayList<>();

    public AttendenceAdapterDisplay(List<String> attndencedata, List<String> transactionID, List<String> INout,
                                    List<String> AttndenceType, List<String> Srvts, Activity activity, int rowLayout, Context context) {
        this.attndencedata = attndencedata;
        this.transactionID = transactionID;
        this.INout = INout;
        this.AttndenceType = AttndenceType;
        this.Srvts = Srvts;

        this.activity = activity;
        this.rowLayout = R.layout.table_item_attendancedata;
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

        // setting all attendence details to the textviews
        holder.ATTNDENCEDATA.setText(attndencedata.get(position));
        holder.TRANSACTIONID.setText(transactionID.get(position));
        holder.INOUT.setText(INout.get(position));
        holder.ATTENDNCE.setText(AttndenceType.get(position));
        holder.SRVTS.setText(Srvts.get(position));


        System.out.println("CHILLAR:eldho " + attndencedata.get(position));


    }


    @Override
    public int getItemCount() {
        return attndencedata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ATTNDENCEDATA, TRANSACTIONID, INOUT, ATTENDNCE, SRVTS;


        public ViewHolder(View itemView) {
            super(itemView);

            ATTNDENCEDATA = (TextView) itemView.findViewById(R.id.attnddata);
            TRANSACTIONID = (TextView) itemView.findViewById(R.id.transidd2);
            INOUT = (TextView) itemView.findViewById(R.id.inout);
            ATTENDNCE = (TextView) itemView.findViewById(R.id.attndencetyp);
            SRVTS = (TextView) itemView.findViewById(R.id.Servertimestamp1);


        }
    }

}