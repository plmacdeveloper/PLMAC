package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminSubject extends AppCompatActivity {

    private Button SubjectChooseOperation, adminSubjectAdd, adminSubjectDelete, adminSubjectUpdate;
    private LinearLayout adminSubjectChoose, layoutAdminSubjectAdd, layoutAdminSubjectDelete;

    //    Add
    private EditText adminSubjectNameAdd, adminSubjectFacultyAdd, adminSubjectCodeAdd;
    private Button adminSubjectAddUser;

    //    Delete
    private EditText adminSubjectCodeDelete;
    private Button adminSubjectDeleteUser;
    private RecyclerView adminSubjectCodeView;

    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_subject);

        progressDialog = new ProgressDialog(this);

        SubjectChooseOperation = findViewById(R.id.btSubjectChooseOperation);
        adminSubjectChoose = findViewById(R.id.llAdminSubjectChoose);
        adminSubjectAdd = findViewById(R.id.btAdminSubjectAdd);
        adminSubjectDelete = findViewById(R.id.btAdminSubjectDelete);
        adminSubjectUpdate = findViewById(R.id.btAdminSubjectUpdate);
        layoutAdminSubjectAdd = findViewById(R.id.llAdminSubjectAdd);
        layoutAdminSubjectDelete = findViewById(R.id.llAdminSubjectDelete);

        adminSubjectNameAdd = findViewById(R.id.etAdminSubjectNameAdd);
        adminSubjectFacultyAdd = findViewById(R.id.etAdminSubjectFacultyAdd);
        adminSubjectCodeAdd = findViewById(R.id.etAdminSubjectCodeAdd);
        adminSubjectAddUser = findViewById(R.id.btAdminSubjectAddUser);

        adminSubjectCodeDelete = findViewById(R.id.etAdminSubjectCodeDelete);
        adminSubjectCodeView = findViewById(R.id.rvAdminSubjectCode);
        adminSubjectDeleteUser = findViewById(R.id.btAdminSubjectDeleteUser);



        adminSubjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminSubjectChoose.setVisibility(View.GONE);
                layoutAdminSubjectAdd.setVisibility(View.VISIBLE);
                layoutAdminSubjectDelete.setVisibility(View.GONE);
                SubjectChooseOperation.setText(R.string.add);
            }
        });

        adminSubjectUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminSubject.this, QRScanner.class));

            }
        });
        adminSubjectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminSubjectChoose.setVisibility(View.GONE);
                layoutAdminSubjectAdd.setVisibility(View.GONE);
                layoutAdminSubjectDelete.setVisibility(View.VISIBLE);
                SubjectChooseOperation.setText(R.string.delete);
            }
        });

        adminSubjectAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String name = adminSubjectNameAdd.getText().toString().trim();
                String faculty = adminSubjectFacultyAdd.getText().toString().trim();
                String code = adminSubjectCodeAdd.getText().toString().trim().toUpperCase();
                if(name.isEmpty() || faculty.isEmpty() || code.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(AdminSubject.this,"Information Incomplete.",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference("Subject");
                    myRef.child(code).child("Name").setValue(name);
                    myRef.child(code).child("Faculty").setValue(faculty);
                    Toast.makeText(AdminSubject.this,"Add Information Successful.",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    onBackPressed();
                    finish();
                }
            }
        });

        adminSubjectDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code = adminSubjectCodeDelete.getText().toString().trim().toUpperCase();

                if(code.isEmpty()){
                    Toast.makeText(AdminSubject.this, "Input name.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference("Subject");
                    databaseReference.child(code).removeValue();
                    Toast.makeText(AdminSubject.this, "Delete Information Successful.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
            }
        });

    }
}
