package com.asadmshah.mobileanalyticssample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by asadmshah on 7/2/15.
 */
public class PhonesAdapter extends RecyclerView.Adapter<PhonesAdapter.ViewHolder> {

    private static final String PRICE_FORMAT = "$%.2f";
    private DataSource mDataSource;
    private OnItemClickListener mOnItemClickListener;

    public void setDataSource(DataSource dataSource) {
        mDataSource = dataSource;
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.product_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Phone phone = mDataSource.getProduct(i);
        viewHolder.viewName.setText(phone.name);
        viewHolder.viewPrice.setText(String.format(PRICE_FORMAT, phone.price));
    }

    @Override
    public int getItemCount() {
        return mDataSource != null ? mDataSource.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView viewName;
        public final TextView viewPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            viewName = (TextView) itemView.findViewById(R.id.name);
            viewPrice = (TextView) itemView.findViewById(R.id.price);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClicked(this);
            }
        }

    }

    public interface DataSource {
        Phone getProduct(int position);
        int size();
    }

    public interface OnItemClickListener {
        void onItemClicked(ViewHolder holder);
    }

}
