package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentViewProfile extends AppCompatActivity {

    private Button svp_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_viewprofile);

        svp_up=findViewById(R.id.btStudentViewProfileUpdatePassword);
        svp_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StudentViewProfile.this,StudentUpdatePassword.class));

            }
        });

    }
}