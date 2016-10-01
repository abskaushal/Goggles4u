package com.ts.mobilelab.goggles4u.data;

/**
 * Created by PC2 on 17-03-2016.
 */
public class NewArrivalData {
    String productimgurl;
    String productprice;
    String productname;
    String productId;
    String formated_price;

    public String getFormated_price() {
        return formated_price;
    }

    public void setFormated_price(String formated_price) {
        this.formated_price = formated_price;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    String productSku;

    public String getProductimgurl() {
        return productimgurl;
    }

    public void setProductimgurl(String productimgurl) {
        this.productimgurl = productimgurl;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductsize() {
        return productsize;
    }

    public void setProductsize(String productsize) {
        this.productsize = productsize;
    }

    String productsize;
}
