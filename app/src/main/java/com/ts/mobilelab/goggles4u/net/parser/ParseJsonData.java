package com.ts.mobilelab.goggles4u.net.parser;

import android.content.Context;
import android.util.Log;

import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.CartData;
import com.ts.mobilelab.goggles4u.data.ChildData;
import com.ts.mobilelab.goggles4u.data.CollectionImageData;
import com.ts.mobilelab.goggles4u.data.HeaderData;
import com.ts.mobilelab.goggles4u.data.NewArrivalData;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.data.PrescriptionData;
import com.ts.mobilelab.goggles4u.data.ProductColorConfigureData;
import com.ts.mobilelab.goggles4u.data.ProductData;
import com.ts.mobilelab.goggles4u.logs.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Mahesh on 16-03-2016.
 */
public class ParseJsonData {
    private String status = "";
    private String responseMessage = "";
    private String hasTrue;
    private PreferenceData mPrefdata;
    private JSONArray promoimgArray;
    private ArrayList<String> bannerimgList;
    private ArrayList<NewArrivalData> productDataList;
    private JSONArray productDetailsArray;
    private JSONArray collectionImageArray;
    private ArrayList<CollectionImageData> colectionImageList;

    public ParseJsonData(Context mContext) {

        mPrefdata = new PreferenceData();
    }


    /*public String parseData(JSONObject receiveJSon, Context mContext) {

       // Log.v("receiveJSon","in parse"+receiveJSon);
        try {

            status = receiveJSon.getString("ErrorMessage");
          // tagid = receiveJSon.getInt("tagId")
            JSONObject result = receiveJSon.getJSONObject("Results");
            Log.v("result total",""+result);
            bannerimgList = new ArrayList<String>();

            collectionImageArray = result.getJSONArray("CollectionsImageData");
           // Log.v("CollectionsImageData",""+collectionImageArray);
            productDetailsArray = result.getJSONArray("NewArrivalProductList");
           // Log.v("productDetailsArray",""+productDetailsArray);

            promoimgArray = result.getJSONArray("PromoImages");
           // Log.v("promoimgArray",""+promoimgArray);
            for(int i=0;i<promoimgArray.length();i++){

                bannerimgList.add(promoimgArray.getString(i));
            }
            Log.v("bannerimgList size",""+bannerimgList.size());
            productDataList = new ArrayList<NewArrivalData>();
            for(int i=0;i<productDetailsArray.length();i++){
                JSONObject productobj = productDetailsArray.getJSONObject(i);
                NewArrivalData arrivalData = new NewArrivalData();
                arrivalData.setProductId(productobj.getString("ProductId"));
                arrivalData.setProductname(productobj.getString("ProductName"));
                arrivalData.setProductprice(productobj.getString("Price"));
                arrivalData.setProductsize(productobj.getString("Size"));
                arrivalData.setProductimgurl(productobj.getString("ProductImageurl"));
                productDataList.add(arrivalData);
            }
            Log.v("productDataList",""+productDataList.size());
            colectionImageList = new ArrayList<CollectionImageData>();
            for (int i=0;i<collectionImageArray.length();i++ ){
                JSONObject imagejson = collectionImageArray.getJSONObject(i);
                CollectionImageData data = new CollectionImageData();
                data.setBannerheaderText(imagejson.getString("Title"));
                data.setBannerDetails(imagejson.getString("Text"));
                data.setBannerImageUrl(imagejson.getString("ImgUrl"));
                colectionImageList.add(data);
            }

            Log.v("colectionImageList","qqq"+colectionImageList.size());
            GogglesManager.getInstance().setBannerImgList(bannerimgList);
            GogglesManager.getInstance().setImageDataList(colectionImageList);
            GogglesManager.getInstance().setNewArrivalDataList(productDataList);


            if (status.equals(AppConstants.SUCCESSFUL)) {
                responseMessage = AppConstants.SUCCESSFUL;
            } else {
                responseMessage = "unable to process";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseMessage;


    }*/
    public String parseHomeData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");

            String cacheflag = receiveJSon.getString("navcache");
            Log.v("cacheflag", "" + cacheflag);
            mPrefdata.setCategorycacheFlag(cacheflag);
            bannerimgList = new ArrayList<String>();
            promoimgArray = receiveJSon.getJSONArray("images");
            Log.v("promoimgArray", "" + promoimgArray);
            for (int i = 0; i < promoimgArray.length(); i++) {
                bannerimgList.add(promoimgArray.getString(i));
            }
            Log.v("bannerimgList", "" + bannerimgList.size());

            // products
            productDetailsArray = receiveJSon.getJSONArray("products");
            Log.v("productDetailsArray", "" + productDetailsArray);
            productDataList = new ArrayList<NewArrivalData>();
            for (int i = 0; i < productDetailsArray.length(); i++) {
                JSONObject productobj = productDetailsArray.getJSONObject(i);
                NewArrivalData arrivalData = new NewArrivalData();
                arrivalData.setProductId(productobj.getString("id"));
                arrivalData.setProductname(productobj.getString("name"));
                arrivalData.setProductprice(productobj.getString("price"));
                arrivalData.setFormated_price(productobj.getString("format_you_pay"));
                arrivalData.setProductSku(productobj.getString("sku"));
                //arrivalData.setProductsize(productobj.getString("Size"))item_img_url;
                arrivalData.setProductimgurl(productobj.getString("item_img_thumbnail"));
                productDataList.add(arrivalData);
            }
            Log.v("productDataList", "" + productDataList.size());
            collectionImageArray = receiveJSon.getJSONArray("collection");
            Log.v("CollectionsImageData", "" + collectionImageArray);
            colectionImageList = new ArrayList<CollectionImageData>();
            for (int i = 0; i < collectionImageArray.length(); i++) {
                JSONObject imagejson = collectionImageArray.getJSONObject(i);
                CollectionImageData data = new CollectionImageData();
                data.setBannerheaderText(imagejson.getString("title"));
                data.setBannerDetails(imagejson.getString("content"));
                data.setBannerImageUrl(imagejson.getString("imageurl"));
                colectionImageList.add(data);
            }
            Log.v("colectionImageList", "qqq" + colectionImageList.size());
            GogglesManager.getInstance().setBannerImgList(bannerimgList);
            GogglesManager.getInstance().setNewArrivalDataList(productDataList);
            GogglesManager.getInstance().setImageDataList(colectionImageList);
            responseMessage = AppConstants.SUCCESSFUL;
        } catch (JSONException e) {
            Logger.addRecordToLog("parse Homedata : " + e.getMessage());
            e.printStackTrace();
        }
        return responseMessage;
    }

    // login data {"Error":false,"results":{"UserID":"4","FirstName":"Mahesh","LastName":"Subudhi ",
// "Active":"1","create_date":"2016-04-11T06:32:46-04:00","store_name":"Goggles4u.com","website_id":"1",
// "email":"mahesh.subudhi@gmail.com"}}
    //{"HasError":true,"ErrorMessage":"Invalid login or password."}

    public String parseloginData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");
            Log.v("hasTrue", "" + hasTrue);


            if (hasTrue.equals("true")) {
                status = receiveJSon.getString("Message");
            } else {
                //"results":{"UserID":"15","FirstName":"Mahesh","LastName":"subudhi","Active":"1","create_date":"2016-04-19T10:26:48-04:00",
                //    "store_name":"Goggles4u.com","website_id":"1","email":"pmahesh@gmail.com"}}
                JSONObject result = receiveJSon.getJSONObject("results");
                Log.v("result", "" + result);
                String userid = result.getString("UserID");
                Log.v("userid", "" + userid);
                mPrefdata.setLogincheck(true);
                mPrefdata.setCustomerId(userid);
                mPrefdata.setCustomerMailId(result.getString("email"));
                // String name = result.getString("FirstName")+ "  "+result.getString("LastName");
                mPrefdata.setCustomerFName(result.getString("FirstName"));
                mPrefdata.setCustomerLName(result.getString("LastName"));
                if(result.has("quote")) {
                    mPrefdata.setCartQuoteID(result.getString("quote"));
                }
                status = AppConstants.SUCCESSFUL;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    //{"Error":true,"ErrorMessage":"This customer email already exists"}
    //{"Error":false,"ErrorMessage":"","results":{"UserID":"9","FirstName":"mahesh","LastName":"sss","Active":null,
    // "email":"mahesh1@gmail.com"}}
    public String parseRegistrationData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");

            Log.v("hasTrue", "" + hasTrue);
            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "dd" + hasTrue);
                status = receiveJSon.getString("Message");
                Log.v("status", "dd" + status);
            } else {
                Log.v("hasTrue", "mm" + hasTrue);
                JSONObject result = receiveJSon.getJSONObject("results");
                Log.v("result", "" + result);
                mPrefdata.setCustomerId(result.getString("UserID"));
                mPrefdata.setLogincheck(true);
                mPrefdata.setCustomerMailId(result.getString("email"));
                // String name = result.getString("FirstName")+ ""+result.getString("LastName");
                mPrefdata.setCustomerFName(result.getString("FirstName"));
                mPrefdata.setCustomerLName(result.getString("LastName"));

                status = AppConstants.SUCCESSFUL;
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return status;
    }


    public String parseaddressData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");
            Log.v("hasTrue", "11" + hasTrue);

            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "11" + hasTrue);
                status = receiveJSon.getString("Message");
                Log.v("status", "22" + status);
            } else {
                status = AppConstants.SUCCESSFUL;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return status;
    }


    public String parsePrescriptionData(JSONObject receiveJSon, Context mContext) {
        try {
            hasTrue = receiveJSon.getString("Error");
            Log.v("hasTrue", "11" + hasTrue);

            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "11" + hasTrue);
                status = receiveJSon.getString("Message");
                Log.v("Error msg", "" + status);
            } else {
                ArrayList<PrescriptionData> prescdataList = new ArrayList<>();

                JSONArray prescrArray = new JSONArray();
                prescrArray = receiveJSon.getJSONArray("data");
                Log.v("prescrArray", "11" + prescrArray);

                for (int i = 0; i < prescrArray.length(); i++) {
                    try {
                        JSONObject presjson = prescrArray.getJSONObject(i);
                        PrescriptionData pdata = new PrescriptionData();
                        pdata.setPrescription_id(presjson.getString("prescription_id"));
                        pdata.setPrescriptiion_name(presjson.getString("name"));
                        pdata.setCreatedate(presjson.getString("date"));
                        pdata.setModifiedby(presjson.getString("modifiedby"));
                        pdata.setCreatedby(presjson.getString("createdby"));
                        prescdataList.add(pdata);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.v("prescdataList", "" + prescdataList.size());
                GogglesManager.getInstance().setPrescriptionDataArrayList(prescdataList);
                Log.v("mDataList size", "data" + GogglesManager.getInstance().getPrescriptionDataArrayList().size());

                status = AppConstants.SUCCESSFUL;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }


    public String parseJSONData(JSONObject receiveJSon, Context mContext) {
        try {
            hasTrue = receiveJSon.getString("Error");
            Log.v("hasTrue", "inn parse" + hasTrue);

            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "parseJSONData11" + hasTrue);
                status = receiveJSon.getString("Message");

            } else {

                status = AppConstants.SUCCESSFUL;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;


    }

    private ArrayList<ProductData> mProductList;
    private ArrayList<ProductData> dataList = new ArrayList<>();

    public String parseProductListingData(JSONObject receiveJSon, Context mContext, int pFlag) {


        try {
            hasTrue = receiveJSon.getString("Error");

            if(hasTrue.equals("true")){
                status = receiveJSon.getString("Message");

            }else{

               // String paginationflage = sendJsonobj.getString("mPaginationenableFlag");
            JSONArray resultjsonary = receiveJSon.getJSONArray("data");
            //Log.v("resultjsonary", "11" + resultjsonary);
            //{"id":"47742","name":"G4U 3312","sku":"112663-c","short_description":"G4U 3312 in stylish rectangular shape is made of TR90 material in front black color with four different temple colors for your choice. Great for young generations to look eye-catching in any sporty gathering or even in official wear. Get yours now!","description":null,"price":"6.9500","special_price":null,"product_url":"http:\/\/www.giftbox.pk\/g4u-3312.html",
            // "image_url":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/image\/265x\/9df78eab33525d08d6e5fb8d27136e95\/images\/catalog\/product\/placeholder\/image.jpg","small_imageurl":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/small_image\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/112664a.jpg","thumbnail":{},"is_configurable":"configurable","configurable_options":{"Color":{"273":"Black-Red","Black-Red":"http:\/\/www.giftbox.pk\/media\/catalog\/swatches\/1\/21x21\/media\/black-red.png","261":"Black-Green",
            // "Black-Green":"http:\/\/www.giftbox.pk\/media\/catalog\/swatches\/1\/21x21\/media\/black-green.png"}}}
            mProductList = new ArrayList<ProductData>();
                mProductList.clear();
            for (int i = 0; i < resultjsonary.length(); i++) {
                JSONObject datajson = resultjsonary.getJSONObject(i);
                ProductData productData = new ProductData();
                productData.setProductId(datajson.getString("id"));
                productData.setProductName(datajson.getString("name"));
                productData.setProductSku(datajson.getString("sku"));
                productData.setPrice(datajson.getString("price"));
                productData.setFormated_given_price(datajson.getString("formated_price"));
                productData.setspecial_price(datajson.getString("special_price"));
                productData.setSmallimg_url(datajson.getString("small_imageurl"));

                productData.setProductMarked(Boolean.parseBoolean(datajson.getString("marked")));

                if (datajson.has("configurable_options") && !"null".equalsIgnoreCase(datajson.getString("configurable_options"))) {

                    JSONObject confgurejson = datajson.getJSONObject("configurable_options");
                    if (confgurejson != null) {

                        if (confgurejson.has("Color")) {
                            JSONArray colorjsonary = confgurejson.getJSONArray("Color");

                            //Log.v("colorjson array", "" + colorjsonary);
                            // [{"273":"Black-Red","Black-Red":"http:\/\/www.giftbox.pk\/media\/catalog\/swatches\/1\/21x21\/media\/black-red.png"},
                            // {"261":"Black-Green","Black-Green":"http:\/\/www.giftbox.pk\/media\/catalog\/swatches\/1\/21x21\/media\/black-green.png"}]
                            ArrayList<ProductColorConfigureData> colorList = new ArrayList<>();

                            for (int j = 0; j < colorjsonary.length(); j++) {
                                int colorkey = -1;
                                JSONObject colorjson = colorjsonary.getJSONObject(j);
                                ProductColorConfigureData mColorConfigureData = new ProductColorConfigureData();
                                Iterator<?> iterator = colorjson.keys();
                                while (iterator.hasNext()) {
                                    //Log.v("iterator", "iterator");
                                    try {
                                        colorkey = Integer.parseInt((String) iterator.next());

                                        break;
                                    } catch (NumberFormatException e) {
                                        //continue;
                                    }
                                }
                                if (colorkey != -1 && colorjson.has(colorkey + "")) {
                                    String colorvalue = colorjson.getString(colorkey + "");
                                    //Log.v("colorvalue", "" + colorvalue);
                                    String imgurl = colorjson.getString(colorvalue);
                                    //Log.v("imgurl", "" + imgurl);
                                    mColorConfigureData.setId(colorkey + "");
                                    mColorConfigureData.setName(colorvalue);
                                    mColorConfigureData.setClrimg_url(imgurl);
                                    colorList.add(mColorConfigureData);
                                }


                            }
                            productData.setColrListData(colorList);
                        }


                    }

                }
                mProductList.add(productData);
                Log.v("colorList", "in parse" +  productData.getColrListData().size());
                // mProductList =  GogglesManager.getInstance().getProductDataArrayList();
            }

            dataList = GogglesManager.getInstance().getProductDataArrayList();
                Log.v("dataList","in parse"+dataList.size());
                Log.v("mProductList","in parse"+mProductList.size());

                if(pFlag != 0){
                    Log.v("dataList","if"+mProductList.size());
                    dataList.addAll(mProductList);
                    GogglesManager.getInstance().setProductDataArrayList(dataList);
                }else{
                    Log.v("dataList","else"+mProductList.size());
                    GogglesManager.getInstance().setProductDataArrayList(mProductList);
                    Log.v("size","in parser"+GogglesManager.getInstance().getProductDataArrayList().size());
                }


            status = AppConstants.SUCCESSFUL;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return status;
    }


    public String parseLenseData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");
            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "11" + hasTrue);
                status = receiveJSon.getString("Message");
                Log.v("Error msg", "" + status);
            } else {
                //JSONObject resultjson = receiveJSon.getJSONObject("options");
                Log.v("inpares lens", "11");
                status = AppConstants.SUCCESSFUL;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

    public String parsecategoryData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");
            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "11" + hasTrue);
                status = receiveJSon.getString("Message");
                Log.v("Error msg", "" + status);
            } else {
                JSONArray resultAry = receiveJSon.getJSONArray("parent");
                Log.v("inpares lens", "11" + resultAry);
                mPrefdata.setCategoryData(resultAry.toString());
                HashMap<String, ArrayList<ChildData>> hashMap = new HashMap<>();

                for (int i = 0; i < resultAry.length(); i++) {
                    JSONObject json = resultAry.getJSONObject(i);
                    //HeaderData headerData = new HeaderData();
                    //headerData.setName(json.getString("name"));
                    //headerData.setId(json.getString("id"));
                    // headerList.add(headerData);
                    ArrayList<ChildData> childList = new ArrayList<>();
                    JSONArray childry = json.getJSONArray("child");
                    Log.v("inpares childry", "11" + resultAry);
                    for (int j = 0; j < childry.length(); j++) {
                        JSONObject childjson = childry.getJSONObject(j);
                        ChildData childData = new ChildData();
                        childData.setParent(childjson.getString("parent"));
                        childData.setName(childjson.getString("name"));
                        childData.setId(childjson.getString("id"));
                        childList.add(childData);

                    }
                    hashMap.put(json.getString("name"), childList);
                }
                GogglesManager.getInstance().setCategoryMap(hashMap);
                //Log.v(" headerList","11"+headerList);
                status = AppConstants.SUCCESSFUL;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public String parsefilterJSONData(JSONObject receiveJSon, Context mContext) {
        status = AppConstants.SUCCESSFUL;
        return status;
    }
    public String parseFavriteJSONData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");
            Log.v("hasTrue", "inn parse" + hasTrue);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

    public String parseCartData(JSONObject receiveJSon, Context mContext) {

        try {
            hasTrue = receiveJSon.getString("Error");
            Log.v("hasTrue", "inn parse" + hasTrue);

            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "11" + hasTrue);
                status = receiveJSon.getString("Message");
                Log.v("Error msg", "" + status);
            } else {
//"Error":false,"quote_id":"41","Message":"G4U 3312 has been successfully added to cart."}
                mPrefdata.setCartQuoteID(receiveJSon.getString("quote_id"));

                status = AppConstants.SUCCESSFUL;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

    PreferenceData mPreferenceData = new PreferenceData();

    public String parseCartDetailsData(JSONObject receiveJSon, Context mContext) {
        try {
            hasTrue = receiveJSon.getString("Error");
            Log.v("hasTrue", "inn parse" + hasTrue);

            if (hasTrue.equals("true")) {
                Log.v("hasTrue", "11" + hasTrue);
                status = receiveJSon.getString("Message");
                Log.v("Error msg", "" + status);
            } else {
//"Error":false,"quote_id":"41","Message":"G4U 3312 has been successfully added to cart."}

                String data = receiveJSon.getString("data");
                //if(str != null && !str.isEmpty()) {

                if (data != null && !data.isEmpty() && !"null".equalsIgnoreCase(data)) {
                    JSONObject mainjson = receiveJSon.getJSONObject("data");
                    ArrayList<CartData> cartList = new ArrayList<>();
                    JSONArray itemAry = mainjson.getJSONArray("items");

                    for (int i = 0; i < itemAry.length(); i++) {
                        JSONObject cartjson = itemAry.getJSONObject(i);

                        CartData mCartData = new CartData();
                        mCartData.setItemName(cartjson.getString("name"));
                        mCartData.setItemPrice(cartjson.getString("price"));
                        //Log.v("qty", "" + cartjson.getString("qty"));
                        mCartData.setQty(cartjson.getString("qty"));
                        mCartData.setProductID(cartjson.getString("product_id"));
                        mCartData.setItemId(cartjson.getString("item_id"));
                        mCartData.setBase_row_total(cartjson.getString("base_row_total"));

                        JSONObject optionjson = cartjson.getJSONObject("options");

                        JSONObject detailjson = optionjson.getJSONObject("prescription_type");
                        mCartData.setPrescriType(detailjson.getString("value"));

                        if (optionjson.has("lens")) {
                            JSONObject lens = optionjson.getJSONObject("lens");
                            mCartData.setLens(lens.getString("value"));
                        }


                        if (optionjson.has("anti_reflective_coating")) {
                            JSONObject anti_reflective_coating = optionjson.getJSONObject("anti_reflective_coating");
                            mCartData.setReflectingCoating(anti_reflective_coating.getString("value"));
                        }
                        if (optionjson.has("odsph")) {
                            JSONObject odsph = optionjson.getJSONObject("odsph");
                            mCartData.setOdsphr(odsph.getString("value"));
                        }
                        if (optionjson.has("odcyl")) {
                            JSONObject odcyl = optionjson.getJSONObject("odcyl");
                            mCartData.setOdcyl(odcyl.getString("value"));
                        }
                        if (optionjson.has("odaxis")) {
                            JSONObject odaxis = optionjson.getJSONObject("odaxis");
                            mCartData.setOdaxis(odaxis.getString("value"));
                        }

                        if (optionjson.has("odadd")) {
                            JSONObject odadd = optionjson.getJSONObject("odadd");
                            mCartData.setOdadd(odadd.getString("value"));
                        }
                        if (optionjson.has("ossph")) {
                            JSONObject ossph = optionjson.getJSONObject("ossph");
                            mCartData.setOssphr(ossph.getString("value"));
                        }

                        if (optionjson.has("oscyl")) {
                            JSONObject oscyl = optionjson.getJSONObject("oscyl");
                            mCartData.setOscyl(oscyl.getString("value"));
                        }

                        if (optionjson.has("osaxis")) {
                            JSONObject osaxis = optionjson.getJSONObject("osaxis");
                            mCartData.setOsaxis(osaxis.getString("value"));
                        }

                        if (optionjson.has("osadd")) {
                            JSONObject osadd = optionjson.getJSONObject("osadd");
                            mCartData.setOsadd(osadd.getString("value"));
                        }

                        if (optionjson.has("singlepd")) {
                            JSONObject singlepd = optionjson.getJSONObject("singlepd");
                            mCartData.setSinglepd(singlepd.getString("value"));
                        }

                        if (optionjson.has("leftpd")) {
                            JSONObject leftpd = optionjson.getJSONObject("leftpd");
                            mCartData.setLeftpd(leftpd.getString("value"));
                        }
                        if (optionjson.has("rightpd")) {
                            JSONObject rightpd = optionjson.getJSONObject("rightpd");
                            mCartData.setRightpd(rightpd.getString("value"));
                        }
                        if (cartjson.has("configurable_option")) {

                            JSONObject colorjson = cartjson.getJSONObject("configurable_option");
                            if (colorjson.has("attribute-value")) {
                                JSONObject valjson = colorjson.getJSONObject("attribute-value");


                    /*"attribute-value": {
                        "92": 261
                    },
                    "se*/
                                Iterator<String> it = valjson.keys();
                                while (it.hasNext()) {
                                    String key = (String) it.next();
                                    //Log.v("key", "" + key);
                                    //Log.v("val", "" + valjson.get(key));
                                    mCartData.setColorid(key);
                                    mCartData.setColratrvalue(valjson.getString(key));
                                }
                            }
                        }


                        mCartData.setTotalMrp(mainjson.getString("total_mrp"));
                        mCartData.setGlassimgurl(cartjson.getString("image"));
                        cartList.add(mCartData);


                    }
                    GogglesManager.getInstance().setCartDataArrayList(cartList);

                    //mPrefdata.setCartQuoteID(receiveJSon.getString("quote_id"));
                } else {

                    mPreferenceData.setCartQuoteID("");
                    ArrayList<CartData> blanklist = new ArrayList<>();
                    GogglesManager.getInstance().setCartDataArrayList(blanklist);
                }
                status = AppConstants.SUCCESSFUL;

            }
        } catch (JSONException e) {
            status = AppConstants.FAILURE;
            e.printStackTrace();
        }

        return status;

    }


}













