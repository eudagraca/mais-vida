package mz.co.vida.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.entities.Endereco;
import mz.co.vida.entities.LocaisDoacao;

public class DonnateDetailsActivity extends AppCompatActivity {

    private TextView tv_nome_hospital, tv_nome_local_endereco, tv_data, tv_hora, tv_provincia, tv_telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mz.co.vida.R.layout.activity_donnate_details);

        String id = getIntent().getStringExtra("hospitalId");
        //Database
        DatabaseReference database = ConfiguracaoFirebase.getFirebase();

        tv_data                = (TextView) findViewById(mz.co.vida.R.id.tv_data_doacao_local);
        tv_hora                = (TextView) findViewById(mz.co.vida.R.id.tv_hora_local);
        tv_nome_hospital       = (TextView) findViewById(mz.co.vida.R.id.tv_nome_hp);
        tv_provincia           = (TextView) findViewById(mz.co.vida.R.id.tv_local_doacao_endereco_prov);
        tv_telefone            = (TextView) findViewById(mz.co.vida.R.id.tv_telefone_local);
        tv_nome_local_endereco = (TextView) findViewById(mz.co.vida.R.id.tv_nome_local_coleta);

        tv_nome_hospital.setOnClickListener(v ->{
            onBackPressed();
        });
        database.child("Colecta_Sanguinea").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LocaisDoacao doacao = new LocaisDoacao();
                Endereco endereco = new Endereco();
                //Data hospital
                doacao.setNome(dataSnapshot.child("nome").getValue(String.class));
                doacao.setDataColeta(dataSnapshot.child("data_coleta").getValue(String.class));
                doacao.setTelefone(dataSnapshot.child("telefone").getValue(String.class));
                doacao.setHoras(dataSnapshot.child("horas_coleta").getValue(String.class));
                doacao.setDataFim(dataSnapshot.child("data_fim").getValue(String.class));
                doacao.setHorasFim(dataSnapshot.child("horas_fim").getValue(String.class));

                //Address
                endereco.setNome(dataSnapshot.child("endereco_nome").getValue(String.class));
                endereco.setLocal(dataSnapshot.child("endereco_local").getValue(String.class));
                endereco.setProvincia(dataSnapshot.child("endereco_provincia").getValue(String.class));

                tv_data.setText("De "+doacao.getDataColeta()+ " a "+ doacao.getDataFim());
                tv_hora.setText("Das "+doacao.getHoras()+" às "+ doacao.getHorasFim());
                tv_nome_hospital.setText(doacao.getNome());
                tv_telefone.setText(doacao.getTelefone());
                tv_nome_local_endereco.setText(doacao.getNome());
                tv_provincia.setText(endereco.getNome()+", "+endereco.getLocal()+", "+endereco.getProvincia());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
