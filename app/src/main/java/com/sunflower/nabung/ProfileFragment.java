package com.sunflower.nabung;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.nabung.R;

import com.sunflower.nabung.base.MethodFunction;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private DatabaseReference databaseReference;
    private String mUserID;
    private TextView mNamaUser;
    private TextView mSaldoUser;
    private TextView mPemasukanUser,mPengeluaranUser;
    private TextView mEmailUser;
    private Button mLogout;
    private AnyChartView anyChartView;
    private Session session;

    MethodFunction methodFunction = new MethodFunction();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_profil = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        session = new Session(getContext());
        mNamaUser = view_profil.findViewById(R.id.tv_profil_nama);
        mPemasukanUser = view_profil.findViewById(R.id.tv_pemasukan_total);
        mPengeluaranUser = view_profil.findViewById(R.id.tv_pengeluaran_total);
        mEmailUser = view_profil.findViewById(R.id.tv_email_user);
        mSaldoUser = view_profil.findViewById(R.id.tv_profile_saldo);
        anyChartView = view_profil.findViewById(R.id.any_chart_view);
        mLogout = view_profil.findViewById(R.id.bt_logout);
//        mLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                session.setLoggedin(false);
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getContext(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        setupPieChart();

        databaseReference = FirebaseDatabase.getInstance().getReference(mUserID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //contoh pengambilan data
                String nama = dataSnapshot.child("namaUser").getValue().toString();
                String email = dataSnapshot.child("emailUser").getValue().toString();
                int saldoWishlist = Integer.parseInt(dataSnapshot.child("saldoWishlist").getValue().toString());
                int saldoPemasukan = Integer.parseInt(dataSnapshot.child("saldoPemasukan").getValue().toString());
                int saldoPengeluaran = Integer.parseInt(dataSnapshot.child("saldoPengeluaran").getValue().toString());
                int Total = saldoWishlist + saldoPemasukan - saldoPengeluaran;


                mNamaUser.setText(nama);
                mEmailUser.setText(email);
                mSaldoUser.setText(methodFunction.currencyIdr(Total));
                mPemasukanUser.setText(methodFunction.currencyIdr(saldoPemasukan));
                mPengeluaranUser.setText(methodFunction.currencyIdr(saldoPengeluaran));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view_profil;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_app :
                startActivity(new Intent(getContext(),AboutAppActivity.class));
                break;
            case R.id.bt_logout:
                session.setLoggedin(false);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setupPieChart(){
        final Pie pie = AnyChart.pie();
        final List<DataEntry> data = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(mUserID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int saldoPemasukan = Integer.parseInt(dataSnapshot.child("saldoPemasukan").getValue().toString());
                int saldoPengeluaran = Integer.parseInt(dataSnapshot.child("saldoPengeluaran").getValue().toString());

                data.add(new ValueDataEntry("Pemasukkan", saldoPemasukan));
                data.add(new ValueDataEntry("Pengeluaran", saldoPengeluaran));
                pie.data(data);
                anyChartView.setChart(pie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
