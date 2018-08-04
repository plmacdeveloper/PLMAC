package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminStudent extends AppCompatActivity {

    private Button StudentChooseOperation, adminStudentAdd, adminStudentDelete, adminStudentUpdate;
    private LinearLayout adminStudentChoose, layoutAdminStudentAdd, layoutAdminStudentDelete, layoutAdminStudentUpdate;

    //    Add
    private EditText adminStudentNameAdd, adminStudentProgramAdd, adminStudentNumberAdd, adminStudentPasswordAdd;
    private Button adminStudentAddUser;

    //    Delete
    private EditText adminStudentNumberDelete;
    private Button adminStudentDeleteUser;

    //    Update
    private EditText adminStudentNameUpdate, adminStudentProgramUpdate, adminStudentNumberUpdate, adminStudentPasswordUpdate;
    private Button adminStudentUpdateUser, adminStudentUpdateSetNumber;
    private LinearLayout adminStudentUpdateSet;

    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_studen);

        progressDialog = new ProgressDialog(this);

        StudentChooseOperation = findViewById(R.id.btStudentChooseOperation);
        adminStudentChoose = findViewById(R.id.llAdminStudentChoose);
        adminStudentAdd = findViewById(R.id.btAdminStudentAdd);
        adminStudentDelete = findViewById(R.id.btAdminStudentDelete);
        adminStudentUpdate = findViewById(R.id.btAdminStudentUpdate);
        layoutAdminStudentAdd = findViewById(R.id.llAdminStudentAdd);
        layoutAdminStudentDelete = findViewById(R.id.llAdminStudentDelete);
        layoutAdminStudentUpdate = findViewById(R.id.llAdminStudentUpdate);

        adminStudentNameAdd = findViewById(R.id.etAdminStudentNameAdd);
        adminStudentProgramAdd = findViewById(R.id.etAdminStudentProgramAdd);
        adminStudentNumberAdd = findViewById(R.id.etAdminStudentNumberAdd);
        adminStudentPasswordAdd = findViewById(R.id.etAdminStudentPasswordAdd);
        adminStudentAddUser = findViewById(R.id.btAdminStudentAddUser);

        adminStudentNumberDelete = findViewById(R.id.etAdminStudentNumberDelete);
        adminStudentDeleteUser = findViewById(R.id.btAdminStudentDeleteUser);

        adminStudentNameUpdate = findViewById(R.id.etAdminStudentNameUpdate);
        adminStudentProgramUpdate = findViewById(R.id.etAdminStudentProgramUpdate);
        adminStudentNumberUpdate = findViewById(R.id.etAdminStudentNumberUpdate);
        adminStudentPasswordUpdate = findViewById(R.id.etAdminStudentPasswordUpdate);
        adminStudentUpdateUser = findViewById(R.id.btAdminStudentUpdateUser);
        adminStudentUpdateSetNumber = findViewById(R.id.btAdminStudentUpdateSetNumber);
        adminStudentUpdateSet = findViewById(R.id.llAdminStudentUpdateSet);

        StudentChooseOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adminStudentChoose.getVisibility() == View.GONE) {
                    adminStudentChoose.setVisibility(View.VISIBLE);
                    layoutAdminStudentAdd.setVisibility(View.GONE);
                    layoutAdminStudentDelete.setVisibility(View.GONE);
                    layoutAdminStudentUpdate.setVisibility(View.GONE);
                    StudentChooseOperation.setText(R.string.choose);
                }else{
                    adminStudentChoose.setVisibility(View.GONE);
                    layoutAdminStudentAdd.setVisibility(View.GONE);
                    layoutAdminStudentDelete.setVisibility(View.GONE);
                    layoutAdminStudentUpdate.setVisibility(View.GONE);
                    StudentChooseOperation.setText(R.string.choose);
                }
            }
        });

        adminStudentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminStudentChoose.setVisibility(View.GONE);
                layoutAdminStudentAdd.setVisibility(View.VISIBLE);
                layoutAdminStudentDelete.setVisibility(View.GONE);
                layoutAdminStudentUpdate.setVisibility(View.GONE);
                StudentChooseOperation.setText(R.string.add);
            }
        });

        adminStudentUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminStudentChoose.setVisibility(View.GONE);
                layoutAdminStudentAdd.setVisibility(View.GONE);
                layoutAdminStudentDelete.setVisibility(View.GONE);
                layoutAdminStudentUpdate.setVisibility(View.VISIBLE);
                StudentChooseOperation.setText(R.string.update);

            }
        });
        adminStudentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminStudentChoose.setVisibility(View.GONE);
                layoutAdminStudentAdd.setVisibility(View.GONE);
                layoutAdminStudentDelete.setVisibility(View.VISIBLE);
                layoutAdminStudentUpdate.setVisibility(View.GONE);
                StudentChooseOperation.setText(R.string.delete);
            }
        });

        adminStudentAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String name = adminStudentNameAdd.getText().toString().trim();
                String program = adminStudentProgramAdd.getText().toString().trim();
                String number = adminStudentNumberAdd.getText().toString().trim();
                String password = adminStudentPasswordAdd.getText().toString().trim();
                if(name.isEmpty() || program.isEmpty() || number.isEmpty() || password.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(AdminStudent.this,"Information Incomplete.",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference("Student");
                    UserProfile userProfile = new UserProfile(name, program, number, password);
                    myRef.child(number).setValue(userProfile);
                    Toast.makeText(AdminStudent.this,"Add Information Successful.",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    onBackPressed();
                    finish();
                }
            }
        });

        adminStudentDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = adminStudentNumberDelete.getText().toString().trim();

                if(number.isEmpty()){
                    Toast.makeText(AdminStudent.this, "Input name.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference("Student");
                    databaseReference.child(number).removeValue();
                    Toast.makeText(AdminStudent.this, "Delete Information Successful.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
            }
        });

        adminStudentUpdateSetNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = adminStudentNumberUpdate.getText().toString().trim();
                if(number.isEmpty()){
                    Toast.makeText(AdminStudent.this,"ID number is required.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    adminStudentUpdateSetNumber.setVisibility(View.INVISIBLE);
                    adminStudentUpdateSet.setVisibility(View.VISIBLE);
                    final DatabaseReference databaseReference = firebaseDatabase.getReference("Student");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserProfile userProfile = dataSnapshot.child(number).getValue(UserProfile.class);
                            adminStudentNameUpdate.setText(userProfile.getUserName());
                            adminStudentProgramUpdate.setText(userProfile.getUserProgram());
                            adminStudentPasswordUpdate.setText(userProfile.getUserPassword());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AdminStudent.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        adminStudentUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                String name = adminStudentNameUpdate.getText().toString().trim();
                String program = adminStudentProgramUpdate.getText().toString().trim();
                String number = adminStudentNumberUpdate.getText().toString().trim();
                String password = adminStudentPasswordUpdate.getText().toString().trim();
                UserProfile userProfile = new UserProfile(name,program,number,password);
                firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference("Student");
                databaseReference.child(number).setValue(userProfile);
                progressDialog.dismiss();
                onBackPressed();
                finish();
            }
        });
    }
}
