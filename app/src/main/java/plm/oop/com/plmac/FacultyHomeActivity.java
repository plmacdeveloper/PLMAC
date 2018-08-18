package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FacultyHomeActivity extends AppCompatActivity {

    private Button fh_vp;
    private Button fh_ss;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);
        Intent i= getIntent();
        final String userNumber= i.getStringExtra("userNumber");
        final String userName= i.getStringExtra("userName");
        welcomeMessage = findViewById(R.id.welcomeMessage);
        String welcome="Welcome "+userName+"!";
        welcomeMessage.setText(welcome);

        fh_ss=findViewById(R.id.btFacultyHomeSelectSubject);
        fh_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacultyHomeActivity.this,FacultyViewSubject.class);
                i.putExtra("userName", userName);
                i.putExtra("userName",userName);
                startActivity(i);
            }
        });

        fh_vp=findViewById(R.id.btFacultyHomeViewProfile);
        fh_vp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacultyHomeActivity.this, FacultyViewProfile.class);
                i.putExtra("userNumber", userNumber);
                startActivity(i);
            }


        });

    }




}
