package org.d3ifcool.nabung;

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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class EditPemasukanActivity extends AppCompatActivity {
    private String mUserID, mJum;
    private EditText mCatatan, mSaldo;
    private Button mSimpan;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pemasukan);

        mCatatan = findViewById(R.id.et_edit_catatan_pms);
        mSaldo = findViewById(R.id.et_edit_saldo_pms);
        mSimpan = findViewById(R.id.bt_edit_simpan_pms);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(EditPemasukanActivity.this, LoginActivity.class));
        }

        String ID = getIntent().getStringExtra("ID");
        final String catatan = getIntent().getStringExtra("catatan");
        final String saldo = getIntent().getStringExtra("saldo");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mUserID).child("pemasukan").child(ID);

        mCatatan.setHint(catatan);
        mSaldo.setHint(saldo);

        mSaldo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSaldo.removeTextChangedListener(this);
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
                    mSaldo.setText(formattedString);
                    mSaldo.setSelection(mSaldo.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                mSaldo.addTextChangedListener(this);


                mSaldo.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catatan = mCatatan.getText().toString().trim();
                String saldo = mSaldo.getText().toString().trim();
                if (TextUtils.isEmpty(catatan) || TextUtils.isEmpty(saldo)){
                    Toast.makeText(EditPemasukanActivity.this, "Semua kolom harus terisi", Toast.LENGTH_LONG).show();
                }
                else {
                    mDatabaseRef.child("catatan").setValue(catatan);
                    mDatabaseRef.child("saldoPemasukan").setValue(mJum);

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

