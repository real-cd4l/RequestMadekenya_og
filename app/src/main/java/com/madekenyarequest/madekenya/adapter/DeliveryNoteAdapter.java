package com.madekenyarequest.madekenya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madekenyarequest.madekenya.R;
import com.madekenyarequest.madekenya.pojos.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class DeliveryNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_HEAD = 0;
    public static final int VIEW_LIST = 1;
    public static final int VIEW_FOOTER = 2;
    private final Context context;
    private final Customer customer;
    private final ArrayList<Integer> views = new ArrayList<>(Arrays.asList(VIEW_HEAD, VIEW_LIST, VIEW_FOOTER));
    private final DeliveryNoteListAdapter deliveryNoteListAdapter;
    private RecyclerView recyclerViewCargoList;

    public DeliveryNoteAdapter(Context context, Customer customer) {
        this.context = context;
        this.customer = customer;
        this.deliveryNoteListAdapter = new DeliveryNoteListAdapter(context, customer.getRegisteredParcels());
    }

    public Customer getCustomer() {
        return customer;
    }

    public ArrayList<Integer> getViews() {
        return views;
    }

    public DeliveryNoteListAdapter getDeliveryNoteListAdapter() {
        return deliveryNoteListAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_HEAD) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_delivery_note_head, parent, false);
            holder = new DeliveryHeadViewHolder(view);
        } else if (viewType == VIEW_LIST) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_delivery_note_list, parent, false);
            holder = new DeliveryListViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_delivery_note_footer, parent, false);
            holder = new DeliveryFooterViewHolder(view);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_HEAD && holder instanceof DeliveryHeadViewHolder) {
            showHeadDetails((DeliveryHeadViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_FOOTER && holder instanceof DeliveryFooterViewHolder) {
            showFooterDetails((DeliveryFooterViewHolder) holder);
        } else {
            showList((DeliveryListViewHolder) holder);
        }

    }

    public RecyclerView getRecyclerViewCargoList() {
        return recyclerViewCargoList;
    }

    private void showList(DeliveryListViewHolder holder) {
        recyclerViewCargoList = holder.recyclerView;
        holder.recyclerView.setAdapter(deliveryNoteListAdapter);
    }

    private void showFooterDetails(DeliveryFooterViewHolder holder) {
        holder.tvDeliveryNumber.setText(String.format(Locale.getDefault(), "%s v%d", customer.getUserDeliveredID(), customer.getVersion()));
    }

    private void showHeadDetails(DeliveryHeadViewHolder holder) {
        holder.tvDate.setText(String.format(Locale.getDefault(), "%s @ %d:%d", customer.getRegisteredDate(), customer.getHours(), customer.getMinutes()));
        holder.tvJina.setText(customer.getJina_la_mteja());
        holder.tvRef.setText(customer.getUserDeliveredID());
    }


    @Override
    public int getItemCount() {
        return views.size();
    }


    @Override
    public int getItemViewType(int position) {
        return views.get(position);
    }

    private static class DeliveryFooterViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDeliveryNumber;

        public DeliveryFooterViewHolder(View view) {
            super(view);
            tvDeliveryNumber = view.findViewById(R.id.tvDeliveryNumber);
        }
    }

    public static class DeliveryListViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;

        public DeliveryListViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class DeliveryHeadViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvJina, tvRef, tvDate;

        public DeliveryHeadViewHolder(View view) {
            super(view);
            tvJina = view.findViewById(R.id.tvJina);
            tvRef = view.findViewById(R.id.tvRef);
            tvDate = view.findViewById(R.id.tvDate);
        }
    }
}
