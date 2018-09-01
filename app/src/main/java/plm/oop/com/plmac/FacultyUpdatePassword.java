package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
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
import com.google.firebase.database.ValueEventListener;

public class FacultyUpdatePassword extends AppCompatActivity {

    private EditText fup_op;
    private EditText fup_np;
    private EditText fup_vnp;
    private Button fup_btsave;
    private ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("Faculty");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_update_password);
        progressDialog = new ProgressDialog(this);
        Intent i = getIntent();
        final String userNumber = i.getStringExtra("userNumber");
        fup_op = findViewById(R.id.etFacultyUpdatePasswordOldPassword);
        fup_np = findViewById(R.id.etFacultyUpdatePasswordNewPassword);
        fup_vnp = findViewById(R.id.etFacultyUpdatePasswordVerifyPassword);
        fup_btsave = findViewById(R.id.btFacultyUpdatePasswordSave);

        fup_btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();


                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldpasscheck;
                        String oldpass = fup_op.getText().toString().trim();
                        String newpass = fup_np.getText().toString().trim();
                        String verify = fup_vnp.getText().toString().trim();
                        oldpasscheck = dataSnapshot.child(userNumber).child("userPassword").getValue().toString();
                        if (oldpass.isEmpty() || newpass.isEmpty() || verify.isEmpty()) {
                            Toast.makeText(FacultyUpdatePassword.this, "Incomplete Information.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            if (oldpass.matches(oldpasscheck)) {
                                if (newpass.matches(verify)) {
                                    String code = userNumber;
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = firebaseDatabase.getReference("Faculty");
                                    myRef.child(code).child("userPassword").setValue(newpass);
                                    Toast.makeText(FacultyUpdatePassword.this, "Password Change Complete.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    onBackPressed();
                                    finish();
                                } else {
                                    Toast.makeText(FacultyUpdatePassword.this, "Password Verification mismatch.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(FacultyUpdatePassword.this, "Wrong Old Password.", Toast.LENGTH_SHORT).show();
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
