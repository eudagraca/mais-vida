package mz.co.vida.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;


public class AboutAppActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;

    public AboutAppActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        //Init Components
        Button bt_back_more_info = (Button) findViewById(R.id.bt_back_more_info);
        Button bt_logout         = (Button) findViewById(R.id.bt_logout);
        Button settings         = (Button) findViewById(R.id.bt_add_hp);



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterHospitalActivity.class));
            }
        });

        //Init FirebaseAuth
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        bt_back_more_info.setOnClickListener(view -> onBackPressed());

        bt_logout.setOnClickListener(v -> new SweetAlertDialog(getBaseContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Alerta!!")
                .setContentText("Sair da aplicação")
                .setConfirmText("Sim")
                .setConfirmClickListener(sDialog -> {

                    mAuth.signOut();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    sDialog.dismissWithAnimation();
                }).setCancelButton("Não", Dialog::dismiss).show());
    }
}