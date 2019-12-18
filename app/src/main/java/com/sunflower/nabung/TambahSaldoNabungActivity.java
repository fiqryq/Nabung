package com.sunflower.nabung;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sunflower.nabung.base.MethodFunction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.nabung.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TambahSaldoNabungActivity extends AppCompatActivity {

    private String mUserID, mJum;
    private DatabaseReference reference;
    private int SisaSaldo, jumlahTabung;
    private TextView mSisaSaldo;
    private Button mTambahSaldoNabung;
    private EditText mTambahSaldo;
    private int utama, saldoA;


    MethodFunction methodFunction = new MethodFunction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_saldo_nabung);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }


        mSisaSaldo = findViewById(R.id.tv_sisa_saldo);
        mTambahSaldoNabung = findViewById(R.id.bt_tambah_saldo_nabung);
        mTambahSaldo = findViewById(R.id.et_tambah_saldo_nabung);

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

        String key = getIntent().getStringExtra("key");
        String saldo = getIntent().getStringExtra("saldo");
        saldoA = Integer.parseInt(saldo);

        reference = FirebaseDatabase.getInstance().getReference(mUserID).child("wishlist").child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SisaSaldo = Integer.parseInt(dataSnapshot.child("saldoTerkumpul").getValue().toString().trim());
                jumlahTabung = Integer.parseInt(dataSnapshot.child("totalSaldo").getValue().toString().trim());
                utama = jumlahTabung - SisaSaldo;
                mSisaSaldo.setText(methodFunction.currencyIdr(utama));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mTambahSaldoNabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TambahSaldo = mTambahSaldo.getText().toString().trim();
                if (TextUtils.isEmpty(TambahSaldo)){
                    Toast.makeText(TambahSaldoNabungActivity.this, "Kolom harus terisi", Toast.LENGTH_LONG).show();
                }
                else {
                    int saldoB = Integer.parseInt(mJum);
                    int totalSaldo =SisaSaldo + saldoB;
                    if (totalSaldo > jumlahTabung){
                        Toast.makeText(TambahSaldoNabungActivity.this,"Jumlah nabung melebihi target", Toast.LENGTH_LONG).show();
                        mTambahSaldo.setText("");
                    }
                    else {
                        reference.child("saldoTerkumpul").setValue(Integer.toString(totalSaldo));
//                        Intent intent = new Intent(TambahSaldoNabungActivity.this, MainActivity.class);
//                        startActivity(intent);
                        finish();
                    }
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
