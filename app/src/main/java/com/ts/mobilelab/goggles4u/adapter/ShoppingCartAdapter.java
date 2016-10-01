package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.CartData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by PC2 on 04-07-2016.
 */
public class ShoppingCartAdapter extends BaseAdapter {

    private ArrayList<CartData> cartList;
    Context context;
    private DisplayImageOptions options;

    public ShoppingCartAdapter(Context mContext, ArrayList<CartData> mCartDataList) {

        this.context = mContext;
        this.cartList = mCartDataList;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.logo_g)
                .showImageOnFail(R.drawable.glass)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                    /*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        return cartList.size();

    }

    public void refresh(ArrayList<CartData> mCartDataList) {
        this.cartList = mCartDataList;
        notifyDataSetChanged();
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
        View view = convertView;
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_cart, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.productimg = (ImageView) convertView.findViewById(R.id.imv_product);
            viewHolder.productadd = (ImageView) convertView.findViewById(R.id.imv_add);
            viewHolder.productremove = (ImageView) convertView.findViewById(R.id.imv_remove);
            viewHolder.productdelete = (ImageView) convertView.findViewById(R.id.imv_deletecart);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.et_qty);

            viewHolder.productName = (TextView) convertView.findViewById(R.id.tv_itemname);
            viewHolder.productunitprice = (TextView) convertView.findViewById(R.id.tv_unitprice);
            viewHolder.perunittotalPrice = (TextView) convertView.findViewById(R.id.tv_totalunitprice);

            viewHolder.ossphr = (TextView) convertView.findViewById(R.id.tv_ossphr);
            viewHolder.oscyl = (TextView) convertView.findViewById(R.id.tv_oscyl);
            viewHolder.osadd = (TextView) convertView.findViewById(R.id.tv_osadd);
            viewHolder.osaxis = (TextView) convertView.findViewById(R.id.tv_osaxis);
            viewHolder.osaxis = (TextView) convertView.findViewById(R.id.tv_osaxis);

            viewHolder.odsphr = (TextView) convertView.findViewById(R.id.tv_odsphr);
            viewHolder.odcyl = (TextView) convertView.findViewById(R.id.tv_odcyl);
            viewHolder.odadd = (TextView) convertView.findViewById(R.id.tv_odadd);
            viewHolder.odaxis = (TextView) convertView.findViewById(R.id.tv_odaxis);

            viewHolder.singlepd = (TextView) convertView.findViewById(R.id.tv_pd);
            viewHolder.rightpd = (TextView) convertView.findViewById(R.id.tv_ritpd);
            viewHolder.odadd = (TextView) convertView.findViewById(R.id.tv_odadd);

            viewHolder.prestype = (TextView) convertView.findViewById(R.id.tv_presctype);
            viewHolder.lens = (TextView) convertView.findViewById(R.id.tv_lens);
            viewHolder.antireflect = (TextView) convertView.findViewById(R.id.tv_antirefl);
            viewHolder.tint = (TextView) convertView.findViewById(R.id.tv_tintying);

            viewHolder.moreview = (ImageView)convertView.findViewById(R.id.imv_more);
            viewHolder.morelt = (LinearLayout)convertView.findViewById(R.id.linear_headerdetails);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.lens.setSelected(true);
        viewHolder.antireflect.setSelected(true);
        DecimalFormat df = new DecimalFormat("#.00");
        viewHolder.productName.setText(cartList.get(position).getItemName());
        viewHolder.productdelete.setVisibility(View.GONE);
        //"$"+df.format(Double.parseDouble(dataArrayList.get(position).getPrice())))

        viewHolder.productunitprice.setText("$" + df.format(Double.parseDouble(cartList.get(position).getItemPrice())));
        viewHolder.perunittotalPrice.setText("$" + df.format(Double.parseDouble(cartList.get(position).getBase_row_total())));
        viewHolder.prestype.setText(cartList.get(position).getPrescriType());
        viewHolder.lens.setText(cartList.get(position).getLens());
        viewHolder.antireflect.setText(cartList.get(position).getReflectingCoating());
        viewHolder.tint.setText(cartList.get(position).getTintying());
        // Log.v("qty", "" + cartList.get(position).getQty());
        viewHolder.quantity.setText(cartList.get(position).getQty());

        ImageLoader.getInstance().displayImage(cartList.get(position).getGlassimgurl(), viewHolder.productimg, options);

        viewHolder.ossphr.setText(cartList.get(position).getOssphr());
        viewHolder.odsphr.setText(cartList.get(position).getOdsphr());
        viewHolder.oscyl.setText(cartList.get(position).getOscyl());
        viewHolder.odcyl.setText(cartList.get(position).getOdcyl());

        viewHolder.osadd.setText(cartList.get(position).getOsadd());
        viewHolder.odadd.setText(cartList.get(position).getOdadd());
        viewHolder.osaxis.setText(cartList.get(position).getOsaxis());
        viewHolder.odaxis.setText(cartList.get(position).getOdaxis());

        viewHolder.moreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("onClick", "" + viewHolder.morelt.getVisibility());
                if (viewHolder.morelt.getVisibility() == View.VISIBLE) {
                    viewHolder.morelt.setVisibility(View.GONE);
                } else {
                    viewHolder.morelt.setVisibility(View.VISIBLE);
                }

            }
        });

        if ((cartList.get(position).getSinglepd() == null) || cartList.get(position).getSinglepd().isEmpty()) {
            viewHolder.singlepd.setText(cartList.get(position).getLeftpd());

            viewHolder.rightpd.setText(cartList.get(position).getRightpd());
        } else {
            viewHolder.singlepd.setText(cartList.get(position).getSinglepd());
        }


      /*  viewHolder.productdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject removejson = new JSONObject();
                try {
                    //{"item_id":"","qty":"","product":"<product_id>","quote_id":"<quote_id>"}
                    removejson.put("item_id", cartList.get(position).getItemId());

                    removejson.put("quote_id", mPreferenceData.getCartQuoteID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                removeCartData(removejson);
            }
        });
*/

        return convertView;
    }

    static class ViewHolder {
        ImageView productimg, productadd, productremove, productdelete,moreview;
        TextView productName, productunitprice, perunittotalPrice, prestype, lens, antireflect, tint, quantity;
        TextView ossphr, odsphr, oscyl, odcyl, osadd, odadd, osaxis, odaxis, singlepd, leftpd, rightpd, odprism, osprism;
        LinearLayout morelt;
    }
}
