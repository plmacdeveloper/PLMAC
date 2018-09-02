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

    private final String[] studNameArray;
    private final String[] studStatsArray;
    public ListAdapter(Activity context,String[] studNameParam,String[] studStatsParam)
    {
        super(context,R.layout.customlist,studNameParam);
        this.context = context;
        this.studNameArray = studNameParam;
        this.studStatsArray = studStatsParam;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.customlist,null,true);
//
//        TextView subjName =(TextView)rowView.findViewById(R.id.cl3tv1);
//        TextView schedule =(TextView)rowView.findViewById(R.id.cl3tv2);
//
//        subjName.setText(studNameArray[position]);
//        schedule.setText(studStatsArray[position]);
        return rowView;
    }
}
