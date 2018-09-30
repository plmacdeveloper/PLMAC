package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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

public class FacultyActivity extends AppCompatActivity {

    private Button facultyLogin;
    private EditText facultyUsername, facultyPassword;
    private ProgressDialog progressDialog;
    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;
    private ScrollView scrollView;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            scrollView.setVisibility(View.VISIBLE);
            facultyLogin.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        progressDialog = new ProgressDialog(this);
        facultyLogin = findViewById(R.id.btFacultyLogin);
        facultyUsername = findViewById(R.id.etFacultyUsername);
        facultyPassword = findViewById(R.id.etFacultyPassword);
        usernameWrapper = findViewById(R.id.wrapperFacultyUsername);
        passwordWrapper = findViewById(R.id.wrapperFacultyPassword);
        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");

        scrollView = findViewById(R.id.linlay1);
        handler.postDelayed(runnable, 1000);

        facultyLogin.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if(!validateForm()){

                }else {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mRef = database.getReference("Faculty");
                    Query query = mRef.orderByChild("userNumber").equalTo(facultyUsername.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
//                                    startActivity(new Intent(FacultyActivity.this, FacultyHomeActivity.class));
//
                                for (DataSnapshot faculty : dataSnapshot.getChildren()) {
//                                Toast.makeText(getApplicationContext(), faculty.child("userProgram").getValue(String.class), Toast.LENGTH_SHORT).show();
                                    if (faculty.child("userPassword").getValue(String.class).equals(facultyPassword.getText().toString())) {
                                        Toast.makeText(FacultyActivity.this, "Hello " + faculty.child("userName").getValue(String.class) + "! Login Successful!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
                                        SharedPreferences.Editor editor = facultyPref.edit();
                                        editor.putString("userNumber", faculty.child("userNumber").getValue(String.class));
                                        editor.putString("userName", faculty.child("userName").getValue(String.class));
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.apply();
                                        Intent i = new Intent(FacultyActivity.this, FacultyViewProfile.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid User Number and Password", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid User Number and Password", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

    }
    private boolean validateForm() {
        boolean valid = true;

        String usernumber = facultyUsername.getText().toString();
        if (TextUtils.isEmpty(usernumber)) {
            usernameWrapper.setError("Required");
            valid = false;
        } else {
            usernameWrapper.setError(null);
        }
        String password = facultyPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordWrapper.setError("Required");
            valid = false;
        } else {
            passwordWrapper.setError(null);
        }


        return valid;
    }

    @Override
    public void onBackPressed() {
           startActivity(new Intent(this,IntroScreenActivity.class));
            finish();
    }
}
