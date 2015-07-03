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

import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

/**
 * Created by asadmshah on 7/3/15.
 */
public class DailySpecialActivity extends AppCompatActivity {

    private Phone mPhone;
    private Button mButtonAddOrRemoveFromCart;
    private boolean mItemAddedToCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_daily_special);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TagManager tagManager = ((Application) getApplication()).getTagManager();
        tagManager.getDataLayer().pushEvent("openScreen", DataLayer.mapOf("screen-name", "Show Daily Special"));

        ContainerHolder containerHolder = ((Application) getApplication()).getContainerHolder();
        String phoneId = containerHolder.getContainer().getString("daily-special");
        Phone[] phones = ((Application) getApplication()).getProductsList();
        for (int i = 0; i < phones.length; i++) {
            if (phones[i].id.equals(phoneId)) {
                mPhone = phones[i];
                break;
            }
        }
        ((TextView) findViewById(R.id.name)).setText(mPhone.name);
        ((TextView) findViewById(R.id.price)).setText(getString(R.string.format_total_price, mPhone.price));

        mButtonAddOrRemoveFromCart = (Button) findViewById(R.id.button_add_or_remove_from_cart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_shopping_cart, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((Application) getApplication()).getShoppingCart().contains(mPhone)) {
            setButtonActionToRemove();
            mItemAddedToCart = true;
        } else {
            setButtonActionToAdd();
            mItemAddedToCart = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mItemAddedToCart) {
            TagManager tagManager = ((Application) getApplication()).getTagManager();
            DataLayer dataLayer = tagManager.getDataLayer();
            dataLayer.pushEvent("exitScreen", DataLayer.mapOf(
                    "screen-name", "Show Daily Special",
                    "dailySpecial", mPhone.id
            ));
        }
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
                mItemAddedToCart = true;
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
                mItemAddedToCart = false;
            }
        });
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DailySpecialActivity.class));
    }

}
