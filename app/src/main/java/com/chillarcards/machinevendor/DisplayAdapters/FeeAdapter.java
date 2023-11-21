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
public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> trID = new ArrayList<>();
    private List<String> Paysource = new ArrayList<>();
    private List<String> BillNO = new ArrayList<>();
    private List<String> TotalAmount = new ArrayList<>();
    private List<String> Srvts = new ArrayList<>();

    public FeeAdapter(List<String> trID, List<String> BillNO,
                      List<String> TotalAmount, List<String> Srvts, Activity activity, int rowLayout, Context context) {
        this.trID = trID;

        this.BillNO = BillNO;
        this.TotalAmount = TotalAmount;
        this.Srvts = Srvts;

        this.activity = activity;
        this.rowLayout = R.layout.table_item_fee;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //setting fee details in listview adapter
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.TRANSACTIONID.setText(trID.get(position));

        holder.BILLNO.setText(BillNO.get(position));
        holder.TOTALAMOUNT.setText(TotalAmount.get(position));
        holder.SERVETS.setText(Srvts.get(position));


        System.out.println("CHILLAR:eldho " + trID.get(position));
    }


    @Override
    public int getItemCount() {
        return trID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView TRANSACTIONID, BILLNO, TOTALAMOUNT, SERVETS;


        public ViewHolder(View itemView) {
            super(itemView);

            TRANSACTIONID = (TextView) itemView.findViewById(R.id.transid);
            BILLNO = (TextView) itemView.findViewById(R.id.billno);
            TOTALAMOUNT = (TextView) itemView.findViewById(R.id.totalamount);
            SERVETS = (TextView) itemView.findViewById(R.id.Servertimestamp);


        }
    }

}