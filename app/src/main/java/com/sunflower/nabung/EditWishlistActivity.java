package com.sunflower.nabung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.d3ifcool.nabung.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class EditWishlistActivity extends AppCompatActivity {
    private EditText mEditNama, mEditHarga, mEditSaldoTerkumpul, mTargetHari;
    private Button mSimpan;
    private String mUserID, mJum, mJumlah;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wishlist);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(EditWishlistActivity.this, LoginActivity.class));
        }

        mEditNama = findViewById(R.id.et_edit_nama_barang);
        mEditHarga = findViewById(R.id.et_edit_harga_barang);
        mEditSaldoTerkumpul = findViewById(R.id.et_edit_saldo_terkumpul);
        mTargetHari = findViewById(R.id.et_edit_target_hari);
        mSimpan = findViewById(R.id.bt_edit_wishlist);

        String ID = getIntent().getStringExtra("ID");
        final String nama = getIntent().getStringExtra("nama");
        final String harga = getIntent().getStringExtra("harga");
        final String terkumpul = getIntent().getStringExtra("terkumpul");
        final String target = getIntent().getStringExtra("target");


        mEditNama.setHint(nama);
        mEditHarga.setHint(harga);
        mEditSaldoTerkumpul.setHint(terkumpul);
        mTargetHari.setHint(target);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mUserID).child("wishlist").child(ID);

        mEditHarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEditHarga.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();
                    originalString = originalString.replaceAll("\\.", "").replaceFirst(",", ".");
                    originalString = originalString.replaceAll("[A-Z]", "").replaceAll("[a-z]", "");
                    int doubleval = Integer.parseInt(originalString);
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    symbols.setGroupingSeparator('.');
                    String pattern = "#,###,###";
                    DecimalFormat formatter = new DecimalFormat(pattern, symbols);
                    String formattedString = formatter.format(doubleval);
                    mJum = formattedString.replace(".","");
                    mEditHarga.setText(formattedString);
                    mEditHarga.setSelection(mEditHarga.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                mEditHarga.addTextChangedListener(this);


                mEditHarga.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditSaldoTerkumpul.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEditSaldoTerkumpul.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();
                    originalString = originalString.replaceAll("\\.", "").replaceFirst(",", ".");
                    originalString = originalString.replaceAll("[A-Z]", "").replaceAll("[a-z]", "");
                    int doubleval = Integer.parseInt(originalString);
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    symbols.setGroupingSeparator('.');
                    String pattern = "#,###,###";
                    DecimalFormat formatter = new DecimalFormat(pattern, symbols);
                    String formattedString = formatter.format(doubleval);
                    mJumlah = formattedString.replace(".","");
                    mEditSaldoTerkumpul.setText(formattedString);
                    mEditSaldoTerkumpul.setSelection(mEditSaldoTerkumpul.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                mEditSaldoTerkumpul.addTextChangedListener(this);


                mEditSaldoTerkumpul.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mNama = mEditNama.getText().toString().trim();
                String mHarga = mEditHarga.getText().toString().trim();
                String mTerkumpul = mEditSaldoTerkumpul.toString().trim();
                String mTarget = mTargetHari.getText().toString().trim();

                if (TextUtils.isEmpty(mNama) || TextUtils.isEmpty(mHarga) || TextUtils.isEmpty(mTerkumpul) || TextUtils.isEmpty(mTarget)){
                    Toast.makeText(EditWishlistActivity.this, "Semua kolom harus terisi", Toast.LENGTH_LONG).show();
                }else{
                    mDatabaseRef.child("judul").setValue(mEditNama.getText().toString());
                    mDatabaseRef.child("totalSaldo").setValue(mJum);
                    mDatabaseRef.child("saldoTerumpul").setValue(mJumlah);
                    mDatabaseRef.child("jangkaWaktu").setValue(mTargetHari.getText().toString());

//                    startActivity(new Intent(EditWishlistActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
