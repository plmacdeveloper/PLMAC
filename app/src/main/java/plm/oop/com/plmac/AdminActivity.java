package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Button adminStudent, adminFaculty, adminSubject, addButtonNews;
    private Switch adminEnableSendGrade;
    private TextView textSendGradeEnable;
    private ImageButton addNews;
    private LinearLayout screenNews;
    private EditText addTitleNews, addContentNews;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        progressDialog = new ProgressDialog(this);
        addButtonNews = findViewById(R.id.btAddButtonNews);
        screenNews = findViewById(R.id.llAddNewsScreen);
        addTitleNews = findViewById(R.id.etAddTitleNews);
        addContentNews = findViewById(R.id.etAddContentNews);
        addNews = findViewById(R.id.ibAddNews);
        adminFaculty = findViewById(R.id.btAdminFacultyInformation);
        adminStudent = findViewById(R.id.btAdminStudentInformation);
        adminSubject = findViewById(R.id.btAdminSubjectInformation);
        adminEnableSendGrade = findViewById(R.id.btEnableSendGrade);
        textSendGradeEnable = findViewById(R.id.tvSendGradeEnable);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference checkRef = firebaseDatabase.getReference("Admin").child("SendGrade");

        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenNews.setVisibility(View.VISIBLE);
            }
        });
        addButtonNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                String title = addTitleNews.getText().toString();
                String content = addContentNews.getText().toString();
                if(!title.isEmpty()){
                    if(!content.isEmpty()){
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat dayTime = new SimpleDateFormat("MMMM-dd-yyyy hh:mm aa");
                        String currentDate = dayTime.format(c.getTime());
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference newsReference = firebaseDatabase.getReference("Admin").child("News").child(currentDate);
                        newsReference.child("Title").setValue(title);
                        newsReference.child("Content").setValue(content);
                        screenNews.setVisibility(View.GONE);
                        Toast.makeText(AdminActivity.this,title+" is added.",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else{
                        Toast.makeText(AdminActivity.this,"No content.",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }else{
                    Toast.makeText(AdminActivity.this,"No title.",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
        checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String check = dataSnapshot.getValue().toString();
                if (check.compareTo("false") == 0) {
                    Log.i("set", "false");
                    adminEnableSendGrade.setChecked(false);
                    textSendGradeEnable.setText("Send Grade is Disabled.");
                } else {
                    Log.i("set", "true");
                    adminEnableSendGrade.setChecked(true);
                    textSendGradeEnable.setText("Send Grade is Enabled.");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adminFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminFaculty.class));
            }
        });
        adminStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminStudent.class));
            }
        });
        adminSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminSubject.class));
            }
        });
        adminEnableSendGrade.setOnCheckedChangeListener(AdminActivity.this);


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        Log.i("press", "Pressed.");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference sendRef = firebaseDatabase.getReference("Admin").child("SendGrade");
        if (adminEnableSendGrade.isChecked()) {
            Log.i("change", "false to true");
            sendRef.setValue("true");
            textSendGradeEnable.setText("Send Grade is Enabled.");
        } else {
            Log.i("change", "true to false");
            sendRef.setValue("false");
            textSendGradeEnable.setText("Send Grade is Disabled.");
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(screenNews.getVisibility() == View.GONE) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(AdminActivity.this, IntroScreenActivity.class));
                            finish();
                        }
                    }).create().show();
        }else{
            screenNews.setVisibility(View.GONE);
        }
    }
}
