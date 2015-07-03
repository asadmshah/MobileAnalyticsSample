package com.asadmshah.mobileanalyticssample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by asadmshah on 7/3/15.
 */
public class ShoppingCartActivity extends AppCompatActivity implements PhonesAdapter.DataSource {

    private ShoppingCart mProducts;
    private PhonesAdapter mListAdapter;
    private RecyclerView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        mProducts = ((Application) getApplication()).getShoppingCart();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mListAdapter = new PhonesAdapter();
        mListAdapter.setDataSource(this);
        mListView = (RecyclerView) findViewById(R.id.recyclerview);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mListAdapter);

        findViewById(R.id.button_clear_cart).setOnClickListener(mOnButtonClearCartClickListener);
        findViewById(R.id.button_checkout).setOnClickListener(mOnButtonCheckoutClickListener);
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

    @Override
    public Phone getProduct(int position) {
        return mProducts.get(position);
    }

    @Override
    public int size() {
        return mProducts.size();
    }

    private final View.OnClickListener mOnButtonClearCartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mProducts.size() > 0) {
                mProducts.clear();
                mListAdapter.notifyDataSetChanged();
                ((Application) getApplication()).getTracker().sendClearCartHit();
            }
        }
    };

    private final View.OnClickListener mOnButtonCheckoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mProducts.size() > 0) {
                CheckoutActivity.startActivity(ShoppingCartActivity.this);
            } else {
                Toast.makeText(ShoppingCartActivity.this, "Your Shopping Cart is Empty", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ShoppingCartActivity.class));
    }

}
