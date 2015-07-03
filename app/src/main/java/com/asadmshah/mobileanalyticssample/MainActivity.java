package com.asadmshah.mobileanalyticssample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements PhonesAdapter.DataSource,
        PhonesAdapter.OnItemClickListener {

    private Phone[] mProductsList;
    private PhonesAdapter mListAdapter;
    private RecyclerView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Phone Store");
        setSupportActionBar(toolbar);

        mProductsList = ((Application) getApplication()).getProductsList();

        mListAdapter = new PhonesAdapter();
        mListAdapter.setDataSource(this);
        mListAdapter.setItemClickListener(this);
        mListView = (RecyclerView) findViewById(R.id.recyclerview);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mListAdapter);

        (findViewById(R.id.button_show_daily_deals)).setOnClickListener(mOnButtonDailySpecialClickListener);

        loadGTMContainer();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListAdapter.setDataSource(null);
        mListAdapter.setItemClickListener(null);
    }

    @Override
    public Phone getProduct(int position) {
        return mProductsList[position];
    }

    @Override
    public int size() {
        return mProductsList.length;
    }

    @Override
    public void onItemClicked(PhonesAdapter.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        DetailActivity.startActivity(this, mProductsList[position]);
    }

    private void loadGTMContainer() {
        TagManager manager = ((Application) getApplication()).getTagManager();
        manager.setVerboseLoggingEnabled(true);
        String containerId = getString(R.string.TAG_MANAGER_DAILY_DEALS_CONTAINER_ID);
        PendingResult<ContainerHolder> pending = manager.loadContainerPreferFresh(containerId, R.raw.gtm_daily_deals);
        pending.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder containerHolder) {
                if (!containerHolder.getStatus().isSuccess()) {
                    return;
                }
                containerHolder.refresh();
                ((Application) getApplication()).setContainerHolder(containerHolder);
            }
        }, 2, TimeUnit.SECONDS);
    }

    private final View.OnClickListener mOnButtonDailySpecialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            TagManager tagManager = ((Application) getApplication()).getTagManager();
            tagManager.getDataLayer().push("hour_of_day", hour);
            DailySpecialActivity.startActivity(MainActivity.this);
        }
    };

}
