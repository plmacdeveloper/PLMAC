package plm.oop.com.plmac;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapters extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listChildData;

    public ExpandableListAdapters(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listChildData = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChildData.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastchid, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.customlist2, null);

        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.dtv1);
        txtListChild.setText(childText);
        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listChildData.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInfalter = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInfalter.inflate(R.layout.customlist,null);
        }

        final TextView mainListHeader = (TextView) convertView.findViewById(R.id.vstv1);
        mainListHeader.setTypeface(null, Typeface.BOLD);
        final TextView mainListHeader1= (TextView) convertView.findViewById(R.id.vstv2);
        mainListHeader1.setTypeface(null, Typeface.BOLD);

        TextView mainListHeader2= (TextView) convertView.findViewById(R.id.vstv3);
        mainListHeader2.setTypeface(null, Typeface.BOLD);

        TextView mainListHeader3= (TextView) convertView.findViewById(R.id.vstv4);
        mainListHeader3.setTypeface(null, Typeface.BOLD);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mref = database.getReference("Subject");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = "Femfem Garcia";
                ArrayList<String> mainListHeaders = new ArrayList<String>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (userName.compareTo(ds.child("Faculty").getValue().toString()) == 0) {
                        mainListHeaders.add(String.valueOf(ds.getKey()));
                        for(int j = 0; j<mainListHeaders.size();j++)
                        mainListHeader.setText(mainListHeaders.toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return  convertView;
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

