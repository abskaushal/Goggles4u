package com.ts.mobilelab.goggles4u.utils;

import com.ts.mobilelab.goggles4u.R;

import java.util.Stack;

/**
 * Created by Abhishek on 08-Oct-16.
 */
public class Utils {

    public static int getDrawable(String type) {
        if (type.equals("PRICE")) {
            return R.drawable.filter_price;

        } else if (type.equals("SIZE")) {
            return R.drawable.filter_size;
        } else if (type.equals("SHAPE")) {
            return R.drawable.filter_shape;
        } else if (type.equals("MATERIAL")) {
            return R.drawable.filter_material;
        } else if (type.equals("FRAME STYLE")) {
            return R.drawable.filter_style;

        } else if (type.equals("PRESCRIPTION TYPE")) {
            return R.drawable.filter_prescription;
        } else if (type.equals("FRAME TYPE")) {
            return R.drawable.ic_actionbar_logo;

        } else if (type.equals("COLOR")) {
            return R.drawable.filter_color;
        } else if (type.equals("PD")) {
            return R.drawable.filter_pd;
        } else if (type.equals("MANUFACTURER")) {
            return R.drawable.ic_actionbar_logo;
        } else {
            return R.drawable.ic_actionbar_logo;
        }

    }
}
