package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FacultyViewProfile extends AppCompatActivity {


    private Button fvp_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_profile);

        fvp_up=findViewById(R.id.btFacultyViewProfileUpdatePassword);
        fvp_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FacultyViewProfile.this,FacultyUpdatePassword.class));

            }
        });


    }
}
