package com.madekenyarequest.madekenya.pojos;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Customer implements Parcelable, Serializable {
    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
    private ArrayList<Cargo> registeredParcels = new ArrayList<>();
    private String jina_la_mteja;
    private String jina_la_mpokeaji;
    private int version = 1;
    private ArrayList<Cargo> onStoreParcels = new ArrayList<>();
    private ArrayList<Cargo> onCarParcels = new ArrayList<>();
    private ArrayList<Cargo> onDeliveredParcels = new ArrayList<>();
    private ArrayList<Cargo> invoices = new ArrayList<>();
    private ArrayList<CustomerHistory> customerHistories = new ArrayList<>();
    private String registeredBy;
    private String officeIndexNo = "";
    private String registeredDate;
    private String databaseRefId;
    private String userDeliveredID;
    private String registeredBy_date;
    private int date;
    private int month;
    private int year;
    private int hours;
    private int minutes;


    public Customer() {
    }

    public Customer(String jina_la_mteja, String jina_la_mpokeaji, ArrayList<Cargo> onStoreParcels, String registeredBy, String officeIndexNo, String userDeliveredID) {
        this.jina_la_mteja = jina_la_mteja.toUpperCase();
        this.jina_la_mpokeaji = jina_la_mpokeaji.toUpperCase();
        this.onStoreParcels = onStoreParcels;
        this.registeredBy = registeredBy;
        this.officeIndexNo = officeIndexNo;
        this.userDeliveredID = userDeliveredID;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        this.registeredDate = simpleDateFormat.format(new Date());
        this.registeredBy_date = registeredBy+"-"+this.registeredDate;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.date = calendar.get(Calendar.DATE);
        this.month = (calendar.get(Calendar.MONTH) + 1);
        this.year = calendar.get(Calendar.YEAR);
    }

    protected Customer(Parcel in) {
        registeredParcels = in.createTypedArrayList(Cargo.CREATOR);
        jina_la_mteja = in.readString();
        jina_la_mpokeaji = in.readString();
        version = in.readInt();
        onStoreParcels = in.createTypedArrayList(Cargo.CREATOR);
        onCarParcels = in.createTypedArrayList(Cargo.CREATOR);
        onDeliveredParcels = in.createTypedArrayList(Cargo.CREATOR);
        invoices = in.createTypedArrayList(Cargo.CREATOR);
        customerHistories = in.createTypedArrayList(CustomerHistory.CREATOR);
        registeredBy = in.readString();
        officeIndexNo = in.readString();
        registeredDate = in.readString();
        databaseRefId = in.readString();
        userDeliveredID = in.readString();
        registeredBy_date = in.readString();
        date = in.readInt();
        month = in.readInt();
        year = in.readInt();
        hours = in.readInt();
        minutes = in.readInt();
    }

    public ArrayList<Cargo> getRegisteredParcels() {
        return registeredParcels;
    }

    public void setRegisteredParcels(ArrayList<Cargo> registeredParcels) {
        this.registeredParcels = registeredParcels;
    }

    public ArrayList<Cargo> getInvoices() {
        ArrayList<Cargo> foundedInvoices = new ArrayList<>();
        if (onStoreParcels.size() > 0) {
            for (Cargo cargo : onStoreParcels) {
                if (cargo.getTotalPrice() > 0) {
                    foundedInvoices.add(cargo);
                }
            }
        }
        if (onCarParcels.size() > 0) {
            for (Cargo cargo : onCarParcels) {
                if (cargo.getTotalPrice() > 0) {
                    foundedInvoices.add(cargo);
                }
            }
        }
        if (onDeliveredParcels.size() > 0) {
            for (Cargo cargo : onDeliveredParcels) {
                if (cargo.getTotalPrice() > 0) {
                    foundedInvoices.add(cargo);
                }
            }
        }
        return foundedInvoices;
    }

    public void setInvoices(ArrayList<Cargo> invoices) {
        this.invoices = invoices;
    }

    public String getJina_la_mteja() {
        return jina_la_mteja;
    }

    public void setJina_la_mteja(String jina_la_mteja) {
        this.jina_la_mteja = jina_la_mteja;
    }

    public String getJina_la_mpokeaji() {
        return jina_la_mpokeaji;
    }

    public void setJina_la_mpokeaji(String jina_la_mpokeaji) {
        this.jina_la_mpokeaji = jina_la_mpokeaji;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ArrayList<Cargo> getOnStoreParcels() {
        return onStoreParcels;
    }

    public void setOnStoreParcels(ArrayList<Cargo> onStoreCargos) {
        this.onStoreParcels = onStoreCargos;
    }

    public Customer deepCopy() throws Exception {
        //Serialization of object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        //De-serialization of object
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (Customer) in.readObject();
    }

    public ArrayList<Cargo> getOnCarParcels() {
        return onCarParcels;
    }

    public void setOnCarParcels(ArrayList<Cargo> onCarCargos) {
        this.onCarParcels = onCarCargos;
    }

    public ArrayList<Cargo> getOnDeliveredParcels() {
        return onDeliveredParcels;
    }

    public void setOnDeliveredParcels(ArrayList<Cargo> onDeliveredCargos) {
        this.onDeliveredParcels = onDeliveredCargos;
    }

    public ArrayList<CustomerHistory> getCustomerHistories() {
        return customerHistories;
    }

    public void setCustomerHistories(ArrayList<CustomerHistory> customerHistories) {
        this.customerHistories = customerHistories;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getOfficeIndexNo() {
        return officeIndexNo;
    }

    public void setOfficeIndexNo(String officeIndexNo) {
        this.officeIndexNo = officeIndexNo;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getDatabaseRefId() {
        return databaseRefId;
    }

    public void setDatabaseRefId(String databaseRefId) {
        this.databaseRefId = databaseRefId;
    }

    public String getUserDeliveredID() {
        return userDeliveredID;
    }

    public void setUserDeliveredID(String userDeliveredID) {
        this.userDeliveredID = userDeliveredID;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(registeredParcels);
        dest.writeString(jina_la_mteja);
        dest.writeString(jina_la_mpokeaji);
        dest.writeInt(version);
        dest.writeTypedList(onStoreParcels);
        dest.writeTypedList(onCarParcels);
        dest.writeTypedList(onDeliveredParcels);
        dest.writeTypedList(invoices);
        dest.writeTypedList(customerHistories);
        dest.writeString(registeredBy);
        dest.writeString(officeIndexNo);
        dest.writeString(registeredDate);
        dest.writeString(databaseRefId);
        dest.writeString(userDeliveredID);
        dest.writeString(registeredBy_date);
        dest.writeInt(date);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(hours);
        dest.writeInt(minutes);
    }

    public String getRegisteredBy_date() {
        return registeredBy_date;
    }

    public void setRegisteredBy_date(String registeredBy_date) {
        this.registeredBy_date = registeredBy_date;
    }
}
