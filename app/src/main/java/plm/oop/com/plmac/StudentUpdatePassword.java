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

public class StudentUpdatePassword extends AppCompatActivity {

    private EditText sup_op;
    private EditText sup_np;
    private EditText sup_vnp;
    private Button sup_btsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_updatepassword);

        sup_op=findViewById(R.id.etStudentUpdatePasswordOldPassword);
        sup_np=findViewById(R.id.etStudentUpdatePasswordNewPassword);
        sup_vnp=findViewById(R.id.etStudentUpdatePasswordVerifyPassword);
        sup_btsave=findViewById(R.id.btFacultyUpdatePasswordSave);
        Intent i = getIntent();
        final String userNumber=i.getStringExtra("userNumber");

        final String oldPass= sup_op.getText().toString();
        final String newPass= sup_np.getText().toString();
        final String verifyPass= sup_vnp.getText().toString();



        sup_btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),newPass,Toast.LENGTH_SHORT);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference("Student");

                Query query =mRef.orderByChild("userNumber").equalTo(userNumber);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(),newPass,Toast.LENGTH_SHORT);
//                          startActivity(new Intent(FacultyActivity.this, FacultyHomeActivity.class));
//
                            for (DataSnapshot student : dataSnapshot.getChildren()) {
//                                Toast.makeText(getApplicationContext(), faculty.child("userProgram").getValue(String.class), Toast.LENGTH_SHORT).show();
                                if (student.child("userPassword").getValue(String.class).equals(oldPass)){
                                    onBackPressed();
                                    finish();
                                    Toast.makeText(getApplicationContext(),oldPass,Toast.LENGTH_SHORT);
//                                startActivity(new Intent(FacultyActivity.this, FacultyHomeActivity.class));
                                    if (newPass.equals(verifyPass)) {

                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        final DatabaseReference databaseReference = firebaseDatabase.getReference("Student");
                                        databaseReference.child("userPassword").setValue(newPass);
                                        Toast.makeText(getApplicationContext(),newPass,Toast.LENGTH_SHORT);

                                    }

                                }

                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }
}
