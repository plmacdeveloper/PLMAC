package plm.oop.com.plmac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import plm.oop.com.plmac.ListAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacultyViewSubject extends AppCompatActivity  {
    ListView listView;

    String[] subject = getResources().getStringArray(R.array.course);
    String[] schedule = getResources().getStringArray(R.array.sched);
    String[] rooms = getResources().getStringArray(R.array.room);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_subject);

        ListAdapter place = new ListAdapter(this,subject,schedule,rooms);


        listView = (ListView)findViewById(R.id.vslv1);
        listView.setAdapter(place);
    }


}
