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
public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_PRODUCT = "product";

    private Phone mPhone;
    private Button mButtonAddOrRemoveFromCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPhone = getIntent().getParcelableExtra(EXTRA_PRODUCT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ((TextView) findViewById(R.id.name)).setText(mPhone.name);
        ((TextView) findViewById(R.id.price)).setText(String.format("$%.2f", mPhone.price));
        mButtonAddOrRemoveFromCart = (Button) findViewById(R.id.button_add_or_remove_from_cart);

        ((Application) getApplication()).getTracker().sendProductDetailViewedHit(mPhone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((Application) getApplication()).getShoppingCart().contains(mPhone)) {
            setButtonActionToRemove();
        } else {
            setButtonActionToAdd();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shopping_cart:
                ShoppingCartActivity.startActivity(this);
                return true;
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setButtonActionToAdd() {
        mButtonAddOrRemoveFromCart.setText(getString(R.string.add_to_cart));
        mButtonAddOrRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActionToRemove();
                Application application = (Application) getApplication();
                application.getShoppingCart().add(mPhone);
                application.getTracker().sendProductAddedToCartHit(mPhone);
            }
        });
    }

    private void setButtonActionToRemove() {
        mButtonAddOrRemoveFromCart.setText(getString(R.string.remove_from_cart));
        mButtonAddOrRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActionToAdd();
                Application application = (Application) getApplication();
                application.getShoppingCart().remove(mPhone);
                application.getTracker().sendProductRemovedFromCartHit(mPhone);
            }
        });
    }

    public static void startActivity(Context context, Phone phone) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT, phone);
        context.startActivity(intent);
    }

}
