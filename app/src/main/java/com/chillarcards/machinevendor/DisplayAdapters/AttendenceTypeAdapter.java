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
public class AttendenceTypeAdapter extends RecyclerView.Adapter<AttendenceTypeAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> ID = new ArrayList<>();
    private List<String> Attypename = new ArrayList<>();

    public AttendenceTypeAdapter(List<String> ID, List<String> Attypename, Activity activity, int rowLayout, Context context) {
        this.ID = ID;
        this.Attypename = Attypename;

        this.activity = activity;
        this.rowLayout = R.layout.table_attendencetype;
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

        //setting attendence types in textviews
        holder.ID.setText(ID.get(position));
        holder.ATTYPEID.setText(Attypename.get(position));

        System.out.println("CHILLAR:eldho " + ID.get(position));
    }


    @Override
    public int getItemCount() {
        return ID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ID, ATTYPEID;

        public ViewHolder(View itemView) {
            super(itemView);

            ID = (TextView) itemView.findViewById(R.id.att_id);
            ATTYPEID = (TextView) itemView.findViewById(R.id.transidd2);

        }
    }


}