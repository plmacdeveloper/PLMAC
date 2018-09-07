package plm.oop.com.plmac;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FacultyViewProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private Button fvp_up,fh_ca;
    private TextView tvFacultyViewProfileName;
    private  TextView tvFacultyViewProfileCollege;
    private TextView tvFacultyViewProfileIDNumber;
    private SurfaceView cameraPreview;
    private TextView textResult;
    private BarcodeDetector barcodeDetector;
    volatile String codeToSend;
    private CameraSource cameraSource;
    private SurfaceHolder holder;
    final int RequestCameraPermissionID = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_profile);
        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
        final String userName = facultyPref.getString("userName", "");
        final String userNumber = facultyPref.getString("userNumber", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Faculty");
        Query query =mRef.orderByChild("userNumber").equalTo(userNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {

                                                         for (DataSnapshot faculty : dataSnapshot.getChildren()) {
                                                             tvFacultyViewProfileName.setText("Profile Name: "+faculty.child("userName").getValue(String.class));
                                                             tvFacultyViewProfileCollege.setText("Program: "+ faculty.child("userProgram").getValue(String.class));
                                                             tvFacultyViewProfileIDNumber.setText("User Number: "+faculty.child("userNumber").getValue(String.class));
                                                         }


                                                 }

                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                 }
                                             });
        tvFacultyViewProfileCollege=findViewById(R.id.tvFacultyViewProfileCollege);
        tvFacultyViewProfileIDNumber=findViewById(R.id.tvFacultyViewProfileIDNumber);
        tvFacultyViewProfileName=findViewById(R.id.tvFacultyViewProfileName);
        fh_ca=findViewById(R.id.btFacultyHomeCheckAttendance);

        final ArrayList<String> startTimeCheck = new ArrayList<>();
        final ArrayList<String> endTimeCheck = new ArrayList<>();
        final ArrayList<String> subjectCodeCheck = new ArrayList<>();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Subject");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                final String dayOfWeek = sdf.format(cal.getTime());
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (userName.compareTo(ds.child("Faculty").getValue(String.class)) == 0) {
                        DatabaseReference refSchedule = firebaseDatabase.getReference("Subject").child(ds.getKey()).child("Schedule");
                        refSchedule.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dsSched : dataSnapshot.getChildren()) {
                                    if (dayOfWeek.matches(dsSched.getKey())) {
                                        startTimeCheck.add(ds.child("Time").child("Start").getValue().toString());
                                        endTimeCheck.add(ds.child("Time").child("End").getValue().toString());
                                        subjectCodeCheck.add(ds.getKey());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final Thread checkDateTime = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Button btnCheckAttendance= findViewById(R.id.btFacultyHomeCheckAttendance);
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm aa");
                                SimpleDateFormat dayDate = new SimpleDateFormat("MMMM-dd-yyyy");
                                String currentTime = dayTime.format(c.getTime());
                                String currentDate = dayDate.format(c.getTime());
                                Date cTime = null;
                                Date sTime = null;
                                Date eTime = null;
                                for (int i = 0; i < startTimeCheck.size(); i++) {
                                    try {
                                        cTime = dayTime.parse(currentTime);
                                        eTime = dayTime.parse(endTimeCheck.get(i));
                                        sTime = dayTime.parse(startTimeCheck.get(i));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (cTime.before(sTime) || cTime.after(eTime)) {
                                        btnCheckAttendance.setVisibility(View.GONE);

                                    } else {
                                        btnCheckAttendance.setVisibility(View.VISIBLE);

                                        DatabaseReference addAttendance = firebaseDatabase.getReference("Subject");
                                        addAttendance.child(subjectCodeCheck.get(i)).child("Attendance").child(currentDate).child(" ").setValue(" ");
                                        codeToSend = subjectCodeCheck.get(0);
                                        interrupt();
                                        break;
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.i("INT", "Exit.");
                }
            }
        };
        checkDateTime.start();
        fh_ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacultyViewProfile.this, FacultyCheckAttendance.class);
                i.putExtra("subjectCode", codeToSend);
                startActivity(i);
            }
        });


        //MENU

        Toolbar mToolbar = findViewById(R.id.nav_action_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);


        DrawerLayout mDrawerLayout = findViewById(R.id.drawerLayoutFaculty);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.headerFacultyName);
        navUsername.setText(userName);
        TextView navUsernumber = (TextView) headerView.findViewById(R.id.headerFacultyNumber);
        navUsernumber.setText(userNumber);
        //END OF MENU
    }

    public void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
                        SharedPreferences.Editor editor = facultyPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(FacultyViewProfile.this, IntroScreenActivity.class));
                        finish();
                    }
                }).create().show();
        Log.i("Back", "Back");
    }


    public void viewHome() {
//        startActivity(new Intent(FacultyViewProfile.this, FacultyViewProfile.class));
    }

    public void viewNews() {
        startActivity(new Intent(FacultyViewProfile.this, NewsFacultyActivity.class));
        finish();
    }

    public void viewUpdatePassword() {
        startActivity(new Intent(FacultyViewProfile.this, FacultyUpdatePassword.class));
        finish();
    }

    public void viewSubjects() {
        startActivity(new Intent(FacultyViewProfile.this, FacultyViewSubject.class));
        finish();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutFaculty);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        logout();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(FacultyViewProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
