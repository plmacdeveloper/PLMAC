package plm.oop.com.plmac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacultyViewSubject extends AppCompatActivity {
    ExpandableListAdapter expListAdapter;
    ExpandableListView expListView;
    ListView listView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listChildData;
//
//    Intent i = getIntent();
//    final String userName = i.getStringExtra("userName");
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference mRef = database.getReference("Subject");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_subject);
        expListView = (ExpandableListView) findViewById(R.id.explv1);
        prepareListData();

        expListAdapter = new ExpandableListAdapters(this, listDataHeader, listChildData);

        expListView.setAdapter(expListAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View v, int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + "Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + "Collapsed", Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + ":" + listChildData.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                return false;
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listChildData = new HashMap<String, List<String>>();
        ArrayList<String> Yes = new ArrayList<String>();
        Yes.add("wiw");
        Yes.add("zzz");
        Yes.add("aweqe");
        Yes.add("wewe");
        listDataHeader.addAll(Yes);
        listDataHeader.add("EW");
        List<String> getSubject = new ArrayList<String>();
        List<String> getRoom = new ArrayList<String>();
        getSubject.add("Keta");
        getRoom.add("wewew");
        listChildData.put(listDataHeader.get(1),getRoom);
        listChildData.put(listDataHeader.get(0), getSubject);
//
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                List<String> getRoom = new ArrayList<String>();
//                List<String> getSchedule = new ArrayList<String>();
//                List<String> getTime= new ArrayList<String>();
//                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
//                    if (userName.compareTo(ds.child("Faculty").getValue().toString()) == 0) {
//                        listDataHeader.add(String.valueOf(ds.getKey()));
//                        listDataHeader.add(String.valueOf(ds.child("Room").getValue()));
//                        listDataHeader.add(String.valueOf((ds.child("Time").child("Start").getValue()) + " - " + String.valueOf(ds.child("Time").child("End").getValue())));
//                        listDataHeader.add(String.valueOf(ds.child("Schedule").child("Monday").getValue()));
//                    }
//                }
//
//
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//    }
//});
//



        }

        }



