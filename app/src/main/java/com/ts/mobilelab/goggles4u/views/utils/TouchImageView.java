package com.ts.mobilelab.goggles4u.views.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class TouchImageView extends ImageView {

	// We can be in one of these 3 states  
    static final int NONE = 0;  
    static final int DRAG = 1;  
    static final int ZOOM = 2;  
  
    int mode = NONE;  
 
    // Remember some things for zooming  
    PointF last = new PointF();  
    PointF start = new PointF();  
    float minScale = 1f;  
    float maxScale = 3f;  
    float[] m;
    int viewWidth, viewHeight;  
  
    static final int CLICK = 3;  
  
    float saveScale = 1f;  
  
    protected float origWidth, origHeight;  
  
    int oldMeasuredWidth, oldMeasuredHeight;  
 
    ScaleGestureDetector mScaleDetector;  
 
    Context context; 
    Matrix matrix;  
    
	public TouchImageView(Context context) {
		super(context);
		//Log.v("TouchImageView","11");
		 sharedConstructing(context); 
		// TODO Auto-generated constructor stub
	}

	public TouchImageView(Context context,AttributeSet attributeSet) {
		super(context,attributeSet);
		Log.v("TouchImageView","22");
		sharedConstructing(context);  
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("ClickableViewAccessibility")
	public void sharedConstructing(Context context2) {
		// TODO Auto-generated method stub
		super.setClickable(true);  
		  
        this.context = context2;  
  
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());  
  
        matrix = new Matrix();  
  
        m = new float[19];
  
        setImageMatrix(matrix);  
  
        setScaleType(ScaleType.MATRIX);  
        
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //Log.v("TouchImageView","touched");

                mScaleDetector.onTouchEvent(event);

                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:

                        Log.v("motion evnt", "ACTION_DOWN" + event.getAction());
                        last.set(curr);

                        start.set(last);

                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        Log.v("motion evnt", "ACTION_MOVE" + event.getAction());
                        if (mode == DRAG) {

                            float deltaX = curr.x - last.x;

                            float deltaY = curr.y - last.y;

                            float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);

                            float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);

                            matrix.postTranslate(fixTransX, fixTransY);

                            fixTrans();

                            last.set(curr.x, curr.y);

                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.v("motion evnt", "ACTION_UP" + event.getAction());

                        mode = NONE;

                        int xDiff = (int) Math.abs(curr.x - start.x);

                        int yDiff = (int) Math.abs(curr.y - start.y);

                        if (xDiff < CLICK && yDiff < CLICK)

                            performClick();

                        break;

                    case MotionEvent.ACTION_POINTER_UP:

                        Log.v("motion evnt", "ACTION_POINTER_UP" + event.getAction());
                        mode = NONE;

                        break;

                }
                setImageMatrix(matrix);

                invalidate();

                return true; // indicate event was handled  
            }
        });
        
	}
    public void getsize(){

    }
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);  
		  
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);  
  
        //  
        // Rescales image on rotation  
        //  
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight  
  
                || viewWidth == 0 || viewHeight == 0)  
  
            return;  
  
        oldMeasuredHeight = viewHeight;  
  
        oldMeasuredWidth = viewWidth; 
        
        if (saveScale == 1) {  
        	  
            //Fit to screen.  
  
            float scale;  
  
            Drawable drawable = getDrawable();  
  
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)  
  
                return;  
  
            int bmWidth = drawable.getIntrinsicWidth();  
  
            int bmHeight = drawable.getIntrinsicHeight();  
  
            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);  
  
            float scaleX = (float) viewWidth / (float) bmWidth;  
  
            float scaleY = (float) viewHeight / (float) bmHeight;  
  
            scale = Math.min(scaleX, scaleY);  
  
            matrix.setScale(scale, scale);  
  
            // Center the image  
  
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);  
  
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);  
  
            redundantYSpace /= (float) 2;  
  
            redundantXSpace /= (float) 2;  
  
            matrix.postTranslate(redundantXSpace, redundantYSpace);  
  
            origWidth = viewWidth - 2 * redundantXSpace;  
  
            origHeight = viewHeight - 2 * redundantYSpace;  
  
            setImageMatrix(matrix);  
  
        }  
        fixTrans();
  
        
        
        
	}
	public void fixTrans() {  
		  
        matrix.getValues(m);  
  
        float transX = m[Matrix.MTRANS_X];  
  
        float transY = m[Matrix.MTRANS_Y];  
  
        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);  
  
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);  
  
        if (fixTransX != 0 || fixTransY != 0)  
  
            matrix.postTranslate(fixTransX, fixTransY);  
  
    }  
	
	public float getFixTrans(float trans, float viewSize, float contentSize) {  
		  
        float minTrans, maxTrans;  
  
        if (contentSize <= viewSize) {  
  
            minTrans = 0;  
  
            maxTrans = viewSize - contentSize;  
  
        } else {  
  
            minTrans = viewSize - contentSize;  
  
            maxTrans = 0;  
  
        }  
  
        if (trans < minTrans)  
  
            return -trans + minTrans;  
  
        if (trans > maxTrans)  
  
            return -trans + maxTrans;  
  
        return 0;  
  
    }  
	
	
	public float getFixDragTrans(float delta, float viewSize, float contentSize) {  

        Log.v("contentSize"+contentSize,"viewSize"+viewSize);
        if (contentSize <= viewSize) {  
  
            return 0;  
  
        }  
  
        return delta;  
  
    }

    public void setImageResource(Drawable drawable) {
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
		
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			mode = ZOOM;
			Log.v("onScaleBegin","");
			return true;
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			Log.v("ScaleGestureDetector","");
            float mScaleFactor = detector.getScaleFactor();  
            
            float origScale = saveScale;  
  
            saveScale *= mScaleFactor;  
  
            if (saveScale > maxScale) {  
  
                saveScale = maxScale;  
  
                mScaleFactor = maxScale / origScale;  
  
            } else if (saveScale < minScale) {  
  
                saveScale = minScale;  
  
                mScaleFactor = minScale / origScale;  
  
            }  
 
            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)  
  
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);  
  
            else  
  
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());  
  
            fixTrans();  
  
            return true;  
            
           
  
        }  
		
	}
	public void setMaxZoom(float x) {  
		  
        maxScale = x;  
  
    }  
}
