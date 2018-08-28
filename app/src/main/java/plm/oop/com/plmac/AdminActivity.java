package plm.oop.com.plmac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Button adminStudent, adminFaculty, adminSubject;
    private Switch adminEnableSendGrade;
    private TextView textSendGradeEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminFaculty = findViewById(R.id.btAdminFacultyInformation);
        adminStudent = findViewById(R.id.btAdminStudentInformation);
        adminSubject = findViewById(R.id.btAdminSubjectInformation);
        adminEnableSendGrade = findViewById(R.id.btEnableSendGrade);
        textSendGradeEnable = findViewById(R.id.tvSendGradeEnable);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference checkRef = firebaseDatabase.getReference("Admin").child("SendGrade");
        checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String check = dataSnapshot.getValue().toString();
                if (check.compareTo("false") == 0) {
                    Log.i("set","false");
                    adminEnableSendGrade.setChecked(false);
                    textSendGradeEnable.setText("Send Grade is Disabled.");
                } else {
                    Log.i("set","true");
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
        Log.i("press","Pressed.");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference sendRef = firebaseDatabase.getReference("Admin").child("SendGrade");
                if(adminEnableSendGrade.isChecked()){
                    Log.i("change","false to true");
                    sendRef.setValue("true");
                    textSendGradeEnable.setText("Send Grade is Enabled.");
                } else {
                    Log.i("change","true to false");
                    sendRef.setValue("false");
                    textSendGradeEnable.setText("Send Grade is Disabled.");
                }
    }
}
