package plm.oop.com.plmac;

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
    customButtonListener customListener;
    public interface customButtonListener{
        public void onButtonListener(int position,String value);
    }
    public void setCustomButtonListener(customButtonListener listener){
        this.customListener = listener;
    }
    private Context context;
    private ArrayList<String> data = new ArrayList<String>();
    public ListAdapter(Context context, ArrayList<String>subj)
    {
        super(context,R.layout.customlist,subj);
        this.data = subj;

        this.context = context;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.customlist, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.vstv1);
            viewHolder.button = (Button) convertView.findViewById(R.id.vsb1);
            viewHolder.button2 = (Button) convertView.findViewById(R.id.vsb2);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListener != null)
                {
                    customListener.onButtonListener(position, temp);
                }
            }
        });
        return convertView;
    }
    public class ViewHolder {
        TextView text;
        Button button, button2;
    }

}


