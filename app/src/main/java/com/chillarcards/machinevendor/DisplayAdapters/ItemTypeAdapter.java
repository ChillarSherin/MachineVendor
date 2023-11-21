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
public class ItemTypeAdapter extends RecyclerView.Adapter<ItemTypeAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> Itemtypeid = new ArrayList<>();
    private List<String> itemtypename = new ArrayList<>();

    public ItemTypeAdapter(List<String> Itemtypeid, List<String> itemtypename, Activity activity, int rowLayout, Context context) {
        this.Itemtypeid = Itemtypeid;
        this.itemtypename = itemtypename;


        this.activity = activity;
        this.rowLayout = R.layout.table_itemtype;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    // setting item types in listview using adpter

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.ITEMTYPEID.setText(Itemtypeid.get(position));
        holder.ITEMTYPENAME.setText(itemtypename.get(position));

        System.out.println("CHILLAR:eldho " + Itemtypeid.get(position));
    }


    @Override
    public int getItemCount() {
        return Itemtypeid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ITEMTYPEID, ITEMTYPENAME;

        public ViewHolder(View itemView) {
            super(itemView);

            ITEMTYPEID = (TextView) itemView.findViewById(R.id.itm_typid);
            ITEMTYPENAME = (TextView) itemView.findViewById(R.id.item_typename);


        }
    }


}