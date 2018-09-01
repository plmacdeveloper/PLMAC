package plm.oop.com.plmac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
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
    List<String> listDataHeaderCode;
    List<String> listDataHeaderName;
    List<String> listDataHeaderRoom;
    List<String> listDataHeaderTime;
    List<String> listDataHeaderDays;
    List<String> listgetAttendanceDate;
    HashMap<String, List<String>> listChildData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_subject);
        expListView = findViewById(R.id.explv1);

        listDataHeaderCode = new ArrayList<>();
        listDataHeaderName = new ArrayList<>();
        listDataHeaderRoom = new ArrayList<>();
        listDataHeaderTime = new ArrayList<>();
        listDataHeaderDays = new ArrayList<>();
        listChildData = new HashMap<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = firebaseDatabase.getReference("Subject");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    SharedPreferences facultyPref = getSharedPreferences("Faculty",0);
                    String userNameFacultyPref = facultyPref.getString("userName", "");
                    if(userNameFacultyPref.compareTo(ds.child("Faculty").getValue(String.class)) == 0){
                        listDataHeaderCode.add(ds.getKey());
                        listDataHeaderName.add(ds.child("Name").getValue(String.class));
                        listDataHeaderRoom.add(ds.child("Room").getValue(String.class));
                        listDataHeaderTime.add(ds.child("Time").child("Start").getValue(String.class)
                        + " - " + ds.child("Time").child("End").getValue(String.class));
                        String conSchedule = "";
                        ArrayList<String> DaysOfTheWeek = new ArrayList<>();
                        DaysOfTheWeek.add("Monday");
                        DaysOfTheWeek.add("Tuesday");
                        DaysOfTheWeek.add("Wednesday");
                        DaysOfTheWeek.add("Thursday");
                        DaysOfTheWeek.add("Friday");
                        DaysOfTheWeek.add("Saturday");
                        DaysOfTheWeek.add("Sunday");
                        DaysOfTheWeek.add("M");
                        DaysOfTheWeek.add("T");
                        DaysOfTheWeek.add("W");
                        DaysOfTheWeek.add("Th");
                        DaysOfTheWeek.add("F");
                        DaysOfTheWeek.add("Sa");
                        DaysOfTheWeek.add("Su");
                        for(int count = 0; count < 7; count++){
                            if(ds.child("Schedule").hasChild(DaysOfTheWeek.get(count))){
                                conSchedule = conSchedule.concat(DaysOfTheWeek.get(count+7)+" ");
                            }
                        }
                        listDataHeaderDays.add(conSchedule);
                        listgetAttendanceDate = new ArrayList<>();
                        if(dataSnapshot.child(ds.getKey()).child("Attendance").getChildrenCount() != 0){
                            for(DataSnapshot atten : dataSnapshot.child(ds.getKey()).child("Attendance").getChildren()){
                                listgetAttendanceDate.add(atten.getKey());
                            }
                            listChildData.put(ds.getKey(),listgetAttendanceDate);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expListAdapter = new ExpandableListAdapters(this, listDataHeaderCode, listDataHeaderName, listDataHeaderRoom, listDataHeaderTime, listDataHeaderDays, listChildData);

        expListView.setAdapter(expListAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View v, int groupPosition, long id) {
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}



