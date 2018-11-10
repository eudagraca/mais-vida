package mz.co.vida;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codesgood.views.JustifiedTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import mz.co.vida.DAO.ConfiguracaoFirebase;

public class PostDetailsActivity extends AppCompatActivity {

    private Button mBtback;
    private TextView tv_data, tv_nome, tv_confirmar, tv_call;
    private JustifiedTextView jtv_comentario;
    private DatabaseReference mdataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        final String nome = intent.getStringExtra("nome");
        final String telefone = intent.getStringExtra("telefone");

        tv_call        = (TextView) findViewById(R.id.tv_call);
        tv_data        = (TextView) findViewById(R.id.tv_data_doacao);
        tv_nome        = (TextView) findViewById(R.id.tv_nome);
        tv_confirmar   = (TextView) findViewById(R.id.tv_confirmarDoacao);
        jtv_comentario = (JustifiedTextView) findViewById(R.id.jtv_comentario);



        mdataRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference userAnuncio = mdataRef.child("anuncios").child(id);

        userAnuncio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comentario     = dataSnapshot.child("comentario").getValue(String.class);
                String data_de_doacao = dataSnapshot.child("dataDoacao").getValue(String.class);
                // int quantidade_sanguinea = dataSnapshot.child("quantSanguinea").getValue(Integer.class);

                tv_call.setText(telefone);
                tv_nome.setText(nome);
                tv_data.setText(data_de_doacao);
                jtv_comentario.setText(comentario);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        mBtback = (Button) findViewById(R.id.btn_voltar);
        mBtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostDetailsActivity.this, HomeActivity.class));
            }
        });
    }
}
