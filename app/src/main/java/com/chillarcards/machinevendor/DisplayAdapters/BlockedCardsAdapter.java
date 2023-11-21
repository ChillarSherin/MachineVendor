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
public class BlockedCardsAdapter extends RecyclerView.Adapter<BlockedCardsAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> cardId = new ArrayList<>();
    private List<String> cardserial = new ArrayList<>();

    public BlockedCardsAdapter(List<String> cardId, List<String> cardserial, Activity activity, int rowLayout, Context context) {
        this.cardId = cardId;
        this.cardserial = cardserial;

        this.activity = activity;
        this.rowLayout = R.layout.table_blocked_cards;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //setting blocked card id and card serials in textview
        holder.CardID.setText(cardId.get(position));
        holder.CardSerial.setText(cardserial.get(position));

        System.out.println("CHILLAR:eldho " + cardId.get(position));


    }


    @Override
    public int getItemCount() {
        return cardId.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView CardID, CardSerial;


        public ViewHolder(View itemView) {
            super(itemView);

            CardID = (TextView) itemView.findViewById(R.id.CardID);
            CardSerial = (TextView) itemView.findViewById(R.id.CardSerial);


        }
    }

}