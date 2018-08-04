package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentMainActivity extends AppCompatActivity {

    private Button s_viewprofile;
    private Button s_viewattendance;
    private Button s_viewannouncements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        s_viewprofile=findViewById(R.id.btStudentMainViewProfile);
        s_viewattendance=findViewById(R.id.btStudentMainViewAttendance);
        s_viewannouncements=findViewById(R.id.btStudentMainViewAnnouncements);

        s_viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMainActivity.this,StudentViewProfile.class));
            }
        });

        s_viewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMainActivity.this,StudentViewAttendance.class));
            }
        });

        s_viewannouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMainActivity.this,StudentViewAnnouncements.class));
            }
        });

    }
}
