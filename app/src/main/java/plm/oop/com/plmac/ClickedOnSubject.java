package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        final String userSubject = i.getStringExtra("userSubject");
//        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("userSubjects").child("CPE420");
//        String name = ref1.getKey();
//        Toast.makeText(ClickedOnSubject.this,name,Toast.LENGTH_SHORT).show();
        final  String refAddress = "Subject/" +userSubject+"/Dates";
        Toast.makeText(ClickedOnSubject.this,refAddress,Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(refAddress);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> date = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    date.add(String.valueOf(dsp.getKey())); //add result into array list
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
