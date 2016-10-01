package com.ts.mobilelab.goggles4u.data;

/**
 * Created by PC2 on 02-06-2016.
 */
public class ProductColorConfigureData {

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClrimg_url() {
        return clrimg_url;
    }

    public void setClrimg_url(String clrimg_url) {
        this.clrimg_url = clrimg_url;
    }

    private String name;
    private String clrimg_url;
}
