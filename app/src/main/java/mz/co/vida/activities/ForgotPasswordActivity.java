package mz.co.vida.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.utils.MyUtils;
import mz.co.vida.utils.VerificaConexao;

public class ForgotPasswordActivity extends AppCompatActivity {
    //components
    private EditText mEmail;
    private FirebaseAuth mAuth;
    //utils
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Init components
        Button btVoltar  = (Button) findViewById(R.id.bt_voltar);
        mEmail           = (EditText) findViewById(R.id.et_email);
        Button enviarRed = (Button) findViewById(R.id.bt_redifinir);

        //init utils
        context = this;

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
                if (TextUtils.isEmpty(mEmail.getText())){
                    mEmail.setError("Introduza o email");
                    mEmail.requestFocus();

                }
                else if (!mEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    mEmail.setError("Endereço de email inválido");
                    mEmail.requestFocus();

                }else

                if (VerificaConexao.isConnected(context)){
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
                                pDialog.setTitleText("Aguarde...");
                                pDialog.setCancelable(true);
                                pDialog.show();

                                if (task.isSuccessful()) {

                                    MyUtils.alertaPosetiva(context, "Redefinição da senha!", "Enviámos instruções de redifinição para a sua caixa de email! " );
                                    abrirLogin();
                                    pDialog.dismiss();
                                }else {

                                    MyUtils.alertaNegativa(context, "Presumimos que tenha introduzido um email não registado na aplicação");

                                }
                            }
                        });
            }else{
                    MyUtils.alertaNegativa(context, "Liga-te a internet");
                }
            }
        });

    }

    private void abrirLogin(){
        Intent intent =new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}