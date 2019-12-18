package org.d3ifcool.nabung;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private TextView mDaftar;
    private Button mButtonLogin;
    private FirebaseAuth auth;
    private String mUserID;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);
        if (session.loggedIn()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        auth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.et_email_login);
        mPassword = findViewById(R.id.et_password_login);
        mButtonLogin = findViewById(R.id.bt_login);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mDaftar = findViewById(R.id.tv_daftar);
        mDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "Masukan Email Address", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Masukan Password", Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_LONG).show();
                }
                else {
                    session.setLoggedin(true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
