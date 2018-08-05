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

public class ListAdapter extends ArrayAdapter{

    private final Activity context;

    private final String[] courseArray;
    private final String[] schedArray;
    private final String[] roomArray;
    public ListAdapter(Activity context,String[] courseParam,String[] schedParam, String[] roomParam)
    {
        super(context,R.layout.customlist,courseParam);
        this.context = context;
        this.courseArray = courseParam;
        this.schedArray = schedParam;
        this.roomArray = roomParam;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.customlist,null,true);

        TextView subjName =(TextView)rowView.findViewById(R.id.vstv1);
        TextView schedule =(TextView)rowView.findViewById(R.id.vstv2);
        TextView rooms =(TextView)rowView.findViewById(R.id.vstv3);

        subjName.setText(courseArray[position]);
        schedule.setText(schedArray[position]);
        rooms.setText(roomArray[position]);
        return rowView;
    }
}
