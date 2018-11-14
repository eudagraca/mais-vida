package mz.co.vida;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codesgood.views.JustifiedTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.entidades.Anuncio;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView tv_data;
    private TextView tv_nome;
    private TextView tv_call;
    private JustifiedTextView jtv_comentario;
    private TextView tv_quant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Intent intent = getIntent();
        final String id         = intent.getStringExtra("id");
        final String nome       = intent.getStringExtra("nome");
        final String telefone   = intent.getStringExtra("telefone");

        tv_call             = (TextView) findViewById(R.id.tv_call);
        tv_data             = (TextView) findViewById(R.id.tv_data_doacao);
        tv_nome             = (TextView) findViewById(R.id.tv_nome);
        Button btn_confirmar = (Button) findViewById(R.id.bt_confirmarDoacao);
        jtv_comentario       = (JustifiedTextView) findViewById(R.id.jtv_comentario);
        tv_quant            = (TextView) findViewById(R.id.tv_quant_S);


        DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference userAnuncio = mdataRef.child("anuncios").child(id);

        userAnuncio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Anuncio anuncio = new Anuncio();
                //anuncio.setQuantSanguinea(String.valueOf((Long) dataSnapshot.child("quantSanguinea").getValue()));
                anuncio.setComentario(dataSnapshot.child("comentario").getValue(String.class));
                anuncio.setDataDoacao(dataSnapshot.child("dataDoacao").getValue(String.class));


                jtv_comentario.setText(anuncio.getComentario());
                tv_call.setText(telefone);
                tv_nome.setText(nome);
                tv_data.setText(anuncio.getDataDoacao());
                //tv_quant.setText(anuncio.getQuantSanguinea());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        Button mBtback = (Button) findViewById(R.id.btn_voltar);
        mBtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostDetailsActivity.this, HomeActivity.class));
            }
        });


        /*If current user Select Conf button, add +1 and send notification to
            User Id of annoucement
        */

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mRef = ConfiguracaoFirebase.getFirebase();
                final DatabaseReference maisUm = mRef.child("anuncios").child(id);

                 final int[] quantidade = {0};

                maisUm.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        quantidade[0] = Integer.parseInt(String.valueOf(Objects.requireNonNull(dataSnapshot.child("quantSanguinea").getValue(Integer.class))));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                quantidade[0] += 1;
                maisUm.child("quant_adquirida").setValue(quantidade[0]);
                Toast.makeText(PostDetailsActivity.this, "Disponibilidade enviada", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(PostDetailsActivity.this, HomeActivity.class);
                startActivity(i);


            }
        });


    }
}
