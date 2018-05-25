package com.example.lyantorres.torreslyan_pp6.Objects;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final ArrayList<User> mSavedCards;

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

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group,null);
        }

        // populate the fields
        TextView firstItem = convertView.findViewById(R.id.list_firstItem_TV);
        TextView secondItem = convertView.findViewById(R.id.list_secondItem_TV);

        firstItem.setTypeface(null, Typeface.BOLD);
        firstItem.setText(name);

        secondItem.setText(jobTitle);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        final ProgressBar pb = convertView.findViewById(R.id.list_item_progress);
        ImageView iv = convertView.findViewById(R.id.list_imageView_card);

        // to show when it's loading so that they don't freak out
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);


        // load in the image
        Picasso.with(mContext)
                .load(mSavedCards.get(groupPosition).getSmallCard())
                .into(iv, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // let them know something went wrong
                        pb.setVisibility(View.GONE);
                        Toast.makeText(mContext, "Something went wrong with "+mSavedCards.get(groupPosition).getName()+"'s Card", Toast.LENGTH_SHORT);
                    }
                });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
