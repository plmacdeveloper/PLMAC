package plm.oop.com.plmac;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StudentActivity extends AppCompatActivity {

    private Button  studentLogin, studentForgotPassword;
    private EditText studentUsername, studentPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        studentLogin = findViewById(R.id.btStudentLogin);
        studentForgotPassword = findViewById(R.id.btStudentForgotPassword);
        studentUsername = findViewById(R.id.etStudentUsername);
        studentPassword = findViewById(R.id.etStudentPassword);



        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentUsername = studentLogin.getText().toString().trim();

                if(studentUsername != null){

                }
                startActivity(new Intent(StudentActivity.this, StudentMainActivity.class));
            }
        });
        studentForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentActivity.this, ForgotPasswordActivity.class));
            }
        });

    }
}
