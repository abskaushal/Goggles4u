package com.ts.mobilelab.goggles4u.filter;

import org.json.JSONObject;

/**
 * Created by Abhishek on 07-Oct-16.
 */
public interface IFilterData {

    public void onFilterDataReceived(String result, JSONObject receiveJSon);
}
