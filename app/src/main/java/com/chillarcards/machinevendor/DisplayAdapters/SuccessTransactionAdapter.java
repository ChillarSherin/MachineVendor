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
public class SuccessTransactionAdapter extends RecyclerView.Adapter<SuccessTransactionAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> transid = new ArrayList<>();
    private List<String> userid = new ArrayList<>();
    private List<String> typeid = new ArrayList<>();
    private List<String> prev = new ArrayList<>();
    private List<String> current = new ArrayList<>();
    private List<String> cardserial = new ArrayList<>();
    private List<String> timestamp = new ArrayList<>();
    private List<String> serverts = new ArrayList<>();

    public SuccessTransactionAdapter(List<String> transid, List<String> userid, List<String> typeid, List<String> prev,
                                     List<String> current, List<String> cardserial, List<String> timestamp,
                                     List<String> serverts, Activity activity, int rowLayout, Context context) {
        this.transid = transid;
        this.userid = userid;
        this.typeid = typeid;
        this.prev = prev;
        this.current = current;
        this.cardserial = cardserial;
        this.timestamp = timestamp;
        this.serverts = serverts;
        this.activity = activity;
        this.rowLayout = R.layout.table_item_success;
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

        holder.transid.setText(transid.get(position));
        holder.userid.setText(userid.get(position));
        holder.typeid.setText(typeid.get(position));
        holder.prev.setText(prev.get(position));
        holder.current.setText(current.get(position));
        holder.cardserial.setText(cardserial.get(position));
        holder.timestamp.setText(timestamp.get(position));
        holder.serverts.setText(serverts.get(position));


        System.out.println("CHILLAR:eldho " + transid.get(position));


    }


    @Override
    public int getItemCount() {
        return transid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView transid, userid, typeid, prev, current, cardserial, timestamp, serverts;


        final Button button;

        public ViewHolder(View itemView) {
            super(itemView);

            transid = (TextView) itemView.findViewById(R.id.transid);
            userid = (TextView) itemView.findViewById(R.id.userid);
            typeid = (TextView) itemView.findViewById(R.id.transtypeid);
            prev = (TextView) itemView.findViewById(R.id.prevbaltxt);
            current = (TextView) itemView.findViewById(R.id.currbaltxt);
            cardserial = (TextView) itemView.findViewById(R.id.cardserialtxt);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            serverts = (TextView) itemView.findViewById(R.id.serverts);


            button = (Button) itemView.findViewById(R.id.button_item);

        }
    }

}