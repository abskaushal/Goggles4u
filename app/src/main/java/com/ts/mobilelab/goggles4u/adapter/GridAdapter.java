package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.paging.gridview.PagingBaseAdapter;
import com.ts.mobilelab.goggles4u.ProductDetailsActivity;
import com.ts.mobilelab.goggles4u.R;

import com.ts.mobilelab.goggles4u.TryOnActivity;
import com.ts.mobilelab.goggles4u.data.NewArrivalData;
import com.ts.mobilelab.goggles4u.data.ProductData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mahesh on 22-03-2016.
 */
public class GridAdapter extends PagingBaseAdapter<String> {

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Context ctx;
    private ArrayList<ProductData> dataArrayList;

    public GridAdapter(Context mContext, ArrayList<ProductData> dataList) {
        dataArrayList = dataList;
        ctx = mContext;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.logo_g)
                .showImageOnFail(R.drawable.ic_close_24dp)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
					/*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }


    public void updateData(ArrayList<ProductData> dataList){
        dataArrayList = dataList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dataArrayList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.product_griditem,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.product_img = (ImageView) convertView.findViewById(R.id.imv_productgrid);
            viewHolder.productId = (TextView) convertView.findViewById(R.id.tv_productid);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.tv_productprice);
            viewHolder.productrating = (RatingBar) convertView.findViewById(R.id.ratingBar_grid);
            viewHolder.colorlt = (LinearLayout) convertView.findViewById(R.id.linear_colrlt);
            viewHolder.tryonBtn = (Button) convertView.findViewById(R.id.btn_tryon_grid);
            viewHolder.selectBtn = (Button) convertView.findViewById(R.id.btn_select_grid);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       // viewHolder.product_img.
        convertView.setId(position);
        final View finalConvertView = convertView;
       convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("clicked","convertView");

                ctx.startActivity(new Intent(ctx, ProductDetailsActivity.class).putExtra("id",dataArrayList.get(position).getProductSku()));
            }
        });
        viewHolder.product_img.setTag(dataArrayList.get(position).getProductSku());
       // Log.v("ProductSku", "grid" + dataArrayList.get(position).getProductSku());
       // Log.v("ProductId", "grid" + dataArrayList.get(position).getProductId());
        viewHolder.productId.setText(dataArrayList.get(position).getProductName());
        DecimalFormat df = new DecimalFormat("#.00");
        viewHolder.productPrice.setText("$"+df.format(Double.parseDouble(dataArrayList.get(position).getPrice())));
        ImageLoader.getInstance().displayImage(dataArrayList.get(position).getSmallimg_url(), viewHolder.product_img, options, animateFirstListener);
            int size  = dataArrayList.get(position).getColrListData().size();
       // Log.v("size",""+size);
       // Log.v("position",""+position);
        viewHolder.colorlt.removeAllViews();
            for(int i=0;i<size;i++){
                ImageView imageView = new ImageView(ctx);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                int id = Integer.parseInt(dataArrayList.get(position).getColrListData().get(i).getId());
                imageView.setId(id);

               // LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();//new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(48, 48);
                params.leftMargin=4;
                //params.bottomMargin=4;
                //params.height = 24;
                //params.weight = 24;
                //params.width = 24;

                imageView.setLayoutParams(params);
                ImageLoader.getInstance().displayImage(dataArrayList.get(position).getColrListData().get(i).getClrimg_url(), imageView, options, animateFirstListener);

                viewHolder.colorlt.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("id", "" + v.getId());
                       // viewHolder.product_img.setImageURI(dataArrayList.get(position).getImage_url());
                        ImageLoader.getInstance().displayImage(dataArrayList.get(position).getImage_url(),  viewHolder.product_img, options, animateFirstListener);
                       // dataArrayList.get(position).

                    }
                });
            }
        viewHolder.tryonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctx.startActivity(new Intent(ctx, TryOnActivity.class).putExtra("skuid",dataArrayList.get(position).getProductSku()));
            }
        });


        return convertView;
    }
    static class ViewHolder{
        ImageView product_img;
        TextView productId,productPrice;
        RatingBar productrating;
        LinearLayout colorlt;
        Button tryonBtn,selectBtn;
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
