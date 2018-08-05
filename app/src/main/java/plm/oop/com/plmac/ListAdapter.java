package plm.oop.com.plmac;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String>{

    private final Activity context;

    private final String[] course1;
    private final String[] sched1;
    private final String[] room1;
public ListAdapter(Activity context,String[] vstv1arrayParam,String[] vstv2arrayParam, String[] vstv3arrayParam)
{
    super(context,R.layout.customlist,vstv1arrayParam);
    this.context = context;
    this.course1 = vstv1arrayParam;
    this.sched1 = vstv2arrayParam;
    this.room1 = vstv3arrayParam;
}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.customlist,null,true);

        TextView subjName =(TextView)rowView.findViewById(R.id.vstv1);
        TextView schedule =(TextView)rowView.findViewById(R.id.vstv2);
        TextView rooms =(TextView)rowView.findViewById(R.id.vstv3);

        subjName.setText(course1[position]);
        schedule.setText(sched1[position]);
        rooms.setText(room1[position]);
    return rowView;
    }
}


