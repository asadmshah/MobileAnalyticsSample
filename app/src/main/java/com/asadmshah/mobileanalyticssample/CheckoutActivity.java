package com.asadmshah.mobileanalyticssample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by asadmshah on 7/3/15.
 */
public class CheckoutActivity extends AppCompatActivity {

    private ShoppingCart mShoppingCart;

    private Button mButtonSubmitPaymentInformation;
    private Button mButtonOrderReviewed;
    private Button mButtonPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mShoppingCart = ((Application) getApplication()).getShoppingCart();
        float totalPrice = 0f;
        for (Phone phone : mShoppingCart) {
            totalPrice += phone.price;
        }
        ((TextView) findViewById(R.id.total_price)).setText(getString(R.string.format_total_price, totalPrice));

        mButtonSubmitPaymentInformation = (Button) findViewById(R.id.button_submit_payment_information);
        mButtonOrderReviewed = (Button) findViewById(R.id.button_order_reviewed);
        mButtonPurchase = (Button) findViewById(R.id.button_purchase);

        mButtonSubmitPaymentInformation.setOnClickListener(mButtonSubmitPaymentInformationListener);

        ((Application) getApplication()).getTracker().sendCheckoutStartedHit(mShoppingCart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final View.OnClickListener mButtonSubmitPaymentInformationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setOnClickListener(null);
            v.setVisibility(View.INVISIBLE);

            ((Application) getApplication()).getTracker().sendPaymentInformationSubmittedHit(mShoppingCart);

            mButtonOrderReviewed.setOnClickListener(mButtonOrderReviewedListener);
            mButtonOrderReviewed.setVisibility(View.VISIBLE);
        }
    };

    private final View.OnClickListener mButtonOrderReviewedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setOnClickListener(null);
            v.setVisibility(View.INVISIBLE);

            ((Application) getApplication()).getTracker().sendOrderReviewedHit(mShoppingCart);

            mButtonPurchase.setOnClickListener(mButtonPurchaseListener);
            mButtonPurchase.setVisibility(View.VISIBLE);
        }
    };

    private final View.OnClickListener mButtonPurchaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setOnClickListener(null);
            v.setVisibility(View.INVISIBLE);

            ((Application) getApplication()).getTracker().sendPurchaseCompletedHit(mShoppingCart);

            mShoppingCart.clear();
            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, CheckoutActivity.class));
    }

}
