package plm.oop.com.plmac;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

public class FacultyHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button fh_vp, fh_ss, fh_ca, fh_sg;
    private TextView welcomeMessage;
    volatile String codeToSend;
    String UserNameGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);
        fh_ca = findViewById(R.id.btFacultyHomeCheckAttendance);

        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
        final String userName = facultyPref.getString("userName", "");
        final String userNumber = facultyPref.getString("userNumber", "");
        UserNameGlobal = userName;
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
                                Button checkAttendance = findViewById(R.id.btFacultyHomeCheckAttendance);
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
                                        checkAttendance.setVisibility(View.GONE);
                                    } else {
                                        checkAttendance.setVisibility(View.VISIBLE);
                                        DatabaseReference addAttendance = firebaseDatabase.getReference("Subject");
                                        addAttendance.child(subjectCodeCheck.get(i)).child("Attendance").child(currentDate).child(" ").setValue(" ");
                                        codeToSend = subjectCodeCheck.get(i);
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

        welcomeMessage = findViewById(R.id.welcomeMessage);
        String welcome = "Welcome " + userName + "!";
        welcomeMessage.setText(welcome);
        fh_ss = findViewById(R.id.btFacultyHomeSelectSubject);
        fh_vp = findViewById(R.id.btFacultyHomeViewProfile);
        fh_sg = findViewById(R.id.btFacultyHomeSendGrade);
        DatabaseReference checkSend = firebaseDatabase.getReference("Admin").child("SendGrade");
        checkSend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().compareTo("false") == 0) {
                    fh_sg.setVisibility(View.GONE);
                } else {
                    fh_sg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fh_sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                    Log.e("ExelLog", "Storage not available or read only");
                } else {
                    final String fileName = userNumber + ".xls";
                    final Workbook wb = new HSSFWorkbook();
                    final CellStyle cs = wb.createCellStyle();
                    cs.setFillForegroundColor(HSSFColor.LIME.index);
                    cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Subject");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Cell cell = null;
                            for (DataSnapshot sub : dataSnapshot.getChildren()) {
                                if (userName.compareTo(sub.child("Faculty").getValue(String.class)) == 0) {
                                    final ArrayList<String> studentsExcel = new ArrayList<>();
                                    final ArrayList<String> attendanceExcel = new ArrayList<>();
                                    Sheet sheet = wb.createSheet(sub.getKey());
                                    sheet.setColumnWidth(0,(15*500));
                                    for (DataSnapshot addStudent : dataSnapshot.child(sub.getKey()).child("Students").getChildren()) {
                                        String name = addStudent.getValue(String.class);
                                        studentsExcel.add(name);
                                    }
                                    Row headings = sheet.createRow(0);
                                    cell = headings.createCell(0);
                                    cell.setCellValue("Student");
                                    int ctrDate = 1;
                                    for (DataSnapshot attendance : dataSnapshot.child(sub.getKey()).child("Attendance").getChildren()){
                                        String date = attendance.getKey();
                                        attendanceExcel.add(date);
                                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM-dd-yyyy");
                                        SimpleDateFormat ndf = new SimpleDateFormat("MM/dd/yy");
                                        Date holder = null;
                                        try {
                                            holder = sdf.parse(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        date = ndf.format(holder);
                                        cell = headings.createCell(ctrDate);
                                        cell.setCellValue(date);
                                        sheet.setColumnWidth(ctrDate,(15*150));
                                        ctrDate++;
                                    }
                                for(int perStudent = 0 ; perStudent<studentsExcel.size();perStudent++) {
                                    Row names = sheet.createRow(perStudent + 1);
                                    cell = names.createCell(0);
                                    cell.setCellValue(studentsExcel.get(perStudent));
                                    for(int perAttendance = 0 ; perAttendance<attendanceExcel.size() ; perAttendance++) {
                                        for (DataSnapshot stud : dataSnapshot.child(sub.getKey()).child("Attendance").child(attendanceExcel.get(perAttendance)).getChildren()) {
                                            if(stud.getKey().matches(studentsExcel.get(perStudent))){
                                                String status = stud.getValue(String.class);
                                                cell = names.createCell(perAttendance+1);
                                                cell.setCellValue(status);
                                            }
                                        }
                                    }
                                }
                                }
                            }
                            File file = new File(FacultyHomeActivity.this.getExternalFilesDir(null), fileName);
                            FileOutputStream os = null;

                            try {
                                os = new FileOutputStream(file);
                                wb.write(os);
                                Log.w("FileUtils", "Writing file" + file);
                            } catch (IOException e) {
                                Log.w("FileUtils", "Error writing " + file, e);
                            } catch (Exception e) {
                                Log.w("FileUtils", "Failed to save file", e);
                            } finally {
                                try {
                                    if (null != os)
                                        os.close();
                                } catch (Exception ex) {
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

//
////                        for (DataSnapshot atten : dataSnapshot.child(ds.getKey()).child("Attendance").getChildren()) {
////                            int count = 1;
////                            for (final DataSnapshot date : dataSnapshot.getChildren()) {
////                                cell = headings.createCell(count);
////                                cell.setCellValue(date.getKey());
////                                count++;
////                                for (DataSnapshot stud : dataSnapshot.child(ds.getKey()).child("Attendance").child(atten.getKey()).getChildren()) {
////                                    for (int a = 0; a < studentsExcel.size(); a++) {
////                                        if (ds.getKey().compareTo(studentsExcel.get(a)) == 0) {
////                                            Log.i("check", studentsExcel.get(a) + " is " + ds.getValue() + " last " + date.getKey());
////
            }
        });

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
                    startActivity(new Intent(FacultyHomeActivity.this, IntroScreenActivity.class));
                    finish();
                }
            }).create().show();
        Log.i("Back", "Back");
    }


    public void viewHome() {
        startActivity(new Intent(FacultyHomeActivity.this, FacultyHomeActivity.class));
    }

    public void viewNews() {
        startActivity(new Intent(FacultyHomeActivity.this, NewsFacultyActivity.class));
    }

    public void viewUpdatePassword() {
        startActivity(new Intent(FacultyHomeActivity.this, FacultyUpdatePassword.class));
    }

    public void viewSubjects() {
        startActivity(new Intent(FacultyHomeActivity.this, FacultyViewSubject.class));
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
    //END MENU


    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
       logout();
    }
}
