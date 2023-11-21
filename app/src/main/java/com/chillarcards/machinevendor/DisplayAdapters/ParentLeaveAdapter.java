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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.R;


/**
 * Created by Lester Oliver on 1/24/2015.
 */
public class ParentLeaveAdapter extends RecyclerView.Adapter<ParentLeaveAdapter.ViewHolder> {

    private List<String> paruserid = new ArrayList<>();
    private List<String> studuserid = new ArrayList<>();
    private List<String> reasontypeid = new ArrayList<>();
    private List<String> reasoncomment = new ArrayList<>();
    private List<String> timestamp = new ArrayList<>();
    private List<String> serverts = new ArrayList<>();
    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;

    public ParentLeaveAdapter(List<String> studuserid, List<String> paruserid,List<String> reasontypeid,List<String> reasoncomment,
                              List<String> timestamp, List<String> serverts, Activity activity, int rowLayout, Context context) {
        this.paruserid = paruserid;
        this.studuserid = studuserid;
        this.reasontypeid = reasontypeid;
        this.reasoncomment = reasoncomment;
        this.timestamp = timestamp;
        this.serverts = serverts;
        this.activity = activity;
        this.rowLayout = R.layout.item_parent_leave;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //Setting all success transaction details

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.parentid.setText(paruserid.get(position));
        holder.studentid.setText(studuserid.get(position));
        holder.reasontypeid.setText(reasontypeid.get(position));
        holder.reasonComt.setText(reasoncomment.get(position));
        holder.timestamp.setText(timestamp.get(position));
        holder.serverts.setText(serverts.get(position));


        System.out.println("CHILLAR:eldho " + reasoncomment.get(position));



    }


    @Override
    public int getItemCount() {
        return paruserid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView parentid,studentid,reasontypeid,reasonComt,timestamp,serverts;




//        final Button button;

        public ViewHolder(View itemView) {
            super(itemView);

            parentid = (TextView) itemView.findViewById(R.id.parentid);
            studentid = (TextView) itemView.findViewById(R.id.studentid);
            reasontypeid = (TextView) itemView.findViewById(R.id.reasontypeid);
            reasonComt = (TextView) itemView.findViewById(R.id.reasonComt);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            serverts = (TextView) itemView.findViewById(R.id.serverts);



//            button = (Button) itemView.findViewById(R.id.button_item);

        }
    }

}