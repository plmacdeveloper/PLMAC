package plm.oop.com.plmac;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Button;



public class IntroScreenActivity extends AppCompatActivity {

    private RelativeLayout rellay;
    private Button studentAccess,facultyAccess,information;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        rellay = findViewById(R.id.rellay);
        studentAccess = findViewById(R.id.btStudentAccess);
        facultyAccess = findViewById(R.id.btFacultyAccess);
        information = findViewById(R.id.btInformation);


        handler.postDelayed(runnable, 2000);

        studentAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroScreenActivity.this,StudentActivity.class));
            }
        });
        facultyAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroScreenActivity.this, FacultyActivity.class));
            }
        });
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroScreenActivity.this, InformationActivity.class));
            }
        });

    }
}
