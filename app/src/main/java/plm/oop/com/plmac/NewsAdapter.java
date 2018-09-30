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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter {

    private final Activity context;

    private final String[] titleArray;
    private final String[] dateArray;
    private final String[] contentArray;

    public NewsAdapter(Activity context, String[] dateParam, String[] titleParam, String[] contentParam) {
        super(context, R.layout.news_custom_list, dateParam);
        this.context = context;
        this.titleArray = titleParam;
        this.dateArray = dateParam;
        this.contentArray = contentParam;
        for (int count = 0; count < dateArray.length - 1; count++) {
            for (int count2 = count + 1; count2 < dateArray.length; count2++) {
                SimpleDateFormat dateArr = new SimpleDateFormat("MMMM-dd-yyyy hh:mm aa");
                Date firstDate = null;
                Date secondDate = null;
                try {
                    firstDate = dateArr.parse(dateArray[count]);
                    secondDate = dateArr.parse(dateArray[count2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (firstDate.before(secondDate)) {
                    String holder;
                    holder = dateArray[count2];
                    dateArray[count2] = dateArray[count];
                    dateArray[count] = holder;
                    holder = titleArray[count2];
                    titleArray[count2] = titleArray[count];
                    titleArray[count] = holder;
                    holder = contentArray[count2];
                    contentArray[count2] = contentArray[count];
                    contentArray[count] = holder;
                }
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.news_custom_list, null, true);
        TextView title = (TextView) rowView.findViewById(R.id.tvAdapterTitle);
        TextView date = (TextView) rowView.findViewById(R.id.tvAdapterDate);
        TextView content = (TextView) rowView.findViewById(R.id.tvAdapterContent);
        title.setText(titleArray[position]);
        date.setText(dateArray[position]);
        content.setText(contentArray[position]);
        return rowView;
    }
}
