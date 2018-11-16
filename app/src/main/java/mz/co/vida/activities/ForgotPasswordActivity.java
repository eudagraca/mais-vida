package mz.co.vida.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText mEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button btVoltar  = (Button) findViewById(R.id.bt_voltar);
        mEmail           = (EditText) findViewById(R.id.et_email);
        Button enviarRed = (Button) findViewById(R.id.bt_redifinir);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
        enviarRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email enviado para sua caixa de emai", Toast.LENGTH_SHORT).show();
                                    abrirLogin();
                                }else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Failed to Send", Toast.LENGTH_SHORT).show();
                                    abrirLogin();
                                }
                            }
                        });
            }
        });
    }
    public void abrirLogin(){
        Intent intent =new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}