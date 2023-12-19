package com.chillarcards.machinevendor.Adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.R;
import com.chillarcards.machinevendor.StoreActivityListAllItems;

import java.util.List;

/**
 * Created by Codmob on 07-07-16.
 */
public class StoreSubmenuListAllAdapter extends RecyclerView.Adapter<StoreSubmenuListAllAdapter.ViewHolder>{

    private final List<String> price1;
    private final List<String> desc;
    private final List<String> itid;
    private final List<String> Itemcode;
    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    int flag=0;
    Activity mActivity;

    StoreActivityListAllItems mParentActivity;

    public StoreSubmenuListAllAdapter(List<String> desc,List<String> price,List<String> Itemid,List<String> Itemcode,StoreActivityListAllItems mParentActivity, Activity activity,int rowLayout, Context context) {
        this.desc = desc;
        this.itid=Itemid;
        this.Itemcode=Itemcode;
        this.price1=price;
        this.activity = activity;
        this.rowLayout = R.layout.ist_storesubmenu_item;
        this.mContext = context;
        this.mParentActivity=mParentActivity;
        this.mActivity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.desc.setText(desc.get(position));
        holder.price.setText(price1.get(position)+" "+mContext.getString(R.string.currency_oman));
        holder.textCode.setText(Itemcode.get(position));
        holder.linearLayout.setOnClickListener(view -> onAddDataToCart(position));

        System.out.println("CHILLAR:ItemID:"+itid.get(position));
    }

    @Override
    public int getItemCount() {
        return desc.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView price;
        final TextView desc;
        final LinearLayout linearLayout;
        final EditText editText;
        final TextView textCode;
        String quantity;

        public ViewHolder(final View itemView) {
            super(itemView);

            price = (TextView) itemView.findViewById(R.id.price);
            desc = (TextView) itemView.findViewById(R.id.textV);
            editText= (EditText) itemView.findViewById(R.id.et_otp);
            textCode = (TextView) itemView.findViewById(R.id.textCode);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.linlay);


        }

        @Override
        public void onClick(View v) {



        }
    }
     void onAddDataToCart(int position){
         System.out.println("Recycleview touched");

         final Dialog dlg = new Dialog(mActivity);
         dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dlg.setContentView(R.layout.amount_popup);
         dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
         dlg.setCancelable(true);
         dlg.setCanceledOnTouchOutside(true);
         dlg.show();

         final TextView itemName= (TextView) dlg.findViewById(R.id.item_name);
         final TextView itemPrice= (TextView) dlg.findViewById(R.id.item_price);
         final EditText editText= (EditText) dlg.findViewById(R.id.et_otp);
         final  Button button= (Button)                                                                                                                                                    dlg.findViewById(R.id.btn_send_pay);
         itemName.setText(desc.get(position));
         itemPrice.setText("Price : "+price1.get(position)+" "+mContext.getString(R.string.currency_oman));

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {



                 int qnty = 0;
                 if (String.valueOf(editText.getText()).equals("") || String.valueOf(editText.getText()).equals("0")) {

                     Toast.makeText(mContext,"Select a valid quantity.",Toast.LENGTH_SHORT).show();

                 } else {

                     mParentActivity.search.setText("");

                     Constants.Qty = Integer.parseInt(String.valueOf(editText.getText()));
                     String id = itid.get(position);
                     System.out.println("CHILLER:ITID:" + id);

                         /*
                         if add same item in many times it will increase the quantity only
                         */
                     for (int i = 0; i < Constants.sales_items.size(); i++) {

                         if (Constants.sales_items.get(i).getitem_id().equals(id)) {

                             try {
                                 qnty = Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()) + Integer.parseInt(String.valueOf(editText.getText()));
                                 System.out.println("CHILLER:QNTY :" + qnty);

                                 System.out.println("CHILLER:QNTY FINAL:" + Constants.Qty);

                             } catch (NumberFormatException e) {
                                 qnty = Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()) + 1;
                                 System.out.println("CHILLER:QNTY :" + qnty);
                                 System.out.println("CHILLER:QNTY FINAL:" + Constants.Qty);
                             }
                             flag = 1;
                             Constants.sales_items.get(i).setitem_quantity(String.valueOf(qnty));
                             break;

                         }

                     }

                     if (flag == 0) {

                         System.out.println("CHILLER:QNTY FINAL:" + Constants.Qty);
                         String pric = price1.get(position);
                         Constants.sales_items.add(new Sales_Item("testtrans", id, String.valueOf(Constants.Qty), pric));
                         System.out.println("CHILLARItem Amount: " + Constants.Qty);

                     }

                     dlg.dismiss();

                 }
             }
         });


     }
}
