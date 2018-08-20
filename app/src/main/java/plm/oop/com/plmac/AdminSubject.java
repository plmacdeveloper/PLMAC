package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminSubject extends AppCompatActivity {

    private Button SubjectChooseOperation, adminSubjectAdd, adminSubjectDelete, adminSubjectUpdate;
    private LinearLayout adminSubjectChoose;
    private ScrollView layoutAdminSubjectAdd, layoutAdminSubjectDelete;
    //    Add
    private EditText adminSubjectNameAdd, adminSubjectFacultyAdd, adminSubjectCodeAdd, adminSubjectRoomAdd;
    private Button adminSubjectAddUser;
    private Spinner spinStartHour, spinStartMin, spinStartAA, spinEndHour, spinEndMin, spinEndAA;
    private ListView searchFaculty;

    //    Delete
    private EditText adminSubjectCodeDelete;
    private Button adminSubjectDeleteUser;
    // private RecyclerView adminSubjectCodeView;

    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private ArrayAdapter adapter;
    public boolean schedMon = false, schedTues = false, schedWed = false, schedThurs = false, schedFri = false, schedSat = false, schedSun = false;

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
        adminSubjectRoomAdd = findViewById(R.id.etAdminSubjectRoomAdd);
        spinEndAA = findViewById(R.id.SpinEndAA);
        spinEndHour = findViewById(R.id.SpinEndHour);
        spinEndMin = findViewById(R.id.SpinEndMin);
        spinStartAA = findViewById(R.id.SpinStartAA);
        spinStartHour = findViewById(R.id.SpinStartHour);
        spinStartMin = findViewById(R.id.SpinStartMin);
        searchFaculty = findViewById(R.id.lvSearchFaculty);
        adminSubjectAddUser = findViewById(R.id.btAdminSubjectAddUser);

        adminSubjectCodeDelete = findViewById(R.id.etAdminSubjectCodeDelete);
        adminSubjectDeleteUser = findViewById(R.id.btAdminSubjectDeleteUser);

        final ArrayList<String> SearchFacultyNameList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference arrayRef = firebaseDatabase.getReference("Faculty");
        arrayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    SearchFacultyNameList.add(ds.child("userName").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter(AdminSubject.this,android.R.layout.simple_list_item_1,SearchFacultyNameList);
        searchFaculty.setAdapter(adapter);

        SubjectChooseOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminSubjectChoose.getVisibility() == View.GONE) {
                    adminSubjectChoose.setVisibility(View.VISIBLE);
                    layoutAdminSubjectAdd.setVisibility(View.GONE);
                    layoutAdminSubjectDelete.setVisibility(View.GONE);
                    SubjectChooseOperation.setText(R.string.choose);
                } else {
                    adminSubjectChoose.setVisibility(View.GONE);
                    layoutAdminSubjectAdd.setVisibility(View.GONE);
                    layoutAdminSubjectDelete.setVisibility(View.GONE);
                    SubjectChooseOperation.setText(R.string.choose);
                }
            }
        });

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
                startActivity(new Intent(AdminSubject.this, FacultyViewSubject.class));

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
        adminSubjectFacultyAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchFaculty.setVisibility(View.VISIBLE);
                (AdminSubject.this).adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchFaculty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adminSubjectFacultyAdd.setText(adapterView.getItemAtPosition(position).toString());
                searchFaculty.setVisibility(View.GONE);
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
                String room = adminSubjectRoomAdd.getText().toString().trim().toUpperCase();
                String startTime = String.valueOf(spinStartHour.getSelectedItem()) + ":" + String.valueOf(spinStartMin.getSelectedItem()) + " " + String.valueOf(spinStartAA.getSelectedItem());
                String endTime = String.valueOf(spinEndHour.getSelectedItem()) + ":" + String.valueOf(spinEndMin.getSelectedItem()) + " " + String.valueOf(spinEndAA.getSelectedItem());
                if (name.isEmpty() || faculty.isEmpty() || code.isEmpty() || room.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminSubject.this, "Information Incomplete.", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference("Subject");
                    myRef.child(code).child("Name").setValue(name);
                    myRef.child(code).child("Faculty").setValue(faculty);
                    myRef.child(code).child("Room").setValue(room);
                    myRef.child(code).child("Time").child("Start").setValue(startTime);
                    myRef.child(code).child("Time").child("End").setValue(endTime);
                    if (schedMon) {
                        myRef.child(code).child("Schedule").child("Monday").setValue("Monday");
                    }
                    if (schedTues) {
                        myRef.child(code).child("Schedule").child("Tuesday").setValue("Tuesday");
                    }
                    if (schedWed) {
                        myRef.child(code).child("Schedule").child("Wednesday").setValue("Wednesday");
                    }
                    if (schedThurs) {
                        myRef.child(code).child("Schedule").child("Thursday").setValue("Thursday");
                    }
                    if (schedFri) {
                        myRef.child(code).child("Schedule").child("Friday").setValue("Friday");
                    }
                    if (schedSat) {
                        myRef.child(code).child("Schedule").child("Saturday").setValue("Saturday");
                    }
                    if (schedSun) {
                        myRef.child(code).child("Schedule").child("Sunday").setValue("Sunday");
                    }

                    Toast.makeText(AdminSubject.this, "Add Information Successful.", Toast.LENGTH_SHORT).show();
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

                if (code.isEmpty()) {
                    Toast.makeText(AdminSubject.this, "Input name.", Toast.LENGTH_SHORT).show();
                } else {
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

    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.cbAdminSubjectScheduleMonday:
                if (checked) {
                    schedMon = true;
                    break;
                } else {
                    schedMon = false;
                }
            case R.id.cbAdminSubjectScheduleTuesday:
                if (checked) {
                    schedTues = true;
                    break;
                } else {
                    schedTues = false;
                }
            case R.id.cbAdminSubjectScheduleWednesday:
                if (checked) {
                    schedWed = true;
                    break;
                } else {
                    schedWed = false;
                }
            case R.id.cbAdminSubjectScheduleThursday:
                if (checked) {
                    schedThurs = true;
                    break;
                } else {
                    schedThurs = false;
                }
            case R.id.cbAdminSubjectScheduleFriday:
                if (checked) {
                    schedFri = true;
                    break;
                } else {
                    schedFri = false;
                }
            case R.id.cbAdminSubjectScheduleSaturday:
                if (checked) {
                    schedSat = true;
                    break;
                } else {
                    schedSat = false;
                }
            case R.id.cbAdminSubjectScheduleSunday:
                if (checked) {
                    schedSun = true;
                    break;
                } else {
                    schedSun = false;
                }

        }
    }
}
