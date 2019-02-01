package mz.co.vida.activities;

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
    FirebaseAuth mAuth;

    public AboutAppActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        //Init Components
        Button bt_back_more_info = (Button) findViewById(R.id.bt_back_more_info);
        Button bt_logout         = (Button) findViewById(R.id.bt_logout);


        //Init FirebaseAuth
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        bt_back_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(getBaseContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Alerta!!")
                        .setContentText("Sair da aplicação")
                        .setConfirmText("Sim")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                mAuth.signOut();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);

                                sDialog.dismissWithAnimation();
                            }
                        }) .setCancelButton("Não", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                }).show();
            }
        });




    }

}