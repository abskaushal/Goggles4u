package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.ChildData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PC2 on 14-06-2016.
 */


public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;

    private HashMap<String, ArrayList<ChildData>> listHashMap;
    private ArrayList<String> headerList, tempChild;
    public ArrayList<Object> Childtem = new ArrayList<Object>();

    public ExpandableListAdapter(Context mContext, HashMap<String, ArrayList<ChildData>> categoryMapData, ArrayList<String> headerlist) {
        this.context = mContext;
        this.listHashMap = categoryMapData;
        headerList = headerlist;
        //Log.v("listHashMap", "adptr"+listHashMap.size());
        // Log.v("headerList", "adptr" + headerList.size());
    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // Log.v("getChildrenCount","adptr"+listHashMap.get(headerList.get(groupPosition)).size());
        return listHashMap.get(headerList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // return childPosition;
        return listHashMap.get(headerList.get(groupPosition)).get(childPosition).getName();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        //Log.v("getGroupView",""+groupPosition);
        // Log.v("getGroupView",""+headerTitle);
        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
            convertView.setTag(headerList.get(groupPosition));




        }

        if(isExpanded){
            convertView.setBackgroundResource(R.color.nav_bg_grey_dark);
        }else{
            convertView.setBackgroundResource(R.color.nav_bg_grey_light);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.lblListHeader);
        tv.setText(headerTitle.toUpperCase());


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Log.v("getChildView",""+groupPosition);
        //Log.v("getChildView",""+childPosition);
        // Log.v("child",""+getChild(groupPosition, childPosition));
        //tempChild = (ArrayList<String>) Childtem.get(groupPosition);
        //Log.v("tempChild",""+tempChild);
        //final ChildData childText = (ChildData) getChild(groupPosition, childPosition);
        final String childText = (String) getChild(groupPosition, childPosition);

       /*ArrayList<ChildData> childList =  (ArrayList<String>)*/

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        //convertView.setTag(tempChild.get(childPosition));
        convertView.setTag(listHashMap.get(headerList.get(groupPosition)).get(childPosition).getId());

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
