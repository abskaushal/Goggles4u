package com.ts.mobilelab.goggles4u.data;

import java.util.ArrayList;

/**
 * Created by Mahesh on 02-06-2016.
 */
public class ProductData {

    private String productId;
    private String productName;
    private String productSku;
    private String shortdesription;
    private String description;
    private String price;
    private String special_price;
    private String product_url;

    public String getFormated_given_price() {
        return formated_given_price;
    }

    public void setFormated_given_price(String formated_given_price) {
        this.formated_given_price = formated_given_price;
    }

    private String formated_given_price;


    private boolean productMarked ;


    //private int favSelected = 0; //1-for fav selcted 0- for unselect

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getShortdesription() {
        return shortdesription;
    }

    public void setShortdesription(String shortdesription) {
        this.shortdesription = shortdesription;
    }
    /*public int getFavSelected() {
        return favSelected;
    }

    public void setFavSelected(int favSelected) {
        this.favSelected = favSelected;
    }*/
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getspecial_price() {
        return special_price;
    }

    public void setspecial_price(String specila_price) {
        this.special_price = specila_price;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSmallimg_url() {
        return smallimg_url;
    }

    public void setSmallimg_url(String smallimg_url) {
        this.smallimg_url = smallimg_url;
    }

    public ArrayList<ProductColorConfigureData> getColrListData() {
        if(colrListData == null){
            colrListData = new ArrayList<>();
        }
        return colrListData;
    }

    public void setColrListData(ArrayList<ProductColorConfigureData> colrListData) {
        this.colrListData = colrListData;
    }

    public boolean isProductMarked() {
        return productMarked;
    }

    public void setProductMarked(boolean productMarked) {
        this.productMarked = productMarked;
    }
    private String image_url;
    private String smallimg_url;
    private ArrayList<ProductColorConfigureData> colrListData;

}
