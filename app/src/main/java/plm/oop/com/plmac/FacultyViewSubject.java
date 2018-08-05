package plm.oop.com.plmac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import plm.oop.com.plmac.ListAdapter.customButtonListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacultyViewSubject extends AppCompatActivity implements customButtonListener {
    private ListView listView;

    ListAdapter adapter;
    ArrayList<String> subj = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_subject);
        String[] subject = getResources().getStringArray(R.array.course);

        List<String> dataTemp = Arrays.asList(subject);

        subj.addAll(dataTemp);

        listView = (ListView)findViewById(R.id.vslv1);
        adapter = new ListAdapter(FacultyViewSubject.this,subj);
        adapter.setCustomButtonListener(FacultyViewSubject.this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onButtonListener(int position, String value) {
        Toast.makeText(FacultyViewSubject.this,"Clicked",Toast.LENGTH_SHORT).show();
    }

}
