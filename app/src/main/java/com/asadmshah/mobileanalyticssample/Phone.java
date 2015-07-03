package com.asadmshah.mobileanalyticssample;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by asadmshah on 7/2/15.
 */
public class Phone implements Parcelable, Comparable<Phone> {

    public final String name;
    public final float price;
    public final String id;

    public Phone(String name, float price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    private Phone(Parcel parcel) {
        this.name = parcel.readString();
        this.price = parcel.readFloat();
        this.id = parcel.readString();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Phone && id.equals(((Phone) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(@NonNull Phone another) {
        return id.compareTo(another.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeString(id);
    }

    public static final Parcelable.Creator<Phone> CREATOR = new Parcelable.Creator<Phone>() {
        @Override
        public Phone createFromParcel(Parcel source) {
            return new Phone(source);
        }

        @Override
        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };
}
