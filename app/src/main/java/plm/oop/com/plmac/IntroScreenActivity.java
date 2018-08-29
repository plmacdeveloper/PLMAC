package plm.oop.com.plmac;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
        SharedPreferences studentPref = getSharedPreferences("Student",0);
        String userNumberStudentPref = studentPref.getString("userNumber","");
        Boolean checkIsLoggedIn = studentPref.getBoolean("isLoggedIn",false);
        SharedPreferences facultyPref = getSharedPreferences("Faculty",0);
        String userNumberFacultyPref = facultyPref.getString("userNumber","");
        String userNameFacultyPref = facultyPref.getString("userName", "");
        Boolean checkIsLoggedIn2 = facultyPref.getBoolean("isLoggedIn",false);
        if(checkIsLoggedIn){
            Intent i = new Intent(IntroScreenActivity.this,StudentMainActivity.class);
            i.putExtra("userNumber",userNumberStudentPref);
            startActivity(i);
        }else if(checkIsLoggedIn2){
            Intent i = new Intent(IntroScreenActivity.this,FacultyHomeActivity.class);
            i.putExtra("userNumber",userNumberFacultyPref);
            i.putExtra("userName",userNameFacultyPref);
            startActivity(i);
        }
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
