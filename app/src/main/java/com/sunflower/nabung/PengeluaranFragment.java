package com.sunflower.nabung;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.nabung.R;

import com.sunflower.nabung.base.MethodFunction;

import java.util.ArrayList;

public class PengeluaranFragment extends Fragment {
    private PengeluaranAdapter mAdapter;
    private ArrayList<Pengeluaran> mData;
    private ArrayList<String> mDataId;
    private ActionMode mActionMode;

    private RecyclerView rvPengeluaran;
    private FloatingActionButton mTambahPengeluaran;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private TextView mSaldoAkhir;
    private String mUserID;

    private  ArrayList<String> tglPengeluaran;


    MethodFunction methodFunction = new MethodFunction();
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mData.add(dataSnapshot.getValue(Pengeluaran.class));
            mDataId.add(dataSnapshot.getKey());
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mData.set(pos, dataSnapshot.getValue(Pengeluaran.class));
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mDataId.remove(pos);
            mData.remove(pos);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_pengeluaran = inflater.inflate(R.layout.fragment_pengeluaran, container, false);
        setHasOptionsMenu(true);

//        mSaldoAkhir = view_pengeluaran.findViewById(R.id.tv_saldo_pengeluaran_akhir);
        mTambahPengeluaran = view_pengeluaran.findViewById(R.id.bt_tambah_pengeluaran);
        mTambahPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TambahPengeluaranActivity.class);
                startActivity(intent);
            }
        });


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        mSaldoAkhir = view_pengeluaran.findViewById(R.id.tv_pengeluaran_total);
        rvPengeluaran =view_pengeluaran.findViewById(R.id.rv_pengeluaran);
        rvPengeluaran.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        mDataId = new ArrayList<>();

        mAdapter = new PengeluaranAdapter(getContext(), mData, mDataId,
                new PengeluaranAdapter.ClickHandler() {
                    @Override
                    public void onItemClick(int position) {
                        if (mActionMode != null) {
                            mAdapter.toggleSelection(mDataId.get(position));
                            if (mAdapter.selectionCount() == 0)
                                mActionMode.finish();
                            else
                                mActionMode.invalidate();
                            return;
                        }
                        Toast.makeText(getContext(), "Tekan lebih lama", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean onItemLongClick(int position) {
                        if (mActionMode != null) return false;

                        mAdapter.toggleSelection(mDataId.get(position));
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                        return true;
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvPengeluaran.setLayoutManager(linearLayoutManager);
        rvPengeluaran.setAdapter(mAdapter);

        reference = FirebaseDatabase.getInstance().getReference(mUserID).child("pengeluaran");
        reference2 = FirebaseDatabase.getInstance().getReference(mUserID).child("saldoPengeluaran");
        reference.addChildEventListener(childEventListener);

        //awal recycler
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int tSaldo = 0;
                for (DataSnapshot postSnapshot :  dataSnapshot.getChildren()){
                    Pengeluaran pengeluaran = postSnapshot.getValue(Pengeluaran.class);
                    int awal = Integer.parseInt(pengeluaran.getTotalPengeluaran());
                    tSaldo = tSaldo + awal;
                    rvPengeluaran.setAdapter(mAdapter);
                    //mData.add(pengeluaran); untuk menambah ke arrayList
                }
                reference2.setValue(Integer.toString(tSaldo));
                mSaldoAkhir.setText((methodFunction.currencyIdr(tSaldo)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        //akhir recycler
        return view_pengeluaran;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(String.valueOf(mAdapter.selectionCount()));
            menu.findItem(R.id.action_edit).setVisible(mAdapter.selectionCount() == 1);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    editPet();
                    return true;

                case R.id.action_delete:
                    deletePet();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            mAdapter.resetSelection();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    private void deletePet() {
        final ArrayList<String> selectedIds = mAdapter.getSelectedId();
        int message = selectedIds.size() == 1 ? R.string.delete_pengeluaran : R.string.delete_pengeluaran_Selected;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (String currentPetId : selectedIds) {
                            reference.child(currentPetId).removeValue();
                        }
                        mActionMode.finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mActionMode.finish();
                    }
                });
        builder.create().show();
    }
    private void editPet() {
        final String currentPetId = mAdapter.getSelectedId().get(0);
        Pengeluaran pengeluaran = mData.get(mDataId.indexOf(currentPetId));

        String id = pengeluaran.getDataID();
        String catatan = pengeluaran.getNamaPengeluaran();
        String saldo = pengeluaran.getTotalPengeluaran();

        Intent intent = new Intent(getContext(), EditPengeluaranActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("catatan", catatan);
        intent.putExtra("saldo", saldo);
        startActivity(intent);
        mActionMode.finish();
    }
}
