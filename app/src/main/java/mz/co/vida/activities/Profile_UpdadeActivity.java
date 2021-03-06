package mz.co.vida.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.DAO.LogicaUsuario;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;
import mz.co.vida.utils.MyUtils;

import static mz.co.vida.utils.MyUtils.SP_IS_DOADOR;

public class Profile_UpdadeActivity extends AppCompatActivity {

    //Components
    private CircleImageView circleImageView;
    private SwitchMultiButton switchMultiButton_estado;
    private EditText editText_contacto, editText_unidade_proxima;
    private Spinner spinner_provincia;
    private LabeledSwitch labeledSwitch_disponibilidade;
    private Context context;
    private RadioButton radioButton_masculino;
    private RadioButton radioButton_femenino;

    //Vars
    private static final int PICK_PHOTO_CODE=100;
    private String key;
    private String foto = null;
    private final Usuario usuario = new Usuario();
    private LogicaUsuario logicaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__updade);

        //Init components
        circleImageView               = (CircleImageView) findViewById(R.id.profile_image__update);
        spinner_provincia             = (Spinner) findViewById(R.id.sp_provincias_update);
        editText_contacto             = (EditText) findViewById(R.id.et_phone_update);
        editText_unidade_proxima      = (EditText) findViewById(R.id.et_unidadeProx_update);
        labeledSwitch_disponibilidade = (LabeledSwitch) findViewById(R.id.stc_disponibilidade_update);
        switchMultiButton_estado      = (SwitchMultiButton) findViewById(R.id.switch_mult_estado_update);
        Button button_update          = (Button) findViewById(R.id.btn_update);
        radioButton_femenino          = (RadioButton) findViewById(R.id.rb_femenino_update);
        radioButton_masculino         = (RadioButton) findViewById(R.id.rb_masculino_update);
        RadioButton radioButton_outro = (RadioButton) findViewById(R.id.rb_outro_update);
        Button button_back            = (Button) findViewById(R.id.bt_back_profile);
        //Utils
        context = this;
        logicaUsuario = new LogicaUsuario(usuario, context);

        //Setup provincias adapter
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.provincias,
                android.R.layout.simple_spinner_item);
        spinner_provincia.setAdapter(adapter);

        if (getIntent().getStringExtra("foto") != null) {
            foto = getIntent().getStringExtra("foto");
        }
        String unidade_proxima = getIntent().getStringExtra("unidade_proxima");
        String sexo = getIntent().getStringExtra("sexo");
        String estado = getIntent().getStringExtra("estado");
        String provincia = getIntent().getStringExtra("provincia");
        String contacto = getIntent().getStringExtra("contacto");
        key = getIntent().getStringExtra("key");
        if (getIntent().getStringExtra("disponibilidade") != null) {
            labeledSwitch_disponibilidade.setVisibility(View.VISIBLE);
            String disponibilidade = getIntent().getStringExtra("disponibilidade");
            if (disponibilidade.toUpperCase().equals("SIM")) {
                labeledSwitch_disponibilidade.setOn(true);
            } else {
                labeledSwitch_disponibilidade.setOn(false);
            }
        } else {
            labeledSwitch_disponibilidade.setVisibility(View.GONE);
        }

        editText_unidade_proxima.setText(unidade_proxima);
        contacto = contacto.substring(4, 13);
        editText_contacto.setText(contacto);

        if (estado.equals("Requisitante")) {
            switchMultiButton_estado.setSelectedTab(1);
        } else {
            switchMultiButton_estado.setSelectedTab(0);
        }

        switch (sexo) {
            case "Masculino":
                radioButton_masculino.setChecked(true);
                break;
            case "Femenino":
                radioButton_femenino.setChecked(true);
                break;
            case "Outro":
                radioButton_outro.setChecked(true);
                break;
            default:
        }

        int pos = adapter.getPosition(provincia);
        spinner_provincia.setSelection(pos);

        if (foto != null) {
            Glide.with(context).load(foto).into(circleImageView);
        }
        switchMultiButton_estado.setOnSwitchListener((position, state) -> Toast.makeText(context, String.valueOf(state), Toast.LENGTH_SHORT).show());
        //Setup photo
        circleImageView.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, PICK_PHOTO_CODE);
        });

        SharedPreferences sharedPreferences = getSharedPreferences(MyUtils.SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        button_update.setOnClickListener(v -> {

            if (switchMultiButton_estado.getSelectedTab() == 0 && labeledSwitch_disponibilidade.isOn()) {
                labeledSwitch_disponibilidade.setVisibility(View.VISIBLE);
                usuario.setEstado("Doador");
                apagar();

                usuario.setDisponibilidade(MyUtils.SIM);
                if (!estado.equals("Doador")) {
                    editor.putBoolean(SP_IS_DOADOR, true);
                    editor.apply();
                }
            } else if (switchMultiButton_estado.getSelectedTab() == 0 && !labeledSwitch_disponibilidade.isOn()) {
                labeledSwitch_disponibilidade.setVisibility(View.VISIBLE);
                usuario.setEstado("Doador");
                apagar();
                usuario.setDisponibilidade(MyUtils.NAO);
                if (!estado.equals("Doador")) {
                    editor.putBoolean(SP_IS_DOADOR, true);
                    editor.apply();
                }
            } else {
                labeledSwitch_disponibilidade.setVisibility(View.GONE);
                usuario.setEstado("Requisitante");
                labeledSwitch_disponibilidade.setVisibility(View.GONE);
                usuario.setDisponibilidade(null);
                if (!estado.equals("Requisitante")) {
                    editor.putBoolean(SP_IS_DOADOR, false);
                    editor.apply();
                }
            }

            if (TextUtils.isEmpty(editText_contacto.getText()) && TextUtils.isEmpty(editText_unidade_proxima.getText())) {
                Toast.makeText(context, "Campos obrigatórios", Toast.LENGTH_SHORT).show();
            } else if (editText_contacto.getText().toString().length() < 9) {
                Toast.makeText(context, "Contacto telefónico inválido", Toast.LENGTH_SHORT).show();
            } else {

                if (radioButton_masculino.isChecked()) {
                    usuario.setSexo("Masculino");
                } else if (radioButton_femenino.isChecked()) {
                    usuario.setSexo("Femenino");
                } else {
                    usuario.setSexo("Outro");
                }
                usuario.setContacto("+258" + editText_contacto.getText().toString());
                if (spinner_provincia.getSelectedItem().equals("Províncias")) {
                    TextView sp = (TextView) spinner_provincia.getSelectedView();
                    sp.setError("Seleccione uma província");
                    sp.setTextColor(Color.RED);
                    sp.requestFocus();
                } else {
                    usuario.setProvincia(spinner_provincia.getSelectedItem().toString());
                }
                DatabaseReference mData = ConfiguracaoFirebase.getFirebase().child("anuncios").child(String.valueOf(key));

                if (switchMultiButton_estado.getSelectedTab() == 1) {
                    mData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                mData.child("provincia").setValue(spinner_provincia.getSelectedItem().toString());
                                mData.child("contacto").setValue(editText_contacto.getText().toString());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                usuario.setUnidadeProxima(editText_unidade_proxima.getText().toString());


                logicaUsuario.updateUser(key, foto);
            }
        });
        button_back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            usuario.setFoto(Objects.requireNonNull(imageUri).toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void apagar() {
        DatabaseReference mData = ConfiguracaoFirebase.getFirebase().child("anuncios").child(key);
        mData.removeValue();
    }

    void home() {
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        home();
    }
}