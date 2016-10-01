package com.ts.mobilelab.goggles4u.views.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.model.Droid;

public class MoveView extends View{
	
	Bitmap bitmap;
	private Droid droid;
	float x,y;
	Paint mPaint;
	Region mImageRegion;
	private int mTouchSlop;  
    private int mTouchMode;  
    int mScreenHeight;  
    int mScreenWidth;  
    int prevX;  
    boolean canImageMove; 
    int prevY;  
    static final int TOUCH_MODE_TAP = 1;  
    static final int TOUCH_MODE_DOWN = 2;  
    final int mImageWidth = 100;  
    final int mImageHeight = 100; 
    Rect mImagePosition;  
  
    int windowwidth;
    int windowheight;

    private LayoutParams layoutParams;
    
    
    private ScaleGestureDetector mScaleGestureDetector;
	public MoveView(Context context) {
		super(context);
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.qq);
		//droid = new Droid(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 100, 100);
		//mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
	/*	mPaint = new Paint();  
	     mPaint.setTextSize(25);  
	     mPaint.setColor(0xFF0000FF);  
	     //Size for image  
	     mImagePosition = new Rect(10,10,mImageWidth,mImageHeight);  
	     mImageRegion = new Region();  
	     mImageRegion.set(mImagePosition);  
	     final ViewConfiguration configuration = ViewConfiguration.get(context);  
	     mTouchSlop = configuration.getScaledTouchSlop();  
	     Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();    
	     mScreenHeight = display.getHeight();   
	     mScreenWidth = display.getWidth();  
	     canImageMove = false;  */
	    
	}
	//private float mScaleFactor = 1.f;

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		 canvas.save();
		 //canvas.scale(mScaleFactor, mScaleFactor);
		 Paint paint = new Paint();
		 paint.setStyle(Paint.Style.FILL);
		 paint.setColor(Color.CYAN);
		 canvas.drawBitmap(bitmap, x, y, paint); 
		 canvas.restore();

	}

	
	/**
	 * The scale listener, used for handling multi-finger scale gestures.
	 */
	/*private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
	        = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
		 private PointF viewportFocus = new PointF();
		    private float lastSpanX;
		    private float lastSpanY;

		    // Detects that new pointers are going down.
		  

	};*/
/*	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			mScaleFactor *= detector.getScaleFactor();

	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

	        invalidate();
	        return true;

		}
	}*/
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//mScaleGestureDetector.onTouchEvent(event);

		//boolean retVal = mScaleGestureDetector.onTouchEvent(event);
	    /*retVal = mGestureDetector.onTouchEvent(event) || retVal;
	    return retVal || super.onTouchEvent(event);*/

		switch(event.getAction())
	    {
	   case MotionEvent.ACTION_DOWN: {

	   }
	   break;

	   case MotionEvent.ACTION_MOVE: 
	   {
	      x=(int)event.getX();
	      y=(int)event.getY();

	 invalidate();
	 }

	 break;
	   case MotionEvent.ACTION_UP: 

	       x=(int)event.getX();
	       y=(int)event.getY();
	       System.out.println(".......size.........."+x+"......"+y); //x= 345 y=530 
	       invalidate();
	    break; 
	   }
		    		
		   
		  return true;  
	}
	/*@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		 paint.setStyle(Paint.Style.FILL);
		 paint.setColor(Color.CYAN);
		 canvas.drawBitmap(bitmap, x, y, paint); 
	}*/
	
	
	/*public boolean onTouchEvent(MotionEvent event)  
	 {  
	 
	 }  
	   
	 @Override  
	 public void onDraw(Canvas canvas)  
	 {  
	  Paint paint = new Paint();  
	  paint.setStyle(Paint.Style.FILL);  
	  
	  // make the entire canvas white  
	  paint.setColor(Color.CYAN);  
	  Rect rect = new Rect(0,0,this.getWidth(),this.getHeight());  
	  canvas.drawRect(mImagePosition, paint);   
	  //canvas.drawBitmap(bitmap, null,mImagePosition, null);  
	 }  
*/

}
