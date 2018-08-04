package plm.oop.com.plmac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText passEmail;
    private Button passReset, back;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        passEmail = findViewById(R.id.etEmailResetPassword);
        passReset = findViewById(R.id.btReset);
        back = findViewById(R.id.btForgotBack);
        firebaseAuth = FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        passReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordEmail = passEmail.getText().toString().trim();

                if(passwordEmail.equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Enter your email.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(passwordEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                                finish();
                            }else{
                                Toast.makeText(ForgotPasswordActivity.this, "Error. Not sent.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }
}
