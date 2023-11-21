package com.chillarcards.machinevendor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.DatabaseHandler;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.R;

import java.util.ArrayList;

public class BillitemAdapter extends RecyclerView.Adapter<BillitemAdapter.ViewHolder> {


    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;


    private ArrayList<Sales_Item> printDataColl;

    public BillitemAdapter(ArrayList<Sales_Item> arrayList, Activity activity, int adapter_print_bill, Context applicationContext) {

        this.printDataColl = arrayList;

        this.activity = activity;
        this.rowLayout = R.layout.adapter_print_bill;
        this.mContext = applicationContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DatabaseHandler db = new DatabaseHandler(mContext);

//        for (int j = position; j < Constants.sales_items.size(); j++) {
            System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(position).getitem_id()+", "+position);

            String id = String.valueOf(Constants.sales_items.get(position).getitem_id());
            String name = db.getItemNamebyID(id);
            String pric = String.valueOf(Constants.sales_items.get(position).getamount());
            String qty = String.valueOf(Constants.sales_items.get(position).getitem_quantity());

            String amount = String.valueOf(Float.parseFloat(Constants.sales_items.get(position).getamount()) *
                    Integer.parseInt(Constants.sales_items.get(position).getitem_quantity()));

            if (name.length() > 6) {
                name = name.substring(0, 6);
            }

            holder.mItemName.setText(name);
            holder.mItemQty.setText(qty);
            holder.mItemPrice.setText(pric);
            holder.mItemPayAmnt.setText(amount);
//        }

//        //Split data from previous page
//        printDataColl = printDataColl.replaceAll("^,|,$", "");
//        // Split data from previous page
//        String[] items = printDataColl.split(",");
//        for (String item : items) {
//            String[] components = item.trim().split("\\*");
//
//            if (components.length == 4) {
//                String one = components[0].trim();  // itemName
//                String two = components[1].trim();  // itemQty
//                String three = components[2].trim();  // itemPrice
//                String four = components[3].trim();  // itemPayAmnt
//
//                System.out.println("one: " + one);
//                System.out.println("two: " + two);
//                System.out.println("three: " + three);
//                System.out.println("four: " + four);
//
//
//
//                System.out.println("CODMOBTECH: " + one + " " + three);
//
//            }
//            else {
//                // Handle cases where there are not enough components
//                System.out.println("Invalid item format: " + item);
//            }
//            System.out.println("-");
//        }
    }


    @Override
    public int getItemCount() {
        return Constants.sales_items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView mItemName;
        TextView mItemQty;
        TextView mItemPrice;
        TextView mItemPayAmnt;

        public ViewHolder(View itemView) {
            super(itemView);

             mItemName = itemView.findViewById(R.id.item_name);
             mItemQty = itemView.findViewById(R.id.item_qty);
             mItemPrice = itemView.findViewById(R.id.item_price);
             mItemPayAmnt = itemView.findViewById(R.id.item_pay_amnt);
        }
    }

}
