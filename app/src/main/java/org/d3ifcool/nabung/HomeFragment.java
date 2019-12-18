package org.d3ifcool.nabung;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.nabung.base.MethodFunction;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private WishlistAdapter mAdapter;
    private ArrayList<Wishlist> mData;
    private ArrayList<String> mDataId;
    private ActionMode mActionMode;

    private RecyclerView rvWishlist;
    private ImageView mTambahWishlist;
    private String mUserID;
    private TextView mHomeTotal;
    private ProgressBar mProgressCircle;

    MethodFunction methodFunction = new MethodFunction();

    private DatabaseReference reference;
    private DatabaseReference reference2;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mData.add(dataSnapshot.getValue(Wishlist.class));
            mDataId.add(dataSnapshot.getKey());
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mData.set(pos, dataSnapshot.getValue(Wishlist.class));
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
        final View view_home = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        rvWishlist = view_home.findViewById(R.id.rv_wishlist);
        rvWishlist.setHasFixedSize(true);
        mData = new ArrayList<>();
        mDataId = new ArrayList<>();
        mProgressCircle = view_home.findViewById(R.id.progress_circle);
        mTambahWishlist = view_home.findViewById(R.id.iv_tambah_wishlist);
        mHomeTotal = view_home.findViewById(R.id.tv_home_total);
        mAdapter = new WishlistAdapter(getContext(), mData, mDataId,
                new WishlistAdapter.ClickHandler() {
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

//        Awal Descending short order
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvWishlist.setLayoutManager(linearLayoutManager);

        rvWishlist.setAdapter(mAdapter);



        mTambahWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TambahWishlistActivity.class);
                startActivity(intent);
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
// ini di hapus karena tidak digunakan
//        rvWishlist.setLayoutManager(new LinearLayoutManager(getContext()));
        reference = FirebaseDatabase.getInstance().getReference(mUserID).child("wishlist");
        reference2 = FirebaseDatabase.getInstance().getReference(mUserID).child("saldoWishlist");
        reference.addChildEventListener(childEventListener);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int tSaldo = 0;
                for (DataSnapshot postSnapshot :  dataSnapshot.getChildren()){
                    Wishlist wishlist = postSnapshot.getValue(Wishlist.class);
                    int saldoA = Integer.parseInt(wishlist.getSaldoTerkumpul());
                    tSaldo = tSaldo + saldoA;
                    reference2.setValue(Integer.toString(tSaldo));
                }
                mProgressCircle.setVisibility(View.INVISIBLE);
                mHomeTotal.setText(methodFunction.currencyIdr(tSaldo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


        return view_home;
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

//    Destroy Contextual Action mode
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }
//akhir
    private void editPet() {
        final String currentPetId = mAdapter.getSelectedId().get(0);
        Wishlist wishlist = mData.get(mDataId.indexOf(currentPetId));

        String id = wishlist.getDataID();
        String nama = wishlist.getJudul();
        String harga = wishlist.getTotalSaldo();
        String terkumpul = wishlist.getSaldoTerkumpul();
        String target = wishlist.getJangkaWaktu();

        Intent intent = new Intent(getContext(), EditWishlistActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("harga", harga);
        intent.putExtra("nama", nama);
        intent.putExtra("terkumpul", terkumpul);
        intent.putExtra("target", target);
        startActivity(intent);
        mActionMode.finish();
    }

    private void deletePet() {
        final ArrayList<String> selectedIds = mAdapter.getSelectedId();
        int message = selectedIds.size() == 1 ? R.string.delete_pet : R.string.delete_pets;

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
}
