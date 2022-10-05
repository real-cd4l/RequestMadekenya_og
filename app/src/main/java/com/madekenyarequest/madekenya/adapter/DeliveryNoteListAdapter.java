package com.madekenyarequest.madekenya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madekenyarequest.madekenya.R;
import com.madekenyarequest.madekenya.pojos.Cargo;

import java.util.ArrayList;
import java.util.Locale;

public class DeliveryNoteListAdapter extends RecyclerView.Adapter<DeliveryNoteListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Cargo> cargos;

    public DeliveryNoteListAdapter(Context context, ArrayList<Cargo> cargos) {
        this.context = context;
        this.cargos = cargos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_delivery_note_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cargo cargo = cargos.get(position);
        holder.tvNumber.setText(String.format(Locale.getDefault(), " #%,d", (position + 1)));
        holder.tvTo.setText(cargo.getUnapoenda());
        holder.tvFrom.setText(cargo.getUmetoka_kwa());
        holder.tvDescription.setText(cargo.getJina());
        holder.tvReceipt.setText(cargo.getMzigoUnaRisiti());
        holder.tvQuantity.setText(String.format(Locale.getDefault(), "%,d", cargo.getIdadi()));
        holder.tvCashReceived.setText(String.format(Locale.getDefault(), "%,d Tsh/=", cargo.getCashReceived()));
    }


    @Override
    public int getItemCount() {
        return cargos == null ? 0 : cargos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTo, tvFrom, tvDescription, tvReceipt, tvQuantity, tvCashReceived, tvNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvReceipt = itemView.findViewById(R.id.tvReceipt);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCashReceived = itemView.findViewById(R.id.tvCashReceived);
            tvNumber = itemView.findViewById(R.id.tvNumber);

        }
    }
}
