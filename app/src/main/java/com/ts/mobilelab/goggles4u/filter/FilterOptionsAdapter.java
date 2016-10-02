package com.ts.mobilelab.goggles4u.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ts.mobilelab.goggles4u.R;

import java.util.ArrayList;

/**
 * Created by Abhishek on 02-Oct-16.
 */
public class FilterOptionsAdapter extends RecyclerView.Adapter<FilterOptionsAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<FilterOptionItem> mOptionsList;

    public FilterOptionsAdapter(Context context) {
        this.mContext = context;
        mOptionsList = new ArrayList<>();
    }


    public void setDataList(ArrayList<FilterOptionItem> list) {
        if (list == null) {
            throw new IllegalArgumentException("There should be atleast one option under each filter");
        }
        mOptionsList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.view_filter_option_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.labelCheckBox.setText(mOptionsList.get(position).getOptionName());
        holder.labelCheckBox.setSelected(mOptionsList.get(position).isSelected());
        holder.labelCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOptionsList.get(position).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOptionsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox labelCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            labelCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_filter_option);
        }
    }

    public void clearData() {
        if (mOptionsList.size() > 0) {
//            for (String s : mOptionsList) {
//                mOptionsList.remove(s);
//            }

            notifyItemRangeRemoved(0, mOptionsList.size());
        }
    }
}
