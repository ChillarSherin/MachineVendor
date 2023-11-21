package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.DisplayAdapters.ParentStudentDetailAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.ParentsMeetingAdapter;
import com.chillarcards.machinevendor.ModelClass.StudentList;

/**
 * Created by Codmob on 15-11-16.
 */
public class ParentStudentDetails extends Activity {

    private final List<String> studentId = new ArrayList<>();
    private final List<String> studentName = new ArrayList<>();
    private final List<String> stdDiv = new ArrayList<>();
    private final List<String> studimage = new ArrayList<>();
    ProgressBar progressBar;
    int userid;
    String cardserial;
    ParentStudentDetailAdapter mParent;
    ParentsMeetingAdapter mParentMeeting;
    TextView title;
    String User_name, Pass_word, MachineCode, type;
    DatabaseHandler db;
    private RecyclerView mRecyclerView;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private Activity activity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tables_disp_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());

        drawerArrow();
        preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = preferences.getString("username", "");
        Pass_word = preferences.getString("password", "");
        MachineCode = preferences.getString("schlMachCode", "");
        userid = preferences.getInt("userid", 0);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_tables);
        title = (TextView) findViewById(R.id.textView15);
        title.setText("Student Details");

        activity = this;
        progressBar = (ProgressBar) findViewById(R.id.progbar1);
        progressBar.setVisibility(View.VISIBLE);


        Bundle b = getIntent().getExtras();
        String parentId = b.getString("parentId");
        cardserial = b.getString("p_cardserial");
        type = b.getString("type");

        studentId.clear();
        studentName.clear();
        stdDiv.clear();
        studimage.clear();

        List<StudentList> studentLists = db.getAllStudsByParent(Integer.parseInt(parentId));

        for (StudentList studentList1 : studentLists) {
            System.out.println("ParentSystem: StdDetails CardSl: " + studentList1.getStudent_card_serial() + " Name: " + studentList1.getStudent_name()
                    + " StudId " + studentList1.getUser_id() + " Class " + studentList1.getStudent_class() + " " + studentList1.getStudent_division());


            studentId.add(studentList1.getUser_id());
            studentName.add(studentList1.getStudent_name());
            stdDiv.add(studentList1.getStudent_class() + ":" + studentList1.getStudent_division());


        }


        System.out.println("CHillRR; " + cardserial);


        if (type.equals("p_meeting")) {


            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mParentMeeting = new ParentsMeetingAdapter(studentId, studentName, stdDiv, parentId, User_name, Pass_word, MachineCode, cardserial, userid, activity, R.layout.item_student_details, getApplicationContext());
            mRecyclerView.setAdapter(mParentMeeting);

            progressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);


        } else {


            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mParent = new ParentStudentDetailAdapter(studentId, studentName, stdDiv, parentId, User_name, Pass_word, MachineCode, cardserial, userid, activity, R.layout.item_student_details, getApplicationContext());
            mRecyclerView.setAdapter(mParent);

            progressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }


    }


    public void drawerArrow() {
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.white));
        imageView.setImageDrawable(drawerArrowDrawable);

        offset = 0;
        flipped = false;
        drawerArrowDrawable.setFlip(flipped);
        drawerArrowDrawable.setParameter(offset);

        offset = 1;
        flipped = true;
        drawerArrowDrawable.setFlip(flipped);
        drawerArrowDrawable.setParameter(offset);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();

            }
        });
    }

}
