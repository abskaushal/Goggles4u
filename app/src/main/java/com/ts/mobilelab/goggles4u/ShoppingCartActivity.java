package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.CartData;
import com.ts.mobilelab.goggles4u.data.Message;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.i.Result;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.net.GoogleAsyncTaskGet;
import com.ts.mobilelab.goggles4u.utils.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {

    private ListView shoppingCartList;
    private ShoppingCartAdapter mShoppingCartAdapter;
    private Context mContext;
    private static ShoppingCartActivity sInstance;
    private PreferenceData mPreferenceData;
    private ArrayList<CartData> mCartDataList;
    private TextView grandTotal,subTotal,discamnt;
    private JSONObject clrjson;
    private Button cpnBtn,checkoutBtn;
    private EditText cpnBox;
    private LinearLayout dicountlt,coupnlt;
    private ImageView applycpn,cancelcpn;
    private  CardView card_subtotalView;
    private int menu_clearflag = 0;
    private ImageView cartimg;
    TextView emptymsg;
    View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        sInstance = this;
        //setHasOptionsMenu(true);

        mPreferenceData = new PreferenceData();
        init();
        shoppingCartList = (ListView) findViewById(R.id.listView_cart);
        shoppingCartList.setEmptyView(empty);
        shoppingCartList.setEmptyView(empty);
        empty = findViewById(R.id.emptylt);

        mCartDataList = new ArrayList<>();
        grandTotal = (TextView) findViewById(R.id.tv_grandtotal);
        shoppingCartList.setEmptyView(empty);
        cpnBox = (EditText) findViewById(R.id.edt_cpnbox);
        //cpnBtn = (Button) findViewById(R.id.btn_cpn);
        applycpn = (ImageView) findViewById(R.id.imv_apply);
        cancelcpn = (ImageView) findViewById(R.id.imv_cancel);
        subTotal = (TextView) findViewById(R.id.tv_subtotal);

        discamnt = (TextView) findViewById(R.id.tv_discamnt);
        dicountlt = (LinearLayout) findViewById(R.id.linear_disc);
        card_subtotalView = (CardView) findViewById(R.id.card_subtotal);
        coupnlt = (LinearLayout) findViewById(R.id.linear_cpn);
        checkoutBtn = (Button) findViewById(R.id.btn_chkout);
        card_subtotalView.setVisibility(View.GONE);
        coupnlt.setVisibility(View.GONE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        cartimg = (ImageView) findViewById(R.id.imv_emptycart);
        applycpn.setOnClickListener(couponListener);
        cancelcpn.setOnClickListener(removecpnListener);
        checkoutBtn.setOnClickListener(chkoutListener);
        if(!mPreferenceData.getCartQuoteID().isEmpty()){
            menu_clearflag = 1;
        }
        invalidateOptionsMenu();
        //mShoppingCartAdapter = new ShoppingCartAdapter(mContext);
        //shoppingCartList.setAdapter(mShoppingCartAdapter);
       // {"coupon_code":"64456456","quote_id":"49","remove":1}

        // {"item_id":"105","qty":"3","product":"47742","quote_id":"45","configurable_option":{"92": 261},}
        cartimg.setOnClickListener(emptyCartListener);
    }

        private View.OnClickListener emptyCartListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(mContext,HomeActivity.class));
            }
        };

    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);
        finish();
    }

    private View.OnClickListener couponListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject cpnjson  = new JSONObject();
            String cpncode = cpnBox.getText().toString();
            if(cpncode.isEmpty()){
                PrescriptionAddActivity.show_dialog(mContext,"Enter coupon code");
            }else{

                try {
                    cpnjson.put("coupon_code",cpncode);
                    cpnjson.put("quote_id",mPreferenceData.getCartQuoteID());
                    cpnjson.put("coupon_code",cpncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCouponData(cpnjson);
            }



        }
    };

    private void sendCouponData(JSONObject cpnjson) {

        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext,AppConstants.CODE_FOR_APPLYCOUPON);
        gogglesAsynctask.execute(cpnjson.toString());


    }

    private View.OnClickListener removecpnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject removecpnjson  = new JSONObject();

            String cpncode = cpnBox.getText().toString();
            if(cpncode.isEmpty()){
                PrescriptionAddActivity.show_dialog(mContext, "Enter coupon code");
            }else {
                try {
                    removecpnjson.put("coupon_code", cpncode);
                    removecpnjson.put("quote_id", mPreferenceData.getCartQuoteID());
                    removecpnjson.put("coupon_code", cpncode);
                    removecpnjson.put("remove", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCouponData(removecpnjson);
            }
        }

    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v("menu_clearflag",""+menu_clearflag);
        if(menu_clearflag == 1){
            menu.findItem(R.id.menu_clear).setVisible(true);
        }else{
            menu.findItem(R.id.menu_clear).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void showData(JSONObject receiveJSon) {


        mCartDataList = GogglesManager.getInstance().getCartDataArrayList();
        shoppingCartList.setAdapter(new ShoppingCartAdapter(mContext, mCartDataList));

            //Log.v("data json", "1111" + receiveJSon.getString("data"));

        String data = null;
        try {
            data = receiveJSon.getString("data");
            if ("null".equalsIgnoreCase(data) || data.isEmpty()){
                View emView = sInstance.shoppingCartList.getEmptyView();
                TextView emptymsgv = (TextView)emView.findViewById(R.id.empty);
                emptymsgv.setText(receiveJSon.getString("Message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //if(str != null && !str.isEmpty()) {

            if (data != null && !data.isEmpty() && !"null".equalsIgnoreCase(data)) {
                try {

                card_subtotalView.setVisibility(View.VISIBLE);
                    coupnlt.setVisibility(View.VISIBLE);
                menu_clearflag = 1;
                   // Log.v("menu_clearflag","insoide"+menu_clearflag);
                JSONObject mainjson = receiveJSon.getJSONObject("data");
                    Log.v("mainjson",""+mainjson);
                DecimalFormat dff = new DecimalFormat("#.00");
               // Log.v("grandTotal", "" + mainjson.getString("grand_total"));
                grandTotal.setText("$" + dff.format(Double.parseDouble(mainjson.getString("grand_total"))));
                subTotal.setText("$" + dff.format(Double.parseDouble(mainjson.getString("subtotal"))));
                int discval = Integer.parseInt(mainjson.getString("discount_amount"));
                    Log.v("discount_amount",""+discval);
                if (discval!=0) {
                    dicountlt.setVisibility(View.VISIBLE);
                } else {
                    dicountlt.setVisibility(View.INVISIBLE);
                }
                //Double d = Double.parseDouble(mainjson.getString("subtotal")) - Double.parseDouble(mainjson.getString("grand_total"));
                //discamnt.setText("$"+dff.format(Double.parseDouble(mainjson.getString(""))));
                discamnt.setText("$" +discval);
                JSONArray itemAry = mainjson.getJSONArray("items");
                Log.v("itemAry size", "" + itemAry.length());
                mPreferenceData.setCartItemCount("" + itemAry.length());

                for (int i = 0; i < itemAry.length(); i++) {
                    JSONObject cartjson = itemAry.getJSONObject(i);
                    if (cartjson.has("configurable_option")) {
                        JSONObject optionjson = cartjson.getJSONObject("configurable_option");
                        clrjson = optionjson.getJSONObject("attribute-value");
                        Log.v("clrjson", "" + clrjson);
                    }

                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }else{
                card_subtotalView.setVisibility(View.GONE);
                coupnlt.setVisibility(View.GONE);

             /*   try {
                    Toast.makeText(mContext, "" + receiveJSon.getString("Message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

    }

    private View.OnClickListener chkoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!mPreferenceData.getCartQuoteID().isEmpty()) {
                if (mPreferenceData.isLogincheck()) {
                    startActivity(new Intent(mContext, CheckoutActivity.class));
                } else {

                    startActivityForResult(new Intent(mContext, Login.class).putExtra("loginintent", "addtocart"), 112);
                }
            }else{
                Toast.makeText(mContext,"Add Product to continue.",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_OK){
            if(resultCode == 112){
                startActivity(new Intent(mContext,CheckoutActivity.class));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {

        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_CARTDETAILS);
        JSONObject productjson = new JSONObject();
        try {
            productjson.put("quote_id", mPreferenceData.getCartQuoteID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(productjson.toString());
    }

    public static void updateData(String result, JSONObject receiveJSon) {
        Log.v("updateData", "" + result);
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.showData(receiveJSon);
        } else {


            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }

    }

    public static void updateCartData(String result, JSONObject sendJSon) {
        Log.v("updateCartData", "" + result);
        if (result.equals(AppConstants.SUCCESSFUL)) {
            //sInstance.showData(receiveJSon);
            sInstance.init();
            try {
                if(sendJSon.has("coupon_code")) {
                    sInstance.cpnBox.setText(sendJSon.getString("coupon_code") + " is applied");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v("updateCartData", "" + result);
        } else {

            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
    public static void clearCartData(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            //sInstance.showData(receiveJSon);
            sInstance.mPreferenceData.setCartQuoteID("");
            sInstance.init();
          // ArrayList tempList = GogglesManager.getInstance().getCartDataArrayList();
            //tempList.clear();
            //sInstance.mShoppingCartAdapter.refresh(tempList);

            //sInstance.init();
            Log.v("updateCartData", "" + result);
        } else {
            Toast.makeText(sInstance, "clearCartData" + result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        } else if (item.getItemId() == R.id.menu_clear) {

            if(mPreferenceData.getCartQuoteID().isEmpty()){
                Toast.makeText(mContext,"No item found in your cart",Toast.LENGTH_LONG).show();
            }else{
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Clear Cart");
                alertDialog.setMessage("Do u want to clear the cart!");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("yesss","");
                        clearCart();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.create().show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void clearCart()  {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext,AppConstants.CODE_FOR_CARTCLEAR);
        JSONObject clearjson = new JSONObject();

        try {
            clearjson.put("quote_id",mPreferenceData.getCartQuoteID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(clearjson.toString());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);

        return true;
    }



    private class ShoppingCartAdapter extends BaseAdapter {

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

        public void refresh(ArrayList<CartData> mCartDataList){
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
                // viewHolder.osprism = (TextView) convertView.findViewById(R.id.tv_osprism);

                viewHolder.odsphr = (TextView) convertView.findViewById(R.id.tv_odsphr);
                viewHolder.odcyl = (TextView) convertView.findViewById(R.id.tv_odcyl);
                viewHolder.odadd = (TextView) convertView.findViewById(R.id.tv_odadd);
                viewHolder.odaxis = (TextView) convertView.findViewById(R.id.tv_odaxis);
                //viewHolder.odprism = (TextView) convertView.findViewById(R.id.tv_odprism);


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
            //"$"+df.format(Double.parseDouble(dataArrayList.get(position).getPrice())))

            viewHolder.productunitprice.setText("$" + df.format(Double.parseDouble(cartList.get(position).getItemPrice())));
            viewHolder.perunittotalPrice.setText("$" + df.format(Double.parseDouble(cartList.get(position).getBase_row_total())));
            viewHolder.prestype.setText(cartList.get(position).getPrescriType());
            viewHolder.lens.setText(cartList.get(position).getLens());
            Log.v("antireflect",""+cartList.get(position).getReflectingCoating());
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
                    Log.v("onClick",""+viewHolder.morelt.getVisibility());
                    if (viewHolder.morelt.getVisibility() == View.VISIBLE){
                        viewHolder.morelt.setVisibility(View.GONE);
                    }else{
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


            viewHolder.productdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        //{"item_id":"","qty":"","product":"<product_id>","quote_id":"<quote_id>"}
                        new CustomDialog().showDltDialog("Goggles4u cart", "Do you want to remove the item?", context, new Result() {
                            @Override
                            public void onResult(boolean isSuccess) {
                                if (isSuccess) {
                                    JSONObject removejson = new JSONObject();
                                    try {
                                        removejson.put("item_id", cartList.get(position).getItemId());
                                        removejson.put("quote_id", mPreferenceData.getCartQuoteID());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    removeCartData(removejson);
                                }
                            }

                            @Override
                            public void onResult(Message message, boolean isSuccess) {

                            }
                        });




                }
            });


            return convertView;
        }

        private void deleteCartItem() {


        }

        private void removeCartData(JSONObject removejson) {
            GogglesAsynctask mGogglesAsynctask = new GogglesAsynctask(context, AppConstants.CODE_FOR_CART_REMOVE);
            mGogglesAsynctask.execute(removejson.toString());
        }

        /*private void updateCartData(JSONObject updatejson) {
            GogglesAsynctask mGogglesAsynctask = new GogglesAsynctask(context, AppConstants.CODE_FOR_CARTUPDATE);
            mGogglesAsynctask.execute(updatejson.toString());

        }*/


    }

    static class ViewHolder {
        ImageView productimg, productadd, productremove, productdelete,moreview;
        TextView productName, productunitprice, perunittotalPrice, prestype, lens, antireflect, tint,quantity;
        TextView ossphr, odsphr, oscyl, odcyl, osadd, odadd, osaxis, odaxis, singlepd, leftpd, rightpd, odprism, osprism;
        LinearLayout morelt;

    }

}
