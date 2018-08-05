package plm.oop.com.plmac;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter{
    View view;
    ArrayList<ViewSubject> arrayList;

    public ListAdapter(FacultyViewSubject facultyViewSubject, ArrayList<ViewSubject>arrayOfSubject)
    {
        arrayList = arrayOfSubject;
    }

    @Override
    public int getCount() {
        return arrayList.size();
            }

    @Override
    public Object getItem(int position) {
     return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int postion, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.customlist,parent,false);

        final ViewSubject viewSubject = arrayList.get(postion);
        TextView name = (TextView)view.findViewById(R.id.vstv1);
                name.setText(viewSubject.getName());
        TextView schedule = (TextView)view.findViewById(R.id.vstv2);
            schedule.setText(viewSubject.getSchedule());
            TextView rooms = (TextView)view.findViewById(R.id.vstv3);
                rooms.setText(viewSubject.getRoom());
        return view;
    }
}


