package plm.oop.com.plmac;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListAdapter3 extends ArrayAdapter {


    private final Activity context;

    private final String[] studArray;
    public ListAdapter3(Activity context, String[] studParam)
    {
        super(context,R.layout.customlist2,studParam);
        this.context = context;
        this.studArray = studParam;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View viewList1 = inflater.inflate(R.layout.customlist2,null,true);

        TextView Date =(TextView)viewList1.findViewById(R.id.dtv1);

        Date.setText(studArray[position]);
        return viewList1;
    }


}
