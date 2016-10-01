package com.ts.mobilelab.goggles4u.i;

import com.ts.mobilelab.goggles4u.data.Message;

/**
 * Created by PC2 on 16-03-2016.
 */
public interface Result {
    public void onResult(boolean isSuccess);
    public void onResult(Message message,boolean isSuccess);
}
