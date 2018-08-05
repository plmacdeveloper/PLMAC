package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentViewProfile extends AppCompatActivity {
    private Button svp_up;
    private TextView tvStudentViewProfileName;
    private TextView tvStudentViewProfileProgram;
    private TextView tvStudentViewProfileStudentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_viewprofile);
        Intent i = getIntent();
        final String userNumber = i.getStringExtra("userNumber");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Student");
        Query query = mRef.orderByChild("userNumber").equalTo(userNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot student : dataSnapshot.getChildren()) {
                    tvStudentViewProfileName.setText("Profile Name: " + student.child("userName").getValue(String.class));
                    tvStudentViewProfileProgram.setText("Program: " + student.child("userProgram").getValue(String.class));
                    tvStudentViewProfileStudentNumber.setText("Student Number: " + student.child("userNumber").getValue(String.class));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tvStudentViewProfileProgram = findViewById(R.id.tvStudentViewProfileProgram);
        tvStudentViewProfileStudentNumber = findViewById(R.id.tvStudentViewProfileStudentNumber);
        tvStudentViewProfileName = findViewById(R.id.tvStudentViewProfileName);
        svp_up = findViewById(R.id.btStudentViewProfileUpdatePassword);
        svp_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StudentViewProfile.this, StudentUpdatePassword.class));

            }
        });

    }
}