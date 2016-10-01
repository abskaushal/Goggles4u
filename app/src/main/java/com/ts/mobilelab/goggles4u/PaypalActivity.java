package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.Message;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.i.Result;
import com.ts.mobilelab.goggles4u.logs.Logger;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.utils.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class PaypalActivity extends AppCompatActivity {

    private static final String TAG = "g4upayment";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "Abtd0XUeHxMr3j4Y6tvQYPfgEZzn0z5fhdl6XHZ_pi9_K0czmuncw7O74HQcDRtLR_lFTR5NRxodUxtN";

    private static  int REQUEST_CODE_PAYMENT = 1;
    //private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
   // private static final int REQUEST_CODE_PROFILE_SHARING = 3;
   // private Button paymentBtn;
    private Context mContext;
    private static  PaypalActivity sinstane;
    String orderId;
    String orderidjson;
    Double totalprice;
    private PreferenceData mPreferenceData;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Goggles4u")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");
        mContext = this;
        sinstane = this;
        mPreferenceData = new PreferenceData();
        orderidjson = getIntent().getStringExtra("orderidjson");
        //ManualCardEntry mManualCard = DomainFactory.newManualEntryCardData(cardNumber, expirationDate, cvv2);
        Log.v("orderidjson","paypal"+orderidjson);

       totalprice = Double.valueOf(getIntent().getDoubleExtra("amnt",0d));
        Log.v("amnt","paypal"+totalprice);
        try {
            JSONObject ordejson = new JSONObject(orderidjson);
            orderId = ordejson.getString("OrderId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //paymentBtn = (Button) findViewById(R.id.btn_paypal);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
       // paymentBtn.setOnClickListener(paymentListener);




        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent1 = new Intent(PaypalActivity.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        Logger.addRecordToLog("ThingToBuy in paypal" + thingToBuy);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        finish();
        startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

   private View.OnClickListener paymentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

             /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */

            PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(PaypalActivity.this, PaymentActivity.class);

            // send the same configuration for restart resiliency
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            Logger.addRecordToLog("thingToBuy in paypal" + thingToBuy);
            Log.v("thingToBuy",""+thingToBuy);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
    };

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(totalprice), "USD", "sample item",
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                Log.v("confirm json",""+confirm.toJSONObject());
                //{"response":{"state":"approved","id":"PAY-18X32451H0459092JKO7KFUI","create_time":"2014-07-18T18:46:55Z","intent":"sale"},
                // "client":{"platform":"Android","paypal_sdk_version":"2.14.4","product_name":"PayPal-Android-SDK","environment":"mock"},"response_type":"payment"}
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        JSONObject paymentjson = confirm.toJSONObject().getJSONObject("response");
                        Log.v("paymentjson json",""+paymentjson);
                        String payid = paymentjson.getString("id");
                        JSONObject payjson = new JSONObject();
                        payjson.put("payid",payid); //order_id,status
                        payjson.put("order_id",orderId);
                        payjson.put("status",paymentjson.getString("state"));
                        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext,AppConstants.CODE_FOR_ORDERSUCCESS);
                        gogglesAsynctask.execute(payjson.toString());
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        displayResultText("PaymentConfirmation info received from PayPal");


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
                JSONObject payjson = new JSONObject();

                try {
                    payjson.put("payid",""); //order_id,status
                    payjson.put("order_id",orderId);
                    payjson.put("status","user canceled");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext,AppConstants.CODE_FOR_ORDERSUCCESS);
                gogglesAsynctask.execute(payjson.toString());
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
    protected void displayResultText(String result) {
       // ((TextView)findViewById(R.id.txtResult)).setText("Result : " + result);
        Toast.makeText(
                getApplicationContext(),
                result, Toast.LENGTH_LONG)
                .show();
    }
    @Override
    public void onDestroy() {
// Stop service when done
        stopService(new Intent(this, PayPalService.class));
        Log.i(TAG, "PayPalService end");
        super.onDestroy();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public static void setUIData(String result, JSONObject receiveJSon) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            //sInstance. startActivity(new Intent(sInstance, PaypalActivity.class).putExtra("orderidjson",receiveJSon.toString()));
            //Toast.makeText(sinstane,"Thanks for shopping in Goggles4u.",Toast.LENGTH_LONG).show();
            sinstane.mPreferenceData.setCartQuoteID("");

            sinstane.showReviewDialog();


        }else{
            Toast.makeText(sinstane, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private  void showReviewDialog() {
        startActivity(new Intent(mContext, OrderCompleteActivity.class).putExtra("orderID", orderId));
        /*new CustomDialog().showDltDialog("Goggles4u","Do u want to view the order!",mContext, new Result() {
            @Override
            public void onResult(boolean isSuccess) {
                sinstane.finish();
                startActivity(new Intent(mContext,ViewOrderActivity.class).putExtra("orderID",orderId));
            }

            @Override
            public void onResult(Message message, boolean isSuccess) {
                sinstane.finish();
            }
        });*/
    }



}
