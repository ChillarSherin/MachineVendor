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
public class ItemStockAdapter extends RecyclerView.Adapter<ItemStockAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> StockID = new ArrayList<>();
    private List<String> ItemID = new ArrayList<>();
    private List<String> ItemStock = new ArrayList<>();
    private List<String> Reorder = new ArrayList<>();

    public ItemStockAdapter(List<String> StockID, List<String> ItemID, List<String> ItemStock, List<String> Reorder, Activity activity, int rowLayout, Context context) {
        this.StockID = StockID;
        this.ItemID = ItemID;
        this.ItemStock = ItemStock;
        this.Reorder = Reorder;

        this.activity = activity;
        this.rowLayout = R.layout.table_item_stock;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //setting item stock details in list view using adpater


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.STOCKID.setText(StockID.get(position));
        holder.ITEMID.setText(ItemID.get(position));
        holder.ITEMSTOCK.setText(ItemStock.get(position));
        holder.REORDER.setText(Reorder.get(position));

        System.out.println("CHILLAR:eldho " + StockID.get(position));
    }


    @Override
    public int getItemCount() {
        return StockID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView STOCKID, ITEMID, ITEMSTOCK, REORDER;

        public ViewHolder(View itemView) {
            super(itemView);

            STOCKID = (TextView) itemView.findViewById(R.id.stockid);
            ITEMID = (TextView) itemView.findViewById(R.id.itemid);
            ITEMSTOCK = (TextView) itemView.findViewById(R.id.itemstock);
            REORDER = (TextView) itemView.findViewById(R.id.reorderwarning);


        }
    }


}