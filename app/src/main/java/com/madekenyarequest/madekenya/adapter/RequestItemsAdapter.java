package com.madekenyarequest.madekenya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.madekenyarequest.madekenya.R;
import com.madekenyarequest.madekenya.pojos.Item;
import com.madekenyarequest.madekenya.pojos.Request;

import java.util.ArrayList;
import java.util.Locale;

public class RequestItemsAdapter extends RecyclerView.Adapter<RequestItemsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Item> items;
    private final Request request;
    private final MaterialButton submitButton;
    private final TextView tvNoCargoAttached;


    public RequestItemsAdapter(Context context, ArrayList<Item> items, Request request, MaterialButton submitButton, TextView tvNoCargoAttached) {
        this.context = context;
        this.items = items;
        this.request = request;
        this.submitButton = submitButton;
        this.tvNoCargoAttached = tvNoCargoAttached;
    }

    @NonNull
    @Override
    public RequestItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_cargo_request, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RequestItemsAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        if (item != null) {
            holder.tvDetails.setText(String.format(Locale.getDefault(), "%s (%,d)", item.getJina(), item.getIdadi()));
            String risitiPrompt;
            if (item.isUnarisiti()) {
                risitiPrompt = "Una risiti ya TRA";
            } else {
                risitiPrompt = "Hauna risiti ya TRA";
            }
            holder.tvReceiptDetails.setText(risitiPrompt);
        }
        if (request.getStatus() == Request.REQUEST_PENDING || request.getStatus() == Request.REQUEST_SENT) {
            holder.imbRemoveCargo.setVisibility(View.VISIBLE);
            final int removePos = position;
            holder.imbRemoveCargo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (items.size() > removePos) {
                        items.remove(removePos);
                        notifyItemRemoved(removePos);
                        if (request.getStatus() == Request.REQUEST_SENT) {
                            submitButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            holder.imbRemoveCargo.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
        }

        tvNoCargoAttached.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        int lloop = items == null ? 0 : items.size();
        if(lloop == 0){
            tvNoCargoAttached.setVisibility(View.VISIBLE);
        }
        return lloop;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDetails, tvReceiptDetails;
        private final ImageButton imbRemoveCargo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvReceiptDetails = itemView.findViewById(R.id.tvReceiptDetails);
            imbRemoveCargo = itemView.findViewById(R.id.imbRemoveCargo);
        }
    }


}
