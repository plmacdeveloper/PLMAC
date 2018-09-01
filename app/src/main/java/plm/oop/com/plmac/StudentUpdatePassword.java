package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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

public class StudentUpdatePassword extends AppCompatActivity {

    private EditText sup_op;
    private EditText sup_np;
    private EditText sup_vnp;
    private Button sup_btsave;
    private ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("Student");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_updatepassword);
        progressDialog = new ProgressDialog(this);
        sup_op=findViewById(R.id.etStudentUpdatePasswordOldPassword);
        sup_np=findViewById(R.id.etStudentUpdatePasswordNewPassword);
        sup_vnp=findViewById(R.id.etStudentUpdatePasswordVerifyPassword);
        sup_btsave=findViewById(R.id.btFacultyUpdatePasswordSave);

        sup_btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SharedPreferences studentPref = getSharedPreferences("Student",0);
                        String userNumber = studentPref.getString("userNumber","");
                        String oldpasscheck;
                        String oldpass= sup_op.getText().toString().trim();
                        String newpass= sup_np.getText().toString().trim();
                        String verify= sup_vnp.getText().toString().trim();
                        oldpasscheck = dataSnapshot.child(userNumber).child("userPassword").getValue(String.class);
                        if (oldpass.isEmpty() || newpass.isEmpty() || verify.isEmpty() || oldpasscheck.isEmpty()) {
                            Toast.makeText(StudentUpdatePassword.this, "Incomplete Information.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            if (oldpass.compareTo(oldpasscheck) == 0) {
                                if (newpass.compareTo(verify) == 0) {
                                    String code = userNumber;
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = firebaseDatabase.getReference("Student");
                                    myRef.child(code).child("userPassword").setValue(newpass);
                                    Toast.makeText(StudentUpdatePassword.this, "Password Change Complete.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    onBackPressed();
                                    finish();
                                } else {
                                    Toast.makeText(StudentUpdatePassword.this, "Password Verification mismatch.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(StudentUpdatePassword.this, "Wrong Old Password.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });



    }
}
