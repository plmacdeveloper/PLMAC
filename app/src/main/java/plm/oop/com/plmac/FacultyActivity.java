package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FacultyActivity extends AppCompatActivity {

    private Button facultyForgotPassword,facultyLogin;
    private EditText facultyUsername, facultyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        facultyForgotPassword = findViewById(R.id.btFacultyForgotPassword);
        facultyLogin = findViewById(R.id.btFacultyLogin);
        facultyUsername = findViewById(R.id.etFacultyUsername);
        facultyPassword = findViewById(R.id.etFacultyPassword);

        facultyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacultyActivity.this,FacultyHomeActivity.class));
            }
        });
        facultyForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacultyActivity.this,ForgotPasswordActivity.class));
            }
        });
    }
}
