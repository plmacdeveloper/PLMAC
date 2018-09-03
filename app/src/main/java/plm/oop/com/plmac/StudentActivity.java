package plm.oop.com.plmac;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentActivity extends AppCompatActivity {

    private Button  studentLogin;
    private EditText studentUsername, studentPassword;

    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            scrollView.setVisibility(View.VISIBLE);
            studentLogin.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        progressDialog = new ProgressDialog(this);
        studentLogin = findViewById(R.id.btStudentLogin);

        studentUsername = findViewById(R.id.etStudentUsername);
        studentPassword = findViewById(R.id.etStudentPassword);
        scrollView = findViewById(R.id.linlay11);
        handler.postDelayed(runnable, 1000);

        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference("Student");
                Query query =mRef.orderByChild("userNumber").equalTo(studentUsername.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
//                                    startActivity(new Intent(FacultyActivity.this, FacultyHomeActivity.class));
//
                            for (DataSnapshot student : dataSnapshot.getChildren()) {
//                                Toast.makeText(getApplicationContext(), faculty.child("userProgram").getValue(String.class), Toast.LENGTH_SHORT).show();
                                if (student.child("userPassword").getValue(String.class).equals(studentPassword.getText().toString())){
//                                startActivity(new Intent(FacultyActivity.this, FacultyHomeActivity.class));
                                    Toast.makeText(StudentActivity.this,"Hello "+student.child("userName").getValue(String.class)+"! Login Successful!",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    SharedPreferences studentPref = getSharedPreferences("Student",0);
                                    SharedPreferences.Editor editor = studentPref.edit();
                                    editor.putString("userNumber",student.child("userNumber").getValue(String.class));
                                    editor.putString("userName",student.child("userName").getValue(String.class));
                                    editor.putBoolean("isLoggedIn",true);
                                    editor.apply();
                                    Intent i = new Intent(StudentActivity.this, StudentMainActivity.class);
                                    i.putExtra("userNumber",student.child("userNumber").getValue(String.class));
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Invalid User Number and Password", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid User Number and Password", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
            });
            }
        });



    }
}
