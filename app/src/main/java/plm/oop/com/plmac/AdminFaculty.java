package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminFaculty extends AppCompatActivity {

    private Button facultyChooseOperation, adminFacultyAdd, adminFacultyDelete, adminFacultyUpdate;
    private LinearLayout adminFacultyChoose;
    private ScrollView layoutAdminFacultyAdd, layoutAdminFacultyDelete, layoutAdminFacultyUpdate;
//    Add
    private EditText adminFacultyNameAdd, adminFacultyProgramAdd, adminFacultyNumberAdd, adminFacultyPasswordAdd;
    private Button adminFacultyAddUser;

//    Delete
    private EditText adminFacultyNumberDelete;
    private Button adminFacultyDeleteUser;

//    Update
    private EditText adminFacultyNameUpdate, adminFacultyProgramUpdate, adminFacultyNumberUpdate, adminFacultyPasswordUpdate;
    private Button adminFacultyUpdateUser, adminFacultyUpdateSetNumber;
    private ScrollView adminFacultyUpdateSet;

    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_faculty);

        progressDialog = new ProgressDialog(this);

        facultyChooseOperation = findViewById(R.id.btFacultyChooseOperation);
        adminFacultyChoose = findViewById(R.id.llAdminFacultyChoose);
        adminFacultyAdd = findViewById(R.id.btAdminFacultyAdd);
        adminFacultyDelete = findViewById(R.id.btAdminFacultyDelete);
        adminFacultyUpdate = findViewById(R.id.btAdminFacultyUpdate);
        layoutAdminFacultyAdd = findViewById(R.id.llAdminFacultyAdd);
        layoutAdminFacultyDelete = findViewById(R.id.llAdminFacultyDelete);
        layoutAdminFacultyUpdate = findViewById(R.id.llAdminFacultyUpdate);

        adminFacultyNameAdd = findViewById(R.id.etAdminFacultyNameAdd);
        adminFacultyProgramAdd = findViewById(R.id.etAdminFacultyProgramAdd);
        adminFacultyNumberAdd = findViewById(R.id.etAdminFacultyNumberAdd);
        adminFacultyPasswordAdd = findViewById(R.id.etAdminFacultyPasswordAdd);
        adminFacultyAddUser = findViewById(R.id.btAdminFacultyAddUser);

        adminFacultyNumberDelete = findViewById(R.id.etAdminFacultyNumberDelete);
        adminFacultyDeleteUser = findViewById(R.id.btAdminFacultyDeleteUser);

        adminFacultyNameUpdate = findViewById(R.id.etAdminFacultyNameUpdate);
        adminFacultyProgramUpdate = findViewById(R.id.etAdminFacultyProgramUpdate);
        adminFacultyNumberUpdate = findViewById(R.id.etAdminFacultyNumberUpdate);
        adminFacultyPasswordUpdate = findViewById(R.id.etAdminFacultyPasswordUpdate);
        adminFacultyUpdateUser = findViewById(R.id.btAdminFacultyUpdateUser);
        adminFacultyUpdateSetNumber = findViewById(R.id.btAdminFacultyUpdateSetNumber);
        adminFacultyUpdateSet = findViewById(R.id.llAdminFacultyUpdateSet);

        facultyChooseOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adminFacultyChoose.getVisibility() == View.GONE) {
                    adminFacultyChoose.setVisibility(View.VISIBLE);
                    layoutAdminFacultyAdd.setVisibility(View.GONE);
                    layoutAdminFacultyDelete.setVisibility(View.GONE);
                    layoutAdminFacultyUpdate.setVisibility(View.GONE);
                    facultyChooseOperation.setText(R.string.choose);
                }else{
                    adminFacultyChoose.setVisibility(View.GONE);
                    layoutAdminFacultyAdd.setVisibility(View.GONE);
                    layoutAdminFacultyDelete.setVisibility(View.GONE);
                    layoutAdminFacultyUpdate.setVisibility(View.GONE);
                    facultyChooseOperation.setText(R.string.choose);
                }
            }
        });

        adminFacultyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminFacultyChoose.setVisibility(View.GONE);
                layoutAdminFacultyAdd.setVisibility(View.VISIBLE);
                layoutAdminFacultyDelete.setVisibility(View.GONE);
                layoutAdminFacultyUpdate.setVisibility(View.GONE);
                facultyChooseOperation.setText(R.string.add);
            }
        });

        adminFacultyUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminFacultyChoose.setVisibility(View.GONE);
                layoutAdminFacultyAdd.setVisibility(View.GONE);
                layoutAdminFacultyDelete.setVisibility(View.GONE);
                layoutAdminFacultyUpdate.setVisibility(View.VISIBLE);
                facultyChooseOperation.setText(R.string.update);

            }
        });
        adminFacultyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminFacultyChoose.setVisibility(View.GONE);
                layoutAdminFacultyAdd.setVisibility(View.GONE);
                layoutAdminFacultyDelete.setVisibility(View.VISIBLE);
                layoutAdminFacultyUpdate.setVisibility(View.GONE);
                facultyChooseOperation.setText(R.string.delete);
            }
        });

        adminFacultyAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String name = adminFacultyNameAdd.getText().toString().trim();
                String program = adminFacultyProgramAdd.getText().toString().trim();
                String number = adminFacultyNumberAdd.getText().toString().trim();
                String password = adminFacultyPasswordAdd.getText().toString().trim();
                if(name.isEmpty() || program.isEmpty() || number.isEmpty() || password.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(AdminFaculty.this,"Information Incomplete.",Toast.LENGTH_SHORT).show();
                }else{
                firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference("Faculty");
                    UserProfile userProfile = new UserProfile(name, program, number, password);
                    myRef.child(number).setValue(userProfile);
                    Toast.makeText(AdminFaculty.this,"Add Information Successful.",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    onBackPressed();
                    finish();
                }
            }
        });

        adminFacultyDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = adminFacultyNumberDelete.getText().toString().trim();

                if(number.isEmpty()){
                    Toast.makeText(AdminFaculty.this, "Input name.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference("Faculty");
                    databaseReference.child(number).removeValue();
                    Toast.makeText(AdminFaculty.this, "Delete Information Successful.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
            }
        });

        adminFacultyUpdateSetNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = adminFacultyNumberUpdate.getText().toString().trim();
                if(number.isEmpty()){
                    Toast.makeText(AdminFaculty.this,"ID number is required.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    adminFacultyUpdateSetNumber.setVisibility(View.INVISIBLE);
                    adminFacultyUpdateSet.setVisibility(View.VISIBLE);
                    final DatabaseReference databaseReference = firebaseDatabase.getReference("Faculty");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserProfile userProfile = dataSnapshot.child(number).getValue(UserProfile.class);
                            adminFacultyNameUpdate.setText(userProfile.getUserName());
                            adminFacultyProgramUpdate.setText(userProfile.getUserProgram());
                            adminFacultyPasswordUpdate.setText(userProfile.getUserPassword());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AdminFaculty.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        adminFacultyUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                String name = adminFacultyNameUpdate.getText().toString().trim();
                String program = adminFacultyProgramUpdate.getText().toString().trim();
                String number = adminFacultyNumberUpdate.getText().toString().trim();
                String password = adminFacultyPasswordUpdate.getText().toString().trim();
                UserProfile userProfile = new UserProfile(name,program,number,password);
                firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference("Faculty");
                databaseReference.child(number).setValue(userProfile);
                progressDialog.dismiss();
                onBackPressed();
                finish();
            }
        });
    }
}
