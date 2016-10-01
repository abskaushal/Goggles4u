package com.ts.mobilelab.goggles4u;

import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.net.GoogleAsyncTaskGet;
import com.ts.mobilelab.goggles4u.utils.ScaleDListener;
import com.ts.mobilelab.goggles4u.utils.WrapMotionEvent;
import com.ts.mobilelab.goggles4u.views.utils.MoveView;
import com.ts.mobilelab.goggles4u.views.utils.ScaleImageView;
import com.ts.mobilelab.goggles4u.views.utils.TouchImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class TryOnActivity extends AppCompatActivity {


    private static TryOnActivity sInstance;
    private int angle;
    private ScaleImageView homeimg;
    private ImageView frameImg;
    private SeekBar scaleSeekBar,rotateSeekBar;
    private TextView pictureView,frameView;
    private ImageView malesmall,femalesmall,testsmall,selectImage,addimg;
    private LayoutParams layoutParams;
    private RelativeLayout frameLt,homelt,imglt;
    private FrameLayout frameLayout;
    private Context mContext;
    private int viewWidth, viewHeight,screenWidth,screenHeight;
    TouchImageView touchView;
    int roflag=0;
    int imgflag = 1;
    private Bitmap homeBitmap;
    private Dialog imgDialog;
    private static final int PICK_FROM_CAMERA = 3000;
    private static final int PICK_FROM_GALLERY = 4000;
    String skuid;
    Bitmap bm;;
    private  int  frameheight = 100;
    private  int  framewidth = 200;
    private DisplayImageOptions options;
    private LinearLayout framesizelt;
    ImageView addframe,subframe;
    RelativeLayout.LayoutParams layoutParams_frame;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_try_on);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        sInstance = this;
        touchView = new TouchImageView(mContext);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        skuid = getIntent().getStringExtra("skuid");
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.lg)
                .showImageOnFail(R.drawable.logo_g)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                        //.imageScaleType(ImageScaleType.EXACTLY)
                    /*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x-20;
        screenHeight = size.y-200;
       // homelt = (RelativeLayout) findViewById(R.id.linear_view);
       // homelt.addView(touchView);
        pictureView = (TextView) findViewById(R.id.tv_picture);
        frameView = (TextView) findViewById(R.id.tv_frame);
        frameLt = (RelativeLayout) findViewById(R.id.linear_move);
        homeimg = (ScaleImageView) findViewById(R.id.imv_scale);
        frameImg = (ImageView) findViewById(R.id.imv_frame);
        scaleSeekBar = (SeekBar) findViewById(R.id.seekBar_scale);
        rotateSeekBar = (SeekBar) findViewById(R.id.seekBar_rotate);
        //imglt = (LinearLayout) findViewById(R.id.imageView1);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout1);
        addimg = (ImageView) findViewById(R.id.imv_addimg);
        imglt = (RelativeLayout) findViewById(R.id.linear_view);
        framesizelt = (LinearLayout) findViewById(R.id.linear_framsize);
        addframe = (ImageView) findViewById(R.id.imv_addf);
        subframe = (ImageView) findViewById(R.id.imv_subf);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_tryon);
        init(skuid + ".png");
        Log.v("skuid", "" + skuid);

        scaleSeekBar.setEnabled(false);

       if (rotateflag == 1){
            imglt.addView(new TouchImageView(mContext));
            //touchImageView.setImageResource(getResources().getDrawable(R.drawable.glass));
           imglt.addView(new ScaleImageView(mContext));

           //TouchImageView tt = new TouchImageView(homeimg);
        }


        malesmall = (ImageView) findViewById(R.id.imv_smallm);
        femalesmall = (ImageView) findViewById(R.id.imv_smallf);
        testsmall = (ImageView) findViewById(R.id.imv_test);
        Log.v("framewidth","ww"+framewidth);
        Log.v("frameheight","ww"+frameheight);
        layoutParams_frame = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_frame.setMargins(170, 300, 0, 0);
        frameImg.setLayoutParams(layoutParams_frame);


        pictureView.setOnClickListener(pictureclickListener);
        frameView.setOnClickListener(frameClickListener);
        frameImg.setOnTouchListener(framimgTouchListener);
        malesmall.setOnClickListener(malesmallclickListener);
        femalesmall.setOnClickListener(femalesmallclickListener);
        testsmall.setOnClickListener(testsmallclickListener);
       //homeimg.setOnTouchListener(framimgTouchListener);
        addimg.setOnClickListener(addImageListener);
        addframe.setOnClickListener(addFramesizeListener);
        subframe.setOnClickListener(subFramesizeListener);

      scaleSeekBar.setMax(100);
        scaleSeekBar.setProgress(100);
        scaleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.v("progress",""+progress);


                if(rotateflag == 1) {
                    if(imgflag == 1){
                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.male_big);
                    }else if(imgflag == 2){
                         bm = BitmapFactory.decodeResource(getResources(), R.drawable.female_big);
                    }else{

                        //bm = BitmapFactory.decodeResource(getResources(), R.drawable.qq);
                         bm = homeBitmap;
                    }


                    Bitmap resizedbitmap=Bitmap.createScaledBitmap(bm, 48, 48, true);

                    if(progress>0&&progress<=20)
                    {
                        resizedbitmap=Bitmap.createScaledBitmap(bm,196,220, true);
                    }
                    if(progress>20&&progress<=40)
                    {
                        resizedbitmap=Bitmap.createScaledBitmap(bm,240, 290, true);
                    }
                    if(progress>40&&progress<=55)
                    {
                        resizedbitmap=Bitmap.createScaledBitmap(bm,340, 420, true);
                    }
                    if(progress>55&&progress<=65)
                    {
                        resizedbitmap=Bitmap.createScaledBitmap(bm,420, 510, true);
                    }
                    if(progress>65&&progress<=75)
                    {
                        resizedbitmap=Bitmap.createScaledBitmap(bm,470, 560, true);
                    }

                    if(progress>75&&progress<=85)
                    {
                        resizedbitmap=Bitmap.createScaledBitmap(bm,580, 670, true);
                    }
                    if(progress>85&&progress<=95)
                    {
                        resizedbitmap=Bitmap.createScaledBitmap(bm,635, 740, true);
                    }
                    if(progress>95 &&progress<=100)
                    {
                        //resizedbitmap = Bitmap.createScaledBitmap(src,width,height);
                        resizedbitmap=Bitmap.createScaledBitmap(bm,720, 820, true);
                    }
                    Log.v("width",""+homeimg.getWidth());
                   Log.v("height",""+homeimg.getHeight());
                   homeimg.setImageBitmap(resizedbitmap);
                   // touchImageView.setImageBitmap(resizedbitmap);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rotateSeekBar.setMax(360);
        rotateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progresValue, boolean arg2) {
                // TODO Auto-generated method stub
                //Log.v("progress rotate",""+progresValue);
                angle = progresValue;
                if(rotateflag == 0){
                    rotateImage(angle);
                }else{
                    roflag = 1;
                    rotateImage(angle);
                }

            }
        });
    }
    String url = "http://goggles4u.info/frames/";

    private View.OnClickListener addFramesizeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            framewidth = framewidth + 5;
            frameImg.getLayoutParams().width = framewidth;
            frameImg.requestLayout();

        }
    };

    private View.OnClickListener subFramesizeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            framewidth = framewidth - 5;
            frameImg.getLayoutParams().width = framewidth;
           frameImg.requestLayout();

        }
    };

    private void init(String skuid) {
        //Log.v("url+skuid", "" + skuid);
        //ImageLoader.getInstance().displayImage(imgjson.getString("swatchimg"), imageView, options, animateFirstListener);
        //ImageLoader.getInstance().displayImage(url + skuid, frameImg, options);
        Log.v("url+skuid", "" + url + skuid);
        ImageLoader.getInstance().displayImage(url + skuid, frameImg, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                Log.v("failReason", "in frag" + failReason.getType());

                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                }


                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);

                view.setVisibility(View.VISIBLE);

            }


        });
    }

    private  View.OnClickListener pictureclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            homeimg.shouldScaleNow(true);
            pictureView.setTextColor(getResources().getColor(R.color.white));
            pictureView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            frameView.setTextColor(getResources().getColor(R.color.black));
            frameView.setBackgroundColor(getResources().getColor(R.color.white));
            scaleSeekBar.setEnabled(true);
            framesizelt.setVisibility(View.GONE);
            rotateSeekBar.setEnabled(false);

            imglt.addView(new ScaleImageView(mContext));

            rotateflag = 1;
        }
    };
    private View.OnClickListener frameClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            homeimg.shouldScaleNow(true);
            rotateflag = 0;
           // homeimg.setEnabled(false);
            scaleSeekBar.setEnabled(false);
            rotateSeekBar.setEnabled(true);
            frameView.setTextColor(getResources().getColor(R.color.white));
            frameView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            pictureView.setTextColor(getResources().getColor(R.color.black));
            pictureView.setBackgroundColor(getResources().getColor(R.color.white));
            framesizelt.setVisibility(View.VISIBLE);
        }
    };


    private View.OnTouchListener framimgTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            if (rotateflag == 0) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = X - _xDelta;
                        layoutParams.topMargin = Y - _yDelta;
                        layoutParams.rightMargin = -150;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
            }

            frameLayout.invalidate();
            return true;

        }
    };

    private View.OnClickListener malesmallclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imgflag = 1;
            scaleSeekBar.setProgress(100);
            homeimg.setRotation(0);
            rotateSeekBar.setProgress(0);
            homeimg.setImageResource(R.drawable.male_big);
        }
    };
    private View.OnClickListener femalesmallclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imgflag = 2;
            scaleSeekBar.setProgress(100);
            rotateSeekBar.setProgress(0);
            homeimg.setRotation(0);
            homeimg.setImageResource(R.drawable.female_big);
        }
    };
    public View.OnClickListener testsmallclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imgflag = 3;
            scaleSeekBar.setProgress(100);
            rotateSeekBar.setProgress(0);
            homeimg.setRotation(0);

           homeimg.setImageBitmap(homeBitmap);
        }
    };

   private  View.OnClickListener addImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            showImageAddDialog();

        }
    };

    private void showImageAddDialog() {

        imgDialog = new Dialog(mContext);
        imgDialog.setContentView(R.layout.imgdialog);
        imgDialog.setTitle("TryOn");
        imgDialog.show();
        ImageView cameraImg = (ImageView) imgDialog.findViewById(R.id.imv_camera);
        ImageView galeryImg = (ImageView) imgDialog.findViewById(R.id.imv_galery);

        //Select image from camera
        cameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDialog.cancel();

                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
                }

               /* intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                startActivityForResult(intent, PICK_FROM_CAMERA);*/
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
                // photoPickerIntent.putExtra("crop", "true");
                //photoPickerIntent.putExtra("return-data", true);
                //photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(photoPickerIntent, PICK_FROM_GALLERY);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == PICK_FROM_CAMERA){
            if(resultCode == RESULT_OK){
                Log.v("from camera",""+imgflag);
                imgflag = 3;
              Bitmap  cameraBitmap = (Bitmap) data.getExtras().get("data");
               // Log.v("from camera","bp "+homeBitmap);
               // Log.v(" 11camera", "height " + homeBitmap.getHeight());
               // Log.v("11 camera", "width " + homeBitmap.getWidth());

                //homeBitmap.createScaledBitmap(homeBitmap, 600, 700, true);
                //homeBitmap = scaleImage(homeimg,20);
                homeBitmap = scaleBitmap(cameraBitmap,650,800);
                homeimg.setImageBitmap(homeBitmap);
                testsmall.setImageBitmap(homeBitmap);
                scaleSeekBar.setProgress(100);
                homeimg.setRotation(0);
                rotateSeekBar.setProgress(0);

                //Log.v(" camera", "height " + homeBitmap.getHeight());
                //Log.v(" camera", "width " + homeBitmap.getWidth());
            }

        }else if(requestCode == PICK_FROM_GALLERY){


            if(resultCode == RESULT_OK){
                Log.v("from galery", "");
                imgflag = 3;
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
                //to do malebitmap and female bitmap
               // homitmap = BitmapFactory.decodeFile(picturePath);
                //Log.v("from galery","bitmap"+homeBitmap);
                Log.v("bfr 11camera", "height " + homitmap.getHeight());
                Log.v("11 camera", "width " + homitmap.getWidth());
               homeBitmap = scaleBitmap(homitmap,650,800);


                homeimg.setImageBitmap(homeBitmap);
                testsmall.setImageBitmap(homeBitmap);
                scaleSeekBar.setProgress(100);
                homeimg.setRotation(0);
                rotateSeekBar.setProgress(0);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }





    private float curScale = 1F;
    protected void rotateImage(int angle) {
        // TODO Auto-generated method stub
        Log.v("rotate", "" + angle);
        Matrix matrix = new Matrix();
        matrix.postScale(curScale, curScale);
        matrix.postRotate(angle);
       //matrix.postRotate(angle,homeimg.getMeasuredHeight()/2+100,homeimg.getMeasuredWidth()/2+100);

        // Log.v("angle",matrix+""+angle);
        if(rotateflag == 0){
            frameImg.setImageMatrix(matrix);
            frameImg.setRotation(angle);
            frameImg.invalidate();
        }else{

            if(imgflag == 3){
                rotateBitmap = Bitmap.createBitmap(homeBitmap, 0, 0, homeBitmap.getWidth(), homeBitmap.getHeight(), matrix, true);
            }else if(imgflag == 1){
                Bitmap  bmmale = BitmapFactory.decodeResource(getResources(), R.drawable.male_big);
                int ss= homeimg.getWidth()-100;
                int hh = homeimg.getHeight()-50;
                rotateBitmap = Bitmap.createBitmap(bmmale, 0, 0,ss, hh, matrix, true);
            }else {
                Bitmap  bfemale = BitmapFactory.decodeResource(getResources(), R.drawable.female_big);
                rotateBitmap = Bitmap.createBitmap(bfemale, 0, 0, homeimg.getWidth()-100, homeimg.getHeight()-50, matrix, true);
            }

            //Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            homeimg.setImageBitmap(rotateBitmap);
           // Bitmap resizedBitma = Bitmap.createBitmap(getResources().getDrawable(R.drawable.male_big),)
            //homeimg.setImageMatrix(matrix);
           // homeimg.setRotation(angle);
            //homeimg.invalidate();
        }

    }
    Bitmap rotateBitmap;
    protected Bitmap scaleImage(ImageView img, int scale) {
        // TODO Auto-generated method stub
        Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Log.v("width bfr", "" + width);
        Log.v("height bfr",""+height);
        width += scale * WIDTH_SCALE_RATIO;
        height += scale * HEIGHT_SCALE_RATIO;
        Log.v("bitmap width", "" + width);
        Log.v("bitmap height",""+height);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height,
                true);

        // bitmap.compress(CompressFormat.JPEG, 80, imagefile);
        img.setImageBitmap(bitmap);
        return bitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




    private static final int WIDTH_SCALE_RATIO = 10;
    private static final int HEIGHT_SCALE_RATIO = 10;
    private int previousProcess = 0;
    private int PIC_CROP= 222;
    Context context;
    Matrix matrix = new Matrix();
    private int _xDelta;
    private int _yDelta;


    int rotateflag = 0;


    public static void updateUi(String result, JSONObject receiveJSon) {

        if(result.equals(AppConstants.SUCCESSFUL)){


        }else{
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }

    }
}
