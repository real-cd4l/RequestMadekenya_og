package com.madekenyarequest.madekenya.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Request implements Parcelable {
    public static final int REQUEST_SENT = 1;
    public static final int REQUEST_RECEIVED = 2;
    public static final int REQUEST_PENDING = 0;

    long number;
    String date;
    ArrayList<Item> items;
    int status = REQUEST_PENDING;
    Customer customer = null;

    public Request(long number) {
        this.number = number;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss ", Locale.getDefault());
        this.date = simpleDateFormat.format(new Date());
        this.items = new ArrayList<>();
    }

    protected Request(Parcel in) {
        number = in.readLong();
        date = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
        status = in.readInt();
        customer = in.readParcelable(Customer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(number);
        dest.writeString(date);
        dest.writeTypedList(items);
        dest.writeInt(status);
        dest.writeParcelable(customer, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Request() {
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
