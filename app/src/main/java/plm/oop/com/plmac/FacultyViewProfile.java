package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FacultyViewProfile extends AppCompatActivity {


    private Button fvp_up;
    private TextView tvFacultyViewProfileName;
    private  TextView tvFacultyViewProfileCollege;
    private TextView tvFacultyViewProfileIDNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_profile);
        Intent i= getIntent();
        String userName= i.getStringExtra("userName");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Faculty");
        Query query =mRef.orderByChild("userName").equalTo(userName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {

                                                         for (DataSnapshot faculty : dataSnapshot.getChildren()) {
                                                             tvFacultyViewProfileName.setText("Profile Name: "+faculty.child("userName").getValue(String.class));
                                                             tvFacultyViewProfileCollege.setText("Program: "+ faculty.child("userProgram").getValue(String.class));
                                                             tvFacultyViewProfileIDNumber.setText("User Number: "+faculty.child("userNumber").getValue(String.class));
                                                         }


                                                 }

                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                 }
                                             });
        tvFacultyViewProfileCollege=findViewById(R.id.tvFacultyViewProfileCollege);
        tvFacultyViewProfileIDNumber=findViewById(R.id.tvFacultyViewProfileIDNumber);
        tvFacultyViewProfileName=findViewById(R.id.tvFacultyViewProfileName);
        fvp_up=findViewById(R.id.btFacultyViewProfileUpdatePassword);
        fvp_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FacultyViewProfile.this,FacultyUpdatePassword.class));

            }
        });


    }
}
