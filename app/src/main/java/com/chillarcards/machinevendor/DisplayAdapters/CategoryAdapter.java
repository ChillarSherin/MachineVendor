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
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> catID = new ArrayList<>();
    private List<String> itemTyp = new ArrayList<>();
    private List<String> catName = new ArrayList<>();
    private List<String> catShortname = new ArrayList<>();
    private List<String> servrTS = new ArrayList<>();

    public CategoryAdapter(List<String> catID, List<String> itemTyp, List<String> catName, List<String> catShortname, Activity activity, int rowLayout, Context context) {
        this.catID = catID;
        this.itemTyp = itemTyp;
        this.catName = catName;
        this.catShortname = catShortname;


        this.activity = activity;
        this.rowLayout = R.layout.table_category;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }

    //setting listed categories in textviews

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.CATID.setText(catID.get(position));
        holder.ITEMTYP.setText(itemTyp.get(position));
        holder.CATNAME.setText(catName.get(position));
        holder.CATSHORTNAME.setText(catShortname.get(position));


        System.out.println("CHILLAR:eldho " + catID.get(position));
    }


    @Override
    public int getItemCount() {
        return catID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView CATID, ITEMTYP, CATNAME, CATSHORTNAME;

        public ViewHolder(View itemView) {
            super(itemView);

            CATID = (TextView) itemView.findViewById(R.id.catID);
            ITEMTYP = (TextView) itemView.findViewById(R.id.itemtypeID);
            CATNAME = (TextView) itemView.findViewById(R.id.catname);
            CATSHORTNAME = (TextView) itemView.findViewById(R.id.catshortname);


        }
    }


}
