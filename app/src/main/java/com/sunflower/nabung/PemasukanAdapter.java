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

public class PemasukanAdapter extends RecyclerView.Adapter<PemasukanAdapter.PemasukanHolder> {
    private ClickHandler mClickHandler;
    private ArrayList<Pemasukan> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private Context mContext;

    MethodFunction methodFunction = new MethodFunction();


    public PemasukanAdapter(Context context, ArrayList<Pemasukan> data, ArrayList<String> dataId,
                           ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
    }

    @NonNull
    @Override
    public PemasukanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_pemasukan,parent,false);
        PemasukanHolder Ph = new PemasukanHolder(view);
        return Ph;
    }

    @Override
    public void onBindViewHolder(@NonNull PemasukanHolder holder, int position) {
        Pemasukan pemasukan = mData.get(position);

//        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
        holder.mCatatanPemasukan.setText(pemasukan.getCatatan());
        holder.mSaldoPemasukan.setText(methodFunction.currencyIdr(Integer.parseInt(pemasukan.getSaldoPemasukan())));
        holder.mTanggalPemasukan.setText(pemasukan.getTanggal());
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


    class PemasukanHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener{
        private TextView mCatatanPemasukan,mSaldoPemasukan,mTotalPemasukan,mTanggalPemasukan;
        private CardView cardView;

        public PemasukanHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (itemView).findViewById(R.id.textView23);
            mCatatanPemasukan = (itemView).findViewById(R.id.tv_catatan_pemasukan);
            mSaldoPemasukan = (itemView).findViewById(R.id.tv_saldo_pemasukan);
//            mTotalPemasukan = (itemView).findViewById(R.id.tv_total_pemasukan);
            mTanggalPemasukan = (itemView).findViewById(R.id.tv_tanggal_pengeluaran);

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
