package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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


            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference("Faculty");
                Query query =mRef.orderByChild("userNumber").equalTo(facultyUsername.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
//                                    startActivity(new Intent(FacultyActivity.this, FacultyHomeActivity.class));
//
                            for (DataSnapshot faculty : dataSnapshot.getChildren()) {
//                                Toast.makeText(getApplicationContext(), faculty.child("userProgram").getValue(String.class), Toast.LENGTH_SHORT).show();
                            if (faculty.child("userPassword").getValue(String.class).equals(facultyPassword.getText().toString())){
//                                startActivity(new Intent(FacultyActivity.this, FacultyHomeActivity.class));
                                Intent i = new Intent(FacultyActivity.this, FacultyHomeActivity.class);
                                i.putExtra("userNumber",faculty.child("userNumber").getValue(String.class));
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Invalid User Number and Password", Toast.LENGTH_SHORT).show();
                            }
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Invalid User Number and Password", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




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
