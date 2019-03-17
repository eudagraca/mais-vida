package mz.co.vida.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.utils.MyUtils;

public class MainActivity extends AppCompatActivity /*implements GoogleApiClient.OnConnectionFailedListener*/ {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;
        Button btn_register = (Button) findViewById(R.id.bt);

        if (MyUtils.isConnected(context)) {

            FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAuth();

            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            btn_register.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), LoginActivity.class)));
        } else {

            SweetAlertDialog aDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            aDialog.setTitle("Erro de conexão");
            aDialog.setContentText("Conecta-te a internet");
            aDialog.show();
            aDialog.setConfirmText(MyUtils.OK);
            aDialog.setConfirmClickListener(sweetAlertDialog -> {
                aDialog.dismiss();
                ((MainActivity) context).finish();
            });
        }
    }

}