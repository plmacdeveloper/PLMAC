package plm.oop.com.plmac;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class    ListAdapter2 extends ArrayAdapter{


    private final Activity context;

    private final String[] dateArray;
    public ListAdapter2(Activity context, String[] dateParam)
    {
        super(context,R.layout.customlist,dateParam);
        this.context = context;
        this.dateArray = dateParam;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View viewList = inflater.inflate(R.layout.customlist,null,true);

        TextView Date =(TextView)viewList.findViewById(R.id.dtv1);

        Date.setText(dateArray[position]);
        return viewList;
    }
}

