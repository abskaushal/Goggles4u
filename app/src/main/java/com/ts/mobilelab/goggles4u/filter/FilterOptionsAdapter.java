package com.ts.mobilelab.goggles4u.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.ts.mobilelab.goggles4u.R;

import java.util.ArrayList;

/**
 * Created by Abhishek on 02-Oct-16.
 */
public class FilterOptionsAdapter extends RecyclerView.Adapter<FilterOptionsAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<FilterOptionItem> mOptionsList;
    private boolean mMultiSelect = false;

    public FilterOptionsAdapter(Context context) {
        this.mContext = context;
        mOptionsList = new ArrayList<>();
    }


    public void setDataList(ArrayList<FilterOptionItem> list, boolean isMultiSelect) {
        if (list == null) {
            throw new IllegalArgumentException("There should be atleast one option under each filter");
        }
        mOptionsList = list;
        mMultiSelect = isMultiSelect;
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
        holder.labelCheckBox.setOnCheckedChangeListener(null);
        holder.labelCheckBox.setChecked(mOptionsList.get(position).isSelected());
        holder.labelCheckBox.setTag(position);

        holder.radioButton.setText(mOptionsList.get(position).getOptionName());
        holder.radioButton.setOnCheckedChangeListener(null);
        holder.radioButton.setChecked(mOptionsList.get(position).isSelected());
        holder.radioButton.setTag(position);

        if(mMultiSelect){
            holder.radioButton.setVisibility(View.GONE);
            holder.labelCheckBox.setVisibility(View.VISIBLE);
        }else{
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.labelCheckBox.setVisibility(View.GONE);
        }
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int tag = (int) buttonView.getTag();
                resetMultiselect(tag);
            }
        });
        holder.labelCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int tag = (int) buttonView.getTag();

                    mOptionsList.get(tag).setSelected(isChecked);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mOptionsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox labelCheckBox;
        RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            labelCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_filter_option);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_filter_option);
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

    private synchronized void resetMultiselect(int position){
        for(FilterOptionItem item: mOptionsList){
            item.setSelected(false);
        }

        mOptionsList.get(position).setSelected(true);
        notifyDataSetChanged();

    }
}
