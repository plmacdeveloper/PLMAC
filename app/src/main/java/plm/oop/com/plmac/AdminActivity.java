package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

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

        if(adminEnableSendGrade.isChecked()){
                textSendGradeEnable.setText("Send Grade is Enabled.");
        }else{
            textSendGradeEnable.setText("Send Grade is Disabled.");
        }
    }
}
