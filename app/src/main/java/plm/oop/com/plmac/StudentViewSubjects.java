package plm.oop.com.plmac;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentViewSubjects extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ExpandableListAdapter expListAdapter;
    ExpandableListView expListView;
    List<String> listDataHeaderCode;
    List<String> listDataHeaderName;
    List<String> listDataHeaderRoom;
    List<String> listDataHeaderTime;
    List<String> listDataHeaderDays;
    List<String> listgetAttendanceDate;
    List<String> listgetAttendanceStatus;
    HashMap<String, List<String>> listChildDataDate;
    HashMap<String, List<String>> listChildDataStatus;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences studentPref = getSharedPreferences("Student", 0);
        final String userNumber = studentPref.getString("userNumber", "");
        String userName = studentPref.getString("userName", "");
        setContentView(R.layout.activity_student_view_subject);
        expListView = findViewById(R.id.explv2);
        progressDialog = new ProgressDialog(this);
        listDataHeaderCode = new ArrayList<>();
        listDataHeaderName = new ArrayList<>();
        listDataHeaderRoom = new ArrayList<>();
        listDataHeaderTime = new ArrayList<>();
        listDataHeaderDays = new ArrayList<>();
        listChildDataDate = new HashMap<>();
        listChildDataStatus = new HashMap<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = firebaseDatabase.getReference("Subject");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("Students").hasChild(userNumber)) {
                        String userNameStudentPref = ds.child("Students").child(userNumber).getValue(String.class);
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


                        for (int count = 0; count < 7; count++) {
                            if (ds.child("Schedule").hasChild(DaysOfTheWeek.get(count))) {
                                conSchedule = conSchedule.concat(DaysOfTheWeek.get(count + 7) + " ");
                            }
                        }
                        listDataHeaderDays.add(conSchedule);
                        listgetAttendanceDate = new ArrayList<>();
                        listgetAttendanceStatus = new ArrayList<>();
                        if(dataSnapshot.child(ds.getKey()).child("Attendance").getChildrenCount() != 0){
                            for(DataSnapshot atten : dataSnapshot.child(ds.getKey()).child("Attendance").getChildren()){
                                listgetAttendanceDate.add(atten.getKey());
                                Log.i("atten",atten.getKey());
                                Log.i("atten",userNameStudentPref+"!");
                                for(DataSnapshot stat : dataSnapshot.child(ds.getKey()).child("Attendance").child(atten.getKey()).getChildren()){
                                    Log.i("atten",stat.getKey());
                                    Log.i("atten",String.valueOf(userNameStudentPref.compareTo(stat.getKey())));
                                    if(userNameStudentPref.compareTo(stat.getKey()) == 0){
                                        listgetAttendanceStatus.add(stat.getValue(String.class));
                                        Log.i("atten",stat.getValue(String.class));
                                    }
                                }
                            }
                                listChildDataDate.put(ds.getKey(), listgetAttendanceDate);
                                listChildDataStatus.put(ds.getKey(),listgetAttendanceStatus);
                        }else{
                            ArrayList<String> listNothing = new ArrayList<>();
                            listNothing.add("No information.");
                            ArrayList<String> listNothing1 = new ArrayList<>();
                            listNothing1.add(" ");
                            listChildDataDate.put(ds.getKey(),listNothing);
                            listChildDataStatus.put(ds.getKey(),listNothing1);
                        }


                    }
                }

                expListAdapter = new ExpandableListAdapterStudent(StudentViewSubjects.this, listDataHeaderCode, listDataHeaderName, listDataHeaderRoom, listDataHeaderTime, listDataHeaderDays, listChildDataDate, listChildDataStatus);

                expListView.setAdapter(expListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View v, int groupPosition, long id) {
                Toast.makeText(StudentViewSubjects.this,"Click on "+listDataHeaderCode.get(groupPosition),Toast.LENGTH_SHORT).show();
                if(expandableListView.isGroupExpanded(groupPosition)){
                    expandableListView.collapseGroup(groupPosition);
                }else{
                    expandableListView.expandGroup(groupPosition);
                }
                return true;
            }
        });

 //MENU
        Toolbar mToolbar = findViewById(R.id.nav_action_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);


        DrawerLayout mDrawerLayout = findViewById(R.id.drawerLayoutStudent);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.headerStudentName);
        navUsername.setText(userName);
        TextView navUsernumber = (TextView) headerView.findViewById(R.id.headerStudentNumber);
        navUsernumber.setText(userNumber);
        //END OF MENU
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutStudent);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(StudentViewSubjects.this, StudentMainActivity.class));
            finish();
        }
    }

    public void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences studentPref = getSharedPreferences("Student", 0);
                        SharedPreferences.Editor editor = studentPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(StudentViewSubjects.this, IntroScreenActivity.class));
                        finish();
                    }
                }).create().show();
    }


    public void viewHome() {
        startActivity(new Intent(StudentViewSubjects.this, StudentMainActivity.class));
    }

    public void viewNews() {
        startActivity(new Intent(StudentViewSubjects.this, NewsStudentActivity.class));
    }

    public void viewUpdatePassword() {
        startActivity(new Intent(StudentViewSubjects.this, StudentUpdatePassword.class));
    }

    public void viewSubjects() {
        startActivity(new Intent(StudentViewSubjects.this, StudentViewSubjects.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.nav_home) {
            viewNews();
        } else if (i == R.id.nav_profile) {
            viewHome();
        } else if (i == R.id.nav_update_password) {
            viewUpdatePassword();
        } else if (i == R.id.nav_subjects) {
            viewSubjects();
        } else if (i == R.id.nav_logout) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutStudent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //END MENU
    }


