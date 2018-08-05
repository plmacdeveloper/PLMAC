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

    private final String[] vstv1array;
    private final String[] vstv2array;
    private final String[] vstv3array;
public ListAdapter(Activity context,String[] vstv1arrayParam,String[] vstv2arrayParam, String[] vstv3arrayParam)
{
    super(context,R.layout.customlist,vstv1arrayParam);
    this.context = context;
    this.vstv1array = vstv1arrayParam;
    this.vstv2array = vstv2arrayParam;
    this.vstv3array = vstv3arrayParam;
}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.customlist,null,true);

        TextView subjName =(TextView)rowView.findViewById(R.id.vstv1);
        TextView sched =(TextView)rowView.findViewById(R.id.vstv2);
        TextView room =(TextView)rowView.findViewById(R.id.vstv3);

        subjName.setText(vstv1array[position]);
        sched.setText(vstv2array[position]);
        room.setText(vstv3array[position]);
    return rowView;
    }
}


