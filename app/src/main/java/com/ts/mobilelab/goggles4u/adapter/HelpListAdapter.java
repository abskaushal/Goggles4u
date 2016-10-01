package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.FavoriteData;
import com.ts.mobilelab.goggles4u.data.HelpData;

import java.util.ArrayList;

/**
 * Created by PC2 on 09-08-2016.
 */
public class HelpListAdapter extends BaseAdapter {
    private ArrayList<HelpData> helpList;
    Context context;
    public HelpListAdapter(Context mContext, ArrayList<HelpData> helpdataList) {
        this.context = mContext;
        this.helpList = helpdataList;
    }

    @Override
    public int getCount() {
        return helpList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.help_list_row,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.headerView = (TextView) view.findViewById(R.id.tv_header);
            viewHolder.detailsView = (TextView) view.findViewById(R.id.tv_details);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.headerView.setText(helpList.get(position).getHeader());
        viewHolder.detailsView.setText(helpList.get(position).getContent());

        return view;
    }
    private  static class ViewHolder {


        TextView headerView,detailsView;

    }

}
