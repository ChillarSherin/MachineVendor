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
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> ID = new ArrayList<>();
    private List<String> tranID = new ArrayList<>();
    private List<String> issueret = new ArrayList<>();
    private List<String> bookid = new ArrayList<>();
    private List<String> issuetime = new ArrayList<>();
    private List<String> servrTS = new ArrayList<>();

    public LibraryAdapter(List<String> ID, List<String> tranID, List<String> issueret, List<String> bookid,
                          List<String> issuetime, List<String> servrTS, Activity activity, int rowLayout, Context context) {
        this.ID = ID;
        this.tranID = tranID;
        this.issueret = issueret;
        this.bookid = bookid;
        this.issuetime = issuetime;
        this.servrTS = servrTS;

        this.activity = activity;
        this.rowLayout = R.layout.table_item_library;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //setting library details

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.ID.setText(ID.get(position));
        holder.TRANSID.setText(tranID.get(position));
        holder.ISSUERET.setText(issueret.get(position));
        holder.BOOKID.setText(bookid.get(position));
        holder.ISSUETIME.setText(issuetime.get(position));
        holder.SERVERTS.setText(servrTS.get(position));

        System.out.println("CHILLAR:eldho " + ID.get(position));
    }


    @Override
    public int getItemCount() {
        return ID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ID, TRANSID, ISSUERET, BOOKID, ISSUETIME, SERVERTS;

        public ViewHolder(View itemView) {
            super(itemView);

            ID = (TextView) itemView.findViewById(R.id.ID);
            TRANSID = (TextView) itemView.findViewById(R.id.transID);
            ISSUERET = (TextView) itemView.findViewById(R.id.issuereturn);
            BOOKID = (TextView) itemView.findViewById(R.id.bookid);
            ISSUETIME = (TextView) itemView.findViewById(R.id.issuetime);
            SERVERTS = (TextView) itemView.findViewById(R.id.Servertimestamp);


        }
    }


}