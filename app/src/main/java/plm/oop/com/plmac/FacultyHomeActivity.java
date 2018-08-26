package plm.oop.com.plmac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FacultyHomeActivity extends AppCompatActivity {

    private Button fh_vp;
    private Button fh_ss, fh_ca;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);
        fh_ca = findViewById(R.id.btFacultyHomeCheckAttendance);
        Intent i = getIntent();
        final String userNumber = i.getStringExtra("userNumber");
        final String userName = i.getStringExtra("userName");
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
                    if (userName.compareTo(ds.child("Faculty").getValue().toString()) == 0) {
                        DatabaseReference refSchedule = firebaseDatabase.getReference("Subject").child(ds.getKey()).child("Schedule");
                        refSchedule.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dsSched : dataSnapshot.getChildren()) {
                                    if (dayOfWeek.compareTo(dsSched.getKey().toString()) == 0) {
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
                                Button checkAttendance = (Button) findViewById(R.id.btFacultyHomeCheckAttendance);
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm aa");
                                SimpleDateFormat dayDate = new SimpleDateFormat("MMMM-dd-yyyy");
                                String currentTime = dayTime.format(c.getTime());
                                String currentDate = dayDate.format(c.getTime());
                                Log.i("DATE",currentDate);
                                Date cTime = null;
                                Date sTime = null;
                                Date eTime = null;
                                for(int i=0;i<startTimeCheck.size();i++) {
                                    try {
                                        cTime = dayTime.parse(currentTime);
                                        eTime = dayTime.parse(endTimeCheck.get(i));
                                        sTime = dayTime.parse(startTimeCheck.get(i));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (cTime.before(sTime) || cTime.after(eTime)) {
                                        checkAttendance.setVisibility(View.GONE);
                                    } else {
                                        checkAttendance.setVisibility(View.VISIBLE);
                                    DatabaseReference addAttendance = firebaseDatabase.getReference("Subject");
                                    addAttendance.child(subjectCodeCheck.get(i)).child("Attendance").child(currentDate).setValue("true");
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.i("INT", "ERROR BLAH");
                }
            }
        };
        checkDateTime.start();
        welcomeMessage = findViewById(R.id.welcomeMessage);
        String welcome = "Welcome " + userName + "!";
        welcomeMessage.setText(welcome);
        fh_ss = findViewById(R.id.btFacultyHomeSelectSubject);
        fh_vp = findViewById(R.id.btFacultyHomeViewProfile);


        fh_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDateTime.interrupt();
                Intent gotoSS = new Intent(FacultyHomeActivity.this, FacultyViewSubject.class);
                gotoSS.putExtra("userNumber", userNumber);
                gotoSS.putExtra("userName", userName);
                startActivity(gotoSS);
            }
        });

        fh_vp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDateTime.interrupt();
                Intent i = new Intent(FacultyHomeActivity.this, FacultyViewProfile.class);
                i.putExtra("userNumber", userNumber);
                startActivity(i);
            }


        });
        fh_ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacultyHomeActivity.this, FacultyCheckAttendance.class);
                i.putExtra("userNumber", userNumber);
                startActivity(i);
            }
        });

    }


}
