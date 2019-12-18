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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahPengeluaranActivity extends AppCompatActivity {
    private EditText mTambahCatatan, mTambahSaldo;
    private Button mTambahPengeluaran;
    private DatabaseReference mDatabaseRef;
    private String mUserID, mJum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengeluaran);

        mTambahCatatan = findViewById(R.id.et_tambah_catatan_plr);
        mTambahSaldo = findViewById(R.id.et_tambah_saldo_plr);
        mTambahPengeluaran = findViewById(R.id.bt_tambah_plr);

        mTambahSaldo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTambahSaldo.removeTextChangedListener(this);
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
                    mTambahSaldo.setText(formattedString);
                    mTambahSaldo.setSelection(mTambahSaldo.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                mTambahSaldo.addTextChangedListener(this);


                mTambahSaldo.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(TambahPengeluaranActivity.this, LoginActivity.class));
        }

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mUserID).child("pengeluaran");

        mTambahPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Catatan = mTambahCatatan.getText().toString().trim();
                String Saldo = mTambahSaldo.getText().toString().trim();
                if (TextUtils.isEmpty(Catatan) || TextUtils.isEmpty(Saldo)){
                    Toast.makeText(TambahPengeluaranActivity.this, "Semua kolom harus terusi", Toast.LENGTH_LONG).show();
                }
                else {
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("d MMM", Locale.US);
                    String tanggal = sdf1.format(c1.getTime());
                    String dataID = mDatabaseRef.push().getKey();
                    Pengeluaran upload = new Pengeluaran(dataID, mTambahCatatan.getText().toString().trim(), mJum, tanggal);

                    mDatabaseRef.child(dataID).setValue(upload);

//                Intent intent = new Intent(TambahPengeluaranActivity.this, MainActivity.class);
//                startActivity(intent);
                    finish();
                }
            }
        });
    }
    //    Ketika tombol back di tekan maka akan kembali ke fragment sebelumnya bukan ke frame awal
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
