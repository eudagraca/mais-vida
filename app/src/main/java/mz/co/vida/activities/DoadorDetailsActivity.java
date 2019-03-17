package mz.co.vida.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;

public class DoadorDetailsActivity extends AppCompatActivity {

    // Components
    private TextView tv_nome, tv_sexo, tv_contacto, tv_sangue, tv_provincia, tv_unidadeSanitaria, tv_estado;
    private CircleImageView civ_foto;
    //utils
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doador_details);

        // Get Extras
        String id = getIntent().getStringExtra("id");

        // Init Utils
        DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase().child("Usuario").child(id);
        context = DoadorDetailsActivity.this;

        // Init Components
        tv_nome               = (TextView) findViewById(R.id.tv_nome);
        tv_contacto           = (TextView) findViewById(R.id.tv_contacto);
        tv_sexo               = (TextView) findViewById(R.id.tv_Sexo);
        tv_provincia          = (TextView) findViewById(R.id.tv_provincia);
        tv_unidadeSanitaria   = (TextView) findViewById(R.id.tv_unidade_sanitaria);
        tv_estado             = (TextView) findViewById(R.id.tv_estado);
        tv_sangue             = (TextView) findViewById(R.id.tv_tiposanguineo);
        civ_foto              = (CircleImageView) findViewById(R.id.iv_foto);

        // Go back
        tv_nome.setOnClickListener(view -> onBackPressed());

        // Read the doador details
        mdataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Usuario usuario = new Usuario();

                    if (dataSnapshot.child("foto").exists()) {
                        String foto = dataSnapshot.child("foto").getValue(String.class);
                        usuario.setFoto(foto);
                        Glide.with(context).load(foto).into(civ_foto);
                    }

                    usuario.setTipo_sangue(dataSnapshot.child("tipo_sangue").getValue(String.class));
                    usuario.setNome(dataSnapshot.child("nome").getValue(String.class));
                    usuario.setProvincia(dataSnapshot.child("provincia").getValue(String.class));
                    usuario.setUnidadeProxima(dataSnapshot.child("unidadeProxima").getValue(String.class));
                    usuario.setSexo(dataSnapshot.child("sexo").getValue(String.class));
                    usuario.setEstado(dataSnapshot.child("estado").getValue(String.class));
                    usuario.setContacto(dataSnapshot.child("contacto").getValue(String.class));

                    tv_sangue.setText(usuario.getTipo_sangue());
                    tv_nome.setText(usuario.getNome());
                    tv_contacto.setText(usuario.getContacto());
                    tv_estado.setText(usuario.getEstado());
                    tv_sexo.setText(usuario.getSexo());
                    tv_provincia.setText(usuario.getProvincia());
                    tv_unidadeSanitaria.setText(usuario.getUnidadeProxima());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        tv_contacto.setOnClickListener(v -> Dexter.withActivity(DoadorDetailsActivity.this)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        new SweetAlertDialog(DoadorDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Pretende efectuar uma chamada para " + tv_nome.getText().toString())
                                .setConfirmText("Sim!")
                                .setConfirmClickListener(sDialog -> {

                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + tv_contacto.getText().toString()));
                                    startActivity(intent);
                                    sDialog.dismissWithAnimation();
                                })
                                .setCancelButton("Não", SweetAlertDialog::dismissWithAnimation)
                                .show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check());
    }
}