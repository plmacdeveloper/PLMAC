package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClickedOnSubject extends AppCompatActivity {
    ListView listView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_on_list);

        Intent i = getIntent();
        final String userName = i.getStringExtra("userName");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Subject");


        Query zonesQuery = mRef.orderByChild("Faculty").equalTo(userName);
        zonesQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> date = new ArrayList<>();

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    date.add(zoneSnapshot.child("Name").getValue(String.class));
                }

                String[] dateArr = date.toArray(new String[0]);


                ListAdapter2 dates = new ListAdapter2(ClickedOnSubject.this, dateArr);
                listView2 = findViewById(R.id.dls1);
                listView2.setAdapter(dates);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

}
