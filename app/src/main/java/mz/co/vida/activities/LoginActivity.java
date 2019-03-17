package mz.co.vida.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;
import mz.co.vida.utils.MyUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with firebaseUser interaction.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Usuario usuario;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button sign_up     = (Button) findViewById(R.id.bt_signUp);
        mEmail             = (EditText) findViewById(R.id.et_user);
        mPassword          = (EditText) findViewById(R.id.et_password);
        Button signIn      = (Button) findViewById(R.id.btn_entrar);
        Button mForgotPass = (Button) findViewById(R.id.bt_forgotPass);

        context = this;
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        mForgotPass.setOnClickListener(v -> startActivity(new Intent(context, ForgotPasswordActivity.class)));

        sign_up.setOnClickListener(v -> startActivity(new Intent(context, RegistoActivity.class)));

        signIn.setOnClickListener(v -> {
            //Check connection
            if (MyUtils.isConnected(context)){
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
        });
    }

    private void validarAcesso(){
        //progress dialog
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
        pDialog.setTitleText("Aguarde...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(task -> {
            firebaseUser = mAuth.getCurrentUser();
            if (task.isSuccessful()) {

                if (firebaseUser.isEmailVerified()) {
                    DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
                    final DatabaseReference estado = mdataRef.child("Usuario").child(firebaseUser.getUid()).child("estado");
                    estado.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SharedPreferences sharedPreferences = getSharedPreferences(MyUtils.SP_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String isDoador = dataSnapshot.child("estado").getValue(String.class);

                            if (isDoador != null && isDoador.equals("Requisitante")) {
                                editor.putBoolean("isDoador", false);
                                editor.apply();
                                pDialog.dismiss();
                                home();
                            } else if (isDoador != null && isDoador.equals("Doador")) {
                                editor.putBoolean("isDoador", true);
                                editor.apply();
                                pDialog.dismiss();
                                home();
                            } else {
                                pDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    pDialog.dismiss();
                    MyUtils.alertaNegativa(context, "Email  não reconhecido na aplicação!");

                }
            } else {
                pDialog.dismiss();
                MyUtils.alertaNegativa(context, "Senha e email incompatíveis!");

            }
        });
    }

    private void home() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if firebaseUser is signed in (non-null) and updateUser UI accordingly.
        FirebaseUser currentUser = ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()) {

            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}