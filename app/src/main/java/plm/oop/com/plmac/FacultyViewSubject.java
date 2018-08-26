package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FacultyViewSubject extends AppCompatActivity {

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_faculty_view_subject);
        Intent i = getIntent();
        final String userName = i.getStringExtra("userName");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Subject");


        Query zonesQuery = mRef.orderByChild("Faculty").equalTo(userName);
        zonesQuery.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> subject = new ArrayList<>();
                ArrayList<String> room = new ArrayList<>();
                ArrayList<String> schedule = new ArrayList<>();
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    subject.add(zoneSnapshot.getKey().toString());
                    room.add(zoneSnapshot.child("Room").getValue(String.class));
                    schedule.add(zoneSnapshot.child("Schedule").getValue(String.class));
                }


                final String[] subjectArr = subject.toArray(new String[0]);
                String[] roomArr = room.toArray(new String[0]);
                String[] scheduleArr = schedule.toArray(new String[0]);

                ListAdapter place = new ListAdapter(FacultyViewSubject.this, subjectArr, roomArr, scheduleArr);

                listView = findViewById(R.id.vslv1);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent (FacultyViewSubject.this,ClickedOnSubject.class );
                        intent.putExtra("userSubject",subjectArr[i]);
                        startActivity(intent);
                    }
                });
                listView.setAdapter(place);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("waow","aw");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        Intent ii= getIntent();
//        final String userNumber1= ii.getStringExtra("userNumber");
//        final String userName1= ii.getStringExtra("userName");
//        Log.i("Back",userName1+"   "+userNumber1);
        Log.i("waow","waow");
//        Intent i = new Intent(FacultyViewSubject.this,FacultyHomeActivity.class);
//        i.putExtra("userName",userName1);
//        i.putExtra("userNumber",userNumber1);
//        startActivity(i);
//        finish();
    }
}


