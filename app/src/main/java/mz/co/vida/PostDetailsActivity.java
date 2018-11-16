package mz.co.vida;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView tv_data;
    private TextView tv_nome;
    private TextView tv_call;
    private TextView jtv_comentario;
    private TextView tv_quant;
    private TextView tv_location;
    private TextView tv_blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Intent intent = getIntent();
        final String id         = intent.getStringExtra("id");
        final String nome       = intent.getStringExtra("nome");
        final String telefone   = intent.getStringExtra("telefone");
        final String data       = intent.getStringExtra("data");
        //?????????????
        final int quantidade    = intent.getIntExtra("quantidade",0);
        final String comment    = intent.getStringExtra("comentario");
        final String location   = intent.getStringExtra("localizacao");
        final String typeOfBlood = intent.getStringExtra("tipo_sangue");


        tv_call             = (TextView) findViewById(R.id.tv_call);
        tv_data             = (TextView) findViewById(R.id.tv_data_doacao);
        tv_nome             = (TextView) findViewById(R.id.tv_nome);
        jtv_comentario      = (TextView) findViewById(R.id.jtv_comentario);
        tv_quant            = (TextView) findViewById(R.id.tv_quant_S);
        tv_location         = (TextView) findViewById(R.id.tv_location);
        tv_blood            = (TextView) findViewById(R.id.tv_bloodtype);


        jtv_comentario.setText(comment);
        tv_call.setText(telefone);
        tv_nome.setText(nome);
        tv_data.setText(data);
        tv_location.setText(location);
        tv_quant.setText(String.valueOf(quantidade));
        tv_blood.setText(typeOfBlood);


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

//        btn_confirmar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DatabaseReference mRef = ConfiguracaoFirebase.getFirebase();
//                final DatabaseReference maisUm = mRef.child("anuncios").child(id);
//
//
//
//                maisUm.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        quantidade = Integer.parseInt(String.valueOf(Objects.requireNonNull(dataSnapshot.child("quantSanguinea").getValue(Integer.class))));
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//
//                quantidade += 1;
//                maisUm.child("quant_adquirida").setValue(quantidade);
//                Toast.makeText(PostDetailsActivity.this, "Disponibilidade enviada", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(PostDetailsActivity.this, HomeActivity.class);
//                startActivity(i);
//
//            }
//        });

        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(PostDetailsActivity.this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

//                                new MaterialDialog(PostDetailsActivity.this)
//                                        .title(null,"Confirmar")
//                                        .message(null,"Ligar para: " + telefone, false, 1)
//                                        .positiveButton(null, "Ligar", new Function1<MaterialDialog, Unit>() {
//                                            @Override
//                                            public Unit invoke(MaterialDialog materialDialog) {
//                                                Intent i = new Intent(Intent.ACTION_CALL);
//                                                i.setData(Uri.parse("tel:"+telefone));
//                                                startActivity(i);
//                                                return null;
//                                            }
//                                        })
//                                        .negativeButton(null, "Cancelar", new Function1<MaterialDialog, Unit>() {
//                                            @Override
//                                            public Unit invoke(MaterialDialog materialDialog) {
//                                                return null;
//                                            }
//                                        })
//                                        .show();
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
