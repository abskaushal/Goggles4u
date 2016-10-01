package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderCompleteActivity extends AppCompatActivity {

    private Context mContext;
    private Button vOrder,home;
    TextView label;
    String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        orderId = getIntent().getStringExtra("orderID");
        vOrder = (Button) findViewById(R.id.btn_vorder);
        home = (Button) findViewById(R.id.btn_shopping);
        label = (TextView) findViewById(R.id.tv_label);
        label.setText("Thank you for placing order with Goggles4u. Your Order Id is"+orderId);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(mContext, HomeActivity.class));
            }
        });

        vOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(mContext, ViewOrderActivity.class).putExtra("orderID", orderId));
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
