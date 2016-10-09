package com.ts.mobilelab.goggles4u.i;

import java.util.Objects;

/**
 * Created by Abhishek on 02-Oct-16.
 * communication interface for activites/fragments for
 * callback from other application components
 */
public interface ICallback {
    public void onSelected(Object o, boolean multiSelect);
}
