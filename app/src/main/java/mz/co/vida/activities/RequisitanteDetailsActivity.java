package mz.co.vida.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;

public class RequisitanteDetailsActivity extends AppCompatActivity {

    // Components
    private TextView tv_nome, tv_tipo_sangue, tv_quantSanguinea, tv_data_doacao, tv_contacto, tv_provincia, tv_descricao;
    private CircleImageView civ_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisitante_details);


        // Get Extras
        String nome           = getIntent().getStringExtra("nome");
        String data_doacao    = getIntent().getStringExtra("data");
        String contacto       = getIntent().getStringExtra("contacto");
        String quantSanguinea = getIntent().getStringExtra("quantSanguinea");
        String descricao      = getIntent().getStringExtra("descricao");
        String provincia      = getIntent().getStringExtra("provincia");
        String tipo_sangue    = getIntent().getStringExtra("tipo_sangue");

        // Init Components
        tv_nome           = (TextView) findViewById(R.id.tv_nome);
        tv_tipo_sangue    = (TextView) findViewById(R.id.tv_tipo_sangue);
        tv_quantSanguinea = (TextView) findViewById(R.id.tv_quantSanguinea);
        tv_data_doacao    = (TextView) findViewById(R.id.tv_data_doacao);
        tv_contacto       = (TextView) findViewById(R.id.tv_contacto);
        tv_provincia      = (TextView) findViewById(R.id.tv_provincia);
        tv_descricao      = (TextView) findViewById(R.id.tv_descricao);


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
        tv_quantSanguinea.setText(quantSanguinea);
        tv_data_doacao.setText(data_doacao);
        tv_contacto.setText(contacto);
        tv_provincia.setText(provincia);
        tv_descricao.setText(descricao);

    }
}
