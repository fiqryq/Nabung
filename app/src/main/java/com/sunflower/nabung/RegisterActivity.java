package com.sunflower.nabung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.d3ifcool.nabung.R;

public class RegisterActivity extends AppCompatActivity {
    private Button mRegister;
    private EditText mRegisNamaUser, mRegisPass, mRegisEmail;
    private FirebaseAuth auth;
    private String mNamaUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegister = findViewById(R.id.bt_register);
        mRegisNamaUser = findViewById(R.id.et_nama_regis);
        mRegisPass = findViewById(R.id.et_password_regis);
        mRegisEmail = findViewById(R.id.et_email_regis);
        auth = FirebaseAuth.getInstance();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namalengkap = mRegisNamaUser.getText().toString().toLowerCase();
                final String password = mRegisPass.getText().toString();
                final String email = mRegisEmail.getText().toString().toLowerCase();
                if (TextUtils.isEmpty(namalengkap) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this,"Semua kolom harus terisi", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(RegisterActivity.this, "Berhasil Mendaftar" , Toast.LENGTH_SHORT).show();
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Gagal Mendaftar" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                            mNamaUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        } else {
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }
                                        databaseReference = FirebaseDatabase.getInstance().getReference(mNamaUser);
                                        User value = new User(namalengkap, password, email, "0", "0", "0");
                                        databaseReference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }
}
