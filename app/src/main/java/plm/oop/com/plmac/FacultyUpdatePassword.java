package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyUpdatePassword extends FacultyBaseActivity{

    private EditText fup_op;
    private EditText fup_np;
    private EditText fup_vnp;
    private Button fup_btsave;
    private ProgressDialog progressDialog;
    private TextInputLayout oldPasswordWrapper;
    private TextInputLayout newPasswordWrapper;
    private TextInputLayout verifyPasswordWrapper;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("Faculty");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_update_password);
        displayDrawer();
        progressDialog = new ProgressDialog(this);
        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
        final String userName = facultyPref.getString("userName", "");
        final String userNumber = facultyPref.getString("userNumber", "");

        fup_op = findViewById(R.id.etFacultyUpdatePasswordOldPassword);
        fup_np = findViewById(R.id.etFacultyUpdatePasswordNewPassword);
        fup_vnp = findViewById(R.id.etFacultyUpdatePasswordVerifyPassword);
        fup_btsave = findViewById(R.id.btFacultyUpdatePasswordSave);
        oldPasswordWrapper = findViewById(R.id.wrapperOldPassword);
        newPasswordWrapper = findViewById(R.id.wrapperNewPassword);
        verifyPasswordWrapper = findViewById(R.id.wrapperVerifyPassword);
        oldPasswordWrapper.setHint("Old Password");
        newPasswordWrapper.setHint("New Password");
        verifyPasswordWrapper.setHint("Verify New Password");
        fup_btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()){
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
            }
        });



    }



    private boolean validateForm() {
        boolean valid = true;

        String oldPassword = fup_op.getText().toString();
        if (TextUtils.isEmpty(oldPassword)) {
            oldPasswordWrapper.setError("Required");
            valid = false;
        } else {
            oldPasswordWrapper.setError(null);
        }
        String newPassword = fup_np.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            newPasswordWrapper.setError("Required");
            valid = false;
        } else {
            newPasswordWrapper.setError(null);
        }
        String verifyPassword = fup_vnp.getText().toString();
        if (TextUtils.isEmpty(verifyPassword)) {
            verifyPasswordWrapper.setError("Required");
            valid = false;
        } else {
            verifyPasswordWrapper.setError(null);
        }

        if(newPassword.equals(verifyPassword)){

        }else{
            verifyPasswordWrapper.setError("Passwords do no match");
            valid = false;
        }

        return valid;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutFaculty);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            viewHome();
            finish();
        }
    }
}
