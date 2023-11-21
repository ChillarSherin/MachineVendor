package com.chillarcards.machinevendor.DisplayAdapters;

/**
 * Created by Codmob on 02-08-16.
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


/**
 * Created by Lester Oliver on 1/24/2015.
 */
public class DeviceInfoAdapter extends RecyclerView.Adapter<DeviceInfoAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> machineid = new ArrayList<>();
    private List<String> schoolid = new ArrayList<>();
    private List<String> serialno = new ArrayList<>();
    private List<String> schoolname = new ArrayList<>();
    private List<String> schoolplace = new ArrayList<>();

    public DeviceInfoAdapter(List<String> machineid, List<String> schoolid, List<String> serialno,
                             List<String> schoolname, List<String> schoolplace, Activity activity, int rowLayout, Context context) {
        this.machineid = machineid;
        this.schoolid = schoolid;
        this.serialno = serialno;
        this.schoolname = schoolname;
        this.schoolplace = schoolplace;
        this.activity = activity;
        this.rowLayout = R.layout.table_item_devinfo;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //displaying device details like machine id,school id serial no:, etc... into textviews
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.MachineID.setText(machineid.get(position));
        holder.schoolId.setText(schoolid.get(position));
        holder.serialNo.setText(serialno.get(position));
        holder.schoolname.setText(schoolname.get(position));
        holder.schoolplace.setText(schoolplace.get(position));

        System.out.println("CHILLAR:eldho " + schoolname.get(position));


    }


    @Override
    public int getItemCount() {
        return schoolid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView MachineID, schoolId, serialNo, schoolname, schoolplace;


        public ViewHolder(View itemView) {
            super(itemView);

            MachineID = (TextView) itemView.findViewById(R.id.machineID);
            serialNo = (TextView) itemView.findViewById(R.id.serialno);
            schoolname = (TextView) itemView.findViewById(R.id.schoolname);
            schoolplace = (TextView) itemView.findViewById(R.id.schoolplace);
            schoolId = (TextView) itemView.findViewById(R.id.schoolid1);


        }
    }

}