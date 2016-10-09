package com.ts.mobilelab.goggles4u.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.i.ICallback;
import com.ts.mobilelab.goggles4u.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Abhishek on 02-Oct-16.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<FilterItem> mFiltersList;
    private ICallback mListener;
    private int mSelectedPos = 0;

    public FilterAdapter(Context context, ArrayList<FilterItem> list, ICallback listener) {
        this.mContext = context;
        this.mFiltersList = list;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_filter_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.labelView.setText(mFiltersList.get(position).getFilterName());
        holder.imageView.setImageResource(Utils.getDrawable(mFiltersList.get(position).getFilterName()));
        holder.itemView.setActivated((mSelectedPos == position) ? true : false);
        holder.itemView.setOnClickListener(null);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                mSelectedPos = pos;
                boolean multiSelect = mFiltersList.get(pos).getmInputType().equals("select")?true:false;
                mListener.onSelected(mFiltersList.get(pos).getFilterCategories(),multiSelect);
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return mFiltersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView labelView;
        ImageView imageView;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            labelView = (TextView) itemView.findViewById(R.id.textview_filter_item);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_filter_item);
        }
    }
}
