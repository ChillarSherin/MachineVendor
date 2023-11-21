package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.Adapters.ClassListAdapter;
import com.chillarcards.machinevendor.ModelClass.StudentClassDiv;
import com.chillarcards.machinevendor.ModelClass.StudentList;

//import codmob.com.eventswallet.Adapters.ClassListAdapter;
//import codmob.com.eventswallet.ModelClass.StudentList;

/**
 * Created by Codmob on 09-07-16.
 */
public class ClassesDisplayActivity extends Activity {

    RecyclerView mRecyclerView;
    DatabaseHandler db ;
    Activity activity;
    ClassListAdapter mAdapter;
//    Float total=(float) 0;
//    TextView totamount;
//    Button paybtn;
    Button addBtn,searchBtn;
//    ProgressBar progressBar;

//    Dialog PopupConfrm;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    public static ArrayList<String> STUD_CLASS  = new ArrayList<String>();
    public static ArrayList<String> STUD_DIV = new ArrayList<String>();

//    String id;
//    String pric;
//    String qty;;
//    String amount;;
    String attend_typ;

//    private final ArrayList<String> item = new ArrayList<String>();
//    private final ArrayList<String> price = new ArrayList<String>();
//    private final ArrayList<String> quantity = new ArrayList<String>();
//    private final ArrayList<String> totamt = new ArrayList<String>();
    ArrayList<String> st_card_serial  = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_classes_display);

        db = DatabaseHandler.getInstance(getApplicationContext());

//        progressBar= (ProgressBar) findViewById(R.id.progbar);
//        progressBar.setVisibility(View.INVISIBLE);

        mRecyclerView= (RecyclerView) findViewById(R.id.listCheckout);
//        totamount= (TextView) findViewById(R.id.totalamt);

        searchBtn= (Button) findViewById(R.id.search);
        addBtn= (Button) findViewById(R.id.add);


        activity= this;

        drawerArrow();
//
//        final File dbFile = new File(getFilesDir().getParent()+"/databases/"+DatabaseHandler.DATABASE_NAME);
//
//        System.out.println("CHILLAR File " + dbFile);



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),Student_List.class);
                Bundle b=new Bundle();
                b.putString("id",attend_typ);
                i.putExtras(b);
                startActivity(i);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STUD_CLASS.size()==0 && STUD_DIV.size()==0)
                {
                    Toast.makeText(ClassesDisplayActivity.this, "Select Class & Division", Toast.LENGTH_SHORT).show();
                }
                else
                {

//                    Constants.S_Class.clear();
//                    Constants.S_Div.clear();
//                    String s_class=stud_class.getSelectedItem().toString();
//                    String s_div=stud_div.getSelectedItem().toString();
//                    System.out.println("Student "+"Class "+ s_class);
//                    System.out.println("Student "+"Division "+ s_div);
//
//
//
//                    Constants.S_Class.add(s_class);
//                    Constants.S_Div.add(s_div);


                    st_card_serial.clear();

                    for(int i=0;i<STUD_CLASS.size();++i){

                        System.out.print("CHILLERS: ");
                        List<StudentList> contacts = db.getstud_cardserial(STUD_CLASS.get(i),STUD_DIV.get(i));
                        for (StudentList cn : contacts) {
                            String log = "st card serial: " + cn.getStudent_card_serial();
                            // Writing Contacts to log


                            Log.d("Name: ", log);
                            System.out.println("Details\t\t" + log);
                            st_card_serial.add(cn.getStudent_card_serial());
                        }

                    }


                    STUD_CLASS.clear();
                    STUD_DIV.clear();


//                    Constants.S_Class.clear();
//                    Constants.S_Div.clear();

                    Intent i=new Intent(getApplicationContext(),AttendenceNFC_New.class);
                    Bundle b=new Bundle();
                    b.putStringArrayList("card_list", st_card_serial);
                    b.putString("id",attend_typ);
                    i.putExtras(b);
                    startActivity(i);

                    onBackPressed();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle b=getIntent().getExtras();
        attend_typ=b.getString("id");

        System.out.println("OnResume: ");

        STUD_CLASS.clear();
        STUD_DIV.clear();

        List <StudentClassDiv> test11=db.getAllStudentClass();
        for (StudentClassDiv studentList1:test11){
            System.out.println("AttendanceSystem222: StudClasTable222 ID: "+ studentList1.getId()+" ClassName: "+studentList1.getStud_class()
                    +" DivName "+studentList1.getStud_div());

            STUD_CLASS.add(studentList1.getStud_class());
            STUD_DIV.add(studentList1.getStud_div());

        }

        if(STUD_CLASS.size()!=0 && STUD_DIV.size()!=0){
            System.out.println("OnResume: "+ "inside");
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new ClassListAdapter(STUD_CLASS,STUD_DIV,attend_typ, activity,R.layout.layout_class_list_item ,getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);

        }else{
            System.out.println("OnResume: "+ "outside");
            Toast.makeText(getApplicationContext(),"Add Classes to search",Toast.LENGTH_SHORT).show();
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
