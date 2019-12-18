package org.d3ifcool.nabung;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.d3ifcool.nabung.base.MethodFunction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistHolder> {
    private ClickHandler mClickHandler;
    private ArrayList<Wishlist> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private Context mContext;
    private String hasil;

    MethodFunction methodFunction = new MethodFunction();

    public WishlistAdapter(Context context, ArrayList<Wishlist> data, ArrayList<String> dataId,
                           ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
    }

    @NonNull
    @Override
    public WishlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_keinginan, parent, false);
        WishlistHolder wh = new WishlistHolder(view);
        return wh;
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistHolder holder, int position) {
        final Wishlist wishlist = mData.get(position);

        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String tanggal = sdf1.format(c1.getTime());

        String stglAwal = wishlist.getTanggalAwal();
        String stglAkhir = tanggal;
        DateFormat dateAwal = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateAkhir = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date tglAwal = dateAwal.parse(stglAwal);
            Date tglAkhir = dateAkhir.parse(stglAkhir);

            Date TGLAwal = tglAwal;
            Date TGLAkhir = tglAkhir;
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(TGLAwal);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(TGLAkhir);

            hasil = String.valueOf(daysBetween(cal1, cal2));
        }
        catch (ParseException e) {
        }
        //animation
//        holder.mJangkaWaktu.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
//        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

        holder.mJudul.setText(wishlist.getJudul());
        int jangkaAwal = Integer.parseInt(wishlist.getJangkaWaktu());
        int selisih = Integer.parseInt(hasil);
        int jangkaWaktu = jangkaAwal-selisih;
        if (jangkaWaktu > 99 && jangkaWaktu < 365){
            int hasilBulan = jangkaWaktu/30;
            holder.mFormatHari.setText("bulan");
            holder.mJangkaWaktu.setText(Integer.toString(hasilBulan));
        }
        else if (jangkaWaktu > 365){
            int hasilTahun = jangkaWaktu/365;
            holder.mFormatHari.setText("tahun");
            holder.mJangkaWaktu.setText(Integer.toString(hasilTahun));
        }
        else {
            holder.mJangkaWaktu.setText(Integer.toString(jangkaWaktu));
        }
        holder.mTotalSaldo.setText(methodFunction.currencyIdr(Integer.parseInt(wishlist.getTotalSaldo())));
        holder.mSaldoTerkumpul.setText(methodFunction.currencyIdr(Integer.parseInt(wishlist.getSaldoTerkumpul())));
        int saldoA = Integer.parseInt(wishlist.getTotalSaldo());
        int saldoB = Integer.parseInt(wishlist.getSaldoTerkumpul());
        int rekomendasi = saldoA - saldoB;
        int saldoRekomendasi = rekomendasi/jangkaWaktu;
        holder.mSaldoRekomendasi.setText(methodFunction.currencyIdr(saldoRekomendasi));
        holder.progressBar.setMax(Integer.parseInt(wishlist.getTotalSaldo()));
        holder.progressBar.setProgress(Integer.parseInt(wishlist.getSaldoTerkumpul()));
        if (holder.progressBar.getProgress() >= holder.progressBar.getMax()){
            holder.mButtonNabung.setText("Terkumpul");
        } else {
            holder.mButtonNabung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TambahSaldoNabungActivity.class);
                    String key = wishlist.getDataID();
                    String saldo = wishlist.getSaldoTerkumpul();
                    intent.putExtra("key", key);
                    intent.putExtra("saldo", saldo);
                    mContext.startActivity(intent);
                }
            });
        }
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

    class WishlistHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener{
        private TextView mJudul, mJangkaWaktu, mTotalSaldo, mSaldoTerkumpul, mSaldoRekomendasi, mFormatHari;
        private Button mButtonNabung;
        private ProgressBar progressBar;

        private CardView cardView;

        public WishlistHolder(@NonNull View itemView) {
            super(itemView);


            cardView = (itemView).findViewById(R.id.cardView);
            mJudul = (itemView).findViewById(R.id.tv_judul_wishlist);
            mJangkaWaktu = (itemView).findViewById(R.id.tv_jangka_waktu);
            mTotalSaldo = (itemView).findViewById(R.id.tv_total_saldo);
            mFormatHari = (itemView).findViewById(R.id.tv_format_hari);
            mSaldoTerkumpul = (itemView).findViewById(R.id.tv_saldo_terkumpul);
            mSaldoRekomendasi = (itemView).findViewById(R.id.tv_saldo_rekomendasi);
            mButtonNabung = (itemView).findViewById(R.id.bt_nabung);
            progressBar = (itemView).findViewById(R.id.progressBar);

//            cardView.setBackgroundColor(mContext.getResources().getColor(R.color.selectedColor));

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

    private static long daysBetween(Calendar tanggalAwal, Calendar tanggalAkhir) {
        long lama = 0;
        Calendar tanggal = (Calendar) tanggalAwal.clone();
        while (tanggal.before(tanggalAkhir)) {
            tanggal.add(Calendar.DAY_OF_MONTH, 1);
            lama++;
        }
        return lama;
    }
}
