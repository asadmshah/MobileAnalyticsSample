package com.asadmshah.mobileanalyticssample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by asadmshah on 7/3/15.
 */
public class ShoppingCart implements Iterable<Phone> {

    private final ArrayList<Phone> mCart;
    private String mTransactionId;

    public ShoppingCart() {
        mCart = new ArrayList<>();
        mTransactionId = UUID.randomUUID().toString();
    }

    public String getTransactionId() {
        return mTransactionId;
    }

    public void clear() {
        mCart.clear();
        mTransactionId = UUID.randomUUID().toString();
    }

    public Phone get(int position) {
        return mCart.get(position);
    }

    public boolean add(Phone phone) {
        if (!mCart.contains(phone)) {
            mCart.add(phone);
            return true;
        }
        return false;
    }

    public boolean remove(Phone phone) {
        if (mCart.contains(phone)) {
            mCart.remove(phone);
            return true;
        }
        return false;
    }

    public boolean contains(Phone phone) {
        return mCart.contains(phone);
    }

    public int size() {
        return mCart.size();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ShoppingCart && mTransactionId.equals(((ShoppingCart) o).mTransactionId);
    }

    @Override
    public int hashCode() {
        return mTransactionId.hashCode();
    }

    @Override
    public Iterator<Phone> iterator() {
        return mCart.iterator();
    }
}
