package com.sunflower.nabung;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.d3ifcool.nabung.R;

import com.sunflower.nabung.base.MethodFunction;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PengeluaranAdapter extends RecyclerView.Adapter<PengeluaranAdapter.PengeluaranHolder> {
    private ClickHandler mClickHandler;
    private ArrayList<Pengeluaran> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private Context mContext;

    MethodFunction methodFunction = new MethodFunction();

    public PengeluaranAdapter(Context context, ArrayList<Pengeluaran> data, ArrayList<String> dataId,
                            ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
    }

    @NonNull
    @Override
    public PengeluaranHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_pengeluaran,parent,false);

        PengeluaranHolder pengeluaranHolder = new PengeluaranHolder(view);
        return pengeluaranHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PengeluaranHolder holder, int position) {
        Pengeluaran pengeluaran= mData.get(position);
//
//        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
        holder.tvCatatanPengeluaran.setText(pengeluaran.getNamaPengeluaran());
        holder.tvTotalPengeluaran.setText(methodFunction.currencyIdr(Integer.parseInt(pengeluaran.getTotalPengeluaran())));
        holder.tvTanggalPengeluaran.setText(pengeluaran.getTanggalPengeluaran());
        holder.itemView.setSelected(mSelectedId.contains(mDataId.get(position)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void toggleSelection(String dataId) {
        if (mSelectedId.contains(dataId))
            mSelectedId.remove(dataId);
        else
            mSelectedId.add(dataId);
        notifyDataSetChanged();
    }

    public int selectionCount() {
        return mSelectedId.size();
    }

    public void resetSelection() {
        mSelectedId = new ArrayList<>();
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedId() {
        return mSelectedId;
    }

    class PengeluaranHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener{
        private TextView tvCatatanPengeluaran;
        private TextView tvTanggalPengeluaran;
        private TextView tvTotalPengeluaran;
        private CardView cardView;

        public PengeluaranHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (itemView).findViewById(R.id.cv_pengeluaran);
            tvCatatanPengeluaran= (itemView).findViewById(R.id.tv_catatan_pengeluaran);
            tvTanggalPengeluaran= (itemView).findViewById(R.id.tv_tanggal_pengeluaran);
            tvTotalPengeluaran = (itemView).findViewById(R.id.tv_total_pengeluaran);

            itemView.setFocusable(true);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return mClickHandler.onItemLongClick(getAdapterPosition());
        }
    }
    interface ClickHandler {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }
}
