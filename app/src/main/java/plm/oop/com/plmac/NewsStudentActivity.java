package plm.oop.com.plmac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsStudentActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_student);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newsReference = firebaseDatabase.getReference("Admin").child("News");
        newsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> date = new ArrayList<>();
                ArrayList<String> content = new ArrayList<>();
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    date.add(zoneSnapshot.getKey());
                    title.add(zoneSnapshot.child("Title").getValue(String.class));
                    content.add(zoneSnapshot.child("Content").getValue(String.class));
                }
                String[] titleArray = title.toArray(new String[0]);
                String[] dateArray = date.toArray(new String[0]);
                String[] contentArray = content.toArray(new String[0]);

                NewsAdapter place = new NewsAdapter(NewsStudentActivity.this, dateArray, titleArray, contentArray);
                listView = findViewById(R.id.lvNewsStudent);
                listView.setAdapter(place);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
