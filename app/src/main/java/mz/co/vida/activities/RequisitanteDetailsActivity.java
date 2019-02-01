package mz.co.vida.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import mz.co.vida.R;

public class RequisitanteDetailsActivity extends AppCompatActivity {

    // Components
    private TextView tv_nome;
    private TextView tv_contacto;
    private CircleImageView civ_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisitante_details);


        // Get Extras
        String nome           = getIntent().getStringExtra("nome");
        String data_doacao    = getIntent().getStringExtra("data");
        String contacto       = getIntent().getStringExtra("contacto");
        int quantSanguinea    = getIntent().getIntExtra("quantSanguinea", 0);
        String descricao      = getIntent().getStringExtra("descricao");
        String provincia      = getIntent().getStringExtra("provincia");
        String tipo_sangue    = getIntent().getStringExtra("tipo_sangue");

        // Init Components
        tv_nome           = (TextView) findViewById(R.id.tv_nome);
        TextView tv_tipo_sangue = (TextView) findViewById(R.id.tv_tipo_sangue);
        TextView tv_quantSanguinea = (TextView) findViewById(R.id.tv_quantSanguinea);
        TextView tv_data_doacao = (TextView) findViewById(R.id.tv_data_doacao);
        tv_contacto       = (TextView) findViewById(R.id.tv_contacto);
        TextView tv_provincia = (TextView) findViewById(R.id.tv_provincia);
        TextView tv_descricao = (TextView) findViewById(R.id.tv_descricao);


        // Go back
        tv_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Show the requisitante's details
        tv_nome.setText(nome);
        tv_tipo_sangue.setText(tipo_sangue);
        tv_quantSanguinea.setText(String.valueOf(quantSanguinea)+" Litros");
        tv_data_doacao.setText(data_doacao);
        tv_contacto.setText(contacto);
        tv_provincia.setText(provincia);
        tv_descricao.setText(descricao);


        tv_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(RequisitanteDetailsActivity.this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                new SweetAlertDialog(RequisitanteDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Alerta!!")
                                        .setContentText("Pretende efectuar uma chamada para "+ tv_nome.getText().toString())
                                        .setConfirmText("Sim!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(Uri.parse("tel:"+tv_contacto.getText().toString()));
                                                startActivity(intent);
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelButton("Não", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        })
                        .check();

            }
        });


    }
}
