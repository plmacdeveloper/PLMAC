package plm.oop.com.plmac;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

public class FacultyHomeActivity extends AppCompatActivity {

    private Button fh_vp, fh_ss, fh_ca, fh_sg;
    private TextView welcomeMessage;
    volatile String codeToSend;
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
                if(dataSnapshot.getValue().toString().compareTo("false") == 0){
                    fh_sg.setVisibility(View.GONE);
                }else{
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
                saveExcelFile(FacultyHomeActivity.this,"myExcel.xls");
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

    }
    private static boolean saveExcelFile(Context context, String fileName) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("ExelLog", "Storage not available or read only");
            return false;
        }
        boolean success = false;
        Workbook wb = new HSSFWorkbook();
        Cell c = null;
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Subject1");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Item Number");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Quantity");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Price");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File("/storage/emulated/0/Android/data/plm.oop.com.plmac/files", fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.i("context",context.getExternalFilesDir(null).toString());
            Log.w("FileUtils", "Writing file" + file);
            success = true;
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
        return success;
    }
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
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences facultyPref = getSharedPreferences("Faculty",0);
                        SharedPreferences.Editor editor = facultyPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(FacultyHomeActivity.this,IntroScreenActivity.class));
                        finish();
                    }
                }).create().show();
        Log.i("Back","Back");
    }
}
