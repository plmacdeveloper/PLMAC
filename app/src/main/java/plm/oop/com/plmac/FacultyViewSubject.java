package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import plm.oop.com.plmac.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacultyViewSubject extends AppCompatActivity {
//    DatabaseReference database= FirebaseDatabase.getInstance().getReference("Subject");

    ListView listView;
    String userName = "Mamshie Lubao";
    //    List<String> subject = new FacultyViewSubjectsListAdapter();

    String[] schedule = {
            "8 tentacled monster",
            "Delicious in rolls",
            "Great for jumpers"
    };
    String[] rooms = {"Octopus", "Pig", "Sheep"};


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_faculty_view_subject);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRef = database.getReference("Subject");

//        mRef.addValueEventListener(new ValueEventListener() {
//           @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    String sub=snapshot.child("name").getValue(String.class);
//
//                   subject.add(sub);
//
////                    Toast.makeText(getApplicationContext(), facultyFound, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

            Query zonesQuery = mRef.orderByChild("Faculty").equalTo(userName);
            zonesQuery.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> subject = new ArrayList<>();
                    for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                        subject.add(zoneSnapshot.child("Name").getValue(String.class));
                    }


                    String[] courseArr = subject.toArray(new String[0]);

                    ListAdapter place = new ListAdapter(FacultyViewSubject.this, courseArr, schedule, rooms);

                    listView = findViewById(R.id.vslv1);
                    listView.setAdapter(place);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }


    }


