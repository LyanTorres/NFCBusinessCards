package com.example.lyantorres.torreslyan_pp6.Objects;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.R;
import com.squareup.picasso.Picasso;

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
        return 1;
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

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        ImageButton ib = convertView.findViewById(R.id.list_item_imageButton);

        //Picasso.with(mContext).load(mSavedCards.get(groupPosition).getSmallCard()).into(ib);

        Picasso.with(mContext).load(mSavedCards.get(groupPosition).getSmallCard()).fit().placeholder(R.drawable.app_logo).into(ib);

        Toast.makeText(mContext, "Reading in: " + mSavedCards.get(groupPosition).getSmallCard(), Toast.LENGTH_SHORT).show();

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
