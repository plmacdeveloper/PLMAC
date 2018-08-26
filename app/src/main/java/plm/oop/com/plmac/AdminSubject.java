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
    private LinearLayout adminSubjectChoose, adminAddStudent;
    private ScrollView layoutAdminSubjectAdd, layoutAdminSubjectDelete;
    //    Add
    private EditText adminSubjectNameAdd, adminSubjectFacultyAdd, adminSubjectCodeAdd, adminSubjectRoomAdd, adminSubjectStudentAdd;
    private Button adminSubjectAddUser, adminSubjectAddStudents;
    private Spinner spinStartHour, spinStartMin, spinStartAA, spinEndHour, spinEndMin, spinEndAA;
    private ListView searchFaculty, addedStudents, searchStudent;

    //    Delete
    private EditText adminSubjectCodeDelete;
    private Button adminSubjectDeleteUser;
    private ListView deleteSubject;

    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private ArrayAdapter facultyAdapter, studentAdapter, addedStudentAdapter, subjectAdapter;
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

        adminAddStudent = findViewById(R.id.llAdminSubjectAddStudent);
        adminSubjectStudentAdd = findViewById(R.id.etAdminSubjectStudentAdd);
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
        addedStudents = findViewById(R.id.lvAddedStudents);
        searchStudent = findViewById(R.id.lvSearchStudent);
        adminSubjectAddUser = findViewById(R.id.btAdminSubjectAddUser);
        adminSubjectAddStudents = findViewById(R.id.btAdminSubjectAddStudents);

        adminSubjectCodeDelete = findViewById(R.id.etAdminSubjectCodeDelete);
        adminSubjectDeleteUser = findViewById(R.id.btAdminSubjectDeleteUser);
        deleteSubject = findViewById(R.id.lvDeleteSubject);

        final ArrayList<String> subject = new ArrayList<>();
        final ArrayList<String> SearchFacultyNameList = new ArrayList<>();
        final ArrayList<String> SearchFacultyIDList = new ArrayList<>();
        final ArrayList<String> SearchStudentNameList = new ArrayList<>();
        final ArrayList<String> SearchStudentIDList = new ArrayList<>();
        final ArrayList<String> AddedStudentNameList = new ArrayList<>();
        final ArrayList<String> AddedStudentIDList = new ArrayList<>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference arrayRef = firebaseDatabase.getReference("Faculty");
        arrayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SearchFacultyNameList.add(ds.child("userName").getValue(String.class));
                    SearchFacultyIDList.add(ds.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference array2Ref = firebaseDatabase.getReference("Student");
        array2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SearchStudentNameList.add(ds.child("userName").getValue(String.class));
                    SearchStudentIDList.add(ds.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addedStudentAdapter = new ArrayAdapter(AdminSubject.this, android.R.layout.simple_list_item_1, AddedStudentNameList);
        addedStudents.setAdapter(addedStudentAdapter);
        studentAdapter = new ArrayAdapter(AdminSubject.this, android.R.layout.simple_list_item_1, SearchStudentNameList);
        searchStudent.setAdapter(studentAdapter);
        facultyAdapter = new ArrayAdapter(AdminSubject.this, android.R.layout.simple_list_item_1, SearchFacultyNameList);
        searchFaculty.setAdapter(facultyAdapter);

        SubjectChooseOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminSubjectChoose.getVisibility() == View.GONE) {
                    adminSubjectChoose.setVisibility(View.VISIBLE);
                    layoutAdminSubjectAdd.setVisibility(View.GONE);
                    layoutAdminSubjectDelete.setVisibility(View.GONE);
                    adminAddStudent.setVisibility(View.GONE);
                    SubjectChooseOperation.setText(R.string.choose);
                } else {
                    adminSubjectChoose.setVisibility(View.GONE);
                    layoutAdminSubjectAdd.setVisibility(View.GONE);
                    layoutAdminSubjectDelete.setVisibility(View.GONE);
                    adminAddStudent.setVisibility(View.GONE);
                    SubjectChooseOperation.setText(R.string.choose);
                }
            }
        });

        adminSubjectAddStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminAddStudent.setVisibility(View.VISIBLE);
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

        adminSubjectFacultyAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchFaculty.setVisibility(View.VISIBLE);
                (AdminSubject.this).facultyAdapter.getFilter().filter(charSequence);
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

        adminSubjectStudentAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (AdminSubject.this).studentAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String student = adapterView.getItemAtPosition(position).toString();
                String id = SearchStudentIDList.get(position);
                AddedStudentNameList.add(student);
                AddedStudentIDList.add(id);
                SearchStudentNameList.remove(position);
                SearchStudentIDList.remove(position);
                adminAddStudent.setVisibility(View.GONE);
                Toast.makeText(AdminSubject.this, student + " is added.", Toast.LENGTH_SHORT).show();
            }
        });
        addedStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String student = adapterView.getItemAtPosition(position).toString();
                String id = AddedStudentIDList.get(position);
                SearchStudentNameList.add(student);
                SearchStudentIDList.add(id);
                AddedStudentNameList.remove(position);
                AddedStudentIDList.remove(position);
                adminAddStudent.setVisibility(View.GONE);
                Toast.makeText(AdminSubject.this, student + " is removed.", Toast.LENGTH_SHORT).show();
            }
        });
        adminSubjectAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String name = adminSubjectNameAdd.getText().toString().trim();
                String faculty = adminSubjectFacultyAdd.getText().toString().trim();
                final String code = adminSubjectCodeAdd.getText().toString().trim().toUpperCase();
                String room = adminSubjectRoomAdd.getText().toString().trim().toUpperCase();
                String startTime = String.valueOf(spinStartHour.getSelectedItem()) + ":" + String.valueOf(spinStartMin.getSelectedItem()) + " " + String.valueOf(spinStartAA.getSelectedItem());
                String endTime = String.valueOf(spinEndHour.getSelectedItem()) + ":" + String.valueOf(spinEndMin.getSelectedItem()) + " " + String.valueOf(spinEndAA.getSelectedItem());
                if (name.isEmpty() || faculty.isEmpty() || code.isEmpty() || room.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminSubject.this, "Information Incomplete.", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference("Subject");
                    boolean check = false;
                    for (String s : subject) {
                        if (s.matches(code)) {
                            check = true;
                        }
                    }
                    if (check) {
                        Toast.makeText(AdminSubject.this, "A subject with a code of " + code + " is already in the database.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {

                        myRef.child(code).child("Name").setValue(name);
                        myRef.child(code).child("Faculty").setValue(faculty);
                        myRef.child(code).child("Room").setValue(room);
                        myRef.child(code).child("Time").child("Start").setValue(startTime);
                        myRef.child(code).child("Time").child("End").setValue(endTime);
                        for (String student : AddedStudentNameList) {
                            String studentID = AddedStudentIDList.get(AddedStudentNameList.indexOf(student));
                            myRef.child(code).child("Students").child(studentID).setValue(student);
                        }

                        if (schedMon) {
                            myRef.child(code).child("Schedule").child("Monday").setValue("1");
                        }
                        if (schedTues) {
                            myRef.child(code).child("Schedule").child("Tuesday").setValue("2");
                        }
                        if (schedWed) {
                            myRef.child(code).child("Schedule").child("Wednesday").setValue("3");
                        }
                        if (schedThurs) {
                            myRef.child(code).child("Schedule").child("Thursday").setValue("4");
                        }
                        if (schedFri) {
                            myRef.child(code).child("Schedule").child("Friday").setValue("5");
                        }
                        if (schedSat) {
                            myRef.child(code).child("Schedule").child("Saturday").setValue("6");
                        }
                        if (schedSun) {
                            myRef.child(code).child("Schedule").child("Sunday").setValue("7");
                        }

                        Toast.makeText(AdminSubject.this, "Add Information Successful.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        onBackPressed();
                        finish();
                    }
                }
            }
        });

        DatabaseReference subjref = firebaseDatabase.getReference("Subject");
        subjref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    subject.add(ds.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        subjectAdapter = new ArrayAdapter(AdminSubject.this, android.R.layout.simple_list_item_1, subject);
        deleteSubject.setAdapter(subjectAdapter);

        adminSubjectCodeDelete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                deleteSubject.setVisibility(View.VISIBLE);
                (AdminSubject.this).subjectAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        deleteSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adminSubjectCodeDelete.setText(adapterView.getItemAtPosition(i).toString());
                deleteSubject.setVisibility(View.GONE);
            }
        });
        adminSubjectDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code = adminSubjectCodeDelete.getText().toString().trim().toUpperCase();
                if (code.isEmpty()) {
                    Toast.makeText(AdminSubject.this, "Input subject code.", Toast.LENGTH_SHORT).show();
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
