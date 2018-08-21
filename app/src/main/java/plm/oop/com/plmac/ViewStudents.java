package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewStudents extends AppCompatActivity {
    ListView listView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        Intent i = getIntent();
        final String userName = i.getStringExtra("userName");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Subject");


        Query zonesQuery = mRef.orderByChild("Faculty").equalTo(userName);
        zonesQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> student  = new ArrayList<>();

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    student.add(zoneSnapshot.child("Name").getValue(String.class));
                }

                String[] studentArr = student.toArray(new String[0]);

                ListAdapter3 Stud = new ListAdapter3(ViewStudents.this, studentArr);

                listView3 = findViewById(R.id.dls1);
                listView3.setAdapter(Stud);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }
}
