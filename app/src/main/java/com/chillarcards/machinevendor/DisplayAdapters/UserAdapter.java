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
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final int rowLayout;
    private final Context mContext;
    private final Activity activity;
    //    private final ProgressDialog progressDialog;
    String PINN;
    int pos;
    String parameters;
    private List<String> userid = new ArrayList<>();
    private List<String> username = new ArrayList<>();
    private List<String> password = new ArrayList<>();

    public UserAdapter(List<String> userid, List<String> username, List<String> password, Activity activity, int rowLayout, Context context) {
        this.userid = userid;
        this.username = username;
        this.password = password;


        this.activity = activity;
        this.rowLayout = R.layout.table_user;
        this.mContext = context;

//        progressDialog = new ProgressDialog(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);


    }


    // setting user details

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.USERID.setText(userid.get(position));
        holder.USERNAME.setText(username.get(position));
        holder.PASSWORD.setText(password.get(position));

        System.out.println("CHILLAR:eldho " + userid.get(position));
    }


    @Override
    public int getItemCount() {
        return userid.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView USERID, USERNAME, PASSWORD;

        public ViewHolder(View itemView) {
            super(itemView);

            USERID = (TextView) itemView.findViewById(R.id.userid);
            USERNAME = (TextView) itemView.findViewById(R.id.username);
            PASSWORD = (TextView) itemView.findViewById(R.id.password);


        }
    }


}