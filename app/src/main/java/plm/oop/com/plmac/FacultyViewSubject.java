package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import plm.oop.com.plmac.ListAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacultyViewSubject extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_subject);
        ArrayList<ViewSubject> arrayOfSubject = new ArrayList<ViewSubject>();

        arrayOfSubject.add(new ViewSubject("Yes","Yes","Yes") );

        ListAdapter adapter = new ListAdapter(FacultyViewSubject.this,arrayOfSubject);

        ListView list = (ListView)findViewById(R.id.vslv1);
        list.setAdapter(adapter);



    }

}
