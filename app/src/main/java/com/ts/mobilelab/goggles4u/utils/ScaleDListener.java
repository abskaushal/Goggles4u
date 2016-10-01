package com.ts.mobilelab.goggles4u.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.ScaleGestureDetector;

/**
 * Created by PC2 on 07-07-2016.
 */

public class ScaleDListener implements ScaleGestureDetector.OnScaleGestureListener {
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

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub
        mode = ZOOM;
        Log.v("onScaleBegin","");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub
        Log.v("ScaleGestureDetector", "");
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

}
