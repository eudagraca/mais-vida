package mz.co.vida;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import DAO.ConfiguracaoFirebase;
import mz.co.vida.entidades.Usuario;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Usuario usuario;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signuP = (Button) findViewById(R.id.bt_signUp);
        mEmail = (EditText) findViewById(R.id.et_user);
        mPassword = (EditText) findViewById(R.id.et_password);
        Button signin = (Button) findViewById(R.id.btn_entrar);
        Button mForgotPass = (Button) findViewById(R.id.bt_forgotPass);

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ForgotPasswordActivity.class));
            }
        });

        signuP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegistoActivity.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mEmail.getText().toString().equals("") && !mPassword.getText().toString().equals("")) {
                    usuario = new Usuario();
                    usuario.setEmail(mEmail.getText().toString().trim().toLowerCase());
                    usuario.setSenha(mPassword.getText().toString().trim());
                    validarAcesso();
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha os campos de Senha e email ", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getBaseContext(), RegistoActivity.class));
                }
            }
        });
    }

    private void validarAcesso(){
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        final ProgressDialog proDialog = new ProgressDialog(LoginActivity.this);
        proDialog.setTitle("Aguarde");
        proDialog.show();
        mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                user = mAuth.getCurrentUser();

                if(task.isSuccessful() && (user.isEmailVerified())){
                    proDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Wellcome", Toast.LENGTH_SHORT).show();
                }else{
                    proDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Email não verificado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}