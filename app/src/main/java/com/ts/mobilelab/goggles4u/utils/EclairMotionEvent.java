package com.ts.mobilelab.goggles4u.utils;

import android.view.MotionEvent;

/**
 * Created by PC2 on 16-07-2016.
 */
public class EclairMotionEvent extends WrapMotionEvent {
    public EclairMotionEvent(MotionEvent event) {
        super(event);
    }
    public float getX(int pointerIndex) {
        return event.getX(pointerIndex);
    }
    public float getY(int pointerIndex) {
        return event.getY(pointerIndex);
    }
    public int getPointerCount() {
        return event.getPointerCount();
    }
    public int getPointerId(int pointerIndex) {
        return event.getPointerId(pointerIndex);
    }
}
