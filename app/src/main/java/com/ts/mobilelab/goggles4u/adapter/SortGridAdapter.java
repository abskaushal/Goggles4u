package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ts.mobilelab.goggles4u.apis.IWebService;
import com.ts.mobilelab.goggles4u.apis.ProductListAsync;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.data.ProductData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mahesh on 22-03-2016.
 */
public class SortGridAdapter extends PagingBaseAdapter<String> {

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Context ctx;
    private ArrayList<ProductData> dataArrayList;
    private PreferenceData mPreferenceData;
    private final int DUMMY = -1;


    public SortGridAdapter(Context mContext, ArrayList<ProductData> dataList) {
        dataArrayList = dataList;
        ctx = mContext;
        mPreferenceData = new PreferenceData();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.lg)
                .showImageOnFail(R.drawable.ic_close_24dp)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
					/*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    public void refresh(ArrayList<ProductData> dataList){
        dataArrayList = dataList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount()
    {
       // Log.v("SortGridAdapter",""+dataArrayList.size());
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
            convertView = inflater.inflate(R.layout.product_grid,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.product_img = (ImageView) convertView.findViewById(R.id.imv_productgrid);
            viewHolder.productId = (TextView) convertView.findViewById(R.id.tv_productid);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.tv_productprice);
            //viewHolder.productrating = (RatingBar) convertView.findViewById(R.id.ratingBar_grid);
            viewHolder.colorlt = (LinearLayout) convertView.findViewById(R.id.linear_colrlt);
            viewHolder.tryonBtn = (TextView) convertView.findViewById(R.id.btn_tryon_grid);
           // viewHolder.selectBtn = (Button) convertView.findViewById(R.id.btn_select_grid);
            viewHolder.fav_img = (ImageView) convertView.findViewById(R.id.imv_addtofav);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setId(position);
       final ProductData productData = dataArrayList.get(position);



      convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               ctx.startActivity(new Intent(ctx, ProductDetailsActivity.class).putExtra("pid",dataArrayList.get(position).getProductId()).putExtra("skuid",dataArrayList.get(position).getProductSku()));
               // ctx.startActivity(new Intent(ctx, TryOnActivity.class));
            }
        });
        /*viewHolder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, ProductDetailsActivity.class).putExtra("pid",dataArrayList.get(position).getProductId()).putExtra("skuid",dataArrayList.get(position).getProductSku()));
            }
        });*/

        viewHolder.fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject favjson = new JSONObject();

                try {
                    if (mPreferenceData.isLogincheck()) {
                        favjson.put("product_id", productData.getProductId());
                        favjson.put("customer_id", mPreferenceData.getCustomerId());
                        favjson.put("session_id", "");
                    } else {
                        favjson.put("product_id", productData.getProductId());
                        favjson.put("customer_id", 0);
                        favjson.put("session_id", mPreferenceData.getDEVICE_ID());
                    }
                   // Log.v(".isProductMarked()",""+productData.isProductMarked());

                    if (productData.isProductMarked() == false) {
                        favjson.put("mark", 1);
                        //GogglesAsynctask asynctask = new GogglesAsynctask(ctx, AppConstants.CODE_FOR_MARKTOFAVRITE);
                        ProductListAsync asynctask = new ProductListAsync(ctx, (IWebService)ctx,AppConstants.CODE_FOR_MARKTOFAVRITE,DUMMY);
                        asynctask.execute(favjson.toString());
                    } else {
                        //GogglesAsynctask asynctask = new GogglesAsynctask(ctx, AppConstants.CODE_FOR_UNMARKTOFAVRITE);
                        ProductListAsync asynctask = new ProductListAsync(ctx, (IWebService)ctx,AppConstants.CODE_FOR_UNMARKTOFAVRITE,DUMMY);
                        asynctask.execute(favjson.toString());
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        viewHolder.product_img.setTag(productData.getProductSku());
        viewHolder.productId.setText(productData.getProductName());
        //Log.v(".isProductMarked()", "" + productData.isProductMarked());
        if(productData.isProductMarked()){
            viewHolder.fav_img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_favorite_24dp));
        }else{
            viewHolder.fav_img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_favorite_outline_24dp));
        }

        viewHolder.productPrice.setText(productData.getFormated_given_price());

        ImageLoader.getInstance().displayImage(productData.getSmallimg_url(), viewHolder.product_img, options, animateFirstListener);

        int size  = dataArrayList.get(position).getColrListData().size();
        Log.v("size","www"+ dataArrayList.get(position).getColrListData().size());
        viewHolder.colorlt.removeAllViews();
        for(int i=0;i<size;i++){
            ImageView imageView = new ImageView(ctx);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            int id = Integer.parseInt(dataArrayList.get(position).getColrListData().get(i).getId());
            imageView.setId(id);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
            params.leftMargin=4;


            imageView.setLayoutParams(params);
            ImageLoader.getInstance().displayImage(dataArrayList.get(position).getColrListData().get(i).getClrimg_url(), imageView, options, animateFirstListener);

            viewHolder.colorlt.addView(imageView);

        }
        viewHolder.tryonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctx.startActivity(new Intent(ctx, TryOnActivity.class).putExtra("skuid",productData.getProductSku()));
            }
        });

        return convertView;
    }



    static class ViewHolder{
        ImageView product_img,fav_img;
        TextView productId,productPrice,tryonBtn;
        RatingBar productrating;
        LinearLayout colorlt;
        Button selectBtn;
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
