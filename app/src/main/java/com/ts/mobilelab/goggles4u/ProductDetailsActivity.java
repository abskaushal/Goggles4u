package com.ts.mobilelab.goggles4u;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ts.mobilelab.goggles4u.adapter.BannerImageAdapter;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private Context mContext;
    private static ProductDetailsActivity sInstance;
    private LinearLayout clrswatchslt;
    private TextView specialprice, price, avilablestockview, skuView, framewidth, eyeHeight, eyeWidth, nosebridge, templesize, productName, desc;
    ImageView favImg;
    private ImageLoadingListener animateFirstListener;
    private DisplayImageOptions options;
    private Button selectLenseBtn,uploadPresBtn;
    private String skuid;
    private JSONObject superattributejson;
    private PreferenceData mPreferenceData;

    String mrpprice;
    private RatingBar ratingBar;
    int colorflag = 0;
    RelativeLayout homelt;
    TextView notifCountView;
    static int mNotifCount = 0;
    CirclePageIndicator mPageindicator;
    boolean isMarked = false;
    TextView tryon;

    ArrayList<String> imgList = new ArrayList<>();
    ViewPager mViewPagerimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        //CollapsingToolbarLayout tt = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPreferenceData = new PreferenceData();

        mContext = this;
        sInstance = this;

        animateFirstListener = new AnimateFirstDisplayListener();
        //pid = getIntent().getStringExtra("pid");
        skuid = getIntent().getStringExtra("skuid");
        Log.v("ProductSkuid", "" + skuid);
        // Log.v("productid", "" + pid);
        superattributejson = new JSONObject();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.lg)
                .showImageOnFail(R.drawable.logo_g)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)

                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        initialise();
        init(skuid);
        if (!mPreferenceData.getCartQuoteID().isEmpty()) {

            Log.v("pref", "" + mPreferenceData.getCartItemCount());
            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
            Log.v("mNotifCount", "1st" + mNotifCount);
        } else {
            mNotifCount = 0;
        }
        tryon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TryOnActivity.class).putExtra("skuid", skuid));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.v("onResume","prdetails");
        invalidateOptionsMenu();
        if (!mPreferenceData.getCartQuoteID().isEmpty()) {

            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
        } else {
            mNotifCount = 0;
        }

    }

    private void init(String skuid) {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_PRODUCTDETAILS);
        JSONObject productjson = new JSONObject();
        try {
            productjson.put("item_sku", skuid);
            if(mPreferenceData.isLogincheck()){
                productjson.put("customer_id", mPreferenceData.getCustomerId());
                productjson.put("session_id", "");
            }else{

                productjson.put("customer_id", 0);
                productjson.put("session_id", mPreferenceData.getDEVICE_ID());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(productjson.toString());
    }

    private void initialise() {
        specialprice = (TextView) findViewById(R.id.tv_specialprice);
        price = (TextView) findViewById(R.id.tv_price);
        //avilablestockview = (TextView) findViewById(R.id.tv_instock);
        skuView = (TextView) findViewById(R.id.tv_sku);
        framewidth = (TextView) findViewById(R.id.tv_framewidth);
        eyeHeight = (TextView) findViewById(R.id.tv_eyeheight);
        eyeWidth = (TextView) findViewById(R.id.tv_eyewidth);
        nosebridge = (TextView) findViewById(R.id.tv_nosebridge);
        templesize = (TextView) findViewById(R.id.tv_templesize);
        mViewPagerimg = (ViewPager) findViewById(R.id.viewpager_prdctimg);
      //  uploadPresBtn = (Button) findViewById(R.id.btn_uploadlense);
        // mainImg = (ImageView) findViewById(R.id.imv_mainimg);
        // productName = (TextView) findViewById(R.id.tv_productname);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_productdetails);
       // ratingBar.setProgressTintMode(getResources().getColor(R.color.yellow));
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        avilablestockview = (TextView) findViewById(R.id.tv_instock);
        // glassimglt = (LinearLayout) findViewById(R.id.linear_imgsamplelt);
        clrswatchslt = (LinearLayout) findViewById(R.id.linear_clrswatch);
        mPageindicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPageindicator.setFillColor(getResources().getColor(R.color.maincolor));
        mPageindicator.setStrokeColor(getResources().getColor(R.color.active_green));
        mPageindicator.setStrokeWidth(1); //stroke color i
        mPageindicator.setPageColor(getResources().getColor(R.color.light_gray));
        //mPageindicator.setRadius(6 * density);
        selectLenseBtn = (Button) findViewById(R.id.btn_selectlense);
        desc = (TextView) findViewById(R.id.tv_desc);
        homelt = (RelativeLayout) findViewById(R.id.relativehome);
        tryon = (TextView) findViewById(R.id.btn_trythis);
        favImg = (ImageView) findViewById(R.id.imv_marked);
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        favImg.setOnClickListener(markListener);
        selectLenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject cartjson = new JSONObject();
                Log.v("superattributejson", "" + superattributejson.length());
                if (colorflag == 1) {

                    if (superattributejson == null || superattributejson.toString().trim().length() == 2) {
                        Toast.makeText(mContext, "Please select color", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            cartjson.put("super_attribute", superattributejson);
                            cartjson.put("product_id", productid);
                            cartjson.put("actual_price", mrpprice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v("cartjson", "" + cartjson);
                        startActivity(new Intent(mContext, PrescriptionAddActivity.class).putExtra("type", "products").putExtra("productid", productid).putExtra("skuid", skuid).putExtra("cartdatajson", cartjson.toString()));
                    }

                } else {
                    try {

                        cartjson.put("product_id", productid);
                        cartjson.put("actual_price", mrpprice);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("skuid", "in json" + skuid);
                    //Log.v("productid", "in json" + productid);
                    startActivity(new Intent(mContext, PrescriptionAddActivity.class).putExtra("type", "products").putExtra("productid", productid).putExtra("skuid", skuid).putExtra("cartdatajson", cartjson.toString()));
                }
            }
        });
    }

    private View.OnClickListener markListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject favjson = new JSONObject();
            try {
                if (mPreferenceData.isLogincheck()) {
                    favjson.put("product_id", resultjson.getString("product_id"));
                    favjson.put("customer_id", mPreferenceData.getCustomerId());
                    favjson.put("session_id", "");
                } else {
                    favjson.put("product_id", resultjson.getString("product_id"));
                    favjson.put("customer_id", 0);
                    favjson.put("session_id", mPreferenceData.getDEVICE_ID());
                }
               // Log.v("isMarked","markListener"+isMarked);
                if (!isMarked) {
                    favjson.put("mark", 1);
                    GogglesAsynctask asynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_MARKTOFAVRITE_PRODCTDETAILS);
                    asynctask.execute(favjson.toString());
                } else {
                    favjson.put("mark", 0);
                    GogglesAsynctask asynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_UNMARKTOFAVRITE_PRODCTDETAILS);
                    asynctask.execute(favjson.toString());
                }
            } catch (JSONException e) {
                    e.printStackTrace();
                }


        }



    };

    private void setNotifCount(int count) {
        mNotifCount = count;
        invalidateOptionsMenu();
    }
    private Dialog imgDialog;
    private static final int PICK_FROM_CAMERA = 3000;
    private static final int PICK_FROM_GALLERY = 4000;
    private View.OnClickListener uploadPrescListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imgDialog = new Dialog(mContext);
            imgDialog.setContentView(R.layout.imgdialog);
            imgDialog.setTitle("Upload your Prescription.");
            imgDialog.show();
            ImageView cameraImg = (ImageView) imgDialog.findViewById(R.id.imv_camera);
            ImageView galeryImg = (ImageView) imgDialog.findViewById(R.id.imv_galery);

            //Select image from camera
            cameraImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgDialog.cancel();

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                    startActivityForResult(intent, PICK_FROM_CAMERA);
                }
            });

            galeryImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgDialog.cancel();
                    Intent photoPickerIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    photoPickerIntent.setType("image/*");

                    startActivityForResult(photoPickerIntent, PICK_FROM_GALLERY);
                }
            });
        }
    };
    Bitmap homeBitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == PICK_FROM_CAMERA){
            if(resultCode == RESULT_OK){

                //mgflag = 3;

                homeBitmap = (Bitmap) data.getExtras().get("data");
                Log.v("from camera","bp "+homeBitmap);


            }

        }else if(requestCode == PICK_FROM_GALLERY){


            if(resultCode == RESULT_OK){
                Log.v("from galery", "");
                Uri selectedImage = data.getData();
                Log.v("from galery", "selectedImage" + selectedImage);

                String[] projection = { MediaStore.MediaColumns.DATA };
                CursorLoader cursorLoader = new CursorLoader(this,selectedImage, projection, null, null,
                        null);
                Cursor cursor =cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String picturePath = cursor.getString(column_index);



                Bitmap homitmap = BitmapFactory.decodeFile(picturePath);



            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);
        final View menu_view = menu.findItem(R.id.menu_chkout).getActionView();
        notifCountView = (TextView) menu_view.findViewById(R.id.actionbar_notifcation_textview);
        updateHotCount(mNotifCount);
        menu_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShoppingCartActivity.class));
            }
        });

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);

    }

    public void updateHotCount(final int new_hot_number) {
        mNotifCount = new_hot_number;
        if (notifCountView == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (new_hot_number == 0)
                    notifCountView.setVisibility(View.INVISIBLE);
                else {
                    notifCountView.setVisibility(View.VISIBLE);
                    notifCountView.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }


    // {"item_name":"G4U-111551 ","item_id":"111551","product_id":"46376","mrp":15.95,"you_pay":15.95,"actual_price":"$15.95","formated_price":"$15.95","item_description":"Make a stylish look with this Wayfarer style G4U eyeglasses in Shiny Black color having square shaped rim with wide temples; Made of acetate, best for official, casual or retro look to seem sophisticated and good-looking. Order today!","item_img_thumbnail":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/thumbnail\/100x\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/111551a.jpg","item_img_url":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/image\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/111551a.jpg",
    // "stock_status":"In Stock","min_sal_qty":"1.0000","max_sale_qty":"0.0000","additional_details":{"frame_width":"138 mm","eye_height":"39 mm","eye_width":"49 mm","nose_bridge":"18 mm","temple_size":"139 mm","size":"Large","frame_features":false,"material":"Acetate"},"marked":true,"mediaimages":["http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/image\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/111551a.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/111551a.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/111551s.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/111551f.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/111551w.jpg"]}

    private void setData(final JSONObject resultjson) {

        try {
            getSupportActionBar().setTitle(resultjson.getString("item_name"));
            //productName.setText(resultjson.getString("item_name"));

            //DecimalFormat df = new DecimalFormat("#.00");
            specialprice.setText(resultjson.getString("formated_price"));


           // price.setText(resultjson.getString("mrp"));
            avilablestockview.setText(resultjson.getString("stock_status"));
            int stock_status = resultjson.getInt("inStock");
            if(stock_status == 1){
                avilablestockview.setTextColor(getResources().getColor(R.color.active_green));
            }else{
                avilablestockview.setTextColor(getResources().getColor(R.color.macd_red));
            }


           mrpprice = resultjson.getString("you_pay");
            productid = resultjson.getString("product_id");
            //pdmin = resultjson.getString("pdmin");
            //pdmax = resultjson.getString("pdmax");
            //Log.v("main imgurl", "" + resultjson.getString("item_img_url"));
            // ImageLoader.getInstance().displayImage(resultjson.getString("item_img_url"), mainImg, options, animateFirstListener);

            JSONArray imgary = resultjson.getJSONArray("mediaimages");

            if (imgary != null) {
                for (int j = 0; j < imgary.length(); j++) {
                    imgList.add(imgary.get(j).toString());
                }
            }
            //Log.v("imgList", "" + imgList.size());
            mViewPagerimg.setAdapter(new BannerImageAdapter(mContext, imgList, "fromproduct"));
            mPageindicator.setViewPager(mViewPagerimg);

            if(resultjson.has("reviews")) {
                JSONObject rjson = resultjson.getJSONObject("reviews");
                float rating = Float.parseFloat(rjson.getString("rating"));
                Log.v("rating", "" + rating);
                ratingBar.setRating(rating);
            }else{
                ratingBar.setRating(0);
            }
            //{"Error":false,"ErrorMessage":"","result":{"item_name":"G4U 3312","item_id":"112663-c","product_id":"47742","mrp":6.95,"you_pay":6.95,
            // "item_description":"G4U 3312 in stylish rectangular shape is made of TR90 material in front black color with four different temple colors for your choice. Great for young generations to look eye-catching in any sporty gathering or even in official wear. Get yours now!",
            // "item_img_thumbnail":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/thumbnail\/100x\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/112664a.jpg","item_img_url":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/image\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/112664a.jpg",
            // "min_sal_qty":"1.0000","max_sale_qty":"0.0000","qty_increments":"0.0000","created_date":"2015-11-30 05:02:47","updated_date":"2016-04-02 23:02:27","pdmin":"54","pdmax":"84","additional_details":{"frame_width":"138 mm","eye_height":"28 mm","eye_width":"52 mm","nose_bridge":"17 mm","temple_size":"135 mm","size":"Large",
            // "frame_features":false,"material":"TR90"},"mediaimages":["http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112664s.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112664f.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112664w.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112664a.jpg"]},
            // "config_options":{"attr_code":"color","attribute_id":"92",

            // "options":[{"value":"273","label":"Black-Red"},{"value":"261","label":"Black-Green"}]}}
            JSONObject njson = resultjson.getJSONObject("additional_details");
            skuView.setText(resultjson.getString("item_name"));
            framewidth.setText(njson.getString("frame_width"));
            eyeWidth.setText(njson.getString("eye_width"));
            eyeHeight.setText(njson.getString("eye_height"));
            templesize.setText(njson.getString("temple_size"));
            nosebridge.setText(njson.getString("nose_bridge"));
            desc.setText(resultjson.getString("item_description"));
            isMarked = Boolean.parseBoolean(resultjson.getString("marked"));
            Log.v("isMarked",""+isMarked);
            if(isMarked){
                favImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_24dp));
            }else{
                favImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_outline_24dp));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //ImageLoader.getInstance().displayImage(item_img_thumbnail, mainImg, options, animateFirstListener);    }
    //{"Error":false,"ErrorMessage":"","result":{"item_name":"G4U 3126","item_id":"112921-c","product_id":"48056","mrp":6.95,"you_pay":6.95,"item_description":null,
    // "item_img_thumbnail":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/thumbnail\/100x\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/112922a.jpg",
    // "item_img_url":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/image\/9df78eab33525d08d6e5fb8d27136e95\/images\/catalog\/product\/placeholder\/image.jpg","min_sal_qty":"1.0000","max_sale_qty":"0.0000","qty_increments":"0.0000",
    // "created_date":"2015-12-10 04:37:42","updated_date":"2016-04-04 06:28:58",
    // "mediaimages":["http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112922w.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112922f.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112922s.jpg","http:\/\/www.giftbox.pk\/media\/catalog\/product\/1\/1\/112922a.jpg"]},
    // "config_options":{"attr_code":"color","attribute_id":"92","options":[{"value":"232","label":"Shiny-Black"},{"value":"279","label":"Black-Yellow"},
    // {"value":"261","label":"Black-Green"},{"value":"108","label":"Purple"},{"value":"265","label":"Black-Orange"}]}}


    JSONObject totaljson;
    JSONObject resultjson;

    public static void updateUi(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.homelt.setVisibility(View.VISIBLE);
            sInstance.totaljson = receiveJSon;
            try {
                sInstance.resultjson = receiveJSon.getJSONObject("result");
               // Log.v("resultjson", "" + resultjson);
                sInstance.setData(sInstance.resultjson);
                sInstance.setImage(receiveJSon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }

    }
    String selectedimageid = "";
    private void setImage(final JSONObject receiveJSon) {

        if(imgList!= null && imgList.isEmpty()){
            imgList.clear();
        }

        JSONObject configjson = null;
        if(clrswatchslt.getChildCount()>0){
            clrswatchslt.removeAllViews();
        }

        try {
            if (receiveJSon.has("config_options")) {
                colorflag = 1;

                configjson = receiveJSon.getJSONObject("config_options");

                JSONArray configimgary = configjson.getJSONArray("options");
               // Log.v("configimgary", "" + configimgary);
                for (int i = 0; i < configimgary.length(); i++) {
                    JSONObject imgjson = configimgary.getJSONObject(i);
                   // Log.v("imgjson", "" + imgjson);
                    final ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    int id = Integer.parseInt(imgjson.getString("value"));

                    imageView.setTag(id);

                    // LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();//new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(64, 64);
                    params.leftMargin = 4;
                    imageView.setLayoutParams(params);
                    //Log.v("url", "" + imgjson.getString("swatchimg"));
                    ImageLoader.getInstance().displayImage(imgjson.getString("swatchimg"), imageView, options, animateFirstListener);

                    if(selectedimageid.equals(id+"")){
                        imageView.setBackgroundColor(getResources().getColor(R.color.gray));
                        imageView.setPadding(4, 4, 4, 4);
                    }


                    clrswatchslt.addView(imageView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            selectedimageid =  v.getTag().toString();
                            try {

                                superattributejson.put(receiveJSon.getJSONObject("config_options").getString("attribute_id"), v.getTag());
                                JSONArray configary = receiveJSon.getJSONObject("config_options").getJSONArray("options");
                                for (int i = 0; i < configary.length(); i++) {

                                    JSONObject configjson = configary.getJSONObject(i);

                                    if (v.getTag().toString().equals(configjson.get("value"))) {
                                        if (!imgList.contains(configjson.getString("product_image")))
                                            imgList.add(0, configjson.getString("product_image"));
                                        break;
                                    }
                                }

                                mViewPagerimg.setAdapter(new BannerImageAdapter(mContext, imgList, "fromproduct"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            imageView.setBackgroundColor(getResources().getColor(R.color.gray));
                            imageView.setPadding(4, 4, 4, 4);

                            setImage(receiveJSon);
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateMarkedData(String result, JSONObject receiveJSon, JSONObject sendJsonobj) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
//{"Message":"G4U-111551  was sucessfully unmarked.","Error":false,"marked":false}
            sInstance.setResult(RESULT_OK);
            try {
                String markflag = receiveJSon.getString("marked");
                if(markflag.equals("true")){
                    sInstance.isMarked = true;
                    sInstance.favImg.setImageDrawable(sInstance.getResources().getDrawable(R.drawable.ic_favorite_24dp));

                }else{
                    sInstance.favImg.setImageDrawable(sInstance.getResources().getDrawable(R.drawable.ic_favorite_outline_24dp));
                    sInstance.isMarked = false;

                }
                Toast.makeText(sInstance, "" + receiveJSon.getString("Message"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
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

    String productid;
}
