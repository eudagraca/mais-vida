package mz.co.vida.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;
import mz.co.vida.utils.MyUtils;
import mz.co.vida.utils.VerificaConexao;

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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button sign_up = (Button) findViewById(R.id.bt_signUp);
        mEmail = (EditText) findViewById(R.id.et_user);
        mPassword = (EditText) findViewById(R.id.et_password);
        mEmail.setText("steliobraga13@gmail.com");
        mPassword.setText("123456789");
        Button signIn = (Button) findViewById(R.id.btn_entrar);
        Button mForgotPass = (Button) findViewById(R.id.bt_forgotPass);
        //Init Context
        context = this;

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ForgotPasswordActivity.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegistoActivity.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check connection
                if (VerificaConexao.isConnected(context)){
                    if (!mEmail.getText().toString().equals("") && !mPassword.getText().toString().equals("")) {
                            usuario = new Usuario();
                            usuario.setEmail(mEmail.getText().toString().trim().toLowerCase());
                            usuario.setSenha(mPassword.getText().toString().trim());
                            validarAcesso();

                    } else {

                        MyUtils.alertaNegativa(context, "Preencha os campos de e-mail e senha");
                    }
                }else {
                  MyUtils.alertaNegativa(context, "Liga-te a internet");
                }
            }
        });
    }

    private void validarAcesso(){
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        //progress dialog
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
        pDialog.setTitleText("Aguarde...");
        pDialog.setCancelable(false);
        pDialog.show();

        mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                user = mAuth.getCurrentUser();
                if(task.isSuccessful()){

                    if(user.isEmailVerified()){
                    DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
                    final DatabaseReference estado = mdataRef.child("Usuario").child(user.getUid()).child("estado");
                    estado.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SharedPreferences sharedPreferences = getSharedPreferences(MyUtils.SP_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isDoador", Objects.requireNonNull(dataSnapshot.getValue()).equals("Requisitante"));
                            editor.apply();
                            pDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }else {
                        MyUtils.alertaNegativa(context, "Email  não reconhecido na aplicação!");
                                pDialog.dismiss();
                    }
                }else{
                    MyUtils.alertaNegativa(context, "Senha e email incompatíveis!");
                            pDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser();

        if(currentUser!=null){

            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
            finish();

        }


    }



    }