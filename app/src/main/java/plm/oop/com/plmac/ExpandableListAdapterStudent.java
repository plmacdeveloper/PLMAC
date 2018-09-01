package plm.oop.com.plmac;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterStudent extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeaderCode;
    private List<String> _listDataHeaderName;
    private List<String> _listDataHeaderRoom;
    private List<String> _listDataHeaderTime;
    private List<String> _listDataHeaderDays;
    private HashMap<String, List<String>> _listChildDataDate;
    private HashMap<String, List<String>> _listChildDataStatus;

    public ExpandableListAdapterStudent(Context context, List<String> listDataHeaderCode, List<String> listDataHeaderName,
                                  List<String> listDataHeaderRoom, List<String> listDataHeaderTime, List<String> listDataHeaderDays,
                                  HashMap<String, List<String>> listChildDataDate, HashMap<String, List<String>> listChildDataStatus) {
        this._context = context;
        this._listDataHeaderCode = listDataHeaderCode;
        this._listDataHeaderName = listDataHeaderName;
        this._listDataHeaderRoom = listDataHeaderRoom;
        this._listDataHeaderTime = listDataHeaderTime;
        this._listDataHeaderDays = listDataHeaderDays;
        this._listChildDataDate = listChildDataDate;
        this._listChildDataStatus = listChildDataStatus;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChildDataDate.get(this._listDataHeaderCode.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastchid, View convertView, ViewGroup parent) {
            final String date = (String) getChild(groupPosition, childPosition);
            final String status = (String) _listChildDataStatus.get(this._listDataHeaderCode.get(groupPosition)).get(childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.customlist3, null);

            }
            TextView txtListChildDate = (TextView) convertView.findViewById(R.id.dtv1);
            TextView txtListChildStatus = (TextView) convertView.findViewById(R.id.dtv2);
            txtListChildStatus.setText(status);
            txtListChildDate.setText(date);
            return convertView;
        }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listChildDataDate.get(this._listDataHeaderCode.get(groupPosition)).size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeaderCode.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeaderCode.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerCode = (String) getGroup(groupPosition);
        String headerName = _listDataHeaderName.get(groupPosition);
        String headerRoom = _listDataHeaderRoom.get(groupPosition);
        String headerDays = _listDataHeaderDays.get(groupPosition);
        String headerTime = _listDataHeaderTime.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInfalter = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInfalter.inflate(R.layout.customlist, null);
        }

        final TextView mainListHeader = (TextView) convertView.findViewById(R.id.vstv1);
        mainListHeader.setTypeface(null, Typeface.BOLD);
        final TextView mainListHeader1 = (TextView) convertView.findViewById(R.id.vstv2);
        final TextView mainListHeader2 = (TextView) convertView.findViewById(R.id.vstv3);
        final TextView mainListHeader3 = (TextView) convertView.findViewById(R.id.vstv4);
        final TextView mainListHeader4 = (TextView) convertView.findViewById(R.id.vstv5);
        mainListHeader.setText(headerCode);
        mainListHeader1.setText(headerName);
        mainListHeader2.setText(headerRoom);
        mainListHeader3.setText(headerTime);
        mainListHeader4.setText(headerDays);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
