package com.ts.mobilelab.goggles4u.data;

/**
 * Created by PC2 on 16-03-2016.
 */
public class AppConstants {

    public static final int EXCEPTION_CODE_FOR_UNSUPPORTED_ENCODING = 1804; // UnsupportedEncodingException
    public static final int EXCEPTION_CODE_FOR_NUMBER_FORMAT_EXCEPTION = 1805;
    public static final int EXCEPTION_CODE_FOR_SERVER_DOWN = 1806;
    public static final int EXCEPTION_CODE_FOR_UNAUTHORISED_API_ACCESS = 1807;
    public static final int EXCEPTION_CODE_FOR_INSPECT_USER_NOT_FOUND_LOCATION_BASED_INSPECT_ERROR = 109;
    public static final int EXCEPTION_CODE_FOR_NW_TIMEOUT = 1800; // SocketTimeoutException
    public static final int EXCEPTION_CODE_FOR_CLIENT_PROTOCOL = 1803; // ClientProtocolException
    public static final int EXCEPTION_CODE_FOR_EOF = 18022;// EOFException
    public static final int EXCEPTION_CODE_FOR_IO = 1802;// IOException
    public static final int EXCEPTION_CODE_FOR_JSON = 1801;// JSONException


    private static final String	TEST_URL="http://192.168.0.24/";

    private static final String CONTEXT_URL = "http://store2door.in/";
    public static final String USERREGISER_URL =  "http://www.giftbox.pk/restapi/customers/register";

    public static final String USERLOGIN_URL = "http://www.giftbox.pk/restapi/customers/login";

    public static final String FORGETPSWD_URL = "http://www.giftbox.pk/restapi/customers/forgotpassword";

   // public static final String CHANGEPSWD_URL = "http://www.giftbox.pk/restapi/customers/resetPassword";
    public static final String CHANGEPSWD_URL =" http://www.giftbox.pk/restapi/customers/changePassword";

    public static final String ADDRESSLIST_URL = "http://www.giftbox.pk/restapi/customers/listaddress";

    public static final String ADDADDRESS_URL = "http://www.giftbox.pk/restapi/customers/addaddress";

    public static final String EDITADDRESS_URL = "http://www.giftbox.pk/restapi/customers/editAddress";

    public static final String DELETEADDRESS_URL = "http://www.giftbox.pk/restapi/customers/deleteAddress";

    public static final String PRESCRIPTIONlIST_URL = "http://www.giftbox.pk/restapi/customers/prescriptions ";
    public static final String VIEWPRESCRIPTIONS_URL = "http://www.giftbox.pk/restapi/customers/viewprescription";

    public static final String DELETEPRESCRIPTION_URL = "http://www.giftbox.pk/restapi/customers/deleteprescription";
    public static final String EDITPRESCRIPTION_URL = "http://www.giftbox.pk/restapi/customers/prescriptionbyid";

    public static final String MYORDER_URL = "http://www.giftbox.pk/restapi/customers/myorders";
    public static final String  MYORDERROW_URL = "http://www.giftbox.pk/restapi/customers/vieworder";

    public static final String  STORECREDIT_URL = "http://www.giftbox.pk/restapi/customers/storecredit";


    public static final String  EDITINFO_URL = "http://www.giftbox.pk/restapi/customers/edit";

    public static final String HOME_DATA_URL = "http://www.giftbox.pk/restapi/home/sliders";

    public static final String PRODUCT_LISTING = "http://www.giftbox.pk/restapi/catalog/productsbycat";
    public static final String FILTERDATA_URL = "http://www.giftbox.pk/restapi/catalog/filters";
    public static final String GETSORTOPTION_URL = "http://www.giftbox.pk/restapi/catalog/sortoptions";
    public static final String PRODUCT_DETAILS = "http://www.giftbox.pk/restapi/product/detail";
    public static final String LENSE_DETAILS_URL = "http://www.giftbox.pk/restapi/product/loadoptions";
   public static final String GETPRESCRIPTION_LIST_URL = "http://www.giftbox.pk/restapi/product/getuserprescriptions";

    public static final String SAVEPRESCRIPTION_URL = "http://www.giftbox.pk/restapi/customers/saveprescripton";
    public static final String PRESCRIPTION_OPTION_URL = "http://www.giftbox.pk/restapi/customers/getprescriptionoptions";
    public static final String CATEGORYLOAD_URL = "http://www.giftbox.pk/restapi/home/categorymenu";
    public static final String CATEGORY_SUBMIT_URL = "http://www.giftbox.pk/restapi/catalog/layerednav";
    public static final String LENSE_OPTIONS_URL = "http://www.giftbox.pk/restapi/product/loadlensoptions";

    public static final String ADDTOCART_URL = "http://www.giftbox.pk/restapi/checkout/addcart";
    public static final String CART_DETAILS_URL = "http://www.giftbox.pk/restapi/checkout/cartInfo";
    public static final String CART_UPDATE_URL = "http://www.giftbox.pk/restapi/checkout/updateCart";
    public static final String CART_REMOVE_URL = "http://www.giftbox.pk/restapi/checkout/removeFromCart";
    public static final String CART_COUPON_URL = "http://www.giftbox.pk/restapi/checkout/applycoupon";
    public static final String CART_CLEAR_URL = "http://www.giftbox.pk/restapi/checkout/clearcart";
    public static final String LOADLENSEOPION_URL = "http://www.giftbox.pk/restapi/product/loadDataLensOption";
    public static final String LOAD_COUNTRYLIST_URL = "http://www.giftbox.pk/restapi/checkout/getCountryList";
    public static final String LOAD_REGIONLIST_URL = "http://www.giftbox.pk/restapi/checkout/getRegionList";

    public static final String CHECKOUT_URL = "http://www.giftbox.pk/restapi/checkout/saveBilling";
    public static final String SAVESHIPPING_URL = "http://www.giftbox.pk/restapi/checkout/saveShipping";
    public static final String SAVEORDER_URL = "http://www.giftbox.pk/restapi/checkout/saveorder";

    public static final String ORDERSUCCESS_URL = "http://www.giftbox.pk/restapi/checkout/ordersuccess";
    public static final String CONTACTUS_URL = "http://www.giftbox.pk/restapi/content/index";
    public static final String HELPCENTER_URL = "http://www.giftbox.pk/restapi/content/helpcenter";

    public static final String MARKTOFAV_URL = "http://www.giftbox.pk/restapi/catalog/mark";
    public static final String UNMARKTOFAV_URL = "http://www.giftbox.pk/restapi/catalog/unmark";

    public static final String FAVLIST_URL = "http://www.giftbox.pk/restapi/catalog/listBookmark";
    public static final String STORECREDIT_UPDATE_URL = "http://www.giftbox.pk/restapi/checkout/setStoreCredit";

   public static final String UPLOAD_IMAGE_URL = "http://www.giftbox.pk/restapi/product/uploadprescription";


    public static final String FAILURE = "FAILURE";

    public static String UNABLE_TO_PROCESS= "Unable to process your request.Please try again";
    public static String SUCCESSFUL="Success";
    public static String NETWORK_NOT_AVAILABLE="Please check your internet connection";
    public static String  TIME_OUT = "Time out";

    public static int CODE_FOR_SORTINGCLASS =200;
    public static int CODE_FOR_FILTERCLASS =200;
    public static int CODE_FOR_EDITCHKOUTADRS =203;
    public static int CODE_FOR_SELCTADRS =204;
    public static int CODE_FOR_ADDCHKOUTADRS =205;
    public static int CODE_FOR_ADDFSTCHKOUTADRS =206;
    public static int CODE_FOR_ADDSHIPADRS =207;

    public static int CODE_FOR_REGISTRATION = 100;
    public static int CODE_FOR_LOGIN = 101;
    public static int CODE_FOR_HOME = 102;
    public static int CODE_FOR_FORGTPSWD = 103;
    public static int CODE_FOR_CHANGEPSWD = 104;
    public static int CODE_FOR_ADDADRESS = 105;
    public static int CODE_FOR_PRESCRIPTIONDATA = 106;
   public static int CODE_FOR_VIEWPRESCRIPTION = 107;
    public static int CODE_FOR_DELETEPRESCRIPTION = 108;

    public static int CODE_FOR_ADDRESSLIST = 109;
    public static int CODE_FOR_EDITADRESS = 110;
    public static int CODE_FOR_DELETEADRESS = 111;
    public static int CODE_FOR_MYORDER = 112;
    public static int CODE_FOR_MYORDERROW = 113;
    public static int CODE_FOR_STORECREDIT = 114;

    public static int CODE_FOR_EDITINFO= 115;
    public static int CODE_FOR_PRODUCTLISTING= 116;
    public static int CODE_FOR_FILTERDATA= 117;
    public static int CODE_FOR_PRODUCTLISTING_FILTER= 118;
    public static int CODE_FOR_SORTOPTION= 119;
    public static int CODE_FOR_SORTOPTION_SUBMIT= 120;
    public static int CODE_FOR_PRODUCTDETAILS= 121;
    public static int CODE_FOR_LENSE_DETAILS= 122;
    public static int CODE_FOR_FILTERDATA_SUBMIT= 123;
    public static int CODE_FOR_GETPRESCRIPTION_LIST= 124;
    public static int CODE_FOR_EDITPRESCRIPTION = 125;
    public static int CODE_FOR_SAVEPRESCRIPTION = 126;
    public static int CODE_FOR_PRESCRIPTION_OPTION = 127;
    public static int CODE_FOR_CATEGORY = 128;
    public static int CODE_FOR_SEARCH = 129;
    public static int CODE_FOR_CATEGORYSELECTION = 130;
    public static int CODE_FOR_LENSEOPTIONS = 131;
    public static int CODE_FOR_ADDTOCART= 132;
    public static int CODE_FOR_CARTDETAILS= 133;
    public static int CODE_FOR_CARTUPDATE= 134;
    public static int CODE_FOR_CART_REMOVE= 135;
    public static int CODE_FOR_APPLYCOUPON= 136;
    public static int CODE_FOR_CARTCLEAR= 137;
    public static int CODE_FOR_LOADLENSOPTIONDATA= 138;
    public static int CODE_FOR_LOAD_ADDRESSLIST = 139;
    public static int CODE_FOR_LOAD_COUNTRYLIST = 140;
    public static int CODE_FOR_CHECKOUT = 141;
    public static int CODE_FOR_LOAD_REGIONLIST= 142;
    public static int CODE_FOR_LOAD_ADDRESS= 143;
    public static int CODE_FOR_CARTREVIEW= 144;
    public static int CODE_FOR_SAVEORDER= 145;
    public static int CODE_FOR_TTRYON= 146;
    public static int CODE_FOR_LOAD_SHIPINGADRS= 147;
    public static int CODE_FOR_CHECKOUTSHIPADRS = 148;
    public static int CODE_FOR_ORDERSUCCESS = 149;
   // public static int   CODE_FOR_PAYOPTION = 150;
   public static int CODE_FOR_CONTACTUS= 150;
    public static int CODE_FOR_HELPCENTER= 151;
    public static int CODE_FOR_MARKTOFAVRITE= 152;
    public static int CODE_FOR_UNMARKTOFAVRITE= 153;
    public static int CODE_FOR_FAVRITELIST= 154;
    public static int CODE_FOR_DELETEFAVITEM= 155;
    public static int CODE_FOR_MARKTOFAVRITE_PRODCTDETAILS= 156;
    public static int CODE_FOR_UNMARKTOFAVRITE_PRODCTDETAILS= 157;
    public static int CODE_FOR_HELPROW= 158;
    public static int CODE_FOR_STORECREDIT_UPDATE= 159;

 //JSON keys for filters
 public static final String FILTER_ATTRIBUTES = "attributes";
 public static final String FILTER_LABEL = "frontend_label";
 public static final String FILTER_OPTIONS = "options";
 public static final String FILTER_CODE = "code";
 public static final String FILTER_ATTRIBUTE_ID = "attribute_id";
 public static final String FILTER_INPUT_TYPE = "inputtype";


    public static final String[] IMAGES = new String[]{
            // Heavy images
            "http://www.goggles4u.com/designer-glasses.html",
            "http://www.goggles4u.com/promotions/bogo-buy-one-get-one.html",
            "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
            "http://www.goggles4u.com/prescription-eyeglasses.html",

    };
    public static class Extra {
        public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
        public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }
}
