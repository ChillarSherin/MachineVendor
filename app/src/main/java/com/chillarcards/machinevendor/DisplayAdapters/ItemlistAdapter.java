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
public class ItemlistAdapter extends RecyclerView.Adapter<ItemlistAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> itId = new ArrayList<>();
    private List<String> itCode = new ArrayList<>();
    private List<String> itNmae = new ArrayList<>();
    private List<String> itShort = new ArrayList<>();
    private List<String> itCat = new ArrayList<>();
    private List<String> itPrice = new ArrayList<>();
    private List<String> itStockstts = new ArrayList<>();
    private List<String> itTypID = new ArrayList<>();
    private List<String> servrTS = new ArrayList<>();

    public ItemlistAdapter(List<String> itId, List<String> itCode, List<String> itNmae, List<String> itShort,
                           List<String> itCat, List<String> itPrice, List<String> itStockstts, List<String> itTypID
            , Activity activity, int rowLayout, Context context) {
        this.itId = itId;
        this.itCode = itCode;
        this.itNmae = itNmae;
        this.itShort = itShort;
        this.itCat = itCat;
        this.itPrice = itPrice;
        this.itStockstts = itStockstts;
        this.itTypID = itTypID;

        this.activity = activity;
        this.rowLayout = R.layout.table_itemlist;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        //setting item details (set texting)

        holder.ITEMID.setText(itId.get(position));
        holder.ITEMCODE.setText(itCode.get(position));
        holder.ITEMNAME.setText(itNmae.get(position));
        holder.ITEMSHORTNAME.setText(itShort.get(position));
        holder.ITEMCATEGORY.setText(itCat.get(position));
        holder.PRICE.setText(itPrice.get(position));
        holder.STATUS.setText(itStockstts.get(position));
        holder.ITEMTYPEID.setText(itTypID.get(position));

        System.out.println("CHILLAR:eldho " + itId.get(position));
    }


    @Override
    public int getItemCount() {
        return itId.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ITEMID, ITEMCODE, ITEMNAME, ITEMSHORTNAME, ITEMCATEGORY, PRICE, STATUS, ITEMTYPEID;

        public ViewHolder(View itemView) {
            super(itemView);


            ITEMID = (TextView) itemView.findViewById(R.id.itemid);
            ITEMCODE = (TextView) itemView.findViewById(R.id.itemcode);
            ITEMNAME = (TextView) itemView.findViewById(R.id.itemname);
            ITEMSHORTNAME = (TextView) itemView.findViewById(R.id.itemshortnbame);
            ITEMCATEGORY = (TextView) itemView.findViewById(R.id.itemcategory);
            PRICE = (TextView) itemView.findViewById(R.id.itemprice);
            STATUS = (TextView) itemView.findViewById(R.id.stockstatus);
            ITEMTYPEID = (TextView) itemView.findViewById(R.id.itemtypeid);


        }
    }


}
