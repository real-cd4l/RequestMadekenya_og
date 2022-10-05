package com.madekenyarequest.madekenya.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.madekenyarequest.madekenya.R;
import com.madekenyarequest.madekenya.ViewRequestActivity;
import com.madekenyarequest.madekenya.pojos.Request;

import java.util.ArrayList;
import java.util.Locale;

public class RequestSummaryAdapter extends RecyclerView.Adapter<RequestSummaryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Request> requests;

    public RequestSummaryAdapter(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public RequestSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_request_summary, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RequestSummaryAdapter.ViewHolder holder, int position) {
        Request request = requests.get(position);
        if (request != null) {
            holder.tvRequestDateTime.setText(request.getDate());
            holder.tvRequestDetails.setText(String.format(Locale.getDefault(), "Req no %d", request.getNumber()));

            holder.mbViewRequest.setOnClickListener(v -> {
                Intent intent = new Intent(context, ViewRequestActivity.class);
                intent.putExtra("request", request);
                context.startActivity(intent);
            });
            if (request.getStatus() == Request.REQUEST_SENT) {
                holder.imbStatus.setImageResource(R.drawable.ic_baseline_done_24);
                 } else if (request.getStatus() == Request.REQUEST_RECEIVED) {
                holder.imbStatus.setImageResource(R.drawable.ic_baseline_done_all_24);
            }
        }

    }


    @Override
    public int getItemCount() {
        return requests.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRequestDetails, tvRequestDateTime;
        private final MaterialButton mbViewRequest;
        private final ImageButton imbStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestDetails = itemView.findViewById(R.id.tvRequestDetails);
            tvRequestDateTime = itemView.findViewById(R.id.tvRequestDateTime);
            mbViewRequest = itemView.findViewById(R.id.mbViewRequest);
            imbStatus = itemView.findViewById(R.id.imbStatus);

        }
    }
}
