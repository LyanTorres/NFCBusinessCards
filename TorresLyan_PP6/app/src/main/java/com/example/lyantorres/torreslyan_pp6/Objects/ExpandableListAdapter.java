package com.example.lyantorres.torreslyan_pp6.Objects;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.lyantorres.torreslyan_pp6.R;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<User> mSavedCards = new ArrayList<>();

    public ExpandableListAdapter(Context _context, ArrayList<User> _arrayList){
        mContext = _context;
        mSavedCards = _arrayList;
    }

    @Override
    public int getGroupCount() {
        return mSavedCards.size();
    }

    @Override

    public int getChildrenCount(int groupPosition) {
        return mSavedCards.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mSavedCards.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String name = mSavedCards.get(groupPosition).getName();
        String jobTitle = mSavedCards.get(groupPosition).getJobTitle();

        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group,null);
        }

        TextView firstItem = convertView.findViewById(R.id.list_firstItem_TV);
        TextView secondItem = convertView.findViewById(R.id.list_secondItem_TV);

        firstItem.setTypeface(null, Typeface.BOLD);
        firstItem.setText(name);

        secondItem.setText(jobTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // TODO: SET UP CHILD VIEW lol
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
