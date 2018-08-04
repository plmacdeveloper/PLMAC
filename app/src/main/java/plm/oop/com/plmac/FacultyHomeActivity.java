package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FacultyHomeActivity extends AppCompatActivity {

    private Button fh_vp;
    private Button fh_ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);

        fh_ss=findViewById(R.id.btFacultyHomeSelectSubject);
        fh_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        fh_vp=findViewById(R.id.btFacultyHomeViewProfile);
        fh_vp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FacultyHomeActivity.this,FacultyViewProfile.class));

            }
        });

    }




}
